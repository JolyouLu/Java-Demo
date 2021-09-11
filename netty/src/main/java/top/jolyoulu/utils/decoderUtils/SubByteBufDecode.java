package top.jolyoulu.utils.decoderUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import top.jolyoulu.utils.HexUtils;

import java.util.ArrayList;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/14 21:05
 * @Version 1.0
 * 截取buf解码器
 */
public class SubByteBufDecode extends ChannelInboundHandlerAdapter {
    private ByteBuf cache;

    private final byte head;
    private final byte tail;

    public SubByteBufDecode(byte head, byte tail) {
        this.head = head;
        this.tail = tail;
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
            HexUtils.printHex(cache);
            ArrayList<ByteBuf> outputList = new ArrayList<>();
            int frameStartIndex=0;
            int frameEndIndex=0;
            int length = cache.readableBytes();
            while (frameEndIndex <= length){
                //从frameIndex-length 查找是否存在delimiter标志符
                frameStartIndex = cache.indexOf(frameStartIndex,length,head);
                frameEndIndex = cache.indexOf(frameEndIndex,length,tail);

                //如果起始位置小于小于结束位置
                if (frameEndIndex != -1 && frameEndIndex < frameStartIndex ){
                    frameEndIndex = frameStartIndex;
                    continue;
                }

                // 如果都找不到结束循环
                if (frameStartIndex == -1 && frameEndIndex == -1) {
                    break;
                }

                // 缓存存在无协议头有协议尾的内容
                //  XX XX XX XX tail
                //  XX XX XX XX tail XX XX XX XX tail
                if (frameStartIndex == -1 && frameEndIndex != -1){
                    int lastTailIndex = cache.indexOf(length, 0, tail);
                    frameEndIndex = lastTailIndex + 1;
                    cache.readerIndex(frameEndIndex);
                    break;
                }

                // 缓存存在有协议头无协议尾的内容
                // head XX XX XX XX
                // head XX XX XX XX head XX XX XX XX
                if (frameStartIndex != -1 && frameEndIndex == -1){
                    int lastHeadIndex = cache.indexOf(length, 0, head);
                    frameStartIndex = lastHeadIndex;
                    cache.readerIndex(frameStartIndex);
                    break;
                }

                //如果查到了 从起始点到搜到那个位置的数据加入到容器中
                int readStart = frameStartIndex + 1;
                int readEnd = frameEndIndex;
                cache.readerIndex(readStart);
//                HexUtils.printHex(cache.readBytes(readEnd - readStart));
                outputList.add(cache.readBytes(readEnd - readStart));
                cache.skipBytes(1);
                //把起始点改为读取到的最后点
                frameStartIndex += 1;
                frameEndIndex += 1;
            }

            //清理前面已读的数据
            cache.discardReadBytes();

            //遍历容器将来拆分的包一个一个传入到下一个Handler中
            for (ByteBuf byteBuf : outputList) {
                HexUtils.printHex(byteBuf);
                ctx.fireChannelRead(byteBuf);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            data.release();
        }
    }

    /**
     * 扩容缓存
     */
    private static ByteBuf expandCache(ByteBufAllocator alloc, ByteBuf cache, int readable) {
        ByteBuf oldCache = cache;
        cache = alloc.buffer(oldCache.readableBytes() + readable);
        cache = cache.writeBytes(oldCache);
        oldCache.release();
        return cache;
    }
}
