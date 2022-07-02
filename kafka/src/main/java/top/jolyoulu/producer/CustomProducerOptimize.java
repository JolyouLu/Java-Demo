package top.jolyoulu.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @Author: JolyouLu
 * @Date: 2022/7/2 21:53
 * @Version 1.0
 * 高吞吐生产者
 */
public class CustomProducerOptimize {
    public static void main(String[] args) {
        //配置
        Properties properties = new Properties();
        //设置 bootstrap.servers 连接kafaka地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.100.101:9092");
        //设置 key.serializer 序列化类型pippi
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //RecordAccnumulator 缓冲区大小，默认32m
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,1024 * 1024 * 64); //64m
        //batch.size 批次大小，默认16k
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG,1024 * 16); //16k
        //linger.ms 等待时间改为5-100ms
        properties.put(ProducerConfig.LINGER_MS_CONFIG,1); //1ms
        //compression.type 压缩snappy
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");
        //创建一个kafka对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        //同步发送数据
        for (int i = 0; i < 50000; i++) {
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
