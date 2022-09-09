package com.learn.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.learn.chat.model.Message;

@Service
public class KafkaProducer {

	@Autowired
	private KafkaTemplate<String, Message> kafkaTemplate;
	
	static final String topic = "chat-message";
	
	public void sendMessage(Message message) {
	   kafkaTemplate.send(topic, message);
	} 
}
