package com.learn.chat.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.chat.model.Message;
import com.learn.chat.service.KafkaProducer;

@RestController
@RequestMapping(value = "/api")
public class ChatController {

	@Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping(value = "/send")
    public void sendMessageToKafkaTopic(@RequestParam String sender, @RequestParam String content) {
    	Message message = new Message();
    	message.setSender(sender);
    	message.setContent(content);
    	message.setTimestamp(LocalDateTime.now().toString());
    	
        this.kafkaProducer.sendMessage(message);
    }
    
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        //Sending this message to all the subscribers
        return message;
    }
}