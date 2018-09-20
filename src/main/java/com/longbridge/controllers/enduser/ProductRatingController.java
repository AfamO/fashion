package com.longbridge.controllers.enduser;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.ProductRating;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.ProductRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 25/04/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/rating")
public class ProductRatingController {
    @Autowired
    ProductRatingService productRatingService;

    @PostMapping(value = "/{productid}/rateproduct")
    public Response rateProduct(@RequestBody ProductRating productRating, @PathVariable Long productid){
        Response response;
        if(productRatingService.RateProduct(productid,productRating)){
            response = new Response("00","Operation Successful","success");
        }else{
            response = new Response("99","User Has Not Ordered Product","failure");
        }
        return response;
    }

    @PostMapping(value = "/{producid}/updateproductrating")
    public Response updateProductRating(@RequestBody ProductRating productRating){
        productRatingService.updateRating(productRating);
        return new Response("00", "Review updated", "success");
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
