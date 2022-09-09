package com.learn.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.learn.chat.model.Message;

@Service
public class MessageListener {
	
	static final String topic = "multinodetopic";

	@KafkaListener(topics = topic, groupId = "gp3")
	public void listen(Message message) {
	   System.out.println("Json Object reciever : "+ message);
	}
	
	@Autowired
    SimpMessagingTemplate template;

	@KafkaListener(topics = topic, groupId = "gp2")
	public void listen2(Message message) {
		 System.out.println("sending via kafka listener..");
	     template.convertAndSend("/topic/group", message);
	}	
}
