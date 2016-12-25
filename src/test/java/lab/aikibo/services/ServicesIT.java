package lab.aikibo.services;

import lab.aikibo.App;
import lab.aikibo.constant.StatusRespond;
import lab.aikibo.model.StatusInq;
import lab.aikibo.model.StatusTrx;
import org.joda.time.DateTime;
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

    @Autowired
    private PembayaranServices byrServices;

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
        StatusTrx statusTrx = byrServices.prosesPembayaran("3329010001001000010","2013",
                new DateTime(2016,12,20,10,0),null);

        assertEquals(StatusRespond.APPROVED, statusTrx.getCode());
        assertEquals("Pembayaran Telah Tercatat", statusTrx.getMessage());
        assertEquals("332901000100100010", statusTrx.getByrSppt().getNop());
        assertEquals("4.1.1.11.01", statusTrx.getByrSppt().getMataAnggaranPokok());
        assertEquals(new BigInteger("35750"), statusTrx.getByrSppt().getPokok());
        assertEquals("4.1.1.11.02", statusTrx.getByrSppt().getMataAnggaranSanksi());
        assertEquals(new BigInteger("0"), statusTrx.getByrSppt().getSanksi());
        assertEquals("SUKARTA", statusTrx.getByrSppt().getNamaWp());
        assertEquals("GUNUNGJAYA - SALEM", statusTrx.getByrSppt().getAlamatOp());
    }

}
