package top.jolyoulu.demo.protobuf.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/**
 * @Author: LZJ
 * @Date: 2020/10/2 19:32
 * @Version 1.0
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建BossGroup和WorkerGroup
        //1.创建2个线程组 分别是bossGroup 和 workerGroup
        //2.bossGroup只处理连接请求，workerGroup只处理业务
        //3.运行时2个都是死循环
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务器端的启动对象，配置启动参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup) //设置2个线程组
                    .channel(NioServerSocketChannel.class) //设置将来服务器通道实现是NioServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG,128) //设置线程队列等待连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //使用匿名方式 创建一个通道初始对象
                        //给pipline 设置处理器Handler
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            System.out.println("客户端socketChannel hashCode="+ch.hashCode());
                            //指定对那种对象进行解码
                            pipeline.addLast("decoder",new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    }); //设置workerGroup的EventLoopGroup对应的处理器

            System.out.println(".....服务器初始化完成.....");

            //绑定一个端口并且同步，生成一个ChannelFuture
            ChannelFuture cf = bootstrap.bind(6666).sync();
            //给cf注册监听器，监控我们关系的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()){
                        System.out.println("监听到，端口绑定成功！");
                    }else {
                        System.out.println("监听到，端口绑定失败！");
                    }
                }
            });
            System.out.println("ssssssssssssssssssss");
            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
