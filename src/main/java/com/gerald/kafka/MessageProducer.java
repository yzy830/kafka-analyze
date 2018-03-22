package com.gerald.kafka;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class MessageProducer 
{
    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, HashBaseKeySerialize.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        ProducerFactory<Object, Object> pf = new DefaultKafkaProducerFactory<>(props);
        
        return new KafkaTemplate<>(pf);
    } 
    
    public static void main( String[] args ) throws InterruptedException {
        try(ConfigurableApplicationContext cxt = SpringApplication.run(MessageProducer.class, args)) {
            @SuppressWarnings("unchecked")
            KafkaTemplate<Object, Object> template = (KafkaTemplate<Object, Object>)cxt.getBean(KafkaTemplate.class);
            
            MyMessage message1 = new MyMessage("1", "message-1");
            MyMessage message2 = new MyMessage("1", "message-2");
            MyMessage message3 = new MyMessage("2", "message-3");
            
            ListenableFuture<SendResult<Object, Object>> future1 = template.send(Topics.TEST.name, message1.getMsgId(), message1);
            ListenableFuture<SendResult<Object, Object>> future2 = template.send(Topics.TEST.name, message2.getMsgId(), message2);
            ListenableFuture<SendResult<Object, Object>> future3 = template.send(Topics.TEST.name, message3.getMsgId(), message3);
            
            ListenableFutureCallback<SendResult<Object, Object>> callback = new ListenableFutureCallback<SendResult<Object, Object>>() {

                @Override
                public void onSuccess(SendResult<Object, Object> result) {
                    System.out.println(MessageFormat.format("success, topic = {0}, parition = {1}, offset = {2}, "
                            + "key = {3}, value = {4}, choosed partition = {5}, time = {6, date, yyyy-MM-dd HH:mm:ss}", 
                            result.getRecordMetadata().topic(), 
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            result.getProducerRecord().key(),
                            result.getProducerRecord().value(),
                            result.getProducerRecord().partition(),
                            new Date()));
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println(ex);
                }
                
            };
            
            future1.addCallback(callback);
            future2.addCallback(callback);
            future3.addCallback(callback);
            
            TimeUnit.SECONDS.sleep(30);
        }
    }
}
