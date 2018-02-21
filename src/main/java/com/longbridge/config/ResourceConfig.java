package com.longbridge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Adebimpe and KO on 11/01/2018 at exactly 7pm after so much research.
 */

@Configuration
@EnableWebMvc
public class ResourceConfig extends WebMvcConfigurerAdapter {

    @Value("${materialpicfile}")
    private String materialPicturePath;

    @Value("${productpicfile}")
    private String productPicturePath;

    @Value("${artworkpicfile}")
    private String artworkPicturePath;

    @Value("${eventpicturesfile}")
    private String eventPicturePath;

    @Value("${eventmainpicturefile}")
    private String eventMainPicturePath;

    @Value("${designerlogofile}")
    private String designerLogoPath;




    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/eventpictures/**")
                .addResourceLocations(eventPicturePath);
        registry
                .addResourceHandler("/eventmainpictures/**")
                .addResourceLocations(eventMainPicturePath);
        registry
                .addResourceHandler("/productpictures/**")
                .addResourceLocations(productPicturePath);
        registry
                .addResourceHandler("/materialpictures/**")
                .addResourceLocations(materialPicturePath);
        registry
                .addResourceHandler("/artworkpictures/**")
                .addResourceLocations(artworkPicturePath);
        registry
                .addResourceHandler("/designerlogos/**")
                .addResourceLocations(designerLogoPath);
    }


}
