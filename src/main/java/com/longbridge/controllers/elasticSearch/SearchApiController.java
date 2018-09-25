package com.longbridge.controllers.elasticSearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SearchUtilities;
import com.longbridge.apps.searchengine.webservice.RemotePostGet;
import com.longbridge.apps.searchengine.webservice.RemoteWebServiceException;
import com.longbridge.apps.searchengine.webservice.RemoteWebServiceLogger;
import com.longbridge.models.Products;
import com.longbridge.models.Response;
import com.longbridge.models.elasticSearch.ApiResponse;
import com.longbridge.models.elasticSearch.Bool;
import com.longbridge.models.elasticSearch.Query;
import com.longbridge.models.elasticSearch.Range;
import com.longbridge.models.elasticSearch.SearchQueryRequest;
import com.longbridge.models.elasticSearch.SearchRequest;
import com.longbridge.models.elasticSearch.Should;
import com.longbridge.models.elasticSearch.TermFilter;
import com.longbridge.models.elasticSearch.WildCard;
import com.longbridge.repository.ProductAttributeRepository;
import com.longbridge.repository.ProductPictureRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.ElasticSearchService;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Level;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/fashion/se")
@RestController
public class SearchApiController {
   @Value("${search.url}")
   private String host_api_url; //host_api_url
   @Value("${search.auth}")
   private String authkey; //authkey
   @Value("${search.auth}")
   private String x_api_key; //x_api_key 
   RemoteWebServiceLogger apiLogger=new RemoteWebServiceLogger(SearchApiController.class);  
   private JSONObject httpParameters,must,must_not,boolObject,match,should,category,subcategory,name,price,rating;
   Map map=new HashMap();
   private String requestedEndPointPath="";
   private String requestedIndexName="";
   private String requestedServiceName=" ";
   @Autowired
   ElasticSearchService searchService;
   
   @RequestMapping(value="/update/{indexName}/{id}", method=RequestMethod.POST)
    public ApiResponse updateIndex(@RequestBody JsonNode requestBody,@PathVariable("id") String id,@PathVariable("indexName") String indexName,@RequestHeader("authKey") String authKey) {
      if(id.equals("null"))
        {
            
            apiLogger.log(Level.INFO,"The id must be provided");
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The id must be provided!");
        }
         if(indexName.equals("null"))
        {
            
            apiLogger.log(Level.INFO,"The indexName must be provided");
           return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The indexName must be provided!");
        }
        if(requestBody!=null){
            this.httpParameters=new JSONObject(requestBody.toString());
            apiLogger.log(Level.INFO," Received JSON Object :::"+requestBody); 
            requestedEndPointPath="/"+indexName+"/_doc/"+id+"/_update";
            requestedServiceName="update_index";//This data is used for logging.
            this.requestedIndexName=indexName;
            return searchService.makeRemoteRequest(host_api_url,requestedEndPointPath,"post",requestedServiceName, requestedIndexName, httpParameters);
        }
        else{
             return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The the json data to update must be provided!");
        }
       
        
   }
    @PutMapping("/index/{indexName}/{id}")
    public ApiResponse createIndex(@RequestBody JsonNode requestBody,@PathVariable("id") String id,@PathVariable("indexName") String indexName,@RequestHeader("authKey") String authKey) {
      if(id.equals("null"))
        {
            
            apiLogger.log(Level.INFO,"The id must be provided");
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The id must be provided!");
        }
         if(indexName.equals("null"))
        {
            
            apiLogger.log(Level.INFO,"The indexName must be provided");
           return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The indexName must be provided!");
        }
        if(requestBody!=null){
         this.httpParameters=new JSONObject(requestBody.toString());
         requestedEndPointPath="/"+indexName+"/_doc/"+id+"/";
         requestedServiceName="create_index";//This data is used for logging.
         return searchService.makeRemoteRequest(host_api_url,requestedEndPointPath,"put",requestedServiceName, requestedIndexName, httpParameters);
     }
     else{
             return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The the json data to index must be provided!");
        }
        
        
   }
   @RequestMapping("/get/{indexName}/{id}")
    public ApiResponse getIndex(@PathVariable("id") String id,@PathVariable("indexName") String indexName,@RequestHeader("authKey") String authKey ) {
    
        if(id.equals("null"))
        {
            
            apiLogger.log(Level.INFO,"The id must be provided");
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The id must be provided!");
        }
         if(indexName.equals("null"))
        {
            
            apiLogger.log(Level.INFO,"The indexName must be provided");
           return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The indexName must be provided!");
        }
        requestedEndPointPath="/"+indexName+"/_doc/"+id;
        requestedServiceName="query_index";//This data is used for logging.
        this.requestedIndexName=indexName;
        return searchService.makeRemoteRequest(host_api_url,requestedEndPointPath,"get",requestedServiceName, requestedIndexName, httpParameters);
   }
    @RequestMapping(value="/delete/{indexName}/{id}", method=RequestMethod.DELETE)
    public ApiResponse deleteIndex(@PathVariable("id") String id,@PathVariable("indexName") String indexName,@RequestHeader("authKey") String authKey) {
      if(id.equals("null"))
        {
            
            apiLogger.log(Level.INFO,"The id must be provided");
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The id must be provided!");
        }
         if(indexName.equals("null"))
        {
            
            apiLogger.log(Level.INFO,"The indexName must be provided");
           return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The indexName must be provided!");
        }
            requestedEndPointPath="/"+indexName+"/_doc/"+id;
            requestedServiceName="delete_index";//This data is used for logging.
            this.requestedIndexName=indexName;
            return searchService.makeRemoteRequest(host_api_url,requestedEndPointPath,"delete",requestedServiceName, requestedIndexName, httpParameters);
   }
    
    @RequestMapping(value="/search/{indexName}", method=RequestMethod.POST)
    public ApiResponse searchIndex(@RequestBody JsonNode requestBody,@PathVariable("indexName") String indexName,@RequestHeader("authKey") String authKey) {
        if(indexName.equals("null"))
        {
            apiLogger.log(Level.INFO,"The indexName must be provided");
           return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The indexName must be provided!");
        }
        if(requestBody!=null){
            JSONObject requested_search=new JSONObject(requestBody.toString());
            apiLogger.log(Level.INFO," Received JSON Object :::"+requestBody); 
            requestedEndPointPath="/"+indexName+"/_search";
            requestedServiceName="search_index";//This data is used for logging.
            this.requestedIndexName=indexName;
            this.httpParameters=requested_search;
            return searchService.makeRemoteRequest(host_api_url,requestedEndPointPath,"post",requestedServiceName, requestedIndexName, httpParameters);
        }
        else{
             return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The the json data to update must be provided!");
        }     
   }
    
     @RequestMapping(value="/search", method=RequestMethod.POST)
     public ApiResponse searchAnything(@RequestBody SearchRequest searchRequest) throws SecurityException {
         //Do you want to just search?
        if(searchRequest.getSearchTerm()!=null)
        {
            if(searchRequest.getSearchTerm().equals("")){
                apiLogger.log(Level.INFO,"The searchTerm must be provided");
                return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The searchTerm must be provided! It can't be empty.");
            }
            else{
                return searchService.elasticSearch(searchRequest,host_api_url);
            } 
        }
        //Do you want to just aggregate?
        else if(searchRequest.getAggs()!=null){
            if(searchRequest.getAggs().getFieldName().equals("")||searchRequest.getAggs().getType().equals("")){
                apiLogger.log(Level.INFO,"The 'fieldName' and 'type' of aggregation you want to do must be provided!");
                return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The 'fieldName' and 'type' of aggregation you want to do must be provided!");
            }
            else
            {
                return searchService.elasticSearchAggregate(searchRequest,host_api_url);
            }
        }
        else{
                apiLogger.log(Level.INFO,"You must request either search or aggregations using 'searchTerm' or 'aggs' respectively");
                return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"You must request either search or aggregations using 'searchTerm' or 'aggs' respectively");
        }        
   }
   @RequestMapping("/list/indices")
    public ApiResponse GetIndices(@RequestHeader("authKey") String authKey ) {
        requestedEndPointPath="/_cat/indices";
        requestedServiceName="list_indices";//This data is used for logging.
        return searchService.makeRemoteRequest(host_api_url,requestedEndPointPath,"get",requestedServiceName, requestedIndexName, httpParameters);
   }
    @RequestMapping("/load/bulk/{size}")
    public ApiResponse LoadBulkDataFromDBIntoElasticSearch( @PathVariable("size") int size) {
    
        return searchService.LoadSearchDataFromDatabase(host_api_url,size);
   }  
}
