package top.jolyoulu.demo.protobuf.proto;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Author: LZJ
 * @Date: 2020/10/2 19:52
 * @Version 1.0
 * 自定义一个Handler 需要继承netty 规定好的HandlerAdapter(规范)
 * 这时候我们的Handler，才能称为真正的handler
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    //读取数据的事件(这里可以读取客户端发送的消息)
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        //根据dataType显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType){
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("学生: id="+student.getId()+" name="+student.getName());
        }else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType){
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人: age="+worker.getAge()+" name="+worker.getName());
        }else {
            System.out.println("类型异常");
        }
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存，并刷新
        //一般我们需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("客户端,已收到你的消息，正在处理~",CharsetUtil.UTF_8));
    }

    //处理异常，发生异常一般关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
