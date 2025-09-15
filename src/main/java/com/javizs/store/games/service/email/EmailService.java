package com.javizs.store.games.service.email;

public interface EmailService {
    void send(String to, String subject, String body);
}
