package com.longbridge.Util;

import com.longbridge.dto.CloudinaryResponse;
import com.longbridge.dto.DesignerOrderDTO;
import com.longbridge.dto.ItemsDTO;
import com.longbridge.exception.AppException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Orders;
import com.longbridge.models.Product;
import com.longbridge.models.User;
import com.longbridge.repository.DesignerRepository;
import com.longbridge.repository.ItemRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.repository.TokenRepository;
import com.longbridge.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Base64;
import java.util.List;
import java.util.Locale;

/**
 * Created by Longbridge on 25/04/2018.
 */
@Service
public class SendEmailAsync {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    MessageSource messageSource;

    @Autowired
    MailService mailService;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ProductRepository productRepository;
    
    @Autowired
    TokenRepository tokenRepository;

    @Value("${customer.care.email}")
    String customerCareEmail;


    private Locale locale = LocaleContextHolder.getLocale();

    @Async
    public void sendEmailToUser(User user, String orderNumber) {
            String link = "";

        try {
            System.out.println("Execute method asynchronously - "
                + Thread.currentThread().getName());

            try {
                String mail = user.getEmail();
                String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
               // orderNumber = orderNumber.substring(0,4);

                link=messageSource.getMessage("order.status.track", null, locale)+encryptedMail+"&orderNum="+orderNumber;


                Context context = new Context();
                context.setVariable("name", user.getFirstName() + " "+ user.getLastName());
                context.setVariable("orderNum",orderNumber);
                context.setVariable("link",link);

                String message = templateEngine.process("orderemailtemplate", context);
                mailService.prepareAndSend(message,mail,messageSource.getMessage("order.success.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("order.success.subject", null, locale),orderNumber,link,"null");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }


    @Async
    public void sendTransferEmailToUser(User user, Orders orders) {
        String link = "";

        try {

            try {
                String mail = user.getEmail();
                String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
                // orderNumber = orderNumber.substring(0,4);

                link=messageSource.getMessage("order.status.track", null, locale)+encryptedMail+"&orderNum="+orders.getOrderNum();


                Context context = new Context();
                context.setVariable("name", user.getFirstName() + " "+ user.getLastName());

                context.setVariable("link",link);

                String message = templateEngine.process("transferemailtemplate", context);
                mailService.prepareAndSend(message,mail,messageSource.getMessage("order.success.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("order.success.subject", null, locale),orders.getOrderNum(),link,"null");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }


    @Async
    public void sendDeclinedOrderEmailToUser(User user, ItemsDTO itemsDTO) {
        String link = "";
        try {

            try {
                String mail = user.getEmail();
                String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
                link=messageSource.getMessage("order.decline.decision", null, locale)+encryptedMail+"&orderNum="+itemsDTO.getOrderNumber();
                Context context = new Context();
                context.setVariable("name", user.getFirstName() + " "+ user.getLastName());
                context.setVariable("productName",itemsDTO.getProductName());
                context.setVariable("link",link);
                itemsDTO.setLink(link);
                String message = templateEngine.process("admincancelordertemplate", context);
                mailService.prepareAndSend(message,mail,messageSource.getMessage("order.status.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();

                throw new AppException(user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("order.status.subject", null, locale),itemsDTO);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }



    @Async
    public void sendPaymentConfEmailToUser(User user, String orderNumber) {
        String link = "";
        try {
            System.out.println("Execute method asynchronously - "
                    + Thread.currentThread().getName());

            try {
                String mail = user.getEmail();
               // String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
                link = messageSource.getMessage("order.status.track",null,locale)+user.getFirstName();
                Context context = new Context();
                context.setVariable("name", user.getFirstName() + " "+ user.getLastName());
                context.setVariable("orderNum",orderNumber);
                context.setVariable("link",link);
                String message = templateEngine.process("adminconfirmordertemplate", context);
                mailService.prepareAndSend(message,mail,messageSource.getMessage("order.status.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("order.status.subject", null, locale),orderNumber,link,"null");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }



    @Async
    public void sendCancelledOrderEmailToUser(User user, String orderNumber) {
        try {
            try {
                String mail = user.getEmail();
                Context context = new Context();
                context.setVariable("name", user.getFirstName() + " "+ user.getLastName());
                context.setVariable("orderNum",orderNumber);
                String message = templateEngine.process("admincancelordertemplate", context);
                mailService.prepareAndSend(message,mail,messageSource.getMessage("order.status.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("order.status.subject", null, locale),orderNumber,"null");

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }



//    @Async
//    public String sendEmailToAdmin(User user, String orderNumber) {
//
//        try {
//            System.out.println("Execute method asynchronously - "
//                    + Thread.currentThread().getName());
//
//            try {
//                Context context = new Context();
////                context.setVariable("name", user.firstName + " "+ user.lastName);
//                context.setVariable("orderNum",orderNumber);
//                String message = templateEngine.process("orderemailtemplate", context);
//                mailService.prepareAndSend(message,user.email,messageSource.getMessage("order.success.subject", null, locale));
//
//            }catch (MailException me){
//                me.printStackTrace();
//                throw new AppException(user.email,messageSource.getMessage("order.success.subject", null, locale),orderNumber);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new WawoohException();
//        }
//
//        return null;
//    }
//
//


    @Async
    public void sendEmailToDesigner(List<DesignerOrderDTO> designerOrderDTOS, String orderNumber) {

        try {
            System.out.println("Execute method asynchronously - "
                    + Thread.currentThread().getName());


                for (DesignerOrderDTO designerOrderDTO:designerOrderDTOS) {
                    try {
                        Context context = new Context();
                        context.setVariable("name", designerOrderDTO.getStoreName());
                        context.setVariable("productName", designerOrderDTO.getProductName());
                        context.setVariable("orderNum", orderNumber);

                        try {
                            String message = templateEngine.process("designerorderemailtemplate", context);
                            mailService.prepareAndSend(message, designerOrderDTO.getDesignerEmail(), messageSource.getMessage("designerorder.success.subject", null, locale));
                        }catch (Exception e){
                            e.printStackTrace();
                            throw new AppException(designerOrderDTO, designerOrderDTO.getDesignerEmail(), messageSource.getMessage("designerorder.success.subject", null, locale), orderNumber);

                        }

                    } catch (MailException me) {
                        me.printStackTrace();
                        throw new AppException(designerOrderDTO, designerOrderDTO.getDesignerEmail(), messageSource.getMessage("designerorder.success.subject", null, locale), orderNumber);

                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }



    @Async
    public void sendEmailToDesigner(DesignerOrderDTO designerOrderDTO, String orderNumber) {

        try {
            System.out.println("Execute method asynchronously - "
                    + Thread.currentThread().getName());

                try {
                    Context context = new Context();
                    context.setVariable("name", designerOrderDTO.getStoreName());
                    context.setVariable("productName", designerOrderDTO.getProductName());
                    context.setVariable("orderNum", orderNumber);

                    String message = templateEngine.process("designerorderemailtemplate", context);
                    mailService.prepareAndSend(message,designerOrderDTO.getDesignerEmail(), messageSource.getMessage("designerorder.success.subject", null, locale));


                } catch (MailException me) {
                    me.printStackTrace();
                    throw new AppException(designerOrderDTO, designerOrderDTO.getDesignerEmail(), messageSource.getMessage("designerorder.success.subject", null, locale), orderNumber);

                }

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }


    @Async
    public void sendWelcomeEmailToUser(User user) {
        String activationLink="";
        try {
            Context context = new Context();

            String encryptedMail = Base64.getEncoder().encodeToString(user.getEmail().getBytes());
            activationLink = messageSource.getMessage("activation.url.link",null,locale)+encryptedMail;
            String message="";
            context.setVariable("link", activationLink); 
             if(user.getRole().equalsIgnoreCase("designer")) {
                context.setVariable("userToken",tokenRepository.findByUser(user).getToken());
                context.setVariable("name", designerRepository.findByUser(user).getStoreName());
                message = templateEngine.process("designerwelcomeemail", context);
                System.out.println("The Designer Email Message text With Token Is::"+message);
            }
            else {
                context.setVariable("name", user.getFirstName()+" " +user.getLastName());
                message = templateEngine.process("welcomeemail", context);
            }
            mailService.prepareAndSend(message,user.getEmail(),messageSource.getMessage("user.welcome.subject", null, locale));

        }catch (MailException me){
            me.printStackTrace();
            throw new AppException("",user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("user.welcome.subject", null, locale),activationLink);
        }

    }



    @Async
    public void notifyCustomerCare(User user) {

        try {
            Context context = new Context();
            String message="";
                context.setVariable("storename", designerRepository.findByUser(user).getStoreName());
                context.setVariable("phonenumber", user.getPhoneNo());
                message = templateEngine.process("notifycustomercaretemplate", context);
                mailService.prepareAndSend(message,customerCareEmail,messageSource.getMessage("new.designer.subject", null, locale));

        }catch (MailException me){
            me.printStackTrace();
            throw new AppException(customerCareEmail,messageSource.getMessage("new.designer.subject", null, locale),user);
        }

    }





    @Async
    public void sendFailedInspEmailToUser(User user, ItemsDTO itemsDTO) {

        try {
            System.out.println("Execute method asynchronously - "
                    + Thread.currentThread().getName());

            try {
                String mail = user.getEmail();

                Context context = new Context();
                context.setVariable("name", user.getFirstName() + " "+ user.getLastName());
                context.setVariable("productName",itemsDTO.getProductName());


                String message = templateEngine.process("failedinspforuser", context);
                mailService.prepareAndSend(message,mail,messageSource.getMessage("order.failedinspection.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("order.failedinspection.subject", null, locale),itemsDTO.getProductName());

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }


    @Async
    public void sendFailedInspToDesigner(ItemsDTO itemsDTO) {

        try {
            System.out.println("Execute method asynchronously - "
                    + Thread.currentThread().getName());
            Product product = productRepository.findOne((itemRepository.findOne(itemsDTO.getId())).getProductId());
            User user = product.getDesigner().getUser();
            try {
                String mail = user.getEmail();
                Context context = new Context();
                context.setVariable("name", user.getFirstName() + " "+ user.getLastName());
                context.setVariable("productName", product.getName());
                context.setVariable("failedInspectionReason",itemsDTO.getAction());
                String message = templateEngine.process("failedinspfordesigner", context);
                mailService.prepareAndSend(message,mail,messageSource.getMessage("order.failedinspection.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("order.failedinspection.subject", null, locale), product.getName());

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }
    
    @Async
    public void sendPassedInspectionnToCustomer(String customerEmail,String customerName,CloudinaryResponse c) {

        try {
            System.out.println("Execute method asynchronously - "
                    + Thread.currentThread().getName());

            try {
                

                Context context = new Context();
                context.setVariable("name", customerName);
                context.setVariable("passedproductPicture", c.getUrl());

                String message = templateEngine.process("passedPhysicalinspectionemail", context);
                mailService.prepareAndSend(message, customerEmail, messageSource.getMessage("order.passedinspection.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(customerName,messageSource.getMessage("order.passedinspection.subject", null, locale),c.getUrl());

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }



    @Async
    public void sendOrderPaymentErrorToUser(User user, String orderNumber, String productName) {
        String link = "";
        try {
            System.out.println("Execute method asynchronously - "
                    + Thread.currentThread().getName());

            try {
                String mail = user.getEmail();
                String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
                link=messageSource.getMessage("order.status.track", null, locale)+encryptedMail+"&orderNum="+orderNumber;
                Context context = new Context();
                context.setVariable("name", user.getFirstName() + " "+ user.getLastName());
                context.setVariable("orderNum",orderNumber);
                context.setVariable("productName",productName);
                context.setVariable("link",link);
                String message = templateEngine.process("orderpaymenttemplate", context);
                mailService.prepareAndSend(message,mail,messageSource.getMessage("order.status.subject", null, locale));

            }catch (MailException me){
                me.printStackTrace();
                throw new AppException(user.getFirstName() + user.getLastName(),user.getEmail(),messageSource.getMessage("order.status.subject", null, locale),orderNumber,link,productName);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }



}
