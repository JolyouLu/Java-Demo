import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @Author: JolyouLu
 * @Date: 2021/9/11 13:42
 * @Version 1.0
 */
public class ByteBufApiTest {

    //使用ByteBufAllocator分配创建ByteBuf
    @Test
    public void ByteBufAllocator(){
        //分配ByteBuf(默认以directBuffer方式分配内存)
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        //分配ByteBuf(directBuffer分配内存，从直接内存中申请)
        ByteBuf directBuffer = ByteBufAllocator.DEFAULT.directBuffer(10);
        //分配ByteBuf(heapBuffer分配内存，从jvm堆内存中申请)
        ByteBuf heapBuffer = ByteBufAllocator.DEFAULT.heapBuffer(10);

        //由于netty 4.1之后ByteBuf使用了池化技术(内存复用技术)，所以申请的内存中可能会存在之前的的数据
        //使用ByteBufAllocator申请内存后如果没有打算覆盖整个ByteBuf那么最好先初始化一下，否则会数据混乱
        for (int i = 0; i < byteBuf.readableBytes(); i++) {
            byteBuf.writeByte(0x00);
        }
        byteBuf.resetWriterIndex();
        log(byteBuf);
    }

    //使用Unpooled工具类创建ByteBuf
    //即非池化方式创建一个ByteBuf，永远都是分配新的空内存
    @Test
    public void Unpooled(){
        //根据给定的str生成ByteBuf
        ByteBuf str2buf = Unpooled.copiedBuffer("hello,world!", CharsetUtil.UTF_8);
        //从直接内存中申请一个ByteBuf
        ByteBuf byteBuf = Unpooled.buffer(10);
        //根据byte数组生成ByteBuf
        ByteBuf byte2buf = Unpooled.wrappedBuffer(new byte[]{'H', 'e', 'l', 'l', 'o'});
    }

    //ByteBuf 内存回收控制
    @Test
    public void memFree(){
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        //引用计算器+1(引用计算器>0，内存永远不会被释放)
        byteBuf.retain();
        //引用计算器-1(引用计算器=0，内存会被释放)
        byteBuf.release();
    }

    //零拷贝,高性能操作ByteBuf
    @Test
    public void zeroCopy(){
        //slice切片，指定范围切片，切片后数据固定，无法扩容(只支持修改内容)
        System.out.println("#################################slice对数据进行切片#################################");
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a','b','c','d','e','f','g','h','i','j'});
        System.out.println("原始ByteBuf===================================");
        log(buf);
        //切片
        ByteBuf slice1 = buf.slice(0, 5);
        ByteBuf slice2 = buf.slice(5, 5);
        System.out.println("切片1内容=====================================");
        log(slice1);
        System.out.println("切片2内容=====================================");
        log(slice2);
        //修改slice1值
        slice1.setByte(0,'z');
        System.out.println("修改切片内容，原始ByteBuf也跟着变==============");
        log(buf);

        //duplicate拷贝，拷贝整个ByteBuf，可追加内容扩容(支持修改、追加内容),扩容后的内容对原始切片影响
        System.out.println("#################################duplicate对数据进行拷贝#################################");
        System.out.println("原始ByteBuf=================================");
        log(buf);
        ByteBuf duplicate = buf.duplicate();
        System.out.println("修改拷贝内容，原始ByteBuf也跟着变==============");
        slice1.setByte(0,'A');
        log(buf);
        duplicate.writeBytes(new byte[]{'1','2','3','4','5'});
        System.out.println("扩容后原始ByteBuf不会变==============");
        log(buf);
        System.out.println("扩容后拷贝的ByteBuf会改变==============");
        log(duplicate);

        //组合2个Buf，零拷贝
        System.out.println("############################CompositeByteBuf对多Buf组合############################");
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1,2,3,4,5});

        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes(new byte[]{6,7,8,9,10});
        //组合2个ByteBuf
        CompositeByteBuf buffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        buffer.addComponents(true,buf1,buf2);
        log(buffer);
    }

    //打印ByteBuf 16进制内容
    public static void log(ByteBuf buf){
        int length = buf.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder builder = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buf.readerIndex())
                .append(" write index:").append(buf.writerIndex())
                .append(" capacity:").append(buf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(builder,buf);
        System.out.println(builder.toString());
    }
}
