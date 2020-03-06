package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Pace
 * @Data: 2020/3/5 21:45
 * @Version: v1.0
 */
@Api(value = "媒资文件上传接口",description = "媒资上传接口，上传文件，文件处理接口")
public interface MediaUploadControllerApi {

    /**
     * 文件上传前 进行一系列文件检查 及文件夹创建的接口
     * @param fileMd5 文件唯一标识
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @param mimetype 文件mimetype
     * @param fileExt 文件扩展名
     * @return
     */
    @ApiOperation("文件上传注册接口")
    ResponseResult register(String fileMd5,
                              String fileName,
                              Long fileSize,
                              String mimetype,
                              String fileExt);

    /**
     * 检查文件块是否存在
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    @ApiOperation("文件块检查接口")
    CheckChunkResult checkchunk(String fileMd5,
                              Integer chunk,
                              Integer chunkSize);

    /**
     * 文件块全部上传完毕后，进行合并，并删除之前的文件块
     * @param fileMd5 文件唯一标识
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @param mimetype 文件mimetype
     * @param fileExt 文件扩展名
     * @return
     */
    @ApiOperation("文件合并接口")
    ResponseResult mergechunks(String fileMd5,
                               String fileName,
                               Long fileSize,
                               String mimetype,
                               String fileExt);

    /**
     * 文件上传，文件块
     * @param file 文件块
     * @param chunk 第几个文件块
     * @param fileMd5 文件唯一标识
     * @return
     */
    @ApiOperation("文件上传接口")
    ResponseResult uploadchunk(MultipartFile file,
                               Integer chunk,
                               String fileMd5);

    @ApiOperation("发送消息，处理视频")
    ResponseResult mediaProcess(String fileMd5);
}
