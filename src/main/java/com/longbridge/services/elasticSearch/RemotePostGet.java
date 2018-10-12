/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.services.elasticSearch;

/**
 *
 * @author Tivas-Tech
 */
//import com.tivasgroups.apps.jamasms.config.AppConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
public final class RemotePostGet {
  private String remoteUrl="http://ussd.api.getjama.com/";
  private String x_Api_Key="111";
  static RemoteWebServiceLogger webServiceLogger=new RemoteWebServiceLogger(RemotePostGet.class); 
  public static final String POST_METHOD = "post";
  public static final String GET_METHOD = "get";
  public static final String PUT_METHOD = "put";
  public static final String DELETE_METHOD = "delete";
  private static int statusCode=0;

    /**

    This is a constructor of the class that implements push message notification to various mobile devices.

    It pushes received sms alerts to registered devices.
     * @param remoteUrl
     * @param x_Api_Key
     * @throws com.longbridge.apps.searchengine.webservice.RemoteWebServiceException

    */

    public RemotePostGet(String remoteUrl,String x_Api_Key) throws RemoteWebServiceException
    {
        if(remoteUrl==null||remoteUrl.equals(""))

        {
            
            webServiceLogger.log(Level.INFO, "The applications's configuration file for making remote api connection \ncannot be found OR has been tampered with"); 
            throw new RemoteWebServiceException("No request or remote Url Passed");
        }

        //authenticate the parameters to ensure they have not been tampered with.

        if(x_Api_Key==null||x_Api_Key.equals("")){
            webServiceLogger.log(Level.INFO, " One of the parameters for making remote connection has been tampered with");
            throw new RemoteWebServiceException("No x_Api_Key Passed");
        }
        this.remoteUrl=remoteUrl;
        this.x_Api_Key=x_Api_Key;
    }

    /*

    * This method actually does the remote connection in question using a specified url,

    * http method containing the parameters in question.

    * @throws UnsupportedEncodingException,ClientProtocolException,IOException

    */

    public  String makeRemoteAPIConnectionRequest(String httpMethod,Object httpParameters, List<NameValuePair> params,String apiPath) throws  RemoteWebServiceException

    {
        String result="";
        InputStream inputStream = null;
        try 
        {

            // check for request method
           if(httpMethod.equalsIgnoreCase(POST_METHOD))
                {
                    // request method is POST
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpPost httpPost = new HttpPost(remoteUrl+apiPath);
                    httpPost.addHeader("x-api-key", x_Api_Key);
                     //You can use either JSON httpParameters or List NameValue Pairs.
                    if(httpParameters!=null){

                        httpPost.setEntity(new StringEntity(httpParameters.toString()));
                        System.out.println("URL encoded format IS: " +httpParameters);
                        //System.out.println("postEntity:::"+httpParameters.toString());
                        httpPost.addHeader("Content-Type", "application/json");
                        httpPost.addHeader("Accept", "application/json");
                    }
                    else{
                        params.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded"));
                        httpPost.setEntity(new UrlEncodedFormEntity(params));

                        httpPost.addHeader("Content-Type", "x-www-form-urlencoded");
                        System.out.println("URL encoded format IS: " +params);
                        System.out.println("postEntity:::"+params);

                    }


                    System.out.println("log_tag:POST's URL IS: " + remoteUrl+apiPath);

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    inputStream = httpEntity.getContent();
                    statusCode=httpResponse.getStatusLine().getStatusCode();
                    //webServiceLogger.log(Level.INFO,"Content lent="+httpEntity.getContentLength());
                    //webServiceLogger.log(Level.INFO, "content is: " +httpEntity.getContent().toString()+" while the Status Code is::"+statusCode);

                }
                if(httpMethod.equalsIgnoreCase(PUT_METHOD))
                    {
                        // request method is POST
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpPut httpPut = new HttpPut(remoteUrl+apiPath);
                        httpPut.addHeader("x-api-key", x_Api_Key);
                         //You can use either JSON httpParameters or List NameValue Pairs.
                        if(httpParameters!=null){
                            
                            httpPut.setEntity(new StringEntity(httpParameters.toString()));
                            System.out.println("URL encoded format IS: " +httpParameters);
                            //System.out.println("putEntity:::"+httpParameters.toString());
                            httpPut.addHeader("Content-Type", "application/json");
                            httpPut.addHeader("Accept", "application/json");
                        }
                        else{
                            params.add(new BasicNameValuePair("Content-Type","application/x-www-form-urlencoded"));
                            httpPut.setEntity(new UrlEncodedFormEntity(params));
                            
                            httpPut.addHeader("Content-Type", "x-www-form-urlencoded");
                            System.out.println("URL encoded format IS: " +params);
                            System.out.println("putEntity:::"+params);
                            
                        }
                        
                        
                        System.out.println("log_tag:PUT's URL IS: " + remoteUrl+apiPath);
                        
                        HttpResponse httpResponse = httpClient.execute(httpPut);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        inputStream = httpEntity.getContent();
                        statusCode=httpResponse.getStatusLine().getStatusCode();
                        //webServiceLogger.log(Level.INFO,"Content lent="+httpEntity.getContentLength());
                        //webServiceLogger.log(Level.INFO, "content is: " +httpEntity.getContent().toString()+" while the Status Code is::"+statusCode);
                        
                    }
                if(httpMethod.equalsIgnoreCase(GET_METHOD))
                    {
                        // request method is GET
                       CloseableHttpClient httpClient = HttpClients.createDefault();
                        remoteUrl +=apiPath;
                        if(params!=null){
                            String paramString = URLEncodedUtils.format(params,"utf-8");
                            remoteUrl += "?" + paramString;
                             System.out.println("The GET paramString IS: " +paramString);
                        }
                        webServiceLogger.log(Level.INFO,"log_tag:GET's URL IS: " + remoteUrl);
                        System.out.println("The JSON httpParameters IS: " +httpParameters);
                        HttpGet httpGet = new HttpGet(remoteUrl);
                        httpGet.addHeader("x-api-key", x_Api_Key);
                        httpGet.addHeader("auth", x_Api_Key);
                        httpGet.addHeader("Content-Type", "application/json");
                        httpGet.addHeader("Accept", "application/json");
                        
                        
                        
                        //httpPost.setEntity(new StringEntity(httpParameters.toString()));
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        
                        //System.out.println("Content lent="+httpEntity.getContentLength());
                        HttpEntity httpEntity = httpResponse.getEntity();
                        inputStream = httpEntity.getContent();
                        //httpEntity.getContentType().
                         statusCode=httpResponse.getStatusLine().getStatusCode();
                         //webServiceLogger.log(Level.INFO, "content is: " +httpEntity.getContent().toString()+" while the Status Code is::"+httpResponse.getStatusLine().getStatusCode());   
                    }
                if(httpMethod.equalsIgnoreCase(DELETE_METHOD))
                    {
                        // request method is GET
                       CloseableHttpClient httpClient = HttpClients.createDefault();
                        remoteUrl +=apiPath;
                        webServiceLogger.log(Level.INFO,"log_tag:GET's URL IS: " + remoteUrl);
                        //System.out.println("postEntity:::"+httpParameters.toString());
                        System.out.println("log_tag:DELETE's URL IS: " + remoteUrl);
                        System.out.println("The JSON httpParameters IS: " +httpParameters);
                        HttpDelete httpDelete = new HttpDelete(remoteUrl);
                        httpDelete.addHeader("x-api-key", x_Api_Key);
                        httpDelete.addHeader("Content-Type", "application/json");
                        httpDelete.addHeader("Accept", "application/json");
                        HttpResponse httpResponse = httpClient.execute(httpDelete);
                        
                        //System.out.println("Content lent="+httpEntity.getContentLength());
                        HttpEntity httpEntity = httpResponse.getEntity();
                        inputStream = httpEntity.getContent();
                        //httpEntity.getContentType().
                         statusCode=httpResponse.getStatusLine().getStatusCode();
                         //webServiceLogger.log(Level.INFO, "content is: " +httpEntity.getContent().toString()+" while the Status Code is::"+httpResponse.getStatusLine().getStatusCode());   
                    }
        } 
        catch (UnsupportedEncodingException | UnknownHostException e) {
            throw new RemoteWebServiceException(e.getMessage());
        } catch (IOException | RuntimeException  e) {
            throw new RemoteWebServiceException(e.getMessage());
        }

        try {

            

            BufferedReader reader = new BufferedReader(new InputStreamReader(

                    inputStream, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();

            String line = null;

            while ((line = reader.readLine()) != null) {

                sb.append(line).append("\n");

            }

            if(inputStream!=null)
                inputStream.close();
            result = sb.toString();
            //System.out.println("The RESULT FROM REMOTE SERVER for http method "+httpMethod+" is: "+result+" StatusCode is:: "+statusCode);
           
            //gloSmsLogger.log(Level.INFO,"The RESULT FROM REMOTE SERVER for http method "+httpMethod+" is: "+result);
            //jsonObject = new JSONObject(result);
             //jsonObject= jsonObject.put("statusCode", statusCode); 
             
        } catch (IOException |JSONException e) {
            throw new RemoteWebServiceException(e.getMessage());
        }
     return result;
    }
}
