package comp.project.backend.jpa.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 
 */
@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource createDataSource() throws Exception {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        
        
        ds.setJdbcUrl("jdbc:ucanaccess://c:/<CHANGE ME>/test.accdb;showSchema=true");
        ds.setDriverClass("net.ucanaccess.jdbc.UcanaccessDriver");
        
        return ds;
        
    }

}
