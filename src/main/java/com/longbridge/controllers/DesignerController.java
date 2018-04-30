package com.longbridge.controllers;

import com.longbridge.Util.CustomBeanUtilsBean;
import com.longbridge.Util.UserUtil;
import com.longbridge.dto.DesignerDTO;
import com.longbridge.dto.DesignerRatingDTO;
import com.longbridge.models.Designer;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.security.JwtUser;
import com.longbridge.services.DesignerService;
import com.longbridge.services.OrderService;
import com.longbridge.services.ProductService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.modelmapper.ModelMapper;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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
        Response response = new Response("00","Operation Successful",designerList);
        return response;
    }


    @GetMapping(value = "/getdesigner")
    public Response getDesignerById(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user1 = userUtil.fetchUserDetails2(token);
        if(token==null || user1==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        DesignerDTO designer = designerService.getDesigner(user1);
        Response response = new Response("00","Operation Successful",designer);
        return response;
    }

    @GetMapping(value = "/getdesignerbystorename/{storename}")
    public Response getDesignerByStoreName(HttpServletRequest request, @PathVariable String storename){
        DesignerDTO designer = designerService.getDesignerByStoreName(storename);
        Response response = new Response("00","Operation Successful",designer);
        return response;
    }

    @GetMapping(value = "/{id}/getdesignerbyid")
    public Response getDesignerById(HttpServletRequest request, @PathVariable Long id){
        DesignerDTO designer = designerService.getDesignerById(id);
        Response response = new Response("00","Operation Successful",designer);
        return response;
    }

    @PostMapping(value = "/updatedesigner")
    public Response updateDesigner(@RequestBody User passedUser,HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
            User userTemp = userUtil.fetchUserDetails2(token);
            Designer designer = passedUser.designer;
            if(token==null || userTemp==null){
                return userUtil.tokenNullOrInvalidResponse(token);
            }
        designerService.updateDesigner(userTemp,passedUser,designer);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @PostMapping(value = "/updatedesignerlogo")
    public Response updateDesignerLogo(@RequestBody User user,HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        designerService.updateDesignerLogo(userTemp,user.designer);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @PostMapping(value = "/ratedesigner")
    public Response rateDesigner(@RequestBody DesignerRatingDTO ratingDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        designerService.rateDesigner(ratingDTO);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @GetMapping(value = "/{designerId}/deleteDesigner")
    public Object deleteDesigner(@PathVariable Long designerId){
        designerService.deleteDesigner(designerId);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }


    @GetMapping(value = "/{designerId}/{status}/changestatus")
    public Object updateDesignerStatus(@PathVariable Long designerId, @PathVariable String status){
        designerService.updateDesignerStatus(designerId,status);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @GetMapping(value = "/getsuccessfulsales")
    public Response getSuccessfulSales(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",orderService.getSuccessfulSales(userTemp));
        return response;
    }


    @GetMapping(value = "/gettotalproducts")
    public Response getTotalProducts(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Response response = new Response("00","Operation Successful",productService.getTotalProducts(userTemp));
        return response;
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
