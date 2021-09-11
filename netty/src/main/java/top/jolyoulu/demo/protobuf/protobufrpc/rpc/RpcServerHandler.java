package top.jolyoulu.demo.protobuf.protobufrpc.rpc;

import top.jolyoulu.demo.protobuf.protobufrpc.proto.RPCRequest;
import top.jolyoulu.demo.protobuf.protobufrpc.proto.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author: JolyouLu
 * @Date: 2020/12/19 22:49
 * @Version 1.0
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RPCRequest request = (RPCRequest) msg;
        System.out.println("收到客户端请求:["+request.toString()+"]");
        ctx.writeAndFlush(createResponse(request.getId()));
    }

    private RPCResponse createResponse(long id){
        RPCResponse.Builder builder = RPCResponse.newBuilder();
        builder.setId(id);
        builder.setMethodName("sayHello"+id);
        builder.setServiceName("HelloService"+id);
        builder.setVersion(1);
        builder.setResult("HelloClient["+id+"]");
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
