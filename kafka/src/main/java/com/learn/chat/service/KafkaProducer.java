package com.learn.chat.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.learn.chat.model.MessageModel;

@Service
public class KafkaProducer {

//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	Gson gson;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	static final String topic = "chat-topic";
	
	public void sendMessage(MessageModel message) {
		String jsonInString;
		jsonInString = gson.toJson(message);
		kafkaTemplate.send(topic, jsonInString);
	} 
	
	boolean flag = false;
	long time = 2000;
	Random random = new Random();
	public void generateMessage(boolean flagP, long time) {
		this.flag = flagP;
		this.time = time;
		
		CompletableFuture.runAsync(()->{
			while(flag) {
				System.out.println(flag);
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				MessageModel message = new MessageModel();
				message.setSender(String.valueOf(random.nextInt()));
				message.setContent(String.valueOf(random.nextInt()));
				message.setTimestamp(LocalDateTime.now().toString());
				String jsonInString;
				jsonInString = gson.toJson(message);
				kafkaTemplate.send(topic, jsonInString);
			}
		});
	} 
}
