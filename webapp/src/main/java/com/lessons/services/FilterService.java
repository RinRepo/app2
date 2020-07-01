package com.lessons.services;

import com.lessons.models.FilterChildDTO;
import com.lessons.models.FilterPopsDTO;
import com.lessons.models.SqlInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilterService {
    private static final Logger logger = LoggerFactory.getLogger(FilterService.class);

    @Resource
    private DataSource datasource;

    int i = 1;

    public FilterService()
    {
        logger.debug("FilterService() constructor is called.");
    }

    @PostConstruct
    public void init()
    {
        logger.debug("Filter Service postconstructor");
    }

    public SqlInfoDTO getSqlInfoForFilter(FilterPopsDTO filterPopsDTO)
    {
        logger.debug("getSqlInfoForFilter is called");

        SqlInfoDTO sqlInfo = new SqlInfoDTO();

        String limitClause = " limit " + filterPopsDTO.getPagesize() + " offset " + filterPopsDTO.getOffset();
        sqlInfo.setLimitClause(limitClause);

//        String whereClause = generateWhereClause(filterPopsDTO.getFilters());

        String whereClause = "where ";

        List<FilterChildDTO> filterChildDTOList = filterPopsDTO.getFilters();
        if (filterChildDTOList == null) {
            whereClause = "";
        }
        if (filterChildDTOList.isEmpty()) {
            whereClause = "";
        }

        Map<String, Object> paramMap = new HashMap<>();

        for (FilterChildDTO filter : filterChildDTOList) {
            i = i +1;
            String paramName = "param" + 1;

            if (filter.getType().equalsIgnoreCase("string") && filter.getFieldValues().size() == 1) {
                whereClause += filter.getFieldName() + " ilike '%" + filter.getFieldValues().get(0) + "%' AND ";
            }

            if (filter.getType().equalsIgnoreCase("number")) {
                whereClause += filter.getFieldName() + " in (" + StringUtils.join(", ", filter.getFieldValues()) + ") and ";
            }

            paramMap.put(paramName, + "%")
        }
         whereClause.substring(0, whereClause.length() - 4);
        sqlInfo.setWhereClause(whereClause);
        return sqlInfo;
    }
}
