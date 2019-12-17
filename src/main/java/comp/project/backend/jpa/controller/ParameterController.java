package comp.project.backend.jpa.controller;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.metadata.TableMetaDataContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import comp.project.backend.jpa.persistence.metaparameters.ClassParameter;

/**
 *
 * Author: TOS
 */
@CrossOrigin( origins = {"http://localhost:3000",  "http://localhost:3001"} )
@RestController
@Slf4j
public class ParameterController {
    
    private final String TABLE_NAME_METAPARTS = "MetaParts";
    private final String TABLE_NAME_PARTS = "Parts";
    private final String COLUMN_NAME_CLASS_1 = "Class I";
    private final String COLUMN_NAME_TYPE = "Type";
    
    private final String classColumnsSQL = "["+COLUMN_NAME_CLASS_1+"], "
                                        +"["+COLUMN_NAME_TYPE+"]";
    
    @Autowired
    JdbcTemplate jdbcTemplate;    

    @Autowired
    DataSource dataSource;
    
    // TODO: re-write so that response includes the order they should appear in
    // TODO: can response include which parameters are 'required'?
    @GetMapping("/parameters")
    public List<String> getAllParameters() throws SQLException {
        
        TableMetaDataContext tableMetadataContext = new TableMetaDataContext();
        tableMetadataContext.setTableName(TABLE_NAME_PARTS);
        tableMetadataContext.processMetaData(dataSource, Collections.<String>emptyList(), new String[0]);
        
        return tableMetadataContext.getTableColumns();
        
//        ArrayList<MetaParameter> metaParameterList = new ArrayList();
//            
//        https://www.deadcoderising.com/2015-05-19-java-8-replace-traditional-for-loops-with-intstreams/
//        
//        jdbcTemplate.query("SELECT * FROM " + TABLE_NAME_METAPARTS + " WHERE [Part Number]='_dummy_entry'",
//                            (resultStream, rowNow) -> resultStream.getMetaData())
//                    .stream()
//                    .mapToInt(resultStreamMetaData -> {
//                        try {
//                            resultStreamMetaData.getColumnName(0);
//                  <additional logic needed here>
//                            return resultStreamMetaData.getColumnCount();
//                        } catch (SQLException sqlEx) {
//                           return 0;
//                       }
//                    })
//                 <additional logic needed here>
//                      ;
//        return metaParameterList;
    }
    
    
    // TODO: This isn't a very generic REST resource/path... needs optimization.
    @GetMapping("/parameters/class")
    public List getClassParameters() {
        
        return jdbcTemplate
            .query("SELECT " + classColumnsSQL + " "
                 + "FROM " + TABLE_NAME_METAPARTS + " "
                 + "WHERE ["+COLUMN_NAME_CLASS_1+"] IS NOT NULL",
                (rs, rowNum) -> 
                    new ClassParameter(rs.getString("Class I"), rs.getString("Type"))
            )
            .stream()
            .collect(Collectors.toList());
    }
    
    // TODO: This isn't a very generic REST resource/path... needs optimization.
    // TODO Hint: Start by making the response first.
    // TEST: curl localhost:8080/parameters/class/Class%20I/Passives
    @GetMapping("/parameters/class/{parameterName}/{parameterValue}")
    public List getSpecificClassParameter(@PathVariable String parameterName,
                                 @PathVariable String parameterValue) {
        
        return jdbcTemplate
            .query(
                    "SELECT " + classColumnsSQL + " "
                    + "FROM " + TABLE_NAME_METAPARTS + " "
                    + "WHERE [" + parameterName + "]= ?", new Object[] { parameterValue },
                    (rs, rowNow) ->
                        new ClassParameter(rs.getString("Class I"), rs.getString("Type"))
            )
            .stream()
            .collect(Collectors.toList());
    }
    
    // curl localhost:8080/parameters/Class%20I
    // curl localhost:8080/parameters/LifeCycle
    @GetMapping("/parameters/{parameterName}")
    public List getSpecificParameter(@PathVariable String parameterName) {
        
        return jdbcTemplate
            .query(
                "SELECT [" + parameterName + "] "
                + "FROM " + TABLE_NAME_METAPARTS + " ",
                //+ "WHERE [" + parameterName + "] IS NOT NULL", <<--- not needed due to .filter(...)
                (rs, rowNow) -> rs.getString(parameterName)
            )
            .stream()
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
    }
    
    
}
