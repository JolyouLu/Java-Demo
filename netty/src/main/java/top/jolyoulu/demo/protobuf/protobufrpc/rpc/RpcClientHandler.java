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
public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 2; i++) {
            ctx.write(createRpcRequest(i));
        }
        ctx.flush();
    }

    private RPCRequest createRpcRequest(int id){
        RPCRequest.Builder builder = RPCRequest.newBuilder();
        builder.setId(id);
        builder.setMethodName("sayHello"+id);
        builder.setServiceName("HelloService"+id);
        builder.setVersion(1);
        builder.setParameters("method parameters:"+id);
        return builder.build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RPCResponse response = (RPCResponse) msg;
        System.out.println("服务器回复消息: "+response.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
