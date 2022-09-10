package com.learn.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;


@Service
public class MessageListener {
	
	@Autowired
	Gson gson;
	
	static final String topic = "chat-topic";

	@KafkaListener(topics = topic, groupId = "g3")
	public void listen(Message<?> message) {
	   System.out.println("listen : "+ message.getPayload());
	}
	
	@Autowired
    SimpMessagingTemplate template;
	
	@KafkaListener(topics = topic, groupId = "g4")
	public void listen4(Message<?> message) {
		 System.out.println("listen4..");
	     template.convertAndSend("/topic/group", message.getPayload());
	}
}
