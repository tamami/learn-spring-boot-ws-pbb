package lab.aikibo.services;

import lab.aikibo.App;
import lab.aikibo.constant.StatusRespond;
import lab.aikibo.model.StatusInq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * Created by tamami on 22/12/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=App.class)
public class ServicesIT {

    @Autowired
    private SpptServices spptServices;

    @Test
    public void testInquiry() {
        StatusInq statusInq = spptServices.getSpptByNopThn("332901000100100010","2013","192.168.2.1");

        assertEquals(StatusRespond.APPROVED, statusInq.getCode());
        assertEquals("Data Ditemukan", statusInq.getMessage());
        assertEquals("332901000100100010", statusInq.getSppt().getNop());
        assertEquals("2013", statusInq.getSppt().getThn());
        assertEquals("SUKARTA", statusInq.getSppt().getNama());
        assertEquals("GUNUNGJAYA - SALEM", statusInq.getSppt().getAlamatOp());
        assertEquals(new BigInteger("35750"), statusInq.getSppt().getPokok());
        assertEquals(new BigInteger("0"), statusInq.getSppt().getDenda());
    }

    @Test
    public void testTrx() {

    }

}
