package com.longbridge.config;

/**
 * Created by Longbridge on 15/03/2018.
 */
import javax.persistence.EntityManager;

import com.longbridge.services.HibernateSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
public class HibernateSearchConfiguration {

    @Autowired
    private EntityManager bentityManager;

    @Bean
    HibernateSearchService hibernateSearchService() {
        HibernateSearchService hibernateSearchService = new HibernateSearchService(bentityManager);
       // hibernateSearchService.initializeHibernateSearch();
        return hibernateSearchService;
    }
}
