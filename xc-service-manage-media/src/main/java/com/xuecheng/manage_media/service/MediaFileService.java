package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Pace
 * @Data: 2020/3/6 20:33
 * @Version: v1.0
 */
@Service
public class MediaFileService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    /**
     * 查询媒资列表
     * @param page
     * @param size
     * @param queryMediaFileRequest
     * @return
     */
    public QueryResult<MediaFile> list(int page,
                                       int size,
                                       QueryMediaFileRequest queryMediaFileRequest) {
        if(queryMediaFileRequest == null){
            queryMediaFileRequest = new QueryMediaFileRequest();
        }
        // 条件值对象
        MediaFile mediaFile = new MediaFile();
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getTag())){
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())){
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if(StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())){
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }

        // 设置查询条件

        // 设置条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("tag",ExampleMatcher.GenericPropertyMatchers.contains()) // 模糊匹配
                .withMatcher("fileOriginalName",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("processStatus",ExampleMatcher.GenericPropertyMatchers.exact()); // 精确匹配

        Example<MediaFile> example = Example.of(mediaFile,exampleMatcher);

        // 分页设置
        int realpage = page < 1 ? 0 : page - 1;
        Pageable pageable = new PageRequest(realpage,size);

        // 查询
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        long totalElements = all.getTotalElements();
        List<MediaFile> content = all.getContent();
        QueryResult<MediaFile> queryResult = new QueryResult<>();
        queryResult.setList(content);
        queryResult.setTotal(totalElements);
        return queryResult;
    }
}
