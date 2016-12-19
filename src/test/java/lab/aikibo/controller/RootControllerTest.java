package lab.aikibo.controller;

import lab.aikibo.constant.StatusRespond;
import lab.aikibo.model.PembayaranSppt;
import lab.aikibo.model.Sppt;
import lab.aikibo.model.StatusInq;
import lab.aikibo.model.StatusTrx;
import lab.aikibo.services.PembayaranServices;
import lab.aikibo.services.SpptServices;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Created by tamami on 19/12/16.
 */
@RunWith(SpringRunner.class)
public class RootControllerTest {

    @InjectMocks
    private RootController rootController = new RootController();

    HttpServletRequest mock = Mockito.mock(HttpServletRequest.class);

    @Mock
    private SpptServices spptServices;
    @Mock
    private PembayaranServices byrServices;

    private StatusInq status;
    private StatusInq statusInqGagalDataTidakAda;
    private StatusInq statusInqError;
    private Sppt sppt;
    private StatusTrx statusTrxBerhasil;
    private StatusTrx statusTrxNihil;
    private StatusTrx statusTrxTerbayar;
    private StatusTrx statusTrxBatal;
    private StatusTrx statusTrxError;
    private PembayaranSppt byrSppt;

    @Before
    public void init() {
        sppt = new Sppt("332901000100100010", "2013", "FULAN", "BREBES",
                new BigInteger("10000"), new BigInteger("0"));
        status = new StatusInq(1, "Data Ditemukan", sppt);
        statusInqGagalDataTidakAda = new StatusInq(StatusRespond.DATA_INQ_NIHIL, "Data Tidak Ditemukan", null);
        statusInqError = new StatusInq(StatusRespond.DATABASE_ERROR, "Kesalahan DB", null);

        byrSppt = new PembayaranSppt("332901000100100010","2013","KODE_NTPD","4.1.1.12.1",
                new BigInteger("10000"), "4.1.1.12.2", new BigInteger("0"), "FULAN",
                "BREBES");
        statusTrxBerhasil = new StatusTrx(StatusRespond.APPROVED, "Pembayaran Telah Tercatat", byrSppt);
        statusTrxNihil = new StatusTrx(StatusRespond.TAGIHAN_TELAH_TERBAYAR,
                "Tagihan Telah Terbayar atau Pokok Pajak Nihil", null);
        statusTrxTerbayar = new StatusTrx(StatusRespond.TAGIHAN_TELAH_TERBAYAR,
                "Tagihan Telah Terbayar", null);
        statusTrxBatal = new StatusTrx(StatusRespond.JUMLAH_SETORAN_NIHIL,
                "Tagihan SPPT Telah Dibatalkan", null);
        statusTrxError = new StatusTrx(StatusRespond.DATABASE_ERROR,
                "Kesalahan Server", null);
    }

    @Test
    public void testInquirySpptBerhasil() {
        when(spptServices.getSpptByNopThn("332901000100100010","2013",null))
                .thenReturn(status);

        assertEquals(1, rootController.getSppt("332901000100100010","2013", mock).getCode());
        assertEquals("Data Ditemukan",
                rootController.getSppt("332901000100100010", "2013", mock).getMessage());
        assertEquals("332901000100100010",
                rootController.getSppt("332901000100100010","2013", mock).getSppt().getNop());
        assertEquals("2013",
                rootController.getSppt("332901000100100010","2013", mock).getSppt().getThn());
    }

    @Test
    public void testInquirySppt() {
        when(spptServices.getSpptByNopThn("332901000100100010","2013","192.168.1.1"))
           .thenReturn(status);

        assertEquals(1, spptServices.getSpptByNopThn("332901000100100010","2013","192.168.1.1").getCode());
        assertEquals("Data Ditemukan",
                spptServices.getSpptByNopThn("332901000100100010","2013","192.168.1.1").getMessage());
        assertEquals("332901000100100010",
                spptServices.getSpptByNopThn("332901000100100010","2013", "192.168.1.1").getSppt().getNop());
        assertEquals("2013",
                spptServices.getSpptByNopThn("332901000100100010","2013","192.168.1.1").getSppt().getThn());
        assertEquals("FULAN",
                spptServices.getSpptByNopThn("332901000100100010", "2013","192.168.1.1").getSppt().getNama());
        assertEquals("BREBES",
                spptServices.getSpptByNopThn("332901000100100010","2013","192.168.1.1").getSppt().getAlamatOp());
        assertEquals(new BigInteger("10000"),
                spptServices.getSpptByNopThn("332901000100100010","2013","192.168.1.1").getSppt().getPokok());
        assertEquals(new BigInteger("0"),
                spptServices.getSpptByNopThn("332901000100100010","2013","192.168.1.1").getSppt().getDenda());
    }

    @Test
    public void testInquirySpptGagalKarnaNihil() {
        when(spptServices.getSpptByNopThn("332901000100100020","2013","192.168.1.1"))
                .thenReturn(statusInqGagalDataTidakAda);

        assertEquals(10,
                spptServices.getSpptByNopThn("332901000100100020","2013", "192.168.1.1").getCode());
        assertEquals("Data Tidak Ditemukan",
                spptServices.getSpptByNopThn("332901000100100020","2013","192.168.1.1").getMessage());
        assertNull(spptServices.getSpptByNopThn("332901000100100020","2013","192.168.1.1").getSppt());
    }

    @Test
    public void testInquiryDbError() {
        when(spptServices.getSpptByNopThn("332901000100100030","2013","192.168.1.1"))
                .thenReturn(statusInqError);

        assertEquals(4,
                spptServices.getSpptByNopThn("332901000100100030","2013","192.168.1.1").getCode());
        assertEquals("Kesalahan DB",
                spptServices.getSpptByNopThn("332901000100100030","2013","192.168.1.1").getMessage());
        assertNull(spptServices.getSpptByNopThn("332901000100100030","2013","192.168.1.1").getSppt());
    }

    @Test
    public void testPembayaranSpptSukses() {
        when(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016, 12, 19, 10, 0),"192.168.1.1"))
                .thenReturn(statusTrxBerhasil);


    }

}
