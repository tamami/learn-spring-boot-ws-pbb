package lab.aikibo.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by tamami on 18/12/16.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan
@PropertySource(value = {"classpath:application.properties"})
public class HibernateConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public BoneCPDataSource boneCPDataSource() {
        BoneCPDataSource ds = new BoneCPDataSource();
        ds.setDriverClass(environment.getRequiredProperty("jdbc.driverClassName"));
        ds.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
        ds.setUsername(environment.getRequiredProperty("jdbc.username"));
        ds.setPassword(environment.getRequiredProperty("jdbc.password"));

        ds.setIdleConnectionTestPeriodInMinutes(60);
        ds.setIdleMaxAgeInMinutes(420);
        ds.setMaxConnectionsPerPartition(30);
        ds.setMaxConnectionsPerPartition(10);
        ds.setPartitionCount(10);
        ds.setAcquireIncrement(5);
        ds.setStatementsCacheSize(100);
        return ds;
    }

}
