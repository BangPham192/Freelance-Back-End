package com.backend.freelance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigReader {

    @Value("${app.frontend.url}")
    private String appFrontendUrl;

    @Value("${spring.mail.username")
    private String mailFrom;

    public String getFrontendUrl() {
        return appFrontendUrl;
    }

    public String getMailFrom() {
        return mailFrom
    ;}

}
