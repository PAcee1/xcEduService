package com.xuecheng.order.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.task.XcTask;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * @Author: Pace
 * @Data: 2020/3/16 14:55
 * @Version: v1.0
 */
public interface XcTaskRepository extends JpaRepository<XcTask,String> {

    // 分页查询
    Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

    // 更新任务
    @Modifying
    @Query("update XcTask t set t.updateTime = :updateTime where t.id = :id")
    int updateTaskTime(@Param("updateTime") Date updateTime,
                       @Param("id") String id);

    // 乐观锁控制
    @Modifying
    @Query("update XcTask t set t.version = :version+1 where t.id = :id and t.version = :version")
    public int updateTaskVersion(@Param(value = "id") String id,@Param(value = "version") int version);
}
