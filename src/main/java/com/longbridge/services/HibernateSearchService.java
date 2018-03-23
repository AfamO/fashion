package com.longbridge.services;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.EventsDTO;
import com.longbridge.models.Events;
import com.longbridge.models.Products;
import com.longbridge.respbodydto.ProductRespDTO;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
/**
 * Created by Longbridge on 15/03/2018.
 */
    @Service
    public class HibernateSearchService {


        @Autowired
        private final EntityManager centityManager;

    @Autowired
    private GeneralUtil generalUtil;


        @Autowired
        public HibernateSearchService(EntityManager entityManager) {
            super();
            this.centityManager = entityManager;
        }


        public void initializeHibernateSearch() {

            try {
                FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(centityManager);
                fullTextEntityManager.createIndexer().startAndWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Transactional
        public List<EventsDTO> eventsFuzzySearch(String searchTerm) {

            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(centityManager);
            QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Events.class).get();
            Query luceneQuery = qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1).onFields("eventName")
                    .matching(searchTerm).createQuery();

            javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Events.class);

            // execute search

            List<EventsDTO> events = null;
            try {
                events = generalUtil.convertEntitiesToDTOs(jpaQuery.getResultList());
            } catch (NoResultException nre) {
                ;// do nothing

            }

            return events;


        }


    @Transactional
    public List<ProductRespDTO> productsFuzzySearch(String searchTerm) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(centityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Products.class).get();
        Query luceneQuery = qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1).onFields("name")
                .matching(searchTerm).createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Products.class);

        // execute search

        List<ProductRespDTO> products = null;
        try {
            products = generalUtil.convertProdEntToProdRespDTOs(jpaQuery.getResultList());
        } catch (NoResultException nre) {
            ;// do nothing

        }

        return products;


    }
}
