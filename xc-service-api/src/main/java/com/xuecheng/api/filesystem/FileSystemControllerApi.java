package com.xuecheng.api.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Pace
 * @Data: 2020/2/28 15:41
 * @Version: v1.0
 */
@Api(value = "文件管理系统",description = "文件管理的接口，提供文件的上传下载查询")
public interface FileSystemControllerApi {

    /**
     * 文件上传
     * @param multipartFile 文件
     * @param filetag 文件标签
     * @param businesskey 调用者服务key
     * @param metadata 文件元信息，传json
     * @return
     */
    @ApiOperation("文件上传")
    UploadFileResult upload(MultipartFile multipartFile,
                                   String filetag,
                                   String businesskey,
                                   String metadata);
}
