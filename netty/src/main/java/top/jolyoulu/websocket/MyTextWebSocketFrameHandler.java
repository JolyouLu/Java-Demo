package top.jolyoulu.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Base64;

/**
 * @Author: LZJ
 * @Date: 2020/10/4 13:21
 * @Version 1.0
 * TextWebSocketFrame 表示一个文本帧(frame)
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息:"+msg.text());
        String[] split = msg.text().split(",");
        if (split[0] != null && split[0].contains("image/jpeg;base64")){
            String imgByte = split[1];
            File file = new File("C:\\Users\\mi\\Downloads\\img.jpg");
            //获取文件通道
            FileChannel channel = new FileOutputStream(file).getChannel();
            byte[] decode = Base64.getDecoder().decode(imgByte.getBytes());
            //分配一块内存块，写入byte，
            ByteBuffer buffer = ByteBuffer.allocate(decode.length);
            buffer.put(decode);
            //读写切换
            buffer.flip();
            //写入通道，后关闭
            channel.write(buffer);
            channel.close();
        }
        Channel channel = ctx.channel();
        //遍历channelGroup根据不同情况，回送不同消息
        channelGroup.forEach(ch -> {
            if (channel != ch){ //不是读取的channel，转发
                ch.writeAndFlush(new TextWebSocketFrame("[用户]"+channel.remoteAddress() +" 发送了消息: "+msg.text()));
            }else {
                ch.writeAndFlush(new TextWebSocketFrame("[自己]发送了消息: "+msg.text()));
            }
        });
    }

    //当客户端连接后，会触发
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户端
        //该方法会把channelGroup的所有channel遍历，调用writeAndFlush方法
        channelGroup.writeAndFlush(new TextWebSocketFrame("[客户端]"+channel.remoteAddress()+"加入聊天"));
        channelGroup.add(channel);
    }

    //当客户端断开连接后，会触发
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //提示其它所有用户当前用户已经离线
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(new TextWebSocketFrame("[客户端]"+channel.remoteAddress()+"离开了"));
        System.out.println("channelGroup size"+channelGroup.size());
    }

    //channel 处于活动状态执行该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().id().asLongText()+"上线了~");
    }

    //channel 处于非活动状态执行该方法
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().id().asLongText()+"离线了~");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生"+cause.getMessage());
        ctx.close(); //关闭连接
    }
}
