package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Pace
 * @Data: 2020/3/6 14:38
 * @Version: v1.0
 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    private MediaUploadService mediaUploadService;

    @Override
    @PostMapping("/register")
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        mediaUploadService.register(fileMd5,fileName,fileSize,mimetype,fileExt);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @PostMapping("/checkchunk")
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        boolean fileExist = mediaUploadService.checkchunk(fileMd5,chunk,chunkSize);
        return new CheckChunkResult(CommonCode.SUCCESS,fileExist);
    }

    @Override
    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        mediaUploadService.mergechunks(fileMd5,fileName,fileSize,mimetype,fileExt);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5) {
        mediaUploadService.uploadchunk(file,chunk,fileMd5);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @GetMapping("/process/{id}")
    public ResponseResult mediaProcess(@PathVariable("id") String fileMd5) {
        mediaUploadService.sendMsgToProcessMedia(fileMd5);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
