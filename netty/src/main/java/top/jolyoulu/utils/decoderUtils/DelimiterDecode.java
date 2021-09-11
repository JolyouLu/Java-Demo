package top.jolyoulu.utils.decoderUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;

/**
 * @Author: JolyouLu
 * @Date: 2020/12/19 16:29
 * @Version 1.0
 * 分隔符解码
 */
public class DelimiterDecode extends ChannelInboundHandlerAdapter {

    /**
     * 缓冲区
     */
    private ByteBuf cache;
    /**
     * 分隔符
     */
    private final byte delimiter;

    public DelimiterDecode(ByteBuf delimiter) {
        if (delimiter == null){
            throw new NullPointerException("delimiter");
        }
        if (!delimiter.isReadable()){
            throw new IllegalArgumentException("empty delimiter");
        }
        this.delimiter = delimiter.readByte();
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
                if (cache.writerIndex() > cache.maxCapacity() - data.readableBytes()) {
                    //扩容
                    cache = expandCache(ctx.alloc(), cache, data.readableBytes());
                }
            }
            //把新的数据 追加到缓冲区后面
            cache.writeBytes(data);

            ArrayList<ByteBuf> outputList = new ArrayList<>();
            int frameIndex=0;
            int frameEndIndex=0;
            int length = cache.readableBytes();
            while (frameEndIndex <= length){
                //从frameIndex-length 查找是否存在delimiter标志符
                frameEndIndex = cache.indexOf(frameIndex+1,length,delimiter);

                //搜不到标识符表示没包，把之前的丢弃
                if (frameEndIndex == -1){
                    cache.discardReadBytes();
                    break;
                }

                //如果查到了 从起始点到搜到那个位置的数据加入到容器中
                outputList.add(cache.readBytes(frameEndIndex - frameIndex));
                //跳过分隔符
                cache.skipBytes(1);
                //把起始点改为读取到的最后点
                frameEndIndex = frameEndIndex + 1;
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
