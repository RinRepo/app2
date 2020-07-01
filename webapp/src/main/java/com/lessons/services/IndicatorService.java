package com.lessons.services;

import com.lessons.models.IndicatorDTO;
import com.lessons.models.IndicatorIN;
import com.lessons.models.IndicatorTypesDTO;
import com.lessons.models.SortDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class IndicatorService {
    private static final Logger logger = LoggerFactory.getLogger(IndicatorService.class);

    private DataSource dataSource = null;

    public IndicatorService(DataSource aDataSource) {
        logger.debug("Inside the single-arg contstructor in Indicator Service");

        if (aDataSource == null) {
            logger.error("The datasource was null.");
            throw new RuntimeException("The datasource passed in was null. Why you do dis?");
        }

        this.dataSource = aDataSource;
    }


    public List<IndicatorDTO> getAllIndicators() {
        logger.debug("Started getAllIndicators()");
//        return null;
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(IndicatorDTO.class);

        String sql = "select id, type, value from indicators";
        List<IndicatorDTO> listOfIndicators = jt.query(sql, rowMapper);

        return listOfIndicators;

    }

    public List<IndicatorTypesDTO> getFilteredIndicators(IndicatorIN indicatorIN) {
        logger.debug("Started getIndicatorNamedList");
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(IndicatorTypesDTO.class);

        List<SortDTO> listOfSortDTOs = indicatorIN.getSorting();
        String orderBy = generateOrderBy(listOfSortDTOs);

        String sql = "SELECT i.id, i.value, it.name\n" +
                "from indicators i\n" +
                "JOIN indicator_types it ON it.id = i.type\n" +
                orderBy +
                " limit " + indicatorIN.getPagesize() + " offset " + indicatorIN.getStartingRecordNumber();

        List<IndicatorTypesDTO> listOfIndicators = jt.query(sql, rowMapper);
        return listOfIndicators;
    }

    private String generateOrderBy(List<SortDTO> sorting) {
        logger.debug("Started generateOrderBy");

        if (sorting == null) {
            return "";
        }
        if (sorting.size() == 0) {
            return "";
        }
        String sql = "order by ";
        //for loop is list of DTOs
        for (SortDTO s : sorting){
            sql = sql + s.getField() + " " + s.getDirection() + ",";
        }
        //removes last character of string, have to pass in 2 variables, the string and the character you want to remove
        sql = StringUtils.removeEnd(sql, ",");
        return sql;
    }
}
