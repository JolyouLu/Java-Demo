package top.jolyoulu.demo.protobuf.protobufrpc.rpc;


import top.jolyoulu.demo.protobuf.protobufrpc.proto.RPCResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @Author: JolyouLu
 * @Date: 2020/12/19 22:48
 * @Version 1.0
 */
public class RpcClient {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
        new RpcClient().connect(port,"127.0.0.1");
    }

    public void connect(int port,String host) throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChildChannelHandler());
            ChannelFuture f = b.connect(host, port).sync();
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }

    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new ProtobufVarint32FrameDecoder());
            pipeline.addLast(new ProtobufDecoder(RPCResponse.getDefaultInstance()));
            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
            pipeline.addLast(new ProtobufEncoder());
            pipeline.addLast(new RpcClientHandler());
        }
    }
}
