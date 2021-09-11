package top.jolyoulu.utils.decoderUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;

/**
 * @Author: JolyouLu
 * @Date: 2020/12/19 15:20
 * @Version 1.0
 * 定长拆包 解码器
 */
public class FixedLengthDecode extends ChannelInboundHandlerAdapter {

    /**
     * 缓冲区
     */
    private ByteBuf cache;
    /**
     * 指定拆包长度
     */
    private final int frameLength;

    public FixedLengthDecode(int frameLength) {
        this.frameLength = frameLength;
    }

    /**
     * 扩容缓存
     *
     * @param alloc
     * @param cache
     * @param readable
     * @return
     */
    private static ByteBuf expandCache(ByteBufAllocator alloc, ByteBuf cache, int readable) {
        ByteBuf oldCache = cache;
        cache = alloc.buffer(oldCache.readableBytes() + readable);
        cache = cache.writeBytes(oldCache);
        oldCache.release();
        return cache;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        try {
            //如果缓存空
            if (cache == null) {
                //申请1024size的缓存
                cache = ctx.alloc().buffer(1024);
            } else {
                //判断缓冲区是否需要扩容
                if (cache.writerIndex() > cache.maxCapacity() - data.readableBytes()){
                    //扩容
                    cache = expandCache(ctx.alloc(),cache,data.readableBytes());
                }
            }
            //把新的数据 追加到缓冲区后面
            cache.writeBytes(data);

            ArrayList<ByteBuf> outputList = new ArrayList<>();
            //判断如果缓冲区数据可读长度 > 帧（定长包）的程度
            while (cache.readableBytes() >= frameLength){
                //读取帧的长度的数据放入到ByteBuf容器中
                outputList.add(cache.readBytes(frameLength));
            }

            //判断cache拆分完后是否还存在数据
            if (cache.isReadable()){
                //存在将剩余的数据移到最前面
                cache.discardReadBytes();
            }

            //遍历容器将来拆分的包一个一个传入到下一个Handler中
            for (ByteBuf byteBuf : outputList) {
                ctx.fireChannelRead(byteBuf);
            }
        }finally {
            data.release();
        }

    }
}
