package com.lessons.controllers;

import com.lessons.models.*;
import com.lessons.services.ReportsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Controller
public class ReportsController {
    private static final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    private final int PAGE_SIZE_MAX = 10000;
    private final int PAGE_SIZE_MIN = 1;
    private final int STARTING_RECORD_NUMBER = 1;

    public ReportsController() {
        logger.debug("ReportsController () Constructor called");
    }

    @PostConstruct
    public void init() {
        logger.debug("postconstructor");
    }

    @Resource
    private ReportsService reportsService;


    /*
     * /api/getStuff?id=some_long_string
     */
    @RequestMapping(value = "/api/getStuff", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getStuff(@RequestParam String id) {
        logger.debug("id={}", id);

        String returnValue = "id=" + id;

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnValue);
    }


    /*
     * /api/getStuff?id=some_long_string
     */
    @RequestMapping(value = "/api/getStuff/exercise", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getStuff2(@RequestParam String id,
                                       @RequestParam String name) {
        logger.debug("id={}", id);
        logger.debug("name={}", name);

        String returnValue = "id=" + id + " name=" + name;

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnValue);
    }

    /*
     * /api/getStuff2?id=some_long_string
     */
    @RequestMapping(value = "/api/getStuff3", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getStuff3(@RequestParam(name = "id") String stuffId) {
        logger.debug("stuffId={}", stuffId);

        String returnValue = "stuffId=" + stuffId;

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnValue);
    }

    /*
     * /api/getStuff3?id=7
     */
    @RequestMapping(value = "/api/getStuff4", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getStuff4(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "optional", required = false) String optional) {
        logger.debug("id={}  optional={}", id, optional);

        String returnValue = "id=" + id + " optional=" + optional;

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnValue);
    }

    @RequestMapping(value = "/api/reports/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> addReport(@RequestBody ReportsDTO reportsDTO) {
        logger.debug("addReport yay");

        //running sql code to insert into reports table
        this.reportsService.addRecord(reportsDTO);

        // Return the string as plain-text
        return ResponseEntity

                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("");

    }


    /*
     * /api/getStuff7/{id}
     */
    @RequestMapping(value = "/api/reports/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getReportById(@PathVariable Integer reportId) {
        logger.debug("id={}", reportId);

        //running sql code to get reports by id
        //this. is explict - meaning that the reportService variable exists and is defined within reportsController class
        ReportByIdDTO reportByIdDTO = this.reportsService.getReportById(reportId);

        // Return the map of parameters back as JSON
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportByIdDTO);
    }

    /*
     * /api/getStuff7/{id}
     */
    @RequestMapping(value = "/api/reports/{reportId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> deleteReportById(@PathVariable Integer reportId) {
        logger.debug("id={}", reportId);

        boolean adamIsMean = this.reportsService.doesReportExist(reportId);
        //Verify that the report exists
        if (reportId < 1 || adamIsMean == false){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Cannot Delete Record, Report id not found.");
        }

        int rowsDeleted = this.reportsService.deleteReportById(reportId);
        // Return status that corresponds to 'OK' - body is blank bc we only want the status
        if (rowsDeleted <1){
            return ResponseEntity
                    .status(HttpStatus.NOT_IMPLEMENTED)
                    .body("No reports were deleted. Better luck next time.");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }

//    @RequestMapping(value = "/api/reports/update", method = RequestMethod.POST, produces = "application/json")
//    public  ResponseEntity<?> updateReportById(@RequestBody UpdateReportsDTO updateReportsDTO) {
//        logger.debug("Update Report");
//
//        //running sql code to insert into reports table
//        boolean adamIsKindaAlright = this.reportsService.doesReportExist(updateReportsDTO.getId());
//
//        //Verify that the report exists
//        if (updateReportsDTO.getId() < 1 || adamIsKindaAlright == false){
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body("Cannot Update Record, Report id not found.");
//        }
//
//        int rowsUpdated = this.reportsService.updateReportById(updateReportsDTO);
//        // Return status that corresponds to 'OK' - body is blank bc we only want the status
//        if (rowsUpdated <1){
//            return ResponseEntity
//                    .status(HttpStatus.NOT_IMPLEMENTED)
//                    .body("No reports were updated. Better luck next time.");
//        }
//
//        // Return the string as plain-text
//        return ResponseEntity
//
//                .status(HttpStatus.OK)
//                .contentType(MediaType.TEXT_PLAIN)
//                .body("");
//
//
//    }

//    @RequestMapping(value = "/api/reports/update2", method = RequestMethod.POST, produces = "application/json")
//    public  ResponseEntity<?> updateReportById2(@RequestBody UpdateReportsDTO updateReportsDTO) {
//        logger.debug("Update Report2");
//
//        //running sql code to insert into reports table
//        boolean adamIsKindaAlright = this.reportsService.doesReportExist(updateReportsDTO.getId());
//
//        //Verify that the report exists
//        if (updateReportsDTO.getId() < 1 || adamIsKindaAlright == false){
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .body("Cannot Update Record, Report id not found.");
//        }
//
//        int rowsUpdated = this.reportsService.updateReportById2(updateReportsDTO);
//        // Return status that corresponds to 'OK' - body is blank bc we only want the status
//        if (rowsUpdated <1){
//            return ResponseEntity
//                    .status(HttpStatus.NOT_IMPLEMENTED)
//                    .body("No reports were updated. Better luck next time.");
//        }
//
//        // Return the string as plain-text
//        return ResponseEntity
//
//                .status(HttpStatus.OK)
//                .contentType(MediaType.TEXT_PLAIN)
//                .body("");
//
//
//    }

    @RequestMapping(value = "/api/reports/fancyUpdate", method = RequestMethod.POST, produces = "application/json")
    public  ResponseEntity<?> fancyUpdateAud(@RequestBody UpdateReportsDTO updateReportsDTO) {
        logger.debug("Fancy Update");

        int x = 7;
        if (x == 7) {
            throw new RuntimeException("Hola Mama");
        }

        //running sql code to insert into reports table
        boolean doesReportExist = this.reportsService.doesReportExist(updateReportsDTO.getId());

        //Verify that the report exists
        if (updateReportsDTO.getId() < 1 || doesReportExist == false){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Cannot Update Record, Report id not found.");
        }

        if (updateReportsDTO.getVersion() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Cannot update record, version cannot be null");
        }

        if (reportsService.isVersionLatest(updateReportsDTO.getId(), updateReportsDTO.getVersion()) == false) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Update failed; you are trying to updat an already updated record. You will lose data upon refresh, my bad.");
        }

        this.reportsService.fancyUpdateAud(updateReportsDTO, "Rin");

        // Return the string as plain-text
        return ResponseEntity

                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body("");

    }

    /*************************************************************************
     * REST endpoint /api/reports/add
     *
     * @return nothing
     *************************************************************************/
    @RequestMapping(value = "/api/reports/add1", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> addReport1(@RequestBody AddReportDTO aAddReportDTO) {
        logger.debug("addReport() started.");

        // Simulate adding a report to the system

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }
    /*************************************************************************
     * REST endpoint /api/reports/all
     *
     * @return list of ReportsDTO objects
     *************************************************************************/
    @RequestMapping(value = "/api/reports/all", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllReports() {
        logger.debug("getAllReports() started.");

        List<ViewReportsDTO> allReports = reportsService.getAllReports();

        // Return the list of ReportDTO objects back to the front-end
        // (Spring will convert from Java to JSON)
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allReports);
    }


    /**
     * @param filterPopsDTO
     * @return
     */
    @RequestMapping(value = "/api/reports/filtered", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> reportGridFiltered(@RequestBody FilterPopsDTO filterPopsDTO) {
        logger.debug("reportGridFiltered() started.");

        if (filterPopsDTO.getPagesize() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Passed in Page size cannot be null.");
        }

        if (filterPopsDTO.getPagesize()>PAGE_SIZE_MAX) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Page size cannot be more that 10k.");
        }

        if (filterPopsDTO.getPagesize()<PAGE_SIZE_MIN) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Page size cannot be more less than 1.");
        }

        if (filterPopsDTO.getOffset() ==null || filterPopsDTO.getOffset() < 0) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Passed in offset cannot be null or less than 1");
        }

        //calls the method and returns list of ViewReportsDTO
        List<ViewReportsDTO> allReports = reportsService.getAllReportsFiltered(filterPopsDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allReports);
    }
}
