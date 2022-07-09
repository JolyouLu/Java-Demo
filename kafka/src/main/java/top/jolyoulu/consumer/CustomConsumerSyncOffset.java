package top.jolyoulu.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/9 17:33
 * @Version 1.0
 */
public class CustomConsumerSyncOffset {
    public static void main(String[] args) {
        //配置
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.100.101:9092");
        //反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        //配置消费者组id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"test");
        //offset自动提交关闭
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        //创建一个消费者
        KafkaConsumer<Object, Object> kafkaConsumer = new KafkaConsumer<>(properties);
        //订阅主题
        ArrayList<String> topics = new ArrayList<>();
        topics.add("first");
        kafkaConsumer.subscribe(topics);
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
            //异步提交offset
            kafkaConsumer.commitAsync();
            //同步提交offset
//            kafkaConsumer.commitSync();

        }
    }
}
