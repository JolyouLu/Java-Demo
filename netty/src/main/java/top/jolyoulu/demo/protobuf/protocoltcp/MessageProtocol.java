package top.jolyoulu.demo.protobuf.protocoltcp;

/**
 * @Author: LZJ
 * @Date: 2020/10/6 17:14
 * @Version 1.0
 * 协议包
 */
public class MessageProtocol {
    private int len; //关键
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
