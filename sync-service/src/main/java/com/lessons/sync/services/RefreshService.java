package com.lessons.sync.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.lessons.sync.models.ReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("com.lessons.sync.services.RefreshService")
public class RefreshService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshService.class);

    private boolean isRefreshInProgress = false;

    @Resource
    private ElasticSearchService elasticSearchService;

    @Resource
    private DataSource dataSource;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init()
    {
        logger.debug("postconstructor");

        this.objectMapper = new ObjectMapper();
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.objectMapper.setDateFormat(dateFormat);
    }


    /**
     * Refresh all mappings
     *
     * @throws Exception if something bad happens
     */
    public void refreshAllMappings() throws Exception {
        logger.debug("refreshAllMappings() started.");

        // Create a new index with an alias of "reports" --> reports_YYYYMMDD__HH24MISS
        refreshMappingWithAlias("reports");

        logger.debug("refreshAllMappings() finished.");
    }


    /**
     * Refresh this mapping
     *  1. Create a new index called         reports_YYYYMMDD__HH24MISS
     *  2. Add records from reports table to reports_YYYYMMDD__HH24MISS
     *  3. Have the reports alias point to   reports_YYYYMMDD__HH24MISS
     *  4. Cleanup:  Delete any other indexes that start with reports_
     *
     * @param aAliasName the alias name -- e.g., "reports"
     * @throws Exception if something bad happens
     */
    private void refreshMappingWithAlias(String aAliasName) throws Exception {
        try {
            // Set the flag to indicate a refresh is work-in-progress
            isRefreshInProgress = true;

            String mappingFilename = aAliasName + ".mapping.json";

            // Get the json mapping as a string
            String jsonMapping = readFileInClasspathToString(mappingFilename);

            // Create a new index
            String esNewIndexName = aAliasName + getCurrentDateTime();
            elasticSearchService.createIndex(esNewIndexName, jsonMapping);

            addDataToIndex(esNewIndexName);

            // Switch the alias
            setAliasToUseThisIndex(aAliasName, esNewIndexName);
        }
        finally {
            isRefreshInProgress = false;
        }
    }

    /**
     * Switch the aliases
     *  1) Get the list of indeces that are used by the current alias name
     *  2) Construct the JSON to
     *      a) Remove the existing indeces
     *      b) Add the newly-created index
     *  3) Submit the JSON to make this alias change
     *
     * @param aAliasName
     * @param esNewIndexName
     */
    private void setAliasToUseThisIndex(String aAliasName, String esNewIndexName) throws Exception {
        List<String> currentIndexesAliasUses = elasticSearchService.getIndexesUsedByAlias(aAliasName);

        String jsonAliasChange = "{" +
                "     \"actions\": [" +
                "       {" +
                "          \"add\": {\n" +
                "             \"index\": \"" + esNewIndexName + "\"," +
                "             \"alias\": \"" + aAliasName + "\"" +
                "          }" +
                "       }";


        for (String indexName: currentIndexesAliasUses) {
            jsonAliasChange = jsonAliasChange + ",{\n" +
                    "          \"remove\": {\n" +
                    "             \"index\": \"" + indexName + "\",\n" +
                    "             \"alias\": \"" + aAliasName + "\"\n" +
                    "          }\n" +
                    "       }";
        }


        // Complete the JSON string
        jsonAliasChange = jsonAliasChange + "]}";

        // Submit the JSON request
        elasticSearchService.setAliases(jsonAliasChange);
    }


    /**
     * Copy data from the Reports db table to the Reports ES Index
     * @param aIndexName holds the name of the index to write to
     * @throws Exception
     */
    private void addDataToIndex(String aIndexName) throws Exception
    {
        logger.debug("addDataToIndex() started  aIndexName={}", aIndexName);

        // Execute the SQL to get a list of ReportDTO objects
        String sql = "select id, display_name, description, priority, created_date from view_all_reports";
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(ReportDTO.class);
        List<ReportDTO> allReports = jt.query(sql, rowMapper);

        StringBuilder bulkJsonRequest = new StringBuilder();

        // Loop through the ReportDTO objects building a Bulk JSON string
        for (ReportDTO rpt: allReports) {
            String line1 = "{ \"index\": { \"_index\": \"" + aIndexName + "\", \"_type\": \"record\", \"_id\": " + rpt.getId() + " }}\n";

            // Use the objectMapper to convert this ReportDTO object --> JSON
            String line2 = objectMapper.writeValueAsString(rpt) + "\n";

            bulkJsonRequest.append(line1);
            bulkJsonRequest.append(line2);
        }

        // Submit the Bulk Request to ElasticSearch
        elasticSearchService.submitBulkJsonRequest(bulkJsonRequest.toString(), false);

        logger.debug("addDataToIndex() finishedaIndexName={}", aIndexName);
    }


    /**************************************************************
     * getCurrentDateTime()
     **************************************************************/
    private static String getCurrentDateTime()
    {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return(df.format(new Date()));
    }


    /**
     * @param aFilename holds the name of the filename to look for in the classpath
     * @return the file contents as a string
     * @throws IOException
     */
    public static String readFileInClasspathToString(String aFilename) throws IOException {
        try (InputStream inputStream =  RefreshService.class.getResourceAsStream("/" + aFilename)) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }
    }


    public boolean isRefreshInProgress() {
        return isRefreshInProgress;
    }
}