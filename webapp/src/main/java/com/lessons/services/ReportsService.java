package com.lessons.services;

import com.lessons.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportsService {
    private static final Logger logger = LoggerFactory.getLogger(ReportsService.class);

    @Resource
    private DataSource dataSource;  //this injects my datasource

    @Resource
    private FilterService filterService;

    @Value("${bypass.id:}")
    private Integer byPassId;

    public ReportsService()
    {

        logger.debug("ReportsService() Constructor is called.");
    }

    @PostConstruct
    public void init()
    {
        logger.debug("postconstructor");
    }

    //this is a public 'void' because we are returning nothing; only a runtimeException will return if unhappy.
    public void addRecord(ReportsDTO reportsDTO)
    {
        logger.debug("reportsDTO is called");

        String sql = "insert into reports (id, description, reviewed, priority)" +
                "values(?, ?, ?, ?)";

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);

        int rowsCreated = jt.update(sql, getNextId(), reportsDTO.getDescription(), reportsDTO.isReviewed(), reportsDTO.getPriority());
        if(rowsCreated != 1)
        {
            throw new RuntimeException("Attempted to insert 1 record, instead " + rowsCreated + " were created.");
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
// we are only passing in the id value from frontend
    public ReportByIdDTO getReportById(int reportId)
    {
        logger.debug("ReportByIdDTO is called");

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(ReportByIdDTO.class);

        String sql = "select id, description, reviewed, priority, created_date, last_modified_date " +
                "from reports where id = ?";

        ReportByIdDTO reportByIdDTO = (ReportByIdDTO) jt.queryForObject(sql, rowMapper, reportId);

        return reportByIdDTO;

    }

    //this is a public 'void' because we are returning nothing; only a runtimeException will return if unhappy.
    public int deleteReportById(int reportId)
    {
        logger.debug("DeleteReportById is called");

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        String sql = "delete from reports where id = ?";

       int rowsDeleted = jt.update(sql, reportId);

       return rowsDeleted;


    }

//in order to check if we have reports by a certain id
    public boolean doesReportExist(int reportId)
    {
        logger.debug("DoesReportExist is called");

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        String sql = "select count(*) from reports where id = ?" ;

        int reportCount = jt.queryForObject(sql, Integer.class, reportId);

        if (reportCount < 1) {return false;}
        else {return true;}
    }

//    //this is a public 'void' because we are returning nothing; only a runtimeException will return if unhappy.
//    public int updateReportById(UpdateReportsDTO updateReportsDTO)
//    {
//        logger.debug("UpdateReportsDTO is called");
//
//        String sql = "update reports\n" +
//                "set description = ?,\n" +
//                "    version = ?,\n" +
//                "    priority = ?,\n" +
//                "    display_name = ?,\n" +
//                "    reference_source = ?,\n" +
//                "last_modified_date = now()\n" +
//                "where id = ?";
//
//        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
//
//        int rowsUpdated = jt.update(sql, updateReportsDTO.getDescription(), updateReportsDTO.getVersion(),
//                updateReportsDTO.getPriority(), updateReportsDTO.getDisplay_name(), updateReportsDTO.getReference_source(), updateReportsDTO.getId());
//        if(rowsUpdated!= 1)
//        {
//            throw new RuntimeException("Attempted to update 1 record, instead " + rowsUpdated + " were updated.");
//        }
//        return rowsUpdated;
//    }

//    //this is a public 'void' because we are returning nothing; only a runtimeException will return if unhappy.
//    public int updateReportById2(UpdateReportsDTO updateReportsDTO)
//    {
//        logger.debug("UpdateReportsDTO is called");
//
//
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("description", updateReportsDTO.getDescription());
//        map.put("version", updateReportsDTO.getVersion());
//        map.put("priority", updateReportsDTO.getPriority());
//        map.put("display_name", updateReportsDTO.getDisplay_name());
//        map.put("reference_source", updateReportsDTO.getReference_source());
//        map.put("id", updateReportsDTO.getId());
//
//        String sql = "update reports\n" +
//                "set description = :description,\n" +
//                "    version = :version,\n" +
//                "    priority = :priority,\n" +
//                "    display_name = :display_name,\n" +
//                "    reference_source = :reference_source,\n" +
//                "last_modified_date = now()\n" +
//                "where id = :id";
//
//        NamedParameterJdbcTemplate np = new NamedParameterJdbcTemplate(this.dataSource);
//
//        int rowsUpdated = np.update(sql, map);
//        if(rowsUpdated!= 1)
//        {
//            throw new RuntimeException("Attempted to update 1 record, instead " + rowsUpdated + " were updated.");
//        }
//        return rowsUpdated;
//    }

    //this is a public 'void' because we are returning nothing; only a runtimeException will return if unhappy.
    public void fancyUpdateAud(final UpdateReportsDTO updateReportsDTO, final String username)
    {
        logger.debug("UpdateReportsDTO is called");


        TransactionTemplate tt = new TransactionTemplate();
        tt.setTransactionManager(new DataSourceTransactionManager(dataSource));

        // This transaction will throw a TransactionTimedOutException after 60 seconds (causing the transaction to rollback)
        tt.setTimeout(60);

        tt.execute(new TransactionCallbackWithoutResult()
        {
            protected void doInTransactionWithoutResult(TransactionStatus aStatus)
            {
                NamedParameterJdbcTemplate np = new NamedParameterJdbcTemplate(dataSource);

                HashMap<String, Object> map = new HashMap<>();
                map.put("description", updateReportsDTO.getDescription());
//                map.put("version", updateReportsDTO.getVersion());
                map.put("priority", updateReportsDTO.getPriority());
                map.put("display_name", updateReportsDTO.getDisplay_name());
                map.put("reference_source", updateReportsDTO.getReference_source());
                map.put("id", updateReportsDTO.getId());

                // Run SQL Statement #1
                String sql = "update reports\n" +
                        "set description = :description,\n" +
                        "    version = version + 1,\n" +
                        "    priority = :priority,\n" +
                        "    display_name = :display_name,\n" +
                        "    reference_source = :reference_source,\n" +
                        "last_modified_date = now()\n" +
                        "where id = :id returning *";


                Map<String, Object> updatedMap = np.queryForMap(sql, map);
                updatedMap.put("rev_type", 1);
                updatedMap.put("username", username);
                updatedMap.put("rev", getNextId());

                // Run SQL Statement #2
                String Sql = "Insert into reports_aud(id, version, description, display_name, reviewed, reference_source, priority, created_date, last_modified_date, \n" +
                        "                        is_custom_report, reserved, reserved_by, rev, rev_type, username, timestamp) \n" +
                        "                        values (:id, :version, :description, :display_name, :reviewed, :reference_source, :priority, :created_date, :last_modified_date,\n" +
                        "                                :is_custom_report, :reserved, :reserved_by, :rev, :rev_type, :username, now())";

                int rowsInserted = np.update(Sql, updatedMap);

                if(rowsInserted != 1)
                {
                    throw new RuntimeException("Attempted to insert an audit record, instead " + rowsInserted + " were inserted.");
                }

                // Commit the transaction if I get to the end of this method
            }
        });

        logger.debug("runTransactionWithNoResults() finished");
    }

    public boolean isVersionLatest(int reportId, Integer version) {
        logger.debug("isVersionLatest called");

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        String sql = "select version from reports where id = ?";

        Integer currentVersion = jt.queryForObject(sql, Integer.class, reportId);

        if (currentVersion == null){
            return false;
        }

        if (currentVersion.intValue() == version) {
            return true;
        } else {
            return false;
        }

    }

    public boolean whoDunnit(int reportId, String display_name) {
        logger.debug("whoDunnit was called");

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        String sql = "select username from reports_aud where id = ?";

        String whoDunnit = jt.queryForObject(sql, String.class, reportId);

        if (whoDunnit == null){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * @return list of ReportDTO objects
     */
    public List<ViewReportsDTO> getAllReports() {
        logger.debug("getAllReports() started.");

        String sql = "SELECT id, version, description, display_name, priority, created_date, last_modified_date, active " +
                "FROM reports " +
                "ORDER BY id";

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(ViewReportsDTO.class);

        // Execute the SQL and use the rowMapper to convert the returned list of records into a list of DTO objects
        List<ViewReportsDTO> listOfReports = jt.query(sql, rowMapper);

        logger.debug("getAllReports() finished.  Returning listOfReports.size={}", listOfReports.size());
        return listOfReports;
    }

    public List<ViewReportsDTO> getAllReportsFiltered(FilterPopsDTO filterPopsDTO) {
        logger.debug("getAllReportsFiltered() started.");

        SqlInfoDTO sqlInfo = this.filterService.getSqlInfoForFilter(filterPopsDTO);


        String sql = "SELECT id, version, description, display_name, priority, created_date, last_modified_date, active " +
                "FROM reports " + sqlInfo.getWhereClause() +
                "ORDER BY id" + sqlInfo.getLimitClause();

        JdbcTemplate jt = new JdbcTemplate(this.dataSource);
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(ViewReportsDTO.class);

        // Execute the SQL and use the rowMapper to convert the returned list of records into a list of DTO objects
        List<ViewReportsDTO> listOfFilteredReports = jt.query(sql, rowMapper);

        logger.debug("getAllReportsFiltered() finished.  Returning listOfFilteredReports.size={}", listOfFilteredReports.size());
        return listOfFilteredReports;

    }
}
