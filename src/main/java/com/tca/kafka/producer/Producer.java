package com.tca.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhoua
 * @Date 2019/11/6
 */
@RestController
@RequestMapping("/producer")
public class Producer {

    private static final String SUCCESS = "SUCCESS";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/{message}")
    public String send(@PathVariable String message) {
        kafkaTemplate.send("first-topic", message);
        return SUCCESS;
    }


}
