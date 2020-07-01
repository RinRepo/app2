package com.lessons.controllers;

import com.lessons.models.AddCountermeasuresDTO;
import com.lessons.services.CountermeasureService;
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

@Controller
public class CountermeasuresController {
    private static final Logger logger = LoggerFactory.getLogger(CountermeasuresController.class);

    public CountermeasuresController() {logger.debug("CountermeasureController() Constructor called");}

    @PostConstruct
    public void init() {logger.debug("postconstructor");}

    @Resource
    private CountermeasureService countermeasureService;


    @RequestMapping(value = "/api/countermeasures/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> addCountermeasure(@RequestBody AddCountermeasuresDTO addCountermeasuresDTO) {
    logger.debug("addCountermeasure() started.");

    return ResponseEntity
            .status(HttpStatus.OK)
            .body("");

    }
}
