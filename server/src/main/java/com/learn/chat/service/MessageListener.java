package com.learn.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.learn.chat.model.Message;

@Service
public class MessageListener {
	
	static final String topic = "chat-message";

	@KafkaListener(topics = topic, groupId = "group_id")
	public void listen(Message message) {
	   System.out.println("Received Messasge in group - group-id: " + message);
	}
	
	@Autowired
    SimpMessagingTemplate template;

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID)
    public void listenMessages(Message message) {
        System.out.println("sending via kafka listener..");
        template.convertAndSend("/topic/group", message);
    }	
}
