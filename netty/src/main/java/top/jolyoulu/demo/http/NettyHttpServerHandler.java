package top.jolyoulu.demo.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @Author: LZJ
 * @Date: 2020/10/3 15:11
 * @Version 1.0
 * SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter 子类
 * 指定客户端和服务端通讯的数据都是 HttpObject类型
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //客户端有读取事件时发生
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg 是不是 httprequest请求
        if (msg instanceof HttpRequest){
            System.out.println("pipeline="+ctx.pipeline().hashCode()+" channel="+ctx.pipeline().channel().hashCode());
            System.out.println("msg 类型="+msg.getClass());
            System.out.println("客户端地址="+ctx.channel().remoteAddress());
            //得到httpRequest
            HttpRequest httpRequest= (HttpRequest) msg;
            //获取url
            URI uri = new URI(httpRequest.uri());
            //获取过滤指定资源
            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了favicon.ico不做响应");
                return;
            }
            //回复信息给浏览器[封装http协议]
            ByteBuf content = Unpooled.copiedBuffer("hell,我是服务器", CharsetUtil.UTF_8);
            //构造一个http的相应，即httpresponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LANGUAGE,content.readableBytes());
            //将构建号的response返回
            ctx.writeAndFlush(response);
        }
    }

}
