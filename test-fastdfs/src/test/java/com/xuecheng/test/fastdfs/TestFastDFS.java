package com.xuecheng.test.fastdfs;

import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Author: Pace
 * @Data: 2020/2/27 20:52
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    @Test
    public void upload() throws Exception {
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        //创建客户端
        TrackerClient trackerClient = new TrackerClient();
        //连接tracker Server
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取一个storage server
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        //创建一个storage存储客户端 , 使用StorageClient1 因为它是新版本扩展的类，功能更强
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
        String item = "E:\\pic\\photo.jpg";
        String fileid = storageClient1.upload_file1(item, "jpg", null);
        System.out.println("Upload local file " + item + " ok, fileid=" + fileid);
    }

    @Test
    public void download() throws Exception {
        // 加载配置文件
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        // 创建Tracker客户端
        TrackerClient trackerClient = new TrackerClient();
        // 根据客户端获取Tracker Server
        TrackerServer trackerServer = trackerClient.getConnection();
        // 然后通过Tracker Server选择一个Storage Server
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        // 通过获取到的Storage Server创建Storage Client来请求Storage
        StorageClient1 storageClient = new StorageClient1(trackerServer,storageServer);

        // 根据id获取文件
        byte[] bytes = storageClient.download_file1("group1/M00/00/00/wKg4eF4LkeaAewbOAACWdQKHIuQ261.jpg");

        // 保存文件
        FileOutputStream fileOutputStream = new FileOutputStream(
                new File("E:\\pic\\photo_fdfs.jpg"));
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    @Test
    public void search() throws Exception {
        // 加载配置文件
        ClientGlobal.initByProperties("config/fastdfs-client.properties");
        // 创建Tracker客户端
        TrackerClient trackerClient = new TrackerClient();
        // 根据客户端获取Tracker Server
        TrackerServer trackerServer = trackerClient.getConnection();
        // 然后通过Tracker Server选择一个Storage Server
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        // 通过获取到的Storage Server创建Storage Client来请求Storage
        StorageClient1 storageClient = new StorageClient1(trackerServer,storageServer);

        // 查询文件信息
        FileInfo fileInfo = storageClient.query_file_info1("group1/M00/00/00/wKg4eF4LkeaAewbOAACWdQKHIuQ261.jpg");
        System.out.println(fileInfo);
    }
}
