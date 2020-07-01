package com.lessons.services;

import com.lessons.models.AddCountermeasuresDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@Service
public class CountermeasureService {
    private static final Logger logger = LoggerFactory.getLogger(CountermeasureService.class);

    @Resource
    private DataSource dataSource;

    public CountermeasureService()
    {
        logger.debug("CountermeasureService() Constructructor is called.");
    }

    @PostConstruct
    public void init()
    {
        logger.debug("postconstructor");
    }

    public void addCountermeasure(AddCountermeasuresDTO addCountermeasuresDTO)
    {
        logger.debug("AddCountermeasureDTO is called.");

        String sql = "insert into countermeasures(id, value, start_date, end_date, status)\n" +
                "values(?,?,?,?,?);";

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);

        int rowsCreated = jt.update(sql, getNextId(), addCountermeasuresDTO.getValue(), addCountermeasuresDTO.getEnd_date(), addCountermeasuresDTO.getStart_date(), addCountermeasuresDTO.getStatus());
        if(rowsCreated !=1)
        {
            throw new RuntimeException("Attempted to insert 1 record into Countermeasures, instead" + rowsCreated + " were created.");
        }
    }

    public Integer getNextId(){
        logger.debug("Started getReportId()");
        //needed in order to run sql
        JdbcTemplate jt = new JdbcTemplate(dataSource);
        String sql = "select nextval('seq_table_ids')";

        Integer reportId = jt.queryForObject(sql, Integer.class);
        return reportId;
    }

//    /in order to check if we have reports by a certain id
//    public boolean doesReportExist(int reportId)
//    {
//        logger.debug("DoesReportExist is called");
//
//        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
//        String sql = "select count(*) from reports where id = ?" ;
//
//        int reportCount = jt.queryForObject(sql, Integer.class, reportId);
//
//        if (reportCount < 1) {return false;}
//        else {return true;}
//    }

}
