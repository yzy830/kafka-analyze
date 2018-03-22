package com.gerald.kafka;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchMessageListener;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@SpringBootApplication
public class MessageReceiver {
    @Bean
    public KafkaMessageListenerContainer<Integer, MyMessage> kafkaMessageListenerContainer() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-1");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        DefaultKafkaConsumerFactory<Integer, MyMessage> cf = new DefaultKafkaConsumerFactory<>(props);
        cf.setValueDeserializer(new JsonDeserializer<>(MyMessage.class));
        
        ContainerProperties containerProps = new ContainerProperties("test");
        containerProps.setMessageListener(new BatchMessageListener<Integer, MyMessage>() {

            @Override
            public void onMessage(List<ConsumerRecord<Integer, MyMessage>> records) {
                for(ConsumerRecord<Integer, MyMessage> record : records) {
                    System.out.println(MessageFormat.format("topic = {0}, partition = {1}, offset = {2},"
                            + "key = {3}, value = ({4})", record.topic(), record.partition(), record.offset(), 
                            record.key(), record.value()));
                }
            }
        });
        
        KafkaMessageListenerContainer<Integer, MyMessage> container = new KafkaMessageListenerContainer<>(cf, containerProps);
        
        container.start();
        
        return container;
    }
    
    public static void main(String[] args) throws InterruptedException {
        try(ConfigurableApplicationContext cxt = SpringApplication.run(MessageProducer.class, args)) {
            TimeUnit.SECONDS.sleep(3600);
        }
    }
}
