package com.tca.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhoua
 * @Date 2019/11/6
 */
@RestController
@RequestMapping("/producer")
public class Producer {

    private static final String SUCCESS = "SUCCESS";

    private static final String TOPIC_NAME = "first-topic";

    private static final String MESSAGE_PREFIX = "message-";

    private static AtomicInteger count = new AtomicInteger();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/{message}")
    public String message(@PathVariable String message) {
        kafkaTemplate.send("", message);
        return SUCCESS;
    }

    @GetMapping("/send")
    public String send() {
        kafkaTemplate.send(TOPIC_NAME, MESSAGE_PREFIX + count.getAndIncrement());
        return SUCCESS;
    }


}
