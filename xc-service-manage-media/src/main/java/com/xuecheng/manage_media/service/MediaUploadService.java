package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.constant.MediaConstant;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @Author: Pace
 * @Data: 2020/3/6 14:40
 * @Version: v1.0
 */
@Service
public class MediaUploadService {

    @Value("${xc-service-manage-media.upload-location}")
    private String uploadLocation;
    //视频处理路由
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    public  String routingkey_media_video;

    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 文件上传前，检查文件是否存在，不存在创建文件夹
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     */
    public void register(String fileMd5,
                         String fileName,
                         Long fileSize,
                         String mimetype,
                         String fileExt) {
        // 1.检查文件是否存在
        // 获取文件名以及文件路径
        String fileFolderPath = getFileFolderPath(fileMd5);
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        // 查询数据库
        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        if(optional.isPresent() && file.exists()){
            // 如果存在，抛出异常
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }

        // 2.不存在创建文件夹
        File fileFolder = new File(fileFolderPath);
        if(!fileFolder.exists()){
            // 文件夹不存在才创建
            fileFolder.mkdirs();
        }
    }

    /**
     * 上传分块前检查分块是否存在，断点续传
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    public boolean checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        // 首先得到分块所在路径
        String chunkFolderPath = getChunkFolderPath(fileMd5);
        // 然后检查
        String chunkFilePath = chunkFolderPath + chunk;
        File file = new File(chunkFilePath);
        return file.exists();
    }

    /**
     * 上传文件
     * @param file
     * @param chunk
     * @param fileMd5
     */
    public void uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        // 检查分块目录是否存在
        String chunkFolderPath = getChunkFolderPath(fileMd5);
        File chunkFolderFile = new File(chunkFolderPath);
        if(!chunkFolderFile.exists()){
            chunkFolderFile.mkdirs();
        }

        // 进行文件上传
        String chunkFilePath = chunkFolderPath + chunk;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(new File(chunkFilePath));
            IOUtils.copy(inputStream,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并文件
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     */
    public void mergechunks(String fileMd5,
                            String fileName,
                            Long fileSize,
                            String mimetype,
                            String fileExt) {
        // 1.文件合并
        // 获取分块文件夹位置
        String chunkFolderPath = getChunkFolderPath(fileMd5);
        File file = new File(chunkFolderPath);
        File[] files = file.listFiles();
        // 将file列表转成List保存，方便排序
        List<File> fileList = Arrays.asList(files);
        // 设置合并后文件路径
        String mergeFilePath = getFilePath(fileMd5, fileExt);
        File mergeFile = new File(mergeFilePath);
        // 合并文件
        mergeFile = mergeFile(fileList,mergeFile);

        // 2.文件md5校验
        boolean checkFileMd5 = checkFileMd5(mergeFile,fileMd5);
        if(!checkFileMd5){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }

        // 3.保存文件信息到数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        // 文件相对路径
        mediaFile.setFilePath(getRelativeFilePath(fileMd5,fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        mediaFile.setFileStatus(MediaConstant.FILE_STATUS_YES);
        mediaFileRepository.save(mediaFile);

        // 4.上传完毕，需要发送消息给MQ，进行消息处理
        sendMsgToProcessMedia(mediaFile.getFileId());

        // 5.删除chunk
        deleteChunk(mediaFile.getFileId());
    }

    /**
     * 发送消息给MQ，通知视频处理服务处理视频
     * @param mediaId
     */
    public void sendMsgToProcessMedia(String mediaId){
        // 验证id是否正确
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        // 消息
        Map map = new HashMap();
        map.put("mediaId",mediaId);
        String msg = JSON.toJSONString(map);

        // 发送消息
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK,
                routingkey_media_video,msg);
    }

    /**
     * 删除Chunk
     * @param fileId
     */
    private void deleteChunk(String fileId) {
        String chunkFolderPath = getChunkFolderPath(fileId);
        File file = new File(chunkFolderPath);
        File[] files = file.listFiles();
        for(File delFile : files){
            delFile.delete();
        }
        file.delete();;
    }


    /**
     * 检查合并出来的文件md5是否一致
     * @param mergeFile
     * @param fileMd5
     * @return
     */
    private boolean checkFileMd5(File mergeFile, String fileMd5) {
        // 创建输入流
        try {
            FileInputStream fileInputStream = new FileInputStream(mergeFile);
            String md5Hex = DigestUtils.md5Hex(fileInputStream);
            if(md5Hex.equalsIgnoreCase(fileMd5)){
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 合并文件块
     * @param fileList
     * @param mergeFile
     * @return
     */
    private File mergeFile(List<File> fileList, File mergeFile) {
        // 判断文件是否存在，如果存在先删除，再创建新文件
        if(mergeFile.exists()){
            mergeFile.delete();
        }
        try {
            mergeFile.createNewFile();

            // 文件块排序
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                        return 1;
                    }
                    return -1;
                }
            });

            // 文件合并
            RandomAccessFile writeIO = new RandomAccessFile(mergeFile,"rw");
            byte[] bytes = new byte[1024];
            for(File file : fileList){
                // 文件读写
                RandomAccessFile readIO = new RandomAccessFile(file,"r");
                int len = -1;
                while((len = readIO.read(bytes)) != -1){
                    writeIO.write(bytes,0,len);
                }
                readIO.close();
            }
            writeIO.close();
            return mergeFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取分块文件所在目录
     * @param fileMd5
     * @return
     */
    private String getChunkFolderPath(String fileMd5){
        /**
         * 文件路径为：
         * 根路径（配置的路径） + md5第一个字符/ md5第二个字符/ md5值/ chunk
         */
        String chunkFolderPath = uploadLocation +
                fileMd5.substring(0,1) + "/" +
                fileMd5.substring(1,2) + "/" +
                fileMd5 + "/chunk/";
        return chunkFolderPath;
    }

    /**
     * 获取文件相对路径
     * @param fileMd5
     * @param fileExt
     * @return
     */
    private String getRelativeFilePath(String fileMd5,String fileExt){
        /**
         * 文件路径为：
         * 根路径（配置的路径） + md5第一个字符 + md5第二个字符 + md5值 + 文件扩展名
         */
        String filePath = fileMd5.substring(0,1) + "/" +
                fileMd5.substring(1,2) + "/" +
                fileMd5 + "/";
        return filePath;
    }

    /**
     * 获取上传文件的文件夹路径
     * @param fileMd5
     * @return
     */
    private String getFileFolderPath(String fileMd5){
        /**
         * 文件路径为：
         * 根路径（配置的路径） + md5第一个字符/ md5第二个字符/ md5值/ md5值.文件扩展名
         */
        String fileFolderPath = uploadLocation +
                fileMd5.substring(0,1) + "/" +
                fileMd5.substring(1,2) + "/" +
                fileMd5 + "/";
        return fileFolderPath;
    }

    /**
     * 获取上传文件的路径
     * @param fileMd5
     * @param fileExt
     * @return
     */
    private String getFilePath(String fileMd5,String fileExt){
        /**
         * 文件路径为：
         * 根路径（配置的路径） + md5第一个字符 + md5第二个字符 + md5值 + 文件扩展名
         */
        String filePath = uploadLocation +
                fileMd5.substring(0,1) + "/" +
                fileMd5.substring(1,2) + "/" +
                fileMd5 + "/" +
                fileMd5 + "." + fileExt;
        return filePath;
    }

}
