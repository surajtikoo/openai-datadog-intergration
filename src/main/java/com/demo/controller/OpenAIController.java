package com.demo.controller;

import datadog.trace.api.Trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.dto.OpenAIRequest;
import com.demo.dto.OpenAIResponse;

@RestController
@RequestMapping("/bot")
public class OpenAIController {

    @Value("${openai.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    @Trace(operationName = "chat.request")
    public String chat(@RequestParam("prompt") String prompt){
        OpenAIRequest request=new OpenAIRequest(model, prompt);
        OpenAIResponse chatGptResponse = template.postForObject(apiURL, request, OpenAIResponse.class);
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }
    
    
    @GetMapping("/hello")
    @Trace(operationName = "hello.request")
    public String hello() {
        // Your endpoint logic here
        return "Hello, Datadog!";
    }
}
