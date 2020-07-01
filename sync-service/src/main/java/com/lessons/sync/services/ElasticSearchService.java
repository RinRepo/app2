package com.lessons.sync.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lessons.sync.config.ElasticSearchResources;
import com.lessons.sync.models.ErrorsDTO;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("com.lessons.sync.services.ElasticSearchService")
public class ElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    @Resource
    private ElasticSearchResources elasticSearchResources;

    @Resource
    private DataSource dataSource;



    private String elasticSearchUrl;
    private AsyncHttpClient asyncHttpClient;
    private final int ES_REQUEST_TIMEOUT_IN_MILLISECS = 30000;   // All ES requests timeout after 30 seconds

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        logger.debug("init() started.");

        // In order to make outgoing calls to ElasticSearch you need 2 things:
        //  1) The elastic search url
        //  2) The initialiaed AsyncHttpClient object
        this.elasticSearchUrl = elasticSearchResources.getElasticSearchUrl();
        this.asyncHttpClient = elasticSearchResources.getAsyncHttpClient();
        this.objectMapper = new ObjectMapper();
        //date format not necessarilly needed here
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.objectMapper.setDateFormat(dateFormat);
    }


    public void createIndex(String aIndexName, String aJsonMapping) throws Exception {
        if (StringUtils.isEmpty(aIndexName)) {
            throw new RuntimeException("The passed-in aIndexName is null or empty.");
        }

        // Make a synchronous POST call to ElasticSearch to create this an index
        Response response = this.asyncHttpClient.preparePut(this.elasticSearchUrl + "/" + aIndexName)
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setBody(aJsonMapping)
                .execute()
                .get();

        if (response.getStatusCode() != 200) {
            // ElasticSearch returned a non-200 status response
            throw new RuntimeException("Error in createIndex:  ES returned a status code of " + response.getStatusCode() + " with an error of: " + response.getResponseBody());
        }

        logger.info("Successfully created this ES index: {}", aIndexName);
    }

//    public void submitBulkJson(String json) throws Exception {
//        if (StringUtils.isEmpty(json)) {
//            throw new RuntimeException("The passed-in json is null or empty.");
//        }
//
//        // Make a synchronous POST call to ElasticSearch to create this an index
//        Response response = this.asyncHttpClient.preparePost(this.elasticSearchUrl + "/_bulk")
//                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
//                .setHeader("accept", "application/json")
//                .setHeader("Content-Type", "application/json")
//                .setBody(json)
//                .execute()
//                .get();
//
//        if (response.getStatusCode() != 200) {
//            // ElasticSearch returned a non-200 status response
//            throw new RuntimeException("Error in bulk update:  ES returned a status code of " + response.getStatusCode() + " with an error of: " + response.getResponseBody());
//        }
//
//        String esJsonResponse = response.getResponseBody();
//
//        // Convert the response JSON string into an errorsDto object
//        // NOTE:  This is substantially faster as Jackson can ignore the other fields
//        ErrorsDTO errorsDTO = objectMapper.readValue(esJsonResponse, ErrorsDTO.class);
//
//        if (errorsDTO.isErrors()) {
//            logger.error("The JSON string contained errors.");
//
//            // ElasticSearch returned a non-200 status response
//            throw new RuntimeException("Error in submitBulkJsonRequest:  There were errors performing this bulk index.  ES returned this message:  " + response.getResponseBody());
//        }
//
//
//        logger.info("Successfully created bulk update.");
//    }


    /**
     * Submit a bulk JSON request to ElasticSearch
     *
     * @param aJsonBody Holds the bulk JSON request
     * @param aForceRefresh Force an ES refresh if set to True.  Otherwise, wait for refrehs interval
     */
    public void submitBulkJsonRequest(String aJsonBody, boolean aForceRefresh) throws Exception {
        if (StringUtils.isEmpty(aJsonBody)) {
            throw new RuntimeException("The passed-in JSON body is null or empty.");
        }

        String esRequestUrl;
        if (aForceRefresh) {
            esRequestUrl = this.elasticSearchUrl + "/_bulk?refresh=true";
        }
        else {
            esRequestUrl = this.elasticSearchUrl + "/_bulk";
        }

        // Make a synchronous POST call to ElasticSearch to submit this Bulk JSON request
        Response response = this.asyncHttpClient.preparePost(esRequestUrl)
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setBody(aJsonBody)
                .execute()
                .get();

        if (response.getStatusCode() != 200) {
            // ElasticSearch returned a non-200 status response
            throw new RuntimeException("Error in submitBulkJsonRequest:  ES returned a status code of " + response.getStatusCode() + " with an error of: " + response.getResponseBody());
        }

        String esJsonResponse = response.getResponseBody();

        // Convert the response JSON string into an errorsDto object
        // NOTE:  This is substantially faster as Jackson will ignore the other fields
        ErrorsDTO errorsDTO = objectMapper.readValue(esJsonResponse, ErrorsDTO.class);

        if (errorsDTO.isErrors()) {
            logger.error("The JSON string contained errors.");

            // ElasticSearch returned a non-200 status response
            throw new RuntimeException("Error in submitBulkJsonRequest:  There were errors performing this bulk index.  ES returned this message:  " + response.getResponseBody());
        }

    }

    /**
     * Submit an alias change to ElasticSearch
     *
     * @param aJsonBody holds the JSON for a list of actions to add/delete aliases
     * @throws Exception
     */
    public void setAliases(String aJsonBody) throws Exception {
        if (StringUtils.isEmpty(aJsonBody)) {
            throw new RuntimeException("The passed-in JSON body is null or empty.");
        }

        // Make a synchronous POST call to ElasticSearch to set/unset these aliases
        Response response = this.asyncHttpClient.preparePost(this.elasticSearchUrl + "/_aliases")
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .setBody(aJsonBody)
                .execute()
                .get();

        if (response.getStatusCode() != 200) {
            // ElasticSearch returned a non-200 status response
            throw new RuntimeException("Error in setAliases:  ES returned a status code of " + response.getStatusCode() + " with an error of: " + response.getResponseBody());
        }

    }


    /**
     * Query ES for a list of indicies that are currently used by this index
     *
     * @param aAliasName holds the alias name to query
     * @return a list of ES indeces that are used by this alias (empty list if none are found)
     * @throws Exception
     */
    public List<String> getIndexesUsedByAlias(String aAliasName) throws Exception {
        if (StringUtils.isEmpty(aAliasName)) {
            throw new RuntimeException("The passed-in alias name is null or empty.");
        }

        // Make a synchronous POST call to ElasticSearch to get all indicies (if any) that are used by this alias
        Response response = this.asyncHttpClient.prepareGet(this.elasticSearchUrl + "/_cat/aliases/" + aAliasName)
                .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                .setHeader("accept", "application/json")
                .setHeader("Content-Type", "application/json")
                .execute()
                .get();

        if (response.getStatusCode() != 200) {
            // ElasticSearch returned a non-200 status response
            throw new RuntimeException("Error in getIndexesUsedByAlias:  ES returned a status code of " + response.getStatusCode() + " with an error of: " + response.getResponseBody());
        }

        // Convert the ES response into a list of java maps
        String esJsonResponse = response.getResponseBody();
        List<Map<String, Object>> listOfMaps = this.objectMapper.readValue(esJsonResponse, new TypeReference<List<Map<String, Object>>>(){});


        ArrayList<String> indexNamesUsingAlias = new ArrayList<>();

        // Loop through the list of maps, pulling-out the name from the map
        for (Map<String, Object> indexMapDetails: listOfMaps) {
            String indexName = (String) indexMapDetails.get("index");
            if (StringUtils.isNotEmpty(indexName)) {
                indexNamesUsingAlias.add(indexName);
            }
        }

        // Return an unmodifiable list
        return Collections.unmodifiableList(indexNamesUsingAlias);
    }


    //method 1 cleanup method
    private void cleanup(String aIndexNameToKeep){}
}