package top.jolyoulu.demo.enanddecoder.delimiter;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author: JolyouLu
 * @Date: 2020/12/19 14:44
 * @Version 1.0
 */
public class NettyClient {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new NettyClient().connect(port,"127.0.0.1");
    }

    public void connect(int port,String host) throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChildChannelHandler());
            ChannelFuture f = b.connect(host, port).sync();
        }finally {
            group.shutdownGracefully();
        }

    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new DelimiterDecode(Unpooled.wrappedBuffer(new byte[]{'#'})));
            pipeline.addLast(new NettyClientHandler());
        }
    }
}
