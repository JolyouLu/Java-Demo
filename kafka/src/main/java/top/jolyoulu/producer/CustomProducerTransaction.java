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
 * 异步消息无回调
 */
public class CustomProducerTransaction {
    public static void main(String[] args) {
        //配置
        Properties properties = new Properties();
        //设置 bootstrap.servers 连接kafaka地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.100.101:9092");
        //设置 key.serializer 序列化类型pippi
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //指定事务id
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,"transaction_id_01");
        //创建一个kafka对象
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        //初始化事务
        kafkaProducer.initTransactions();
        //开启事务
        kafkaProducer.beginTransaction();
        //发送数据
        try {
            for (int i = 0; i < 5; i++) {
                kafkaProducer.send(new ProducerRecord<>("first","hello"+i));
            }
            //提交事务
            kafkaProducer.commitTransaction();
        }catch (Exception e){
            kafkaProducer.abortTransaction();
        }finally {
            kafkaProducer.close();
        }
        //关闭资源
        kafkaProducer.close();
    }
}
