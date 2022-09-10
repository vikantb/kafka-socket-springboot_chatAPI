package com.learn.chat.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learn.chat.model.MessageModel;
import com.learn.chat.service.KafkaProducer;

@RestController
public class ChatController {

	@Autowired
	private KafkaProducer kafkaProducer;

	@GetMapping("/api/send")
	public void sendMessageToKafkaTopic(@RequestParam String sender, @RequestParam String content) {
		MessageModel message = new MessageModel();
		message.setSender(sender);
		message.setContent(content);
		message.setTimestamp(LocalDateTime.now().toString());

		this.kafkaProducer.sendMessage(message);
	}
	
	@PostMapping(value = "/api/send", consumes = "application/json", produces = "application/json")
    public void sendMessage(@RequestBody MessageModel message) {
        message.setTimestamp(LocalDateTime.now().toString());
        this.kafkaProducer.sendMessage(message);
    }

	@GetMapping(value = "/api/sendV2")
	public void sendMessageToKafkaTopicV2(boolean flag, long time) {
		this.kafkaProducer.generateMessage(flag, time);
	}

//  -------------- WebSocket API ----------------
	@MessageMapping("/send")
	@SendTo("/topic/group")
	public MessageModel broadcastGroupMessage(MessageModel message) {
		// Sending this message to all the subscribers
		System.out.println("message send" + message);
		return message;
	}
}