package top.jolyoulu.myrediscli.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author: JolyouLu
 * @Date: 2021/7/24 1:33
 * @Version 1.0
 */
public class Protocol {

    public static final String DOLLAR_BYTE = "$";
    public static final String STAR_BYTE = "*";
    public static final String FRAME_BYTE = "\r\n";

    /**
     * Redis协议
     * *3 数组3(共有3段文本)
     * $3 第一段文本：字符串长度3
     * SET
     * $4 第二段文本：字符串长度4
     * test
     * $3 第三段文本：字符串长度3
     * 123
     *
     * @param out
     * @param command
     * @param bytes
     */
    public static void sendCommand(OutputStream out,Protocol.Command command, byte[] ...bytes){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(STAR_BYTE).append(bytes.length+1).append(FRAME_BYTE);
        stringBuffer.append(DOLLAR_BYTE).append(command.name().length()).append(FRAME_BYTE);
        stringBuffer.append(command).append(FRAME_BYTE);
        for (byte[] arg : bytes) {
            stringBuffer.append(DOLLAR_BYTE).append(arg.length).append(FRAME_BYTE);
            stringBuffer.append(new String(arg)).append(FRAME_BYTE);
        }
        try {
            out.write(stringBuffer.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static enum Command{
        SET,GET
    }

}
