package com.longbridge.services;

import com.longbridge.models.Email;
import org.springframework.mail.MailException;

/**
 * Created by Fortune on 5/12/2017.
 */
public interface MailService {


    void send(String recipient, String subject, String message) throws MailException;


    void send(Email email) throws MailException;

    void prepareAndSend(String message, String recipient, String subject) throws MailException;
}
