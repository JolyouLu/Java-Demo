package top.jolyoulu.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/9 17:33
 * @Version 1.0
 */
public class CustomConsumerSeek {
    public static void main(String[] args) {
        //配置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.100.101:9092");
        //反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        //配置消费者组id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"test");
        //创建一个消费者
        KafkaConsumer<Object, Object> kafkaConsumer = new KafkaConsumer<>(properties);
        //订阅主题
        ArrayList<String> topics = new ArrayList<>();
        topics.add("first");
        kafkaConsumer.subscribe(topics);
        //获取消费者消费的所有主题+分区信息
        Set<TopicPartition> assignment = kafkaConsumer.assignment();
        //保证消费者已获取到分区方案已经指定完毕
        while (assignment.size() == 0){
            //如果消费者没有分区方案，手动去拉取找到获取到分区方案未知
            kafkaConsumer.poll(Duration.ofSeconds(1));
            assignment = kafkaConsumer.assignment();
        }
        //将时间转换为对应的offset，进行消费
        HashMap<TopicPartition, Long> topicPartitionLongHashMap = new HashMap<>();
        for (TopicPartition topicPartition : assignment) {
            //用当前时间-1天的毫秒值
            topicPartitionLongHashMap.put(topicPartition,System.currentTimeMillis() - 1 * 24 * 3600 * 1000);
        }
        //生成offset
        Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap =
                kafkaConsumer.offsetsForTimes(topicPartitionLongHashMap);
        //遍历设置offset
        for (TopicPartition topicPartition : assignment) {
            OffsetAndTimestamp offsetAndTimestamp = topicPartitionOffsetAndTimestampMap.get(topicPartition);
            kafkaConsumer.seek(topicPartition,offsetAndTimestamp.offset());
        }
        boolean flag = false;
        while (true){
            if (flag){
                break;
            }
            //每1秒拉取一次数据
            ConsumerRecords<Object, Object> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<Object, Object> consumerRecord : consumerRecords) {
                System.out.println("收到消息："+consumerRecord);
            }
        }
    }
}
