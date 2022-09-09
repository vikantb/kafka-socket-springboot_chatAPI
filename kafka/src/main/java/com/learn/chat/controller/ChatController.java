package com.learn.chat.controller;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    public void sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now().toString());
        this.kafkaProducer.sendMessage(message);
    }

	@GetMapping(value = "/sendV2")
	public void sendMessageToKafkaTopicV2(boolean flag, long time) {
		this.kafkaProducer.generateMessage(flag, time);
	}

//  -------------- WebSocket API ----------------
	@MessageMapping("/sendMessage")
	@SendTo("/topic/group")
	public Message broadcastGroupMessage(@Payload Message message) {
		// Sending this message to all the subscribers
		System.out.println("message send" + message);
		return message;
	}

	@MessageMapping("/newUser")
	@SendTo("/topic/group")
	public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
		// Add user in web socket session
		headerAccessor.getSessionAttributes().put("username", message.getSender());
		System.out.println("message send" + message);
		return message;
	}
}