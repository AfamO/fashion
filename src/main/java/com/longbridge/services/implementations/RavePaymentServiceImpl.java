//package com.longbridge.services.implementations;
//
//import com.longbridge.Util.SendEmailAsync;
//import com.longbridge.dto.CardPaymentDTO;
//import com.longbridge.models.*;
//import com.longbridge.repository.*;
//import com.longbridge.security.repository.UserRepository;
//import com.longbridge.services.RavePaymentService;
//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
//import com.mashape.unirest.http.exceptions.UnirestException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * Created by Longbridge on 31/07/2018.
// */
//@Service
//public class RavePaymentServiceImpl implements RavePaymentService {
//    @Autowired
//    PaymentRepository ravePaymentRepository;
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    CartRepository cartRepository;
//
//    @Autowired
//    ItemStatusRepository itemStatusRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    ItemRepository itemRepository;
//
//    @Autowired
//    PocketRepository pocketRepository;
//
//    @Autowired
//    SendEmailAsync sendEmailAsync;
//
//    @Value("${rave.secret}")
//    private String secret;
//
//    @Override
//    public Response validateTransaction(CardPaymentDTO cardPaymentDTO) {
//        System.out.println("i got here");
//       Response response = new Response();
//        /**
//         *
//         * Method to
//         *
//         * @param paymententity - <b>paymententity - set as a constant with default value as 1</b>
//         * @param txref - <b>txref - is the unique payment reference generated by the merchant.</b>
//         * @param secret - <b>secret - is the merchant secret key</b>
//         * @return
//         * @throws UnirestException
//         */
//
//       //RavePayment ravePayment = ravePaymentRepository.findByOrderId(cardPaymentDTO.getOrderId());
//        RavePayment ravePayment=null;
//        if(ravePayment == null){
//            response.setStatus("79");
//            response.setMessage("Unable to complete payment.. Invalid order reference");
//            return response;
//        }
//
//        double amount = ravePayment.getTransactionAmount();
//        String trnxRef = ravePayment.getTransactionReference();
//        try {
//            String status = verify(trnxRef,cardPaymentDTO.getFlwRef(),secret,amount,1).getString("status");
//
//            if(status.equalsIgnoreCase("00")){
//                User user = userRepository.findByEmail(cardPaymentDTO.getEmail());
//                deleteCart(user);
//
//               Pocket w= pocketRepository.findByUser(user);
//                if(w!=null){
//                    w.setBalance(w.getBalance()+amount);
//                    w.setPendingSettlement(w.getPendingSettlement()+amount);
//                }
//                else {
//                    w = new Pocket();
//                    w.setBalance(amount);
//                    w.setPendingSettlement(amount);
//                    w.setUser(user);
//                }
//                pocketRepository.save(w);
//
//                ItemStatus itemStatus = itemStatusRepository.findByStatus("PC");
//                Orders orders = orderRepository.findOne(ravePayment.getOrderId());
//                for (Items item:orders.getItems()) {
//                    item.setItemStatus(itemStatus);
//                    itemRepository.save(item);
//                }
//                orders.setDeliveryStatus("PC");
//                orderRepository.save(orders);
//                sendEmailAsync.sendEmailToUser(user,orders.getOrderNum());
//                sendEmailAsync.sendPaymentConfEmailToUser(user,orders.getOrderNum());
//                response.setStatus("00");
//                response.setData(orders.getOrderNum());
//                response.setMessage("Order sucessfully placed");
//                return response;
//            }
//
//            if(status.equalsIgnoreCase("56")){
//                response.setStatus("56");
//                response.setMessage("Amount does not match");
//                return response;
//            }
//            else if(status.equalsIgnoreCase("16")){
//                response.setStatus("16");
//                response.setMessage("No response from server");
//                return response;
//            }
//            else if(status.equalsIgnoreCase("26")){
//                response.setStatus("26");
//                response.setMessage("Transaction status unknown");
//                return response;
//            }
//            else{
//                response.setStatus("59");
//                response.setMessage(status);
//               return response;
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//            response.setStatus("99");
//            response.setMessage("Unable to complete payment, An error occurred");
//            return response;
//        }
//
//
//    }
//
//
//    public JSONObject verify(String transactionRef, String flwRef, String secret, double amount, int paymententity) throws UnirestException, Exception {
//
//        // This packages the payload
//        JSONObject data = new JSONObject();
//        data.put("txref", transactionRef);
//        data.put("SECKEY", secret);
//
//        // end of payload
//
//        // This sends the request to server with payload
//        String VERIFY_ENDPOINT = "https://ravesandboxapi.flutterwave.com/flwv3-pug/getpaidx/api/v2/verify";
//        HttpResponse<JsonNode> response = Unirest.post(VERIFY_ENDPOINT)
//                .header("Content-Type", "application/json")
//                .body(data)
//                .asJson();
//
//        // This get the response from payload
//        JsonNode jsonNode = response.getBody();
//
//        // This get the json object from payload
//        JSONObject responseObject = jsonNode.getObject();
//
//        // check of no object is returned
//        if(responseObject == null){
//            data.put("status","16");
//           // throw new Exception("No response from server");
//            return data;
//        }
//
//
//        // This get status from returned payload
//        String status = responseObject.optString("status", null);
//
//        // this ensures that status is not null
//        if(status == null) {
//            //throw new Exception("Transaction status unknown");
//            data.put("status", "26");
//            return data;
//        }
//
//        // This confirms the transaction exist on rave
//        if(!"success".equalsIgnoreCase(status)){
//
//            String message = responseObject.optString("message", null);
//
//            //throw new Exception(message);
//            data.put("status",message);
//        }
//
//        data = responseObject.getJSONObject("data");
//
//        // This get the amount stored on server
//        double actualAmount = data.getDouble("amount");
//
//        // This validates that the amount stored on client is same returned
//        if(actualAmount < amount) {
//            data.put("status","56");
//            //throw new Exception("Amount does not match");
//
//            return data;
//        }
//        else {
//            data.put("status","00");
//            return data;
//
//        }
//
//        // now you can give value for payment.
//
//    }
//
//
//    private void deleteCart(User user){
//        List<Cart> carts = cartRepository.findByUser(user);
//        cartRepository.delete(carts);
//    }
//
//}
