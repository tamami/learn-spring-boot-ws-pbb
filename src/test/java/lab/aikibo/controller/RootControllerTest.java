package lab.aikibo.controller;

import lab.aikibo.constant.StatusRespond;
import lab.aikibo.model.*;
import lab.aikibo.services.PembayaranServices;
import lab.aikibo.services.ReversalServices;
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
    @Mock
    private ReversalServices revServices;

    private StatusInq status;
    private StatusInq statusInqGagalDataTidakAda;
    private StatusInq statusInqError;
    private Sppt sppt;

    private StatusTrx statusTrxBerhasil;
    private StatusTrx statusTrxNihil;
    private StatusTrx statusTrxTerbayar;
    private StatusTrx statusTrxBatal;
    private StatusTrx statusTrxError;
    private StatusTrx statusTrxhnPajakBukanAngka;
    private StatusTrx statusTrxWaktuBayarLdWaktuCatat;
    private PembayaranSppt byrSppt;

    private StatusRev statusRevBerhasil;
    private StatusRev statusRevNihil;
    private StatusRev statusRevGanda;
    private StatusRev statusRevError;
    private ReversalPembayaran revSppt;

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

        revSppt = new ReversalPembayaran("332901000100100010","2013","KODE_NTPD");
        statusRevBerhasil = new StatusRev(StatusRespond.APPROVED, "Proses Reversal Berhasil", revSppt);
        statusRevNihil = new StatusRev(StatusRespond.DATA_INQ_NIHIL, "Data Yang Diminta Tidak Ada", null);
        statusRevGanda = new StatusRev(StatusRespond.DATABASE_ERROR, "Data Tersebut Ganda", null);
        statusRevError = new StatusRev(StatusRespond.DATABASE_ERROR, "Kesalahan Server", null);
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
        assertEquals("FULAN",
                rootController.getSppt("332901000100100010","2013", mock).getSppt().getNama());
        assertEquals("BREBES",
                rootController.getSppt("332901000100100010","2013", mock).getSppt().getAlamatOp());
        assertEquals(new BigInteger("10000"),
                rootController.getSppt("332901000100100010", "2013", mock).getSppt().getPokok());
        assertEquals(new BigInteger("0"),
                rootController.getSppt("332901000100100010", "2013", mock).getSppt().getDenda());
    }

    @Test
    public void testInquirySpptGagalKarnaNihil() {
        when(spptServices.getSpptByNopThn("332901000100100020","2013",null))
                .thenReturn(statusInqGagalDataTidakAda);

        assertEquals(10,
                rootController.getSppt("332901000100100020","2013", mock).getCode());
        assertEquals("Data Tidak Ditemukan",
                rootController.getSppt("332901000100100020","2013", mock).getMessage());
        assertNull(rootController.getSppt("332901000100100020","2013", mock).getSppt());
    }

    @Test
    public void testInquiryDbError() {
        when(spptServices.getSpptByNopThn("332901000100100030","2013",null))
                .thenReturn(statusInqError);

        assertEquals(4,
                rootController.getSppt("332901000100100030","2013", mock).getCode());
        assertEquals("Kesalahan DB",
                rootController.getSppt("332901000100100030","2013", mock).getMessage());
        assertNull(rootController.getSppt("332901000100100030","2013", mock).getSppt());
    }

    @Test
    public void testInquiryThnPajakBukanAngka() {
        assertEquals(36,
                rootController.getSppt("332901000100100010","2a13", mock).getCode());
        assertEquals("Tahun Pajak Mengandung Karakter Bukan Angka",
                rootController.getSppt("332901000100100010","2a13", mock).getMessage());
        assertNull(rootController.getSppt("332901000100100010","2a13", mock).getSppt());
    }

    @Test
    public void testTrxWaktuBayarLdWaktuCatat() {
        assertEquals(StatusRespond.TGL_JAM_BAYAR_LD_TGL_JAM_KIRIM,
                rootController.prosesPembayaran("332901000100100010","2013","22122017",
                        "1000", mock).getCode());
        assertEquals("Tanggal atau jam pada saat dibayarkan melebihi tanggal dan jam saat ini",
                rootController.prosesPembayaran("332901000100100010","2013","22122017",
                        "1000", mock).getMessage());
        assertNull(rootController.prosesPembayaran("332901000100100010","2013","22122017",
                "1000", mock).getByrSppt());
    }

    @Test
    public void testTrxSpptSukses() {
        when(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016, 12, 19, 10, 0),null))
                .thenReturn(statusTrxBerhasil);

        assertEquals(1,
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getCode());
        assertEquals("Pembayaran Telah Tercatat",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getMessage());
        assertEquals("332901000100100010",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getByrSppt().getNop());
        assertEquals("2013",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getByrSppt().getThn());
        assertEquals("KODE_NTPD",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getByrSppt().getNtpd());
        assertEquals("4.1.1.12.1",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getByrSppt().getMataAnggaranPokok());
        assertEquals(new BigInteger("10000"),
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getByrSppt().getPokok());
        assertEquals("4.1.1.12.2",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getByrSppt().getMataAnggaranSanksi());
        assertEquals(new BigInteger("0"),
                rootController.prosesPembayaran("332901000100100010", "2013","19122016",
                        "1000", mock).getByrSppt().getSanksi());
        assertEquals("FULAN",
                rootController.prosesPembayaran("332901000100100010", "2013", "19122016",
                        "1000", mock).getByrSppt().getNamaWp());
        assertEquals("BREBES",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getByrSppt().getAlamatOp());
    }



    @Test
    public void testTrxNihil() {
        when(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016, 12, 19, 10, 0),null))
                .thenReturn(statusTrxNihil);

        assertEquals(StatusRespond.TAGIHAN_TELAH_TERBAYAR,
                rootController.prosesPembayaran("332901000100100010","2013",
                        "19122016","1000", mock).getCode());
        assertEquals("Tagihan Telah Terbayar atau Pokok Pajak Nihil",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getMessage());
        assertNull(rootController.prosesPembayaran("332901000100100010","2013","19122016",
                "1000", mock).getByrSppt());
    }

    @Test
    public void testTrxTerbayar() {
        when(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0), null)).thenReturn(statusTrxTerbayar);

        assertEquals(StatusRespond.TAGIHAN_TELAH_TERBAYAR,
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getCode());
        assertEquals("Tagihan Telah Terbayar",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getMessage());
        assertNull(rootController.prosesPembayaran("332901000100100010","2013","19122016",
                "1000", mock).getByrSppt());
    }

    @Test
    public void testTrxBatal() {
        when(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0), null)).thenReturn(statusTrxBatal);

        assertEquals(StatusRespond.JUMLAH_SETORAN_NIHIL,
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getCode());
        assertEquals("Tagihan SPPT Telah Dibatalkan",
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getMessage());
        assertNull(rootController.prosesPembayaran("332901000100100010","2013","19122016",
                "1000", mock).getByrSppt());
    }

    @Test
    public void testTrxError() {
        when(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0), null)).thenReturn(statusTrxError);

        assertEquals(StatusRespond.DATABASE_ERROR,
                rootController.prosesPembayaran("332901000100100010","2013","19122016",
                        "1000", mock).getCode());
        assertEquals("Kesalahan Server",
                rootController.prosesPembayaran("332901000100100010", "2013","19122016",
                        "1000", mock).getMessage());
        assertNull(rootController.prosesPembayaran("332901000100100010","2013","19122016",
                "1000", mock).getByrSppt());
    }

    @Test
    public void testRevSukses() {
        when(revServices.prosesReversal("332901000100100010","2013","KODE_NTPD", null))
                .thenReturn(statusRevBerhasil);

        assertEquals(StatusRespond.APPROVED,
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).getCode());
        assertEquals("Proses Reversal Berhasil",
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).getMessage());
        assertEquals("332901000100100010",
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock)
                        .getRevPembayaran().getNop());
        assertEquals("2013",
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock)
                        .getRevPembayaran().getThn());
        assertEquals("KODE_NTPD",
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock)
                        .getRevPembayaran().getNtpd());
    }

    @Test
    public void testRevNihil() {
        when(revServices.prosesReversal("332901000100100010","2013","KODE_NTPD", null))
                .thenReturn(statusRevNihil);

        assertEquals(StatusRespond.DATA_INQ_NIHIL,
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).getCode());
        assertEquals("Data Yang Diminta Tidak Ada",
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).getMessage());
        assertNull(rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).
                getRevPembayaran());
    }

    @Test
    public void testRevGanda() {
        when(revServices.prosesReversal("332901000100100010","2013","KODE_NTPD", null))
                .thenReturn(statusRevGanda);

        assertEquals(StatusRespond.DATABASE_ERROR,
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).getCode());
        assertEquals("Data Tersebut Ganda",
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).getMessage());
        assertNull(rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock)
                .getRevPembayaran());
    }

    @Test
    public void testRevError() {
        when(revServices.prosesReversal("332901000100100010","2013","KODE_NTPD", null))
                .thenReturn(statusRevError);

        assertEquals(StatusRespond.DATABASE_ERROR,
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).getCode());
        assertEquals("Kesalahan Server",
                rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock).getMessage());
        assertNull(rootController.prosesReversal("332901000100100010","2013","KODE_NTPD", mock)
                .getRevPembayaran());
    }

}
