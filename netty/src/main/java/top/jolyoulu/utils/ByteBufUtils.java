package top.jolyoulu.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/14 21:19
 * @Version 1.0
 */
public class ByteBufUtils {

    /**
     * 对ByteBuf进行扩容
     * @param buf 需扩容的ByteBuf
     * @param size 需扩容多大的size
     * @return 扩容后的ByteBuf
     */
    public static ByteBuf expand(ByteBuf buf, int size) {
        ByteBuf oldCache = buf;
        buf = ByteBufAllocator.DEFAULT.buffer(oldCache.readableBytes() + size);
        buf = buf.writeBytes(oldCache);
        oldCache.release();
        return buf;
    }

    /**
     * 打印16进制内容
     * @param buf
     */
    public static void printHex(ByteBuf buf){
        int length = buf.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder builder = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buf.readerIndex())
                .append(" write index:").append(buf.writerIndex())
                .append(" capacity:").append(buf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(builder,buf);
        println(builder.toString());
    }

    /**
     * 所有方法的打印方法，预留用于方便修改打印格式
     * @param str
     */
    private static void println(String str){
        System.out.println(str);
    }

}
