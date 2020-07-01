package com.lessons.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private DataSource dataSource;

    public UserService()
    {
        logger.debug("Inside the single-arg contstructor in UserService");

    }

    @PostConstruct
    public void init()
    {
        logger.debug("postconstructor");
    }

    public int getCount(){
        logger.debug("Started getCount()");
        //needed in order to run sql
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        String sql = "select count(*) from users";

        Integer count = jt.queryForObject(sql, Integer.class);
        return count;
    }
}
