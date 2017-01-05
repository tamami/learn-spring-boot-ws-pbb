package lab.aikibo.services;

import lab.aikibo.App;
import lab.aikibo.constant.StatusRespond;
import lab.aikibo.model.StatusInq;
import lab.aikibo.model.StatusRev;
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

    @Autowired
    private ReversalServices revServices;

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
        StatusTrx statusTrx = byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0),null);

        assertEquals(StatusRespond.APPROVED, statusTrx.getCode());
        assertEquals("Pembayaran Telah Tercatat", statusTrx.getMessage());
        assertEquals("332901000100100010", statusTrx.getByrSppt().getNop());
        assertEquals("4.1.1.11.02", statusTrx.getByrSppt().getMataAnggaranPokok());
        assertEquals(new BigInteger("35750"), statusTrx.getByrSppt().getPokok());
        assertEquals("4.1.1.11.02", statusTrx.getByrSppt().getMataAnggaranSanksi());
        assertEquals(new BigInteger("0"), statusTrx.getByrSppt().getSanksi());
        assertEquals("SUKARTA", statusTrx.getByrSppt().getNamaWp());
        assertEquals("GUNUNGJAYA - SALEM", statusTrx.getByrSppt().getAlamatOp());
    }

    @Test
    public void testRev() {
        StatusRev statusRev = revServices.prosesReversal("332901000100100010","2013",
                "2016AA74516SB20050812", null);

        assertEquals(StatusRespond.APPROVED, statusRev.getCode());
        assertEquals("Proses Reversal Berhasil", statusRev.getMessage());
        assertEquals("332901000100100010",statusRev.getRevPembayaran().getNop());
        assertEquals("2013", statusRev.getRevPembayaran().getThn());
        assertEquals("2016AA74516SB20050812", statusRev.getRevPembayaran().getNtpd());
    }

}
