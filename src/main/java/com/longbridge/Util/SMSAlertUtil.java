package com.longbridge.Util;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/** This is a singleton class that helps route Alert requests to the
 * Alert system. It makes use of Apache's {@link org.apache.http.impl.client.DefaultHttpClient}
 * Created by longbridge on 26/01/2017.
 */
@Service
public class SMSAlertUtil {

    private static SMSAlertUtil instance = new SMSAlertUtil();
    private SMSAlertUtil(){}

    public static SMSAlertUtil getInstance(){
        if(instance == null){
            instance = new SMSAlertUtil();
        }
        return instance;
    }

    /**
     * Sends an email to all the emails contained in {@code emails}. The message sent
     * is specified by {@code message}.
     * @param emails A {@link List} of strings containing the emails of the recipients
     * @param message The message to be sent
     * @param subject The subject of the email
     * @return an {@link ObjectNode} containing the response gotten from the server
     */
   // public ObjectNode sendEmail(List<String> emails, String message, String subject) throws IOException {

//        DefaultHttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost("http://localhost:9000/sendalert");
//
//
//        JSONObject data = new JSONObject();
//        data.put("firstName", args[0]);
//        data.put("middleName", args[1]);
//        data.put("lastName", args[2]);
//        data.put("freeCode3", 1);
//
//        post.setEntity(new StringEntity(data.toString()));
//        post.setHeader("Accept", "application/json");
//        post.setHeader("Content-type", "application/json");
//
//
//        HttpResponse resp = client.execute(post);
//
//        HttpEntity resEntityPost = resp.getEntity();
//        String response = "";
//        if (resEntityPost != null) {
//
//            response = EntityUtils.toString(resEntityPost);
//        }
    //}
// add request header

    /**
     * Sends an SMS to all the phone numbers contained in {@code phoneNumbers}. The message sent
     * is specified by {@code message}. Phone numbers must be in this format: 080000xxxxxxxx or
     * +23480632xxxxxxxx.
     * @param phoneNumbers
     * @param message
     * @throws IllegalArgumentException if the message is more than 160 characters
     * @return
     */
    public ObjectNode sms(List<String> phoneNumbers, String message) throws IOException {

        DefaultHttpClient client = new DefaultHttpClient();
        //HttpPost post = new HttpPost("http://localhost:9000/sendalert");
        HttpPost post = new HttpPost("http://142.93.25.67:9000/sendalert");

try {
    JSONObject data = new JSONObject();
    data.put("contactList", phoneNumbers);
    data.put("alertType", "SMS");
    data.put("message", message);
    data.put("subject", "");

    post.setEntity(new StringEntity(data.toString()));
    post.setHeader("Accept", "application/json");
    post.setHeader("Content-type", "application/json");


    try {
        HttpResponse resp = client.execute(post);

        HttpEntity resEntityPost = resp.getEntity();


        String response = "";
        if (resEntityPost != null) {

            response = EntityUtils.toString(resEntityPost);
        }
    } catch (Exception e){
        e.printStackTrace();
        return null;
    }
    // ObjectNode node = new ObjectNode();

}
catch (Exception e){
    e.printStackTrace();
}
        return null;
    }







}




