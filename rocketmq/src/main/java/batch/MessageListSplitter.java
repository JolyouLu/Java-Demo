package batch;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: JolyouLu
 * @Date: 2021/8/11 21:03
 * @Version 1.0
 */
public class MessageListSplitter implements Iterator<List<Message>> {
    //指定最大4M
    private final int SIZE_LIMIT = 1204 * 1024 * 4;
    //存放所有需要发送的消息
    private final List<Message> messages;
    //拆分到的消息下标
    private int currIndex;

    public MessageListSplitter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        int totalSize = 0;
        for (;nextIndex < messages.size();nextIndex++){
            Message message = messages.get(nextIndex);
            //获取Message内容所有长度叠加
            int tmpSize = message.getTopic().length() + message.getBody().length;
            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }
            tmpSize = tmpSize + 20;

            //临时存储消息大小 > 4M 结束循环
            if (tmpSize > SIZE_LIMIT){
                //若单条消息 > 4M 把下标移动一位，直接返回单条消息
                if (nextIndex - currIndex == 0){
                    nextIndex++;
                }
                break;
            }

            //将当前临时消息长度假如到总消息长度
            //若 > 4M 结束循环
            if (tmpSize + totalSize > SIZE_LIMIT){
                break;
            }else {
                totalSize += tmpSize;
            }

        }

        //根据其实位置与结束位置下标拆分消息列表
        List<Message> subList = messages.subList(currIndex, nextIndex);
        currIndex = nextIndex;
        return subList;
    }
}
