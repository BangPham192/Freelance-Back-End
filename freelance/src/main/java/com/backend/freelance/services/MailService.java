package com.backend.freelance.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMailWithAttachment(String[] recipients, String subject, String htmlContent, String filePath) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // Implementation for sending email with attachment
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");


        helper.setFrom("phamxuanbang0592@gmail.com");
        helper.setTo(recipients);
        helper.setSubject(subject);

        // HTML content
        helper.setText(htmlContent, true);

        // Attachment
        if (StringUtils.isNotBlank(filePath)) {
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(file.getFilename(), file);
        }

        mailSender.send(mimeMessage);
        System.out.println("Mail Sent " + Arrays.toString(recipients));

    }
}
