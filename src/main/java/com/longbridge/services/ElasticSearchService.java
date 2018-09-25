package com.longbridge.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SearchUtilities;
import com.longbridge.apps.searchengine.webservice.RemotePostGet;
import com.longbridge.apps.searchengine.webservice.RemoteWebServiceException;
import com.longbridge.apps.searchengine.webservice.RemoteWebServiceLogger;
import com.longbridge.controllers.elasticSearch.SearchApiController;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Products;
import com.longbridge.models.elasticSearch.Amount;
import com.longbridge.models.elasticSearch.ApiResponse;
import com.longbridge.models.elasticSearch.Bool;
import com.longbridge.models.elasticSearch.CreateIndexId;
import com.longbridge.models.elasticSearch.Filter;
import com.longbridge.models.elasticSearch.Index;
import com.longbridge.models.elasticSearch.ProductQualityRating;
import com.longbridge.models.elasticSearch.Query;
import com.longbridge.models.elasticSearch.Range;
import com.longbridge.models.elasticSearch.RangeFilter;
import com.longbridge.models.elasticSearch.RangeFilterAmount;
import com.longbridge.models.elasticSearch.RangeFilterProductQualityRating;
import com.longbridge.models.elasticSearch.SearchQueryRequest;
import com.longbridge.models.elasticSearch.SearchRequest;
import com.longbridge.models.elasticSearch.Should;
import com.longbridge.models.elasticSearch.TermFilter;
import com.longbridge.repository.ProductAttributeRepository;
import com.longbridge.repository.ProductPictureRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.respbodydto.ProductRespDTO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Level;
import org.aspectj.util.FileUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
/**
 * Created by Longbridge on 15/03/2018.
 */
    @Service
    public class ElasticSearchService {
     private JSONArray mustArray,shouldArray,filterArray;
     private JSONObject httpParameters,must,must_not,boolObject,match,should,category,subcategory,name,price,rating;
     private  RemoteWebServiceLogger apiLogger=new RemoteWebServiceLogger(SearchApiController.class);
     private String requestedEndPointPath="";
     private String requestedIndexName="";
     private String requestedServiceName=" ";
     @Autowired
     ProductRepository productRepository;
     @Autowired
     GeneralUtil generalUtil;
     SearchRequest searchRequest;
     String[] productTextColumns= {"name", "categoryName","description","designerName","status","designerStatus","subCategoryName","colourName","prodSummary","materialName", "inStock","availability", "mandatoryMeasurements","sizeGuide","acceptCustomSizes"};
     String[] productNumberColumns= {"amount","percentageDiscount","materialPrice", "stockNo","slashedPrice","numOfTimesOrdered","productQualityRating","productDeliveryRating","productServiceRating","numOfDaysToComplete","totalSales","salesInQueue"};
   public ApiResponse makeRemoteRequest(String host_api_url,String path,String httpMethod,String requestedServiceName,String requestedIndexName,Object httpParameters)
   {
       
        apiLogger.log(Level.INFO," Sending request to path :::"+path);
        String responseMessage="";
        int statusCode=0;
        if(path==null||path.equals("")){
           statusCode=HttpStatus.NOT_FOUND.value();
           responseMessage="The requested Path is null or empty";
           apiLogger.log(Level.FATAL,responseMessage);
           return new ApiResponse(statusCode,responseMessage);
       }
       else if(host_api_url==null||host_api_url.equals(""))
       {
           responseMessage="The api encountered an internal error: Please contact admin/developer ";
           statusCode=HttpStatus.INTERNAL_SERVER_ERROR.value();
           apiLogger.log(Level.FATAL,"The web service url is null or empty");
           return new ApiResponse(statusCode,responseMessage);
       }
      
       //**/
        RemotePostGet remotePostGet = null;
       
        Object object = null;
        ApiResponse apiResponse=null;
        try {
            remotePostGet = new RemotePostGet(host_api_url,"074163a43568414d83eabd26d27c09bc");
        } 
        catch (RemoteWebServiceException ex) 
        {
            apiLogger.log(Level.FATAL, ex.getMessage());
        }
        path+="?pretty";
        JSONObject remoteJsonObject = null;
        JSONArray indices =null;
        try {
            Boolean IsIndexFound=false;
            if(requestedServiceName.equalsIgnoreCase("list_indices")||requestedServiceName.equalsIgnoreCase("query_index")
            || requestedServiceName.equalsIgnoreCase("search_index")|| requestedServiceName.equalsIgnoreCase("delete_index")||requestedServiceName.equalsIgnoreCase("search_index_aggs")){
                apiLogger.log(Level.INFO, "The indices is here being listed.....");
               //We assign 'get'method below since  we want to query the list of indices first.
                indices =new JSONArray(remotePostGet.makeRemoteAPIConnectionRequest("get",httpParameters,null,"/_cat/indices?pretty"));
                if(requestedServiceName.equalsIgnoreCase("query_index")|| requestedServiceName.equalsIgnoreCase("search_index")
                        || requestedServiceName.equalsIgnoreCase("delete_index")||requestedServiceName.equalsIgnoreCase("search_index_aggs")){
                    for (Object objec:indices){
                        JSONObject indexVal=(JSONObject)objec;
                        //Does this user requested indice exist in the DB?
                        if(indexVal.getString("index").equalsIgnoreCase(requestedIndexName)){
                            apiLogger.log(Level.INFO, "The  search indices is found here..."+indexVal.getString("index"));
                            IsIndexFound=true;
                            break;
                   }
                }
                if(IsIndexFound){
                    // make then query or search the requested index since it exists
                     try {
                        remotePostGet = new RemotePostGet(host_api_url,"074163a43568414d83eabd26d27c09bc");
                    } 
                    catch (RemoteWebServiceException ex) 
                    {
                        apiLogger.log(Level.FATAL, ex.getMessage());
                    }
                     remoteJsonObject = new JSONObject(remotePostGet.makeRemoteAPIConnectionRequest(httpMethod,httpParameters,null,path));
                     JSONObject remoteJsonObjectNew = new JSONObject();
                     if(requestedServiceName.equalsIgnoreCase("search_index")){
                         apiLogger.log(Level.INFO, "Caught  search_term is here ...");
                         //Did it fail to find any search item
                         if(remoteJsonObject.getJSONObject("hits").getJSONArray("hits").length()==0){
                             // Then refine and change the search query
                             this.httpParameters=new JSONObject();
                              // Set the first query fails. I mean since the item is not found in any of the listed fields in the productColumns.
                            JSONObject _query = new JSONObject().accumulate("query", searchRequest.getSearchTerm().toLowerCase());
                             this.httpParameters.accumulate("query", new JSONObject()
                             .put("query_string", _query)
                              );
                             this.httpParameters.put("size",searchRequest.getSize());
                             apiLogger.log(Level.INFO," The requestedServiceName :::"+requestedServiceName);
                             apiLogger.log(Level.INFO," Extra Composed New JSON httpParameters :::"+this.httpParameters);
                             // Then send for another search query using the new changed query
                             remoteJsonObject = new JSONObject(remotePostGet.makeRemoteAPIConnectionRequest(httpMethod,this.httpParameters,null,path));
                         }
                          JSONArray jsonNewSearchResultsArray= new JSONArray();
                          JSONArray jsonSearchData =remoteJsonObject.getJSONObject("hits").getJSONArray("hits");
                          for (Object objec:jsonSearchData){
                          JSONObject singleSearchResultVal=(JSONObject)objec;
                        //Does this user requested indice exist in the DB?
                         jsonNewSearchResultsArray.put(singleSearchResultVal.getJSONObject("_source"));
                          }
                          remoteJsonObjectNew.put("hits", remoteJsonObject.getJSONObject("hits").getJSONArray("hits"));
                          remoteJsonObjectNew.put("total", remoteJsonObject.getJSONObject("hits").getInt("total"));
                          remoteJsonObjectNew.put("took", remoteJsonObject.getInt("took"));
                          remoteJsonObjectNew.put("timed_out", remoteJsonObject.getBoolean("timed_out"));
                          //remoteJsonObjectNew.put("_shards", remoteJsonObject.getJSONObject("_shards"));
                          //Filter results
                          jsonNewSearchResultsArray=SearchUtilities.filterSearchResults(searchRequest, jsonNewSearchResultsArray);
                         apiResponse= new ApiResponse(HttpStatus.OK.value(),jsonNewSearchResultsArray.toList(),jsonNewSearchResultsArray.length(),
                         remoteJsonObject.getInt("took"),remoteJsonObject.getBoolean("timed_out"));
                     }
                     //The fellow must have requested for only aggregations
                     else if(requestedServiceName.equalsIgnoreCase("search_index_aggs")){
                         //find if the user requested for term aggregation type
                         if(searchRequest.getAggs().getType()!=null && searchRequest.getAggs().getType().equalsIgnoreCase("terms"))
                         {
                             //Then get array of buckets since the the fellow requested for 'terms' aggregations type
                             apiResponse= new ApiResponse(HttpStatus.OK.value(),remoteJsonObject.getJSONObject("aggregations").getJSONObject("aggs_result").getJSONArray("buckets").toList());
                         }      
                         else{
                             apiResponse= new ApiResponse(HttpStatus.OK.value(),remoteJsonObject.getJSONObject("aggregations").getJSONObject("aggs_result"));
                         }
                         
                     }
                     else{
                         apiResponse= new ApiResponse(HttpStatus.OK.value(),remoteJsonObject);
                     }
                     //In any case, send 'success' message.
                     apiResponse.setMessage("success");
                }
                else{
                    //The index name request for is not found in the server.
                    statusCode=HttpStatus.NOT_FOUND.value();// Not found code
                    responseMessage="The requested index '"+requestedIndexName+"' is not found in our system. Ensure you have indexed it.";
                    apiLogger.log(Level.FATAL,responseMessage);
                    apiResponse= new ApiResponse(statusCode,responseMessage);
                }

                }
                // The person just want to list indices 
                else{
                    //Send the list of indices found
                    apiResponse= new ApiResponse(HttpStatus.OK.value(),indices.toList());
                    apiResponse.setMessage("success");
                } 
            }
             else
             {
                 remoteJsonObject = new JSONObject(remotePostGet.makeRemoteAPIConnectionRequest(httpMethod,httpParameters,null,path));
                 apiResponse= new ApiResponse(HttpStatus.OK.value(),remoteJsonObject);
                  apiResponse.setMessage("success");
             }
        } 
        catch (RemoteWebServiceException ex){
            apiLogger.log(Level.FATAL, ex.getMessage());
        } 
        if(remoteJsonObject==null && indices==null)
        {
            apiResponse= new ApiResponse(500,object);
            apiResponse.setMessage("error: Web service did not return anything");
        }

        return apiResponse;
       }
   public ApiResponse LoadSearchDataFromDatabase(String host_api_url,int size){
       
        Page<Products> products = productRepository.findAll(new PageRequest(0,size));
        StringBuilder productDTOS = new StringBuilder();
        Index index=new Index();
        CreateIndexId createIndexId= new CreateIndexId(); 
        for(Products p: products){
            createIndexId._id=Long.toString(p.id);
            index.index=createIndexId;
            ProductRespDTO productDTO = generalUtil.convertEntityToDTO(p);
            ObjectMapper objectMapper = new ObjectMapper();
            String productSearchDTOWriteValueAsString;
         try {
             productSearchDTOWriteValueAsString = objectMapper.writeValueAsString(index);
             productDTOS.append(productSearchDTOWriteValueAsString);
             apiLogger.log(Level.INFO,"The productDTOS's Index is::"+productSearchDTOWriteValueAsString);
             productDTOS.append("\n");
             productSearchDTOWriteValueAsString = objectMapper.writeValueAsString(productDTO );
             productDTOS.append(productSearchDTOWriteValueAsString);
             apiLogger.log(Level.INFO,"The productDTOS's Body is::"+productSearchDTOWriteValueAsString);
             productDTOS.append("\n");
            
         } catch ( JsonProcessingException ex) {
             apiLogger.log("The exception message is:"+ex.getMessage());
             throw new WawoohException(ex);
         }
            
        }
       apiLogger.log(Level.INFO,"The Total productDTO is::"+productDTOS.toString());
       
       Gson gson= new Gson();
         
        ApiResponse makeRemoteRequestIndexProducts = makeRemoteRequest( host_api_url,"/products/_doc/_bulk","put","create_index","products",productDTOS.toString());
        apiLogger.log("The Result Of Indexing A  New Product For Elastic Search Is:"+gson.toJson(makeRemoteRequestIndexProducts));
        return makeRemoteRequestIndexProducts;
         
   }

    public ApiResponse elasticSearch(SearchRequest searchRequest,String host_api_url) {
       JSONObject range;
        this.httpParameters=new JSONObject();
        this.searchRequest=searchRequest;
        boolObject = new JSONObject();
        this.mustArray=new org.json.JSONArray();
        shouldArray=new org.json.JSONArray();
        filterArray=new JSONArray();
        //Does the user want to filter by ranges?
        if(searchRequest.getPageNumber()<=1 && searchRequest.getSize() <0 )
        {
            
            apiLogger.log(Level.INFO,"The PageNnumber and size must be provided");
             return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The PageNnumber and size must be provided and valid!");
           
        }
        else{
            int from=searchRequest.getPageNumber()*searchRequest.getSize();
            httpParameters.put("from",from);
        }
        if(searchRequest.getIndexName()==null)
        {
            apiLogger.log(Level.INFO,"The indexName is not provided");
            requestedEndPointPath="/products/_search";  // Defaults to 'products' when no index is given'
            this.requestedIndexName="products";
        }
        else if (searchRequest.getIndexName().equalsIgnoreCase("all")){ //search all available indices in the cluster
            requestedEndPointPath="/_search"; //Search all indices in the cluster.
            this.requestedIndexName="products";// Just default this to products to avoid giving an exception when looking for 'all'
        }
        else{
            requestedEndPointPath="/"+searchRequest.getIndexName()+"/_search"; //search for the given indice
            this.requestedIndexName=searchRequest.getIndexName();
        }
        apiLogger.log(Level.INFO," Received searchTerm :::"+searchRequest.getSearchTerm()); 
        requestedServiceName="search_index";//This data is used for logging.
        Query query=new Query();
        int shouldCounter=0;
        String searchTerm=searchRequest.getSearchTerm().trim().replace("$", "").replace("@", "").replace("#", "")
                .replace("£", "").replace("€", "").replace("₦", "");
        //Is it a number ?
        if(NumberUtils.isCreatable(searchTerm)){
            for(String ColumName:productNumberColumns){
           JSONObject match= new JSONObject().accumulate("match", new JSONObject()
                   .put(ColumName,Double.parseDouble(searchTerm))
           );
           shouldArray.put(shouldCounter++, match); 

        }
            boolObject=  new JSONObject()
            .accumulate("bool",new JSONObject()
            .put("must", mustArray)
            .put("should", shouldArray)
            .put("filter", filterArray)
            .put("must_not", mustArray));
            httpParameters.accumulate("query", boolObject);
            
        }
        
        //Or a long text with atleast two words ?
        else if(SearchUtilities.countWords(searchRequest.getSearchTerm())>1){
          JSONObject  _query = new JSONObject().accumulate("query", searchRequest.getSearchTerm().toLowerCase());
            httpParameters.accumulate("query", new JSONObject()
                .put("query_string", _query)
                );
            query.query_string=_query.toMap();
        }
        else{
            // It must be first few characterss of a text or a whole word
             JSONObject _queryString = new JSONObject();
             _queryString.put("query", searchRequest.getSearchTerm().toLowerCase()+"*").put("fields", productTextColumns).put("lowercase_expanded_terms",false);
            httpParameters.accumulate("query", new JSONObject()
            .put("query_string", _queryString)
              );
            shouldCounter=0;
            boolObject=  new JSONObject()
            .accumulate("bool",new JSONObject()
            .put("must", mustArray)
            .put("should", shouldArray)
            .put("filter", filterArray)
            .put("must_not", mustArray));
            //httpParameters.accumulate("query", boolObject);
        }
        JSONObject anotherAggs=null;
        httpParameters.put("aggs",SearchUtilities. getCustomAggregate("name","terms","group_by_name",
                SearchUtilities. getCustomAggregate("categoryName","terms","group_by_categoryName",
                SearchUtilities. getCustomAggregate("designerName","terms","group_by_designerName",
                SearchUtilities. getCustomAggregate("amount","avg","average_amount",anotherAggs)))));
        httpParameters.put("size",searchRequest.getSize());
        apiLogger.log(Level.INFO," The requestedServiceName :::"+requestedServiceName);
        apiLogger.log(Level.INFO," Composed JSON httpParameters :::"+httpParameters);
        return makeRemoteRequest(host_api_url,requestedEndPointPath,"post",requestedServiceName, this.requestedIndexName,httpParameters);

    }
    public ApiResponse elasticSearchAggregate(SearchRequest searchRequest,String host_api_url) {
        this.httpParameters=new JSONObject();
        
       
            requestedEndPointPath="/"+searchRequest.getIndexName()+"/_search"; //search for the given indice
            requestedServiceName="search_index_aggs";
            this.searchRequest=searchRequest;
            if(searchRequest.getIndexName()==null){
                this.requestedIndexName="products";
            }
            else{
                this.requestedIndexName=searchRequest.getIndexName();
            }
            JSONObject anotherAggs=null;
            String fieldToAggregate=searchRequest.getAggs().getFieldName();
            httpParameters.put("aggs",SearchUtilities. getCustomAggregate(fieldToAggregate,searchRequest.getAggs().getType(),"aggs_result",
            anotherAggs));
            httpParameters.put("size",0); //Since you are just aggregating. It needs to be zero 0.
            apiLogger.log(Level.INFO," The requestedServiceName  Is :::"+requestedServiceName+" For ONLY AGGREGATION OPERATION");
            apiLogger.log(Level.INFO," Composed JSON httpParameters :::"+httpParameters);
            return makeRemoteRequest(host_api_url,requestedEndPointPath,"post",requestedServiceName, this.requestedIndexName, httpParameters);

    }
 
}
