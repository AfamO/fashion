package com.longbridge.Util;


import com.longbridge.dto.DesignerOrderDTO;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.exception.AppException;
import com.longbridge.models.MailError;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Base64;
import java.util.List;
import java.util.Locale;

/**
 * Created by Longbridge on 28/02/2018.
 */
@Component
public class ResendMailUtil {

    @Autowired
    MessageSource messageSource;



    private Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    private MailService mailService;

    @Autowired
    private MailErrorRepository mailErrorRepository;

    @Autowired
    private UserRepository userRepository;


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

                String productName = mailError.getProductName();

                Context context = new Context();
                context.setVariable("password", newPassword);
                context.setVariable("name", name);
                context.setVariable("link", link);
                context.setVariable("orderNum", orderNum);
                context.setVariable("password", newPassword);

                if(mailError.getMailType().equalsIgnoreCase("userpassword")) {
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
                        throw new AppException(name,mail,subject,orderNum,link,"");

                    }

                }
                else if (mailError.getMailType().equalsIgnoreCase("designerorder")){
                    DesignerOrderDTO designerOrderDTO = new DesignerOrderDTO();
                    try{

                        designerOrderDTO.setProductName(productName);
                        designerOrderDTO.setStoreName(name);
                        message = templateEngine.process("designerorderemailtemplate", context);
                        mailService.prepareAndSend(message,mail,subject);
                        mailError.setDelFlag("Y");
                        mailErrorRepository.save(mailError);
                    }catch (MailException me) {
                        me.printStackTrace();
                        throw new AppException(designerOrderDTO,mail,subject,orderNum);

                    }

                }
                else if (mailError.getMailType().equalsIgnoreCase("adminorder")){
                    try{
                        message = templateEngine.process("adminorderemailtemplate", context);
                        mailService.prepareAndSend(message,mail,subject);
                        mailError.setDelFlag("Y");
                        mailErrorRepository.save(mailError);
                    }catch (MailException me) {
                        me.printStackTrace();
                        throw new AppException(name,mail,subject,orderNum,link,"");

                    }

                }
                else if(mailError.getMailType().equalsIgnoreCase("adminConfirmOrRejectItem")){
                    ItemsDTO itemsDTO = new ItemsDTO();
                        try{
                            productName = mailError.getProductName();
                            //link=mailError.getLink();
                            itemsDTO.setProductName(productName);
                            itemsDTO.setDeliveryStatus(mailError.getOrderItemStatus());

                            context.setVariable("productName", productName);

//
//                            String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
//                            String firstName = name.trim().split("")[0];
                           // link = messageSource.getMessage("order.status.track",null,locale)+firstName+"&email="+encryptedMail;
                            //context.setVariable("link", link);


                            if(mailError.getOrderItemStatus().equalsIgnoreCase("PC")) {
                                message = templateEngine.process("adminconfirmordertemplate", context);
                            }else if(mailError.getOrderItemStatus().equalsIgnoreCase("OR")){
                                itemsDTO.setLink(mailError.getLink());
                                message = templateEngine.process("admincancelordertemplate", context);
                            }
                            else if(mailError.getOrderItemStatus().equalsIgnoreCase("RI")){
                                message = templateEngine.process("readyforinsptemplate", context);
                            }
                            mailService.prepareAndSend(message,mail,subject);
                            mailError.setDelFlag("Y");
                            mailErrorRepository.save(mailError);
                        }catch (MailException me) {
                            me.printStackTrace();
                            throw new AppException(name,mail,subject,itemsDTO);

                        }
                }
                else if (mailError.getMailType().equalsIgnoreCase("welcome")){
                    try{
                        if(userRepository.findByEmail(mail).designer != null)
                        {
                        message = templateEngine.process("designerwelcomeemail", context);
                        }
                        else
                        {
                            message = templateEngine.process("welcomeemail", context);
                        }

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
