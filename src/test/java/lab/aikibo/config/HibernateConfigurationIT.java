package lab.aikibo.config;

import com.jolbox.bonecp.BoneCPDataSource;
import lab.aikibo.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tamami on 19/12/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class)
public class HibernateConfigurationIT {

    @Autowired
    private BoneCPDataSource boneDS;

    /**
     * check driver
     */
    @Test
    public void testDriver() {
        assertEquals("oracle.jdbc.driver.OracleDriver", boneDS.getDriverClass());
    }

    /**
     * check jdbc url
     */
    @Test
    public void testJdbcDriver() {
        assertEquals("jdbc:oracle:thin:pbbdummy/pbbdummy@192.168.2.7:1521/sismiopbck", boneDS.getJdbcUrl());
    }

    /**
     * check db username
     */
    @Test
    public void testUsername() {
        assertEquals("pbbdummy", boneDS.getUsername());
    }

    /**
     * check db password
     */
    @Test
    public void testPassword() {
        assertEquals("pbbdummy", boneDS.getPassword());
    }

    /**
     * check db connection
     */
    @Test
    public void testDbConnection() {
        assertNotNull(boneDS);
    }

}
