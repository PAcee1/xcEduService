package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.constant.MediaConstant;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: Pace
 * @Data: 2020/3/6 17:04
 * @Version: v1.0
 */
@Component
@Slf4j
public class MediaProcessTask {

    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpegPath;
    @Value("${xc-service-manage-media.video-location}")
    private String videoLocation;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @RabbitListener(
            queues = "${xc-service-manage-media.mq.queue-media-video-processor}",
            containerFactory = "customContainerFactory")
    public void reveiveMediaProcessTask(String msg){
        // 1.解析消息，获取mediaID
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String) map.get("mediaId");

        // 2.向数据库查询媒体信息
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if(!optional.isPresent()){
            // TODO 可以后期改进，比如添加到日志，由ELK管理，这样更直观的发现错误
            log.error("media processing failed,the reason is mediaId illegal,mediaId ：{}",mediaId);
            return ;
        }
        MediaFile mediaFile = optional.get();
        // 判断文件是否以.avi结尾
        if(!mediaFile.getFileType().equalsIgnoreCase("avi")){
            // 不处理
            mediaFile.setProcessStatus(MediaConstant.FILE_PROCESS_STATUS_4);
            mediaFileRepository.save(mediaFile);
            return;
        }else {
            // 设置处理状态为正在处理
            mediaFile.setProcessStatus(MediaConstant.FILE_PROCESS_STATUS_1);
            mediaFileRepository.save(mediaFile);
        }

        // 3.根据文件路径使用工具类将avi生成mp4文件
        boolean toMp4Flag = toMp4(mediaFile);
        if(!toMp4Flag){
            return;
        }

        // 4.将mp4生成m3u8和ts文件
        toM3u8(mediaFile);
    }

    /**
     * 将avi转mp4文件
     * @param mediaFile
     * @return
     */
    private boolean toMp4(MediaFile mediaFile){
        // ffmpeg所在路径
        // 获取文件所在路径 根路径 + 相对路径 + 名称
        String filePath = videoLocation + mediaFile.getFilePath() + mediaFile.getFileName();
        // 转成mp4文件名称
        String mp4Name = mediaFile.getFileId() + ".mp4";
        // mp4文件所在文件夹
        String mp4Folder = videoLocation + mediaFile.getFilePath();
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath,filePath,mp4Name,mp4Folder);
        // 转换
        String mp4Result = mp4VideoUtil.generateMp4();
        // 判断是否成功
        if(mp4Result == null || !mp4Result.equals("success")){
            // 失败，记录失败原因保存数据库
            mediaFile.setProcessStatus(MediaConstant.FILE_PROCESS_STATUS_3);
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(mp4Result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            log.error("media processing failed,the reason is avi to mp4 fail," +
                    "fail mediaId：{},fail reason ：{}",mediaFile.getFileId(),mp4Result);
            return false;
        }
        return true;
    }

    /**
     * mp4转m3u8
     * @param mediaFile
     */
    private void toM3u8(MediaFile mediaFile){
        // String ffmpeg_path, String video_path, String m3u8_name,String m3u8folder_path
        // mp4文件路径
        String mp4Path = videoLocation + mediaFile.getFilePath() + mediaFile.getFileId() + ".mp4";;
        // m3u8文件名称
        String m3u8Name = mediaFile.getFileId() + ".m3u8";
        // m3u8文件夹位置
        String m3u8Folder =  videoLocation + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpegPath,mp4Path,m3u8Name,m3u8Folder);
        // 转码
        String m3u8Result = hlsVideoUtil.generateM3u8();

        // 判断结果
        // 判断是否成功
        if(m3u8Result == null || !m3u8Result.equals("success")){
            // 失败，记录失败原因保存数据库
            mediaFile.setProcessStatus(MediaConstant.FILE_PROCESS_STATUS_3);
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(m3u8Result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            log.error("media processing failed,the reason is avi to mp4 fail," +
                    "fail mediaId：{},fail reason ：{}",mediaFile.getFileId(),m3u8Result);
            //return false;
            return ;
        }

        // 结果正确，更新mediaFile对象
        mediaFile.setProcessStatus(MediaConstant.FILE_PROCESS_STATUS_2);
        // 保存m3u8中的list
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        // 保存播放地址最终URL
        String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8Name;
        mediaFile.setFileUrl(fileUrl);
        mediaFileRepository.save(mediaFile);

        //return true;
    }
}
