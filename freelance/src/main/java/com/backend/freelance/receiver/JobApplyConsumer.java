package com.backend.freelance.receiver;

import com.backend.freelance.services.MailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JobApplyConsumer {
    private final MailService mailService;

    public JobApplyConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(topics = "${app.kafka.topic.job-apply}", groupId = "freelance-group")
    public void consumeJobApplication(String message) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode jsonNode = mapper.readTree(message);

            String jobName = jsonNode.get("jobName").asText();
            String freelancerEmail = jsonNode.get("freelancerEmail").asText();
            String clientEmail = jsonNode.get("clientEmail").asText();
            String body = jsonNode.get("body").asText();
            String attachmentUrl = jsonNode.get("attachmentUrl").asText();

            // Send email to freelancer
            String freelancerSubject = "Job Application Received";
            String freelancerContent = String.format("<p>Dear Freelancer,</p><p>Your application for job name %s has been received.</p>", jobName);
            mailService.sendMailWithAttachment(new String[]{clientEmail, freelancerEmail}, freelancerSubject, freelancerContent, attachmentUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
