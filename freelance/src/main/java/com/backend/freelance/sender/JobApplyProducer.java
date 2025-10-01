package com.backend.freelance.sender;

import com.backend.freelance.dtos.JobApplyEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobApplyProducer {
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topic.job-apply}")
    private String topic;

    public JobApplyProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendJobAppliedEvent(JobApplyEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, json);
            System.out.println("Job application event sent to topic " + topic + ": " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
