package com.lessons.controllers;

import com.lessons.models.IndicatorDTO;
import com.lessons.models.IndicatorIN;
import com.lessons.models.IndicatorTypesDTO;
import com.lessons.services.IndicatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Controller
public class IndicatorController {
    private static final Logger logger = LoggerFactory.getLogger(IndicatorController.class);

    public IndicatorController()
    {
        logger.debug("IndicatorController () Constructor called");
    }

    @PostConstruct
        public void init()
    {
        logger.debug("postconstructor");
    }

    @Resource
    private IndicatorService indicatorService;


    /*************************************************************************
     * REST endpoint /api/indicators
     *
     * @return a JSON list of Indicators
     *************************************************************************/
    @RequestMapping(value = "/api/indicators", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getIndicatorList() {
        logger.debug("getDashboardDetails() started.");

        // Get the list of IndicatorDTO objects
        List<IndicatorDTO> indicatorList = indicatorService.getAllIndicators();

        // Return the Indicator List as Json
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(indicatorList);
    }

    /*************************************************************************
     * REST endpoint /api/indicators/filtered
     *
     * @return a list of indicators via indicator dto
     *************************************************************************/
    @RequestMapping(value = "/api/indicators/filtered", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getIndicatorNamedList(@RequestBody IndicatorIN indicatorIN){
        logger.debug("getIndicatorNamedList started.");

        //check that starting record number is not less than 1
        if (indicatorIN.getStartingRecordNumber() <1){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Starting Record Number must be a value 1 or greater.");
        }

        //check that the pageSize is not less than 1
        if (indicatorIN.getPagesize() <1){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("WHY YOU DO THIS? PageSize must be 1 or higher.");
        }
        //check if field or descriptions are null
//        if (SortDTO.get){}
//        else if (){}

        // Get the list of IndicatorTypesDTO
        List<IndicatorTypesDTO> indicatorList = indicatorService.getFilteredIndicators(indicatorIN);

        //return the indicator list as Json
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(indicatorList);

    }

}
