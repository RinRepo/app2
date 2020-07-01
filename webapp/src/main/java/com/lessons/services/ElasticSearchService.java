package com.lessons.services;

import com.lessons.config.ElasticSearchResources;
import com.lessons.models.EsSearchDTO;
import com.lessons.models.SortDTO;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

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


    @PostConstruct
    public void init() {
        logger.debug("init() started.");

        // In order to make outgoing calls to ElasticSearch you need 2 things:
        //  1) The elastic search url
        //  2) The initialiaed AsyncHttpClient object
        this.elasticSearchUrl = elasticSearchResources.getElasticSearchUrl();
        this.asyncHttpClient = elasticSearchResources.getAsyncHttpClient();
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

    public String createSearch(EsSearchDTO esSearchDTO) throws Exception {
        //define this variable to use with quesry string below
        String queryClause;

        String sortClause = generateEsSort(esSearchDTO.getSorts());
        //these two lines do the same as the one above
//        List<SortDTO> sortList = esSearchDTO.getSorts();
//        String sortClause = generateEsSort(sortList);

        if (StringUtils.isBlank(esSearchDTO.getRawQuery())) {
            queryClause = "\n" +
                    "  \"query\": {\n" +
                    "    \"match_all\": {}\n" +
                    "  }\n";
        } else {
            queryClause = "  \"query\": {\n" +
                    "    \"query_string\": {\n" +
                    "      \"query\": \"" + esSearchDTO.getRawQuery().replace("\"", "\\\"") + "\"\n" +
                    "    }}\n";
        }

        queryClause = StringUtils.removeEnd(queryClause, ",");

            String searchParams = "{\n" +
                    sortClause +
                    "  \"from\" : " + esSearchDTO.getStartingRecordNumber() + ", \n" +
                    "  \"size\" : " + esSearchDTO.getPageSize() + ",\n" +
                    queryClause + "\n" +
                    "}";


            // Make a synchronous POST call to ElasticSearch to create this an index
            Response response = this.asyncHttpClient.preparePost(this.elasticSearchUrl + "/reports/_search?pretty")
                    .setRequestTimeout(this.ES_REQUEST_TIMEOUT_IN_MILLISECS)
                    .setHeader("accept", "application/json")
                    .setHeader("Content-Type", "application/json")
                    .setBody(searchParams)
                    .execute()
                    .get();


            if (response.getStatusCode() != 200) {
                // ElasticSearch returned a non-200 status response
                throw new RuntimeException("Error in createIndex:  ES returned a status code of " + response.getStatusCode() + " with an error of: " + response.getResponseBody());
            }
            String jsonResults = response.getResponseBody();
            return jsonResults;
        }

    private String generateEsSort(List<SortDTO> sort) {
        logger.debug("started generateEsSort");

        // || this means OR allows to handle two cases with the same return
        if ((sort == null) || (sort.size() == 0)) {
            return "";
        }

        String sortClause = "\"sort\": [\n";

        for (SortDTO s : sort) {
            sortClause = sortClause + "{\"" + s.getField() + "\": {\"order\": \"" + s.getDirection() + "\"}},";
        }
        //this will remove the comma from the last line
        sortClause = StringUtils.removeEnd(sortClause, ",");
        //this will add the end bracket
        sortClause = sortClause + "],";

        return sortClause;
    }

//    public void isSortingValid(List<SortDTO> sortDTOs) {
//        logger.debug("started isSortingValid");
//
//        if ((sortDTOs == null) || (sortDTOs.size() == 0)) {
//            return true;
//        }
//        //TODO if (es.);
//    }
}




