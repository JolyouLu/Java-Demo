package top.jolyoulu.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * @Author: JolyouLu
 * @Date: 2022/8/14 15:45
 * @Version 1.0
 */
public class HDEFClient {
    public static void main(String[] args) throws Exception {
        //定义配置
        Configuration conf = new Configuration();
        //获取一个hdfs客户端
        //参数1：hdfs地址
        //参数2：配置
        //参数3：使用什么用户访问
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop52:9000"),conf,"root");
        //在hdfs上创建路径
        fs.mkdirs(new Path("/java/client"));
        //关闭资源
        fs.close();
        System.out.println("over");
    }

    //文件上传
    @Test
    public void testCopyFromLocalFile() throws Exception{
        //1.获取fs对象
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop52:9000"),conf,"root");
        //2.执行上传api
        fs.copyFromLocalFile(new Path("C:\\Users\\mi\\Downloads\\testt.txt"),new Path("/testt.txt"));
        //3.关闭资源
        fs.close();
    }

    //文件下载
    @Test
    public void testCopyToLocalFile() throws Exception{
        //1.获取fs对象
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop52:9000"),conf,"root");
        //2.执行
        fs.copyToLocalFile(new Path("/jdk-7u80-linux-x64.tar.gz"),new Path("C:\\Users\\mi\\Downloads\\jdk7.tar.gz"));
        //3.关闭资源
        fs.close();
    }

    //文件删除
    @Test
    public void testDeleteFile() throws Exception{
        //1.获取fs对象
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop52:9000"),conf,"root");
        //2.执行
        fs.delete(new Path("/java"),true);
        //3.关闭资源
        fs.close();
    }

    //文件改名
    @Test
    public void testRename() throws Exception{
        //1.获取fs对象
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop52:9000"),conf,"root");
        //2.执行
        fs.rename(new Path("/jdk-7u80-linux-x64.tar.gz"),new Path("/jdk7.tar.gz"));
        //3.关闭资源
        fs.close();
    }

    //文件详情
    @Test
    public void testListFile() throws Exception{
        //1.获取fs对象
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop52:9000"),conf,"root");
        //2.执行
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
        while (listFiles.hasNext()){
            LocatedFileStatus next = listFiles.next();
            System.out.println("文件名称："+next.getPath().getName());
            System.out.println("文件权限："+next.getPermission());
            System.out.println("文件大小："+next.getLen());
            System.out.println("块信息：");
            for (BlockLocation blockLocation : next.getBlockLocations()) {
                System.out.println("块存储的目标主机："+Arrays.asList(blockLocation.getHosts()));
            }
            System.out.println("===========================");
        }
        //3.关闭资源
        fs.close();
    }

    //文件或文件夹判断
    @Test
    public void testListStatus() throws Exception{
        //1.获取fs对象
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop52:9000"),conf,"root");
        //2.执行
        FileStatus[] listStatus = fs.listStatus(new Path("/"));
        for (FileStatus status : listStatus) {
            if (status.isFile()){
                System.out.println("这是一个文件："+status.getPath().getName());
            }else {
                System.out.println("这是一个文件夹："+status.getPath().getName());
            }
        }
        //3.关闭资源
        fs.close();
    }
}
