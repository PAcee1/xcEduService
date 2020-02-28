package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author: Pace
 * @Data: 2020/2/28 16:37
 * @Version: v1.0
 */
@Service
public class FileSystemService {

    // 注入FastDFS配置
    @Value("${xuecheng.fastdfs.tracker_servers}")
    private String trackerServers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    private Integer connectTimeoutInSeconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    private Integer networkTimeoutInSeconds;
    @Value("${xuecheng.fastdfs.charset}")
    private String charset;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    /**
     * 上传文件
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     */
    public FileSystem upload(MultipartFile multipartFile,
                       String filetag,
                       String businesskey,
                       String metadata){
        //  先判断文件是否存在
        if(multipartFile == null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }

        // 一、上传文件到FastDFS，并获取fileId
        String fileId = uploadToFastDFS(multipartFile);
        if(fileId == null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }

        // 二、将文件信息保存到文件数据库 MongoDB
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileType(multipartFile.getContentType());
        if(StringUtils.isNotEmpty(metadata)){
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemRepository.save(fileSystem);
        return fileSystem;
    }

    /**
     * 上传文件到FDFS
     * @param multipartFile
     * @return
     */
    private String uploadToFastDFS(MultipartFile multipartFile){
        // 首先需要初始化FDFS配置
        initFDFSConfig();

        // 获取Tracker客户端
        TrackerClient trackerClient = new TrackerClient();
        try {
            // 获取Tracker服务器
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取Storage服务器
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 获取Storage客户端
            StorageClient1 storageClient = new StorageClient1(trackerServer,storageServer);

            // 上传文件
            byte[] bytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = storageClient.upload_file1(bytes, ext, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        return null;
    }

    /**
     * 初始化Tracker
     */
    private void initFDFSConfig(){
        try {
            ClientGlobal.initByTrackers(trackerServers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_connect_timeout(connectTimeoutInSeconds);
            ClientGlobal.setG_network_timeout(networkTimeoutInSeconds);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常，抛出
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }
}
