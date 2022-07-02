package top.jolyoulu.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/2 21:53
 * @Version 1.0
 * 异步消息带回调
 */
public class CustomProducerCallback {
    public static void main(String[] args) {
        //配置
        Properties properties = new Properties();
        //设置 bootstrap.servers 连接kafaka地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.100.101:9092");
        //设置 key.serializer 序列化类型
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //创建一个kafka对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        //同步发送数据
        for (int i = 0; i < 5; i++) {
            kafkaProducer.send(new ProducerRecord<>("first", "hello" + i), (metadata, exception) -> {
                if (exception == null){
                    System.out.println("主题："+metadata.topic()+" 分区："+metadata.partition());
                }
            });
        }
        //关闭资源
        kafkaProducer.close();
    }
}
