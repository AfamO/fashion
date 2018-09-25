package com.longbridge.services.implementations;

import com.longbridge.exception.AppException;
import com.longbridge.models.Email;
import com.longbridge.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${mail.from}")
    private String sender;



    @Override public void send(String recipient, String subject, String message) throws MailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(message);
        };

        logger.info("...trying to send mail from {}", sender);
        logger.info("...trying to send mail to {}", recipient);

        mailSender.send(messagePreparator);
            logger.info("Email successfully sent to {}",recipient);
    }

    @Override public void send(Email email) throws MailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(email.getReceiverEmail());
            messageHelper.setSubject(email.getMessageSubject());
            messageHelper.setText(email.getMessageBody());

        };
        logger.info("...trying to send mail from {}", sender);
        logger.info("...trying to send mail to {}", email.getReceiverEmail());

        mailSender.send(messagePreparator);

        logger.info("Email successfully sent to {}", email.getReceiverEmail());
    }



    @Override
    public void prepareAndSend(String message, String recipient, String subject) throws MailException {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(message, true);
//         messageHelper.addCc("farooq.ayoade@longbridgetech.com");

        };

        try {
            logger.info("...trying to send mail from {}", sender);
            logger.info("...trying to send mail to {}", recipient);

             mailSender.send(messagePreparator);
            logger.info("Email successfully sent to {}", recipient);

        } catch (AppException e) {

            e.printStackTrace();
//            throw new AppException(message, recipient, subject);
            // runtime exception; compiler will not force you to handle it
        }
    }
}