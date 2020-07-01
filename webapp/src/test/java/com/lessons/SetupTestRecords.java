package com.lessons;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;

@RunWith(SpringRunner.class)    // Required to work with JUnit 4 -these two lines, tell to start the webapp before the unit tests
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)                // Start up a Spring Application Context
public class SetupTestRecords {
    private static final Logger logger = LoggerFactory.getLogger(FilterServiceTest.class);

    @Resource
    private DataSource dataSource;  //this injects my datasource

    @Test
    public void createTestRecords()
    {
        logger.debug("createTestRecords is called");

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);

        String sql = "insert into reports (id, version, display_name, reviewed, priority, created_date)" +
                "values(nextval('seq_table_ids'), ?, ?, ?, ?, now())";
        for(int i=1; i<=50000; i++)
        {
            int version = getRandomInt(1,9);
            String display_name = "display name " + i;
            boolean reviewed = getRandomBool();
            int priority = getRandomInt(1, 5);

            jt.update(sql, version, display_name, reviewed, priority);
        }

    }

//generates a random number
    private int getRandomInt(int min, int max){
        return (int) ((Math.random() * ((max - min) +1)) + min);
    }
    //generates a random boolean/true or false
    private boolean getRandomBool(){
//        logger.debug("getRandomBool() called");
        double randomDouble = Math.random();
        int randomNum = (int) (randomDouble * 100);
        if (randomNum % 2 == 0){
            return true;
        }
        return false;
    }


    @Test
    public void createIndicatorsRecords()
    {
        logger.debug("createIndicatorRecords is called");
//        int recordsCreated = 5;

        //log time taken to run
        long start = System.currentTimeMillis();

        //object needed to query the database
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);

        //create a stringbuilder object
        StringBuilder sb = new StringBuilder();

        //create some strings to be manipulated by string.format and stringBuilder
        String insert = "INSERT INTO indicators (id, type, value) VALUES";

        sb.append(insert);

        for(int i = 20000000; i <= 23000000; i++) {
            //determine random type and value

            Integer id = i;

            int indicatorType;
            String indicatorValue;

            if (getRandomBool() == true) {
                indicatorType = 3;
                indicatorValue = "yayDomain.org";
            } else {
                indicatorType = 5;
                indicatorValue = getRandomIP();
            }
            //insert the randomized variables into the baseValue string using string.format
            String newValues = String.format("(%s, %d, '%s'),", id.toString() ,indicatorType, indicatorValue);

            //append the formatted string to the stringbuilder object
            sb.append(newValues);

            if ((i % 50000) == 0)
            {
                sb.(sb.length() - 1);
                String newString = sb.toString();
                jt.update(newString);

                sb.setLength(0);
                sb.append( "INSERT INTO indicators (id, type, value) VALUES");
                logger.debug("{} Finished batch. Time elapsed: {}ms", i, System.currentTimeMillis() - start);
            }
        }

        if (sb.length() > 0){
            sb.deleteCharAt(sb.length() - 1);
            String newString = sb.toString();
            jt.update(newString);
        }


        logger.debug("completed update");

    }

    private String getRandomIP() {
        String resultingIP;
        int firstOctet = getRandomInt(1, 255);
        int secondOctet = getRandomInt(0, 255);
        int thirdOctet = getRandomInt(0, 255);
        int fourthOctet = getRandomInt(1, 255);
        resultingIP = firstOctet + "." + secondOctet + "." + thirdOctet + "." + fourthOctet;

        return resultingIP;
    }

}
