package com.lessons.controllers;

import com.lessons.models.EsSearchDTO;
import com.lessons.services.ElasticSearchService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Controller //controller annotation that tells spring that it will contain rest end methods
public class EsSearchController {
    private static final Logger logger = LoggerFactory.getLogger(IndicatorController.class);

    //this is a constant/class variable - used in if/checks for searchStuff
    private final int PAGE_SIZE_MAX = 10000;
    private final int PAGE_SIZE_MIN = 1;
    private final int STARTING_RECORD_NUMBER = 1;

    public EsSearchController() {logger.debug("EsSearchController () Constructor called");}

    @PostConstruct
        public void init() { logger.debug("postconstructor");}

@Resource
private ElasticSearchService elasticSearchService;

    @RequestMapping(value = "/api/search", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> searchStuff(@RequestBody EsSearchDTO esSearchDTO) throws Exception {
        logger.debug("Search Stuff");

        if (StringUtils.isBlank(esSearchDTO.getIndexName())){
            return ResponseEntity

                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Index_name cannot be null, blank, or contain whitespaces. Please try again.");
        }

        if (esSearchDTO.getPageSize()>PAGE_SIZE_MAX){
            return ResponseEntity

                    .status(HttpStatus.I_AM_A_TEAPOT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("WHY YOU DO THIS - PAGE SIZE TOO SWOL. Please try again.");
        }

        if (esSearchDTO.getPageSize()<PAGE_SIZE_MIN){
            return ResponseEntity

                    .status(HttpStatus.I_AM_A_TEAPOT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("WHY YOU DO THIS - page size cannot be less than 1. Please try again.");
        }

        if (esSearchDTO.getStartingRecordNumber()<STARTING_RECORD_NUMBER){
            return ResponseEntity

                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Starting Record cannot be less than 1. Return to start, do not pass go, do not collect 200$.");

        }

//        if (elasticSearchService.isSortingValid(esSearchDTO.getSorts()) == false){
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body("Please check your sorting inputs.");
//        }


String json = elasticSearchService.createSearch(esSearchDTO);

        return ResponseEntity

                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(json);
    }

    @RequestMapping(value = "/api/search", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> searchStuff(@RequestBody EsSearchDTO esSearchDTO) throws Exception {
        logger.debug("Search Stuff");

        if (StringUtils.isBlank(esSearchDTO.getIndexName())){
            return ResponseEntity

                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Index_name cannot be null, blank, or contain whitespaces. Please try again.");
        }

        if (esSearchDTO.getPageSize()>PAGE_SIZE_MAX){
            return ResponseEntity

                    .status(HttpStatus.I_AM_A_TEAPOT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("WHY YOU DO THIS - PAGE SIZE TOO SWOL. Please try again.");
        }

        if (esSearchDTO.getPageSize()<PAGE_SIZE_MIN){
            return ResponseEntity

                    .status(HttpStatus.I_AM_A_TEAPOT)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("WHY YOU DO THIS - page size cannot be less than 1. Please try again.");
        }

        if (esSearchDTO.getStartingRecordNumber()<STARTING_RECORD_NUMBER){
            return ResponseEntity

                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Starting Record cannot be less than 1. Return to start, do not pass go, do not collect 200$.");

        }

//        if (elasticSearchService.isSortingValid(esSearchDTO.getSorts()) == false){
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body("Please check your sorting inputs.");
//        }


        String json = elasticSearchService.createSearch(esSearchDTO);

        return ResponseEntity

                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(json);
    }

}
