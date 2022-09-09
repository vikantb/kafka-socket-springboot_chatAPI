package com.learn.chat.service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.learn.chat.model.Message;

@Service
public class KafkaProducer {

//	@Autowired
//	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private KafkaTemplate<String, Message> kafkaTemplate;
	
	static final String topic = "multinodetopic";
	
	public void sendMessage(Message message) {
	   kafkaTemplate.send(topic, message);
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
				Message message = new Message();
				message.setSender(String.valueOf(random.nextInt()));
				message.setContent(String.valueOf(random.nextInt()));
				message.setTimestamp(LocalDateTime.now().toString());
				kafkaTemplate.send(topic, message);
			}
		});
	} 
}
