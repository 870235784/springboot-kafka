package com.tca.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author zhoua
 * @Date 2019/11/6
 */
@Component
public class Consumer2 {

    @KafkaListener(topics = "first-topic", containerFactory = "kafkaListenerContainerFactory2")
    public void listen(ConsumerRecord<?, ?> record) {
        System.out.println("topic = " + record.topic() + ", value = " + record.value());
    }
}
