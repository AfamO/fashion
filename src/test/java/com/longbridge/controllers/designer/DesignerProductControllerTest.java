package com.longbridge.controllers.designer;
import com.longbridge.dto.ProductDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.longbridge.models.Product;
import com.longbridge.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DesignerProductControllerTest {
 
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;
 
    @Test
    public void contextLoads(){
        
    }
    // write test cases here
    //@Test
    public void getproductbyid()
    throws Exception {
     ///**
    ProductDTO productDTO=new ProductDTO();
    productDTO.id=1L;
    productDTO.name="Junit Test Product Name";
    //**/
        MockHttpServletRequestBuilder get = get("/fashion/secure/designer/product/1/designer/getproductbyid").
                header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZXZlbG9wZXJzQHdhd29vaC5jb20iLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE1Mzc4ODEyMjA2NjIsImV4cCI6MzAwMDAwMTUzNzg4MTIyMH0.rF_GgcOcdXJ-fEEPGjMycupUwD0FyeIhcTWRussEo0ha5AXxf4FHhtLhRUmd_bB6mvb1U9vDYrJNHUPQQ1pAeQ");
                this.mvc.perform(get)
               .andDo(print())
               .andExpect(status().isOk()).andExpect((ResultMatcher) jsonPath("$.status").value("00"))
               .andExpect((ResultMatcher) jsonPath("$.message").value("Operation Successful"));

}

    @Test
    public void testNewArrival(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-3);
        Date tdate = calendar.getTime();
        System.out.println(" date is now : "+new SimpleDateFormat("dd-MM-yyyy").format(tdate));

    }
}