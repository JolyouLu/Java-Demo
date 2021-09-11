package top.jolyoulu.demo.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: JolyouLu
 * @Date: 2020/11/29 13:28
 * @Version 1.0
 */
public class NettyClient {

    //创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;

    //编写方法使用代理模式获取代理对象
    public Object getBean(final Class<?> serviceClass,final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {serviceClass},(proxy,method,args) ->{
                    if (client == null){
                        initClient();
                    }
                    //设置要发送给服务端的消息 providerName:协议头 args:调用时的第一个传输
                    client.setPara(providerName+args[0]);
                    return executor.submit(client).get();
                });
    }

    //初始化客户端
    private static void initClient(){
        client = new NettyClientHandler();

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(client);
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
