package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaFileControllerApi;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Pace
 * @Data: 2020/3/6 14:38
 * @Version: v1.0
 */
@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {

    @Autowired
    private MediaFileService mediaFileService;


    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<MediaFile> list(@PathVariable int page,
                                               @PathVariable int size,
                                               QueryMediaFileRequest queryMediaFileRequest) {
        QueryResult<MediaFile> queryResult = mediaFileService.list(page,size,queryMediaFileRequest);
        return new QueryResponseResult<MediaFile>(CommonCode.SUCCESS,queryResult);
    }
}
