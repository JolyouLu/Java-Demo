package top.jolyoulu.demo.enanddecoder.fixedlength;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: JolyouLu
 * @Date: 2020/12/19 14:44
 * @Version 1.0
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new NettyServer().bind(port);
    }

    public void bind(int port) throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelHandler());
            ChannelFuture f = b.bind(port).sync();
            System.out.println(NettyServer.class.getName() + "启动成功，地址["+f.channel().localAddress()+"]上等待客户端连接");
            f.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ServerChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new FixedLengthDecode(138));
            pipeline.addLast(new NettyServerHandler());
        }
    }
}
