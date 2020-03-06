package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: Pace
 * @Data: 2020/3/5 20:15
 * @Version: v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFile {

    /**
     * 测试文件分块
     */
    @Test
    public void testChunk() throws IOException {
        // 文件位置
        File file = new File("D:\\Videos\\学成在线\\13-在线学习 HLS\\资料\\lucene.avi");

        // 设置文件分块后保存位置
        String chunkFileFolder = "D:\\Videos\\学成在线\\13-在线学习 HLS\\资料\\fileChunk\\";

        // 设置每块大小
        long chunkFileSize = 1 * 1024 * 1024;
        // 块的数量
        long chunkFileNum = (long)Math.ceil((file.length() * 1.0) / chunkFileSize);

        //读取文件
        RandomAccessFile readIO = new RandomAccessFile(file,"r");
        // 循环分块输出
        for (int i = 0; i < chunkFileNum; i++) {
            // 创建写文件
            File outFile = new File(chunkFileFolder + i); // 输出0,1,2,3,4,等文件块
            RandomAccessFile writeIO = new RandomAccessFile(outFile,"rw");

            // 写文件
            byte[] bytes = new byte[1024];
            int len = -1;
            while((len = readIO.read(bytes)) != -1){
                // 写文件
                writeIO.write(bytes);
                // 如果超过块大小，跳出
                if(outFile.length() > chunkFileSize){
                    break;
                }
            }

            writeIO.close();
        }
        readIO.close();
    }

    /**
     * 测试合并
     */
    @Test
    public void testMerge() throws IOException {
        // 合并文件夹
        File mergeFolder = new File("D:\\Videos\\学成在线\\13-在线学习 HLS\\资料\\fileChunk\\");
        // 目标文件
        File file = new File("D:\\Videos\\学成在线\\13-在线学习 HLS\\资料\\fileMerge.avi");
        // 创建文件
        file.createNewFile();

        // 处理需要合并的文件，按文件名升序
        File[] files = mergeFolder.listFiles();
        List<File> list = Arrays.asList(files);
        Collections.sort(list, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())){
                    return 1;
                }
                return -1;
            }
        });

        // 文件读取写入
        byte[] bytes = new byte[1024];
        RandomAccessFile writeIO = new RandomAccessFile(file,"rw");
        for (File merageFile : list){
            RandomAccessFile readIO = new RandomAccessFile(merageFile,"r");
            int len = -1;
            while ((len = readIO.read(bytes)) != -1){
                writeIO.write(bytes,0,len);
            }
            readIO.close();
        }
        writeIO.close();
    }

    @Test
    public void deleteChunk(){
        String chunkFolderPath = "D:\\Github\\xcEdu\\xcEduVideos\\5\\f\\chunk";
        File file = new File(chunkFolderPath);
        File[] files = file.listFiles();
        for(File delFile : files){
            delFile.delete();
        }
        file.delete();
    }
}
