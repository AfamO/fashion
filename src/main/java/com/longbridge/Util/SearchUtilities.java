/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.longbridge.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longbridge.apps.searchengine.webservice.RemoteWebServiceLogger;
import com.longbridge.models.elasticSearch.Bool;
import com.longbridge.models.elasticSearch.Query;
import com.longbridge.models.elasticSearch.Range;
import com.longbridge.models.elasticSearch.SearchQueryRequest;
import com.longbridge.models.elasticSearch.SearchRequest;
import com.longbridge.models.elasticSearch.Should;
import com.longbridge.models.elasticSearch.TermFilter;
import com.longbridge.models.elasticSearch.WildCard;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Tivas-Tech
 */
public class SearchUtilities {
    
    /**
     *Generates custom aggregation command for elastic search
     * @param fieldName
     * @param aggsType
     * @param aggsLabel
     * @return
     */
    RemoteWebServiceLogger apiLogger=new RemoteWebServiceLogger(this.getClass());  
    public static JSONObject getCustomAggregate(String fieldName,String aggsType,String aggsLabel,JSONObject anotherAggregation){
        JSONObject aggs=null;
        //Is it count aggregate
        if(aggsType.equalsIgnoreCase("terms")){
             fieldName+=".keyword";
         }
         try{
             JSONObject field = new JSONObject().accumulate("field", fieldName);
             JSONObject avg = new JSONObject().accumulate(aggsType, field);
             if(anotherAggregation!=null){
                 avg.put("aggs",anotherAggregation);
             }
             
             aggs = new JSONObject().accumulate(aggsLabel, avg);
         }
         catch (JSONException jsonExce){

         }

         return aggs; 
    }
    public String getWildCardShouldArraysQuery(WildCard wildCard,String[] productColumns,String searchTerm){
        WildCard wildcard=new WildCard();
        ArrayList<WildCard> shouldArrays=new ArrayList<>();
       //wildcard.setFieldValue(searchRequest.getSearchTerm().toLowerCase()+"*");
       Field field;
       Field field2;
       try {
           field = wildcard.getClass().getDeclaredField("fieldName");
           field2 = wildcard.getClass().getDeclaredField("fieldValue");
            field.setAccessible(true);
            field2.setAccessible(true);
            
            for(String cloumName: productColumns){
               try {
                   field.set(wildcard, cloumName);
                   field2.set(wildcard, searchTerm.toLowerCase()+"*");
               } catch (IllegalAccessException ex) {
                 apiLogger.log( "Reflection IllegalAccessException Is::"+ex.getMessage());
               }
                shouldArrays.add(wildcard);
       }
       } catch (NoSuchFieldException|IllegalArgumentException ex) {
           apiLogger.log( "Reflection NoSuchFieldException Is::"+ex.getMessage());
       }
        Should shouldQuery=new Should();
        //shouldQuery.setShould(shouldArrays);
        Bool boolQuery=new Bool();
        Query query=new Query();
        query.setBool(boolQuery);
        SearchQueryRequest searchQueryRequest=new SearchQueryRequest();
        searchQueryRequest.setQuery(query);
        ObjectMapper objectMapper = new ObjectMapper();
        String searchQueryRequestValueAsString = null;
       try {
           searchQueryRequestValueAsString = objectMapper.writeValueAsString(searchQueryRequest);
       } catch (JsonProcessingException ex) {
           apiLogger.log( ex.getMessage());
       }
       /**
        * for(String ColumName:productColumns){
          // _name = new JSONObject().put(ColumName, searchRequest.getSearchTerm().toLowerCase()+"*");
           JSONObject wildcard= new JSONObject().accumulate("wildcard", new JSONObject()
                   .put(ColumName, searchRequest.getSearchTerm().toLowerCase()+"*")
           );
           shouldArray.put(mustCounter++, wildcard); 

        }
        */
       return searchQueryRequestValueAsString;
    }
    public static int countWords(String s){

    int wordCount = 0;

    boolean word = false;
    int endOfLine = s.length() - 1;

    for (int i = 0; i < s.length(); i++) {
        // if the char is a letter, word = true.
        if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
            word = true;
            // if char isn't a letter and there have been letters before,
            // counter goes up.
        } else if (!Character.isLetter(s.charAt(i)) && word) {
            wordCount++;
            word = false;
            // last word of String; if it doesn't end with a non letter, it
            // wouldn't count without this.
        } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
            wordCount++;
        }
    }
    return wordCount;
}
    public static List<String>convertStringsListToLowerCase(List <String> stringsList){
        List <String> stringsListResult=new ArrayList<>();
        stringsList.stream().forEach((val) -> {
            stringsListResult.add(val.toLowerCase());
        });
        return stringsListResult;
    }
    public static JSONArray filterSearchResults(SearchRequest searchRequest,JSONArray searchResultsArray){
        
        JSONArray jsonNewSearchResultsArray= new JSONArray();
         if(searchRequest.getRanges()!=null && searchRequest.getRanges().size()>0){
            List<Range> rangesList=searchRequest.getRanges();
            for(Range currentRange:rangesList){
                int min=currentRange.getMin();
                int max=currentRange.getMax();
                for (Object objec:searchResultsArray){
                        JSONObject currentVal=(JSONObject)objec;
                        System.out.println(" At Field "+currentRange.getFieldName()+" Min Is::"+min+" Max Is::"+max+" Val Is::"+currentVal.getInt(currentRange.getFieldName()));
                        //Is the requested value within the range?
                        if(currentVal.getInt(currentRange.getFieldName())>= min&&currentVal.getInt(currentRange.getFieldName())<=max){
                        jsonNewSearchResultsArray.put(currentVal);
                        System.out.println("Found At Field "+currentRange.getFieldName()+" Val Is::"+currentVal.getInt(currentRange.getFieldName()));
                   }
                }
            }
        }
        //Did the user request to filter search results by terms
        if(searchRequest.getTerms()!=null&& searchRequest.getTerms().size()>0){
            
            List<TermFilter> termsList=searchRequest.getTerms();
            for(TermFilter currentTermFilter:termsList){
                List<String> termValues=currentTermFilter.getValues();
                for(String termValue:termValues){
                    for (Object objec:searchResultsArray){
                        JSONObject currentVal=(JSONObject)objec;
                        //Is the requested value found?
                        if(currentVal.getString(currentTermFilter.getFieldName()).equalsIgnoreCase(termValue)){
                        jsonNewSearchResultsArray.put(currentVal);
                   }
                }
              }
                
            }
            
        }
        if(jsonNewSearchResultsArray.length()>0)
            return jsonNewSearchResultsArray;
        else
            return searchResultsArray;
    }

    
    
}
