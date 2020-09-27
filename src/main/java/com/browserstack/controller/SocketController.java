package com.browserstack.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.browserstack.filewatcher.TailFileReader;

@Controller
public class SocketController {
	
	@Autowired
	KafkaTemplate kafkaTemplate;
	
	@Autowired
	private SimpMessagingTemplate  messageTemplate;
	
	@MessageMapping("/fileName")
	public void receiveFileName(@Payload String fileName) {
		TailFileReader fileReader = new TailFileReader("File.txt",5,kafkaTemplate);
		fileReader.setInitalize(true);
		fileReader.start();
	}
	
	@KafkaListener(topics = "update")
    public void consume(String message) throws IOException {
        System.out.println("Sending File Update to WebSocket .....");
        messageTemplate.convertAndSend("/topic/client", message);
    }
	
}
