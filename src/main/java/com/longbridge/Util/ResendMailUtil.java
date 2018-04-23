package com.longbridge.Util;


import com.longbridge.exception.AppException;
import com.longbridge.models.MailError;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

/**
 * Created by Longbridge on 28/02/2018.
 */
@Component
public class ResendMailUtil {

    @Autowired
    private MailService mailService;

    @Autowired
    private MailErrorRepository mailErrorRepository;


    @Autowired
    private TemplateEngine templateEngine;


    @Scheduled(cron = "${wawooh.status.check.rate}")
    private String resendMail(){
        //System.out.println("i got here");
        List<MailError> mailErrorList = mailErrorRepository.findByDelFlag("N");
        String message = null;
        if (mailErrorList.size() > 0) {
            for (MailError mailError:mailErrorList) {
                String newPassword =mailError.getNewPassword();

                String mail = mailError.getRecipient();

                String name = mailError.getName();

                String subject = mailError.getSubject();

                String link = mailError.getLink();

                String orderNum = mailError.getOrderNum();

                Context context = new Context();
                context.setVariable("password", newPassword);
                context.setVariable("name", name);
                context.setVariable("link", link);
                context.setVariable("orderNum", orderNum);

                if(mailError.getMailType().equalsIgnoreCase("user")) {
                    try {
                        message = templateEngine.process("emailtemplate", context);
                        mailService.prepareAndSend(message,mail,subject);
                        mailError.setDelFlag("Y");
                        mailErrorRepository.save(mailError);

                    }catch (MailException me) {
                        me.printStackTrace();
                        throw new AppException(newPassword,name,mail,subject,link);

                    }

                }
                else if (mailError.getMailType().equalsIgnoreCase("order")){
                    try{
                        message = templateEngine.process("orderemailtemplate", context);
                        mailService.prepareAndSend(message,mail,subject);
                        mailError.setDelFlag("Y");
                        mailErrorRepository.save(mailError);
                    }catch (MailException me) {
                        me.printStackTrace();
                        throw new AppException(name,mail,subject,orderNum);

                    }

                }
                else if (mailError.getMailType().equalsIgnoreCase("welcome")){
                    try{
                    message = templateEngine.process("welcomeemail", context);
                        mailService.prepareAndSend(message,mail,subject);
                        mailError.setDelFlag("Y");
                        mailErrorRepository.save(mailError);
                    }catch (MailException me){
                        me.printStackTrace();
                        throw new AppException(name, mail, subject);

                    }
                }

            }

        }

        return "true";
    }



}
