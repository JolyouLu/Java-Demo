package top.jolyoulu.demo.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @Author: LZJ
 * @Date: 2020/10/3 23:05
 * @Version 1.0
 */
public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //得到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //pipeline加入解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //pipeline加入编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            //加入自定义handler
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            System.out.println("----------"+channel.localAddress()+"----------");
            //客户端需要输入消息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String msg = scanner.nextLine();
                //通过channel 发生到服务器端
                channel.writeAndFlush(msg+"\r\n");
            }
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatClient("localhost",6666).run();
    }
}
