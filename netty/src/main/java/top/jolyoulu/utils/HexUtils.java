package top.jolyoulu.utils;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @Author: JolyouLu
 * @Date: 2021/6/2 9:20
 * @Version 1.0
 */
public class HexUtils {

    private static char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };
    private static char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
            'F' };

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
     * 打印16进制内容
     * @param bData byte[]
     */
    public static void printHex(byte[] bData){
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < bData.length; i++) {
            buffer.append(byte2Hex(bData[i])).append(" ");
        }
        println(buffer.toString());
    }

    /**
     * byte字节转16进制字符串
     * @param bData byte
     * @return string
     */
    public static String byte2Hex(byte bData){
        char[] str = new char[2];
        str[0] = DIGITS_UPPER[(bData>>4)&0x0f];
        str[1] = DIGITS_UPPER[(bData>>0)&0x0f];
        return new String(str);
    }

    /**
     * byte数组转16进制字符串
     * @param bData
     * @return
     */
    public static String byteArray2Hex(byte[] bData){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bData.length; i++) {
            builder.append(byte2Hex(bData[i]));
        }
        return builder.toString();
    }

    /**
     * ByteBuf 转16进制字符串
     * @param buf
     * @return
     */
    public static String byteBuf2Hex(ByteBuf buf){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buf.readableBytes(); i++) {
            builder.append(byte2Hex(buf.getByte(i)));
        }
        return builder.toString();
    }

    /**
     * 所有方法的打印方法，预留用于方便修改打印格式
     * @param str
     */
    private static void println(String str){
        System.out.println(str);
    }

}
