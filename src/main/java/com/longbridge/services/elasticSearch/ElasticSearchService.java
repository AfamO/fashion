package com.longbridge.services.elasticSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SearchUtilities;
import com.longbridge.controllers.elasticSearch.SearchApiController;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.elasticSearch.ProductSearchDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Product;
import com.longbridge.models.elasticSearch.ApiResponse;
import com.longbridge.models.elasticSearch.Bool;
import com.longbridge.models.elasticSearch.CreateIndexId;
import com.longbridge.models.elasticSearch.Index;
import com.longbridge.models.elasticSearch.Multi_Match;
import com.longbridge.models.elasticSearch.Query;
import com.longbridge.models.elasticSearch.SearchQueryRequest;
import com.longbridge.models.elasticSearch.SearchRequest;
import com.longbridge.repository.ProductRepository;
import com.longbridge.respbodydto.ProductRespDTO;
import java.util.ArrayList;

import org.apache.log4j.Level;
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
      RemotePostGet remotePostGet = null;
      SearchQueryRequest searchRequestQuery=null;
      JSONArray jsonNewSearchResultsArrayAggs= null;
      JSONObject remoteJsonObjectNewAggs=null;
      String currentColumName="";
     String[] productTextColumns= {"name", "categoryName","description","designerName","status","designerStatus","subCategoryName","colourName","prodSummary","materialName", "inStock","availability", "mandatoryMeasurements","sizeGuide","acceptCustomSizes"};
     String[] productNumberColumns= {"amount","percentageDiscount","materialPrice", "stockNo","slashedPrice","numOfTimesOrdered","productQualityRating","productDeliveryRating","productServiceRating","numOfDaysToComplete","totalSales","salesInQueue"};
     String[] productAggreationsFieldColumns= {"name", "categoryName","designerName","status","subCategoryName","materialName", "inStock","availability","acceptCustomSizes"};
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
       
        Object object = null;
        ApiResponse apiResponse=null;
        try {
            //Construct a constructor for Listing the indices first.
            String indices_host=host_api_url.substring(0, host_api_url.lastIndexOf('/'));
            remotePostGet = new RemotePostGet(indices_host,"074163a43568414d83eabd26d27c09bc");
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
                apiLogger.log(Level.INFO, "The indices is here being listed.....");//update_index
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
                     if(requestedServiceName.equalsIgnoreCase("search_index")){
                         apiLogger.log(Level.INFO, "Caught  search_term is here ...");
                         //process search results
                         return this.convertSearchResultsToResponseDTO(remoteJsonObject);
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
                  try {
                        remotePostGet = new RemotePostGet(host_api_url,"074163a43568414d83eabd26d27c09bc");
                    } 
                    catch (RemoteWebServiceException ex) 
                    {
                        apiLogger.log(Level.FATAL, ex.getMessage());
                    }
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
   public ApiResponse LoadSearchDataFromDatabase(String host_api_url,PageableDetailsDTO pageableDetailsDTO){
       
        int page = pageableDetailsDTO.getPage();
        int size = pageableDetailsDTO.getSize(); 
        Page<Product> products = productRepository.findAll(new PageRequest(page,size));
        StringBuilder productDTOS = new StringBuilder();
        Index index=new Index();
        CreateIndexId createIndexId= new CreateIndexId(); 
        for(Product p: products){
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
        this.requestedIndexName=host_api_url.substring(host_api_url.lastIndexOf('/')+1, host_api_url.length());
        ApiResponse makeRemoteRequestIndexProducts = makeRemoteRequest( host_api_url,"/_doc/_bulk","put","create_index",requestedIndexName,productDTOS.toString());
        apiLogger.log("The Result Of Indexing A  New Product For Elastic Search Is:"+gson.toJson(makeRemoteRequestIndexProducts));
        return makeRemoteRequestIndexProducts;
         
   }
   public ApiResponse UpdateProductIndex(String host_api_url,ProductSearchDTO productSearchDTO){
       if(productSearchDTO!=null){
            this.httpParameters=new JSONObject().put("doc",new JSONObject(SearchUtilities.convertObjectToJson(productSearchDTO)));
            apiLogger.log(Level.INFO," Received JSON Object To Be Updated Is :::"+httpParameters); 
            requestedEndPointPath="/_doc/"+productSearchDTO.getId()+"/_update";
            requestedServiceName="update_index";//This data is used for logging.
            this.requestedIndexName=host_api_url.substring(host_api_url.lastIndexOf('/')+1, host_api_url.length());
            return makeRemoteRequest(host_api_url,requestedEndPointPath,"post",requestedServiceName, requestedIndexName, httpParameters);
        }
        else{
             return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The the json data to update must be provided!");
        }
       
   }
   public ApiResponse AddSearchProductIndex(String host_api_url,ProductSearchDTO productSearchDTO){
       if(productSearchDTO!=null){
            this.httpParameters=new JSONObject(SearchUtilities.convertObjectToJson(productSearchDTO));
            apiLogger.log(Level.INFO," Received JSON Object To Be Indexed Is :::"+httpParameters); 
            requestedEndPointPath="/_doc/"+productSearchDTO.getId()+"/";
            requestedServiceName="create_index";//This data is used for logging.
            this.requestedIndexName=host_api_url.substring(host_api_url.lastIndexOf('/')+1, host_api_url.length());
            return makeRemoteRequest(host_api_url,requestedEndPointPath,"put",requestedServiceName, requestedIndexName, httpParameters);
        }
        else{
             return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The the json data to update must be provided!");
        }
       
   }
   public ProductSearchDTO convertIndexApiReponseToProductDTO(ApiResponse apiResponse){
       JSONObject productObjectResponse=new JSONObject(SearchUtilities.convertObjectToJson(apiResponse));
       ObjectMapper objectMapper = new ObjectMapper();
       ProductSearchDTO  productSearchDTO = objectMapper.convertValue(productObjectResponse.getJSONObject("data").getJSONObject("_source").toMap(),ProductSearchDTO.class);
       return productSearchDTO;
       
   }
   public ApiResponse getProduct(String host_api_url,Long id){
       if(id==null)
        {
            
            apiLogger.log(Level.INFO,"The product id to get its index must be provided");
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(),"The product id to get its index must be provided!");
        }
        requestedEndPointPath="/_doc/"+id;
        requestedServiceName="query_index";//This data is used for logging.
        this.requestedIndexName=host_api_url.substring(host_api_url.lastIndexOf('/')+1, host_api_url.length());
        return makeRemoteRequest(host_api_url,requestedEndPointPath,"get",requestedServiceName, requestedIndexName, httpParameters);
   }
   public ApiResponse processSearchResults(JSONObject remoteJsonObject, String httpMethod,String path) {
       ApiResponse apiResponse=null;
        //Did it fail to find any search item
        if(remoteJsonObject.getJSONObject("hits").getJSONArray("hits").length()==0){
            
            // Then refine and change the search query
            // Prepare this for fuzziness incase if no results where found for lettered search values.
            Query query=new Query();
            Multi_Match multi_Match=new Multi_Match();
            multi_Match.setFuzziness(2);
            multi_Match.setQuery(searchRequest.getSearchTerm().toLowerCase());
            multi_Match.setFields(productTextColumns);
            query.setMulti_match(multi_Match);
            searchRequestQuery =new SearchQueryRequest();
            searchRequestQuery.setQuery(query);
            searchRequestQuery.setSize(searchRequest.getSize());
            //Aggregate
            this.searchRequestQuery.setAggs(getAggregationRequestCommand().toMap());
            apiLogger.log(Level.INFO," Fuzzy Search Starting For.... :::"+searchRequest.getSearchTerm().toLowerCase());
            try {
                // Then send for another search query using the new changed query
                remoteJsonObject = new JSONObject(remotePostGet.makeRemoteAPIConnectionRequest(httpMethod,SearchUtilities.convertObjectToJson(searchRequestQuery),null,path));
                //Further Refine the query
                if(remoteJsonObject.getJSONObject("hits").getJSONArray("hits").length()==0){
                    // Then refine and change the search query
            this.httpParameters=new JSONObject();
             // Since the first query fails. I mean since the item is not found in any of the listed fields in the productColumns.
           JSONObject _query = new JSONObject().accumulate("query", searchRequest.getSearchTerm().toLowerCase());
            this.httpParameters.accumulate("query", new JSONObject()
            .put("query_string", _query)
             );
            this.httpParameters.put("size",searchRequest.getSize());
            httpParameters.put("aggs",getAggregationRequestCommand());
            apiLogger.log(Level.INFO," The requestedServiceName :::"+requestedServiceName);
            apiLogger.log(Level.INFO," Extra Composed New JSON httpParameters :::"+this.httpParameters);
            try {
                // Then send for another search query using the new changed query
                remoteJsonObject = new JSONObject(remotePostGet.makeRemoteAPIConnectionRequest(httpMethod,this.httpParameters,null,path));
                apiResponse=this.convertSearchResultsToResponseDTO(remoteJsonObject);
            }
            catch (RemoteWebServiceException ex) {
            apiLogger.log(Level.INFO, ex.getMessage());
            throw new WawoohException(ex);
            }
                }
                else{
                    apiResponse=this.convertSearchResultsToResponseDTO(remoteJsonObject);
                }
            } catch (RemoteWebServiceException ex) {
                apiLogger.log(Level.INFO, ex.getMessage());
                throw new WawoohException(ex);
            }
             
        }
        else{
            apiResponse=this.convertSearchResultsToResponseDTO(remoteJsonObject);
        }
        return apiResponse;
   }
   /**
    * Renders the results in such a way as to be easily used by api consumer.
    * @param remoteJsonObject
    * @return 
    */
   private ApiResponse convertSearchResultsToResponseDTO(JSONObject remoteJsonObject){
       
       JSONObject remoteJsonObjectNew = new JSONObject();
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
        ApiResponse apiResponse= new ApiResponse(HttpStatus.OK.value(),jsonNewSearchResultsArray.toList(),jsonNewSearchResultsArray.length(),
        remoteJsonObject.getInt("took"),remoteJsonObject.getBoolean("timed_out"),remoteJsonObject.getJSONObject("aggregations").toMap());
        
        return apiResponse;
       
   }

    public ApiResponse elasticSearch(SearchRequest searchRequest,String host_api_url) {
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
            requestedEndPointPath="/_search";  // Defaults to 'products' when no index is given'
            this.requestedIndexName=host_api_url.substring(host_api_url.lastIndexOf('/')+1, host_api_url.length());
        }
        else if (searchRequest.getIndexName().equalsIgnoreCase("all")){ //search all available indices in the cluster
            requestedEndPointPath="/_search"; //Search all indices in the cluster.
            this.requestedIndexName=host_api_url.substring(host_api_url.lastIndexOf('/')+1, host_api_url.length());// Just default this to the one in property file to avoid giving an exception when looking for 'all'
        }
        else{
            requestedEndPointPath="/_search"; //search for the given indice
            this.requestedIndexName=host_api_url.substring(host_api_url.lastIndexOf('/')+1, host_api_url.length());
        }
        apiLogger.log(Level.INFO," Received searchTerm :::"+searchRequest.getSearchTerm()); 
        requestedServiceName="search_index";//This data is used for logging.
        String searchTerm=searchRequest.getSearchTerm().trim().replace("$", "").replace("@", "").replace("#", "")
                .replace("£", "").replace("€", "").replace("₦", "");
        //Is it a number ?
        /** 
         * Close this for now.
        if(NumberUtils.isCreatable(searchTerm)){
            for(String ColumName:productNumberColumns){
           JSONObject match= new JSONObject().accumulate("match", new JSONObject()
                   .put(ColumName,Double.parseDouble(searchTerm))
           );
          // shouldArray.put(shouldCounter++, match); 

        }
            boolObject=  new JSONObject()
            .accumulate("bool",new JSONObject()
            .put("must", mustArray)
            .put("should", shouldArray)
            .put("filter", filterArray)
            .put("must_not", mustArray));
            httpParameters.accumulate("query", boolObject);
            
        }
        **/
        
        //Or a long text with atleast two words ?
        //else if(SearchUtilities.countWords(searchRequest.getSearchTerm())>1){
          JSONObject  _query = new JSONObject().accumulate("query", searchTerm.toLowerCase());
          JSONObject  query_string=new JSONObject().put("query_string", _query);
            shouldArray.put(query_string);
            
        //}
        //else{
            // It must be first few characterss of a text or a whole word
             JSONObject _queryString = new JSONObject();
             _queryString.put("query", searchTerm.toLowerCase()+"*").put("fields", productTextColumns).put("lowercase_expanded_terms",false);
             //httpParameters.accumulate("query", new JSONObject().put("query_string", _queryString));
            query_string=new JSONObject().put("query_string", _queryString);
            shouldArray.put(query_string);
       // }
         // Then refine and change the search query
            // Prepare this for fuzziness incase if no results where found for lettered search values.
            Query query=new Query();
            Multi_Match multi_Match=new Multi_Match();
            multi_Match.setFuzziness(2);
            multi_Match.setQuery(searchTerm.toLowerCase());
            multi_Match.setFields(productTextColumns);
            query.setMulti_match(multi_Match);
            JSONObject _fuzzifiedSearchQuery=new JSONObject(SearchUtilities.convertObjectToJson(query));
            shouldArray.put(_fuzzifiedSearchQuery);
            Bool bool= new Bool();
            bool.should=shouldArray.toList();
            bool.setFilter(new ArrayList<>());
            bool.must=new ArrayList<>();
            boolObject=  new JSONObject()
            .accumulate("bool",new JSONObject(SearchUtilities.convertObjectToJson(bool)));
            //Add Verified Flag as a 'MUST' or 'COMPULSORY' query requirement
            JSONObject  verifiedFlag = new JSONObject().accumulate(searchRequest.getVerifiedFlag(), searchRequest.getVerifiedFlagValue());
            JSONObject matchVerifiedFlag= new JSONObject().accumulate("match", verifiedFlag);
            mustArray.put(matchVerifiedFlag);
            //Add boolObject as a 'MUST' or 'COMPULSORY' query requirement
            mustArray.put(boolObject);//Add boolObject 
            boolObject=  new JSONObject()
            .accumulate("bool",new JSONObject()
            .put("must", mustArray)
            .put("filter", SearchUtilities.getfilterQueryArray(searchRequest)));
            httpParameters.put("aggs",this.getAggregationRequestCommand());
            httpParameters.accumulate("query",boolObject);
            httpParameters.put("size",searchRequest.getSize());
            apiLogger.log(Level.INFO," The requestedServiceName :::"+requestedServiceName);
            apiLogger.log(Level.INFO," Composed JSON httpParameters :::"+httpParameters);
            return makeRemoteRequest(host_api_url,requestedEndPointPath,"post",requestedServiceName, this.requestedIndexName,httpParameters);

    }/**
     * Prepares the aggregations commands needed by search query.
     * @return 
     */
    private JSONObject getAggregationRequestCommand(){
        JSONObject anotherAggs=null;
        JSONObject aggregationRequestCommand=null;
        //Does the user want to request for a particular aggregation field?
        if(searchRequest.getAggs()!=null && !searchRequest.getAggs().equals("")){
            String fieldToAggregate=searchRequest.getAggs().getFieldName();
            aggregationRequestCommand=SearchUtilities.getCustomAggregate(fieldToAggregate,
            searchRequest.getAggs().getType(),"group_by_"+fieldToAggregate,
            anotherAggs);    
        }
        else{
            //Then aggregate these fields in a nested way since Elastic Search doesn't support multiple aggregations in a top level way for now.
            aggregationRequestCommand=SearchUtilities. getCustomAggregate("name","terms","group_by_name",
            SearchUtilities. getCustomAggregate("categoryName","terms","group_by_categoryName",
            SearchUtilities. getCustomAggregate("subCategoryName","terms","group_by_subCategoryName",
            SearchUtilities. getCustomAggregate("designerName","terms","group_by_designerName",
            SearchUtilities. getCustomAggregate("materialName","terms","group_by_materialName",
            SearchUtilities. getCustomAggregate("acceptCustomSizes","terms","group_by_acceptCustomSizes",
            SearchUtilities. getCustomAggregate("availability","terms","group_by_availability",
            SearchUtilities. getCustomAggregate("inStock","terms","group_by_inStock",
            anotherAggs))))))));
        }
        return aggregationRequestCommand;
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
               return  makeRemoteRequest(host_api_url,requestedEndPointPath,"post",requestedServiceName, this.requestedIndexName, httpParameters);
                  
    }
 
}
