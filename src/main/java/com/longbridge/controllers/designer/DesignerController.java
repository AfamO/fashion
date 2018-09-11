package com.longbridge.controllers.designer;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.DesignerDTO;
import com.longbridge.dto.DesignerRatingDTO;
import com.longbridge.dto.MonthsDTO;
import com.longbridge.models.Designer;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.DesignerService;
import com.longbridge.services.OrderService;
import com.longbridge.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longbridge on 15/11/2017.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/designer")
public class DesignerController {
    @Autowired
    UserUtil userUtil;

    @Value("${jwt.header}")
    private String tokenHeader;
    @Autowired
    DesignerService designerService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping(value = "/getdesigners")
    public Response getDesigners(){
        List<DesignerDTO> designerList= designerService.getDesigners();
        return new Response("00","Operation Successful",designerList);

    }


    @PostMapping(value = "/getdesigner")
    public Response getDesignerById(HttpServletRequest request,@RequestBody MonthsDTO months){
        System.out.println("i got here");
        String token = request.getHeader(tokenHeader);
        User user1 = userUtil.fetchUserDetails2(token);
        if(token==null || user1==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(user1.designer == null){
            return new Response("99","You are not a designer","error");
        }

        DesignerDTO designer = designerService.getDesigner(user1,months);
        return new Response("00","Operation Successful",designer);

    }




    @GetMapping(value = "/getdesignerbystorename/{storename}")
    public Response getDesignerByStoreName(HttpServletRequest request, @PathVariable String storename){
        DesignerDTO designer = designerService.getDesignerByStoreName(storename);
        return new Response("00","Operation Successful",designer);

    }


    @GetMapping(value = "/{id}/getdesignerbyid")
    public Response getDesignerById(HttpServletRequest request, @PathVariable Long id){
        DesignerDTO designer = designerService.getDesignerById(id);
        return new Response("00","Operation Successful",designer);

    }

//    @GetMapping(value = "/{id}/getsaleschart")
//    public Response getSalesChart(HttpServletRequest request, @PathVariable Long id){
//        List<SalesChart> salesChart = designerService.getSalesChart(id);
//        Response response = new Response("00","Operation Successful",salesChart);
//        return response;
//    }


    @PostMapping(value = "/updatedesigner")
    public Response updateDesigner(@RequestBody User passedUser,HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        Designer designer = passedUser.designer;
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        designerService.updateDesigner(userTemp,passedUser,designer);
        return new Response("00","Operation Successful","success");

    }

    @PostMapping(value = "/updatepersonalinformation")
    public Response updatePersonalInformation(@RequestBody User user, HttpServletRequest request){

        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        designerService.updateDesignerPersonalInformation(userTemp, user, user.designer);
        return new Response("00", "Profile updated", null);
    }

    @PostMapping(value = "/updatebusinessinformation")
    public Response updateBusinessInformation(@RequestBody User user, HttpServletRequest request){

        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        designerService.updateDesignerBusinessInformation(userTemp, user, user.designer);
        return new Response("00", "Profile updated", null);
    }

    @PostMapping(value = "/updateaccountinformation")
    public Response updateAccountInformation(@RequestBody User user, HttpServletRequest request){

        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        designerService.updateDesignerAccountInformation(userTemp, user, user.designer);
        return new Response("00", "Profile updated", null);
    }

    @PostMapping(value = "/updateinformation")
    public Response updateInformation(@RequestBody User user, HttpServletRequest request){

        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        designerService.updateDesignerInformation(userTemp, user, user.designer);
        return new Response("00", "Profile updated", null);
    }

    @PostMapping(value = "/updatedesignerlogo")
    public Response updateDesignerLogo(@RequestBody User user,HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        designerService.updateDesignerLogo(userTemp,user.designer);
        return new Response("00","Operation Successful","success");

    }

    @PostMapping(value = "/ratedesigner")
    public Response rateDesigner(@RequestBody DesignerRatingDTO ratingDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        designerService.rateDesigner(ratingDTO);
        return new Response("00","Operation Successful","success");

    }

    @GetMapping(value = "/{designerId}/deleteDesigner")
    public Object deleteDesigner(@PathVariable Long designerId){
        designerService.deleteDesigner(designerId);
        return new Response("00","Operation Successful","success");

    }


    @GetMapping(value = "/{designerId}/{status}/changestatus")
    public Object updateDesignerStatus(@PathVariable Long designerId, @PathVariable String status){
        designerService.updateDesignerStatus(designerId,status);
       return new Response("00","Operation Successful","success");

    }

    @GetMapping(value = "/getsuccessfulsales")
    public Response getSuccessfulSales(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getSuccessfulSales(userTemp));

    }



    @GetMapping(value = "/getcancelledorders")
    public Response getCancelledOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getCancelledOrders(userTemp));

    }



    @GetMapping(value = "/getpendingorders")
    public Response getPendingOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getPendingOrders(userTemp));

    }



    @GetMapping(value = "/getactiveorders")
    public Response getActiveOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
       return new Response("00","Operation Successful",orderService.getActiveOrders(userTemp));

    }

    @GetMapping(value = "/getcompletedorders")
    public Response getCompletedOrders(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",orderService.getCompletedOrders(userTemp));

    }


    @GetMapping(value = "/gettotalproducts")
    public Response getTotalProducts(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",productService.getTotalProducts(userTemp));

    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
