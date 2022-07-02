package top.jolyoulu.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/2 23:11
 * @Version 1.0
 * 自定义分区器
 */
public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int partition;
        //获取数据
        String msgValues = value.toString();
        //如果内容包含test发送0分区
        if (msgValues.contains("test")){
            partition = 0;
        }else {
            partition = 1;
        }
        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
