package top.jolyoulu.demo.protobuf.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * @Author: LZJ
 * @Date: 2020/10/2 20:08
 * @Version 1.0
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建客户端的启动对象
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        //给pipline 设置处理器Handler
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入编码器ProtobufEncoder
                            pipeline.addLast("encoder",new ProtobufEncoder());
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });
            System.out.println(".....客户端初始化完成.....");
            //启动客户端连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
}
