package top.jolyoulu.demo.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author: LZJ
 * @Date: 2020/10/3 22:15
 * @Version 1.0
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channle组，管理所有的channel
    //GlobalEventExecutor.INSTANCE 是全局事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //一但建立连接，该方法就会被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其它在线的客户端
        //该方法会把channelGroup的所有channel遍历，调用writeAndFlush方法
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"加入聊天\n");
        channelGroup.add(channel);
    }

    //一但断开连接，该方法就会被执行
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //提示其它所有用户当前用户已经离线
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了\n");
        System.out.println("channelGroup size"+channelGroup.size());
    }

    //channel 处于活动状态执行该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线了~");
    }

    //channel 处于非活动状态执行该方法
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了~");
    }

    //读取数据时触发
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        //遍历channelGroup根据不同情况，回送不同消息
        channelGroup.forEach(ch -> {
            if (channel != ch){ //不是读取的channel，转发
                ch.writeAndFlush("[用户]"+channel.remoteAddress() +" 发送了消息: "+msg+"\n");
            }else {
                ch.writeAndFlush("[自己]发送了消息: "+msg+"\n");
            }
        });
    }

    //发生异常触发
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
