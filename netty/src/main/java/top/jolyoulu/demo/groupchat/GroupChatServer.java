package top.jolyoulu.demo.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: LZJ
 * @Date: 2020/10/3 22:04
 * @Version 1.0
 */
public class GroupChatServer {

    private int port; //监听端口

    public GroupChatServer(int port) {
        this.port = port;
    }

    //编写run方法，处理客户端请求
    public void run() throws InterruptedException {
        //创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //pipeline加入解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //pipeline加入编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            //加入组件的业务处理的handler
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });
            System.out.println("netty 服务器启动");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //监听关闭
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(6666).run();
    }
}
