package lab.aikibo.dao;

import lab.aikibo.constant.StatusRespond;
import lab.aikibo.model.*;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Created by tamami on 20/12/16.
 */
public class StoreProceduresDaoImplTest {

    @Mock
    private StoreProceduresDao spDao;

    private StatusInq statusInq;
    private StatusInq statusInqNihil;
    private StatusInq statusInqError;
    private Sppt sppt;

    private StatusTrx statusTrxBerhasil;
    private StatusTrx statusTrxNihil;
    private StatusTrx statusTrxTerbayar;
    private StatusTrx statusTrxBatal;
    private StatusTrx statusTrxError;
    private PembayaranSppt byrSppt;

    private StatusRev statusRevBerhasil;
    private StatusRev statusRevNihil;
    private StatusRev statusRevGanda;
    private StatusRev statusRevError;
    private ReversalPembayaran revSppt;


    @Before
    public void init() {
        spDao = Mockito.mock(StoreProceduresDaoImpl.class);

        sppt = new Sppt("332901000100100010", "2013", "FULAN", "BREBES",
                new BigInteger("10000"), new BigInteger("0"));
        statusInq = new StatusInq(StatusRespond.APPROVED, "Data Ditemukan", sppt);
        statusInqNihil = new StatusInq(StatusRespond.DATA_INQ_NIHIL, "Data Tidak Ditemukan", null);
        statusInqError = new StatusInq(StatusRespond.DATABASE_ERROR, "Kesalahan DB", null);

        byrSppt = new PembayaranSppt("332901000100100010","2013","KODE_NTPD","4.1.1.12.1",
                new BigInteger("10000"), "4.1.1.12.2", new BigInteger("0"), "FULAN",
                "BREBES");
        statusTrxBerhasil = new StatusTrx(StatusRespond.APPROVED, "Transaksi Telah Tercatat", byrSppt);
        statusTrxNihil = new StatusTrx(StatusRespond.JUMLAH_SETORAN_NIHIL, "Data Yang Diminta Tidak Ada",
                null);
        statusTrxTerbayar = new StatusTrx(StatusRespond.TAGIHAN_TELAH_TERBAYAR, "Tagihan Telah Terbayar", null);
        statusTrxBatal = new StatusTrx(StatusRespond.JUMLAH_SETORAN_NIHIL, "Tagihan Telah Dibatalkan", null);
        statusTrxError = new StatusTrx(StatusRespond.DATABASE_ERROR, "Kesalahan Server", null);

        revSppt = new ReversalPembayaran("332901000100100010","2013","KODE_NTPD");
        statusRevBerhasil = new StatusRev(StatusRespond.APPROVED, "Reversal Telah Berhasil Dilakukan", revSppt);
        statusRevNihil = new StatusRev(StatusRespond.DATA_INQ_NIHIL, "Data Yang Diminta Tidak Ada",
                null);
        statusRevGanda = new StatusRev(StatusRespond.DATABASE_ERROR, "Data Transaksi Tercatat Ganda",
                null);
        statusRevError = new StatusRev(StatusRespond.DATABASE_ERROR, "Kesalahan Server",
                null);
    }

    @Test
    public void testInqBerhasil() {
        when(spDao.getDataSppt("332901000100100010","2013", null))
                .thenReturn(statusInq);

        assertEquals(StatusRespond.APPROVED,
                spDao.getDataSppt("332901000100100010","2013",null).getCode());
        assertEquals("Data Ditemukan",
                spDao.getDataSppt("332901000100100010","2013", null).getMessage());
        assertEquals("332901000100100010",
                spDao.getDataSppt("332901000100100010","2013", null).getSppt().getNop());
        assertEquals("2013",
                spDao.getDataSppt("332901000100100010","2013", null).getSppt().getThn());
        assertEquals("FULAN",
                spDao.getDataSppt("332901000100100010","2013", null).getSppt().getNama());
        assertEquals("BREBES",
                spDao.getDataSppt("332901000100100010","2013", null).getSppt().getAlamatOp());
        assertEquals(new BigInteger("10000"),
                spDao.getDataSppt("332901000100100010","2013", null).getSppt().getPokok());
        assertEquals(new BigInteger("0"),
                spDao.getDataSppt("332901000100100010","2013", null).getSppt().getDenda());
    }

    @Test
    public void testInqNihil() {
        when(spDao.getDataSppt("332901000100100010","2013",null))
                .thenReturn(statusInqNihil);

        assertEquals(StatusRespond.DATA_INQ_NIHIL,
                spDao.getDataSppt("332901000100100010","2013",null).getCode());
        assertEquals("Data Tidak Ditemukan",
                spDao.getDataSppt("332901000100100010","2013", null).getMessage());
        assertNull(spDao.getDataSppt("332901000100100010","2013", null).getSppt());
    }

    @Test
    public void testInqError() {
        when(spDao.getDataSppt("332901000100100010", "2013", null))
                .thenReturn(statusInqError);

        assertEquals(StatusRespond.DATABASE_ERROR,
                spDao.getDataSppt("332901000100100010","2013", null).getCode());
        assertEquals("Kesalahan DB",
                spDao.getDataSppt("332901000100100010","2013", null).getMessage());
        assertNull(spDao.getDataSppt("332901000100100010","2013",null).getSppt());
    }

    @Test
    public void testTrxSukses() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0).toDate(), null))
                .thenReturn(statusTrxBerhasil);

        assertEquals(StatusRespond.APPROVED,
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getCode());
        assertEquals("Transaksi Telah Tercatat",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getMessage());
        assertEquals("332901000100100010",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt().getNop());
        assertEquals("2013",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt().getThn());
        assertEquals("KODE_NTPD",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt().getNtpd());
        assertEquals("4.1.1.12.1",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt()
                        .getMataAnggaranPokok());
        assertEquals(new BigInteger("10000"),
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt()
                        .getPokok());
        assertEquals("4.1.1.12.2",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt()
                        .getMataAnggaranSanksi());
        assertEquals(new BigInteger("0"),
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt().getSanksi());
        assertEquals("FULAN",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt().getNamaWp());
        assertEquals("BREBES",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt().getAlamatOp());
    }

    @Test
    public void testTrxNihil() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0).toDate(), null))
                .thenReturn(statusTrxNihil);

        assertEquals(StatusRespond.JUMLAH_SETORAN_NIHIL,
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getCode());
        assertEquals("Data Yang Diminta Tidak Ada",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getMessage());
        assertNull(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt());
    }

    @Test
    public void testTrxTerbayar() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0).toDate(), null))
                .thenReturn(statusTrxTerbayar);

        assertEquals(StatusRespond.TAGIHAN_TELAH_TERBAYAR,
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getCode());
        assertEquals("Tagihan Telah Terbayar",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getMessage());
        assertNull(spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt());
    }

    @Test
    public void testTrxBatal() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0).toDate(), null))
                .thenReturn(statusTrxBatal);

        assertEquals(StatusRespond.JUMLAH_SETORAN_NIHIL,
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,00).toDate(),null).getCode());
        assertEquals("Tagihan Telah Dibatalkan",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getMessage());
        assertNull(spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt());
    }

    @Test
    public void testTrxError() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0).toDate(), null))
                .thenReturn(statusTrxError);

        assertEquals(StatusRespond.DATABASE_ERROR,
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getCode());
        assertEquals("Kesalahan Server",
                spDao.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,19,10,0).toDate(), null).getMessage());
        assertNull(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,19,10,0).toDate(), null).getByrSppt());
    }

    @Test
    public void testRevSukses() {
        when(spDao.reversalPembayaran("332901000100100010","2013","KODE_NTPD", null))
                .thenReturn(statusRevBerhasil);

        assertEquals(StatusRespond.APPROVED,
                spDao.reversalPembayaran("332901000100100010","2013","KODE_NTPD", null).getCode());
        assertEquals("Reversal Telah Berhasil Dilakukan",
                spDao.reversalPembayaran("332901000100100010", "2013", "KODE_NTPD", null).getMessage());
        assertEquals("332901000100100010",
                spDao.reversalPembayaran("332901000100100010","2013", "KODE_NTPD", null)
            .getRevPembayaran().getNop());
        assertEquals("2013",
                spDao.reversalPembayaran("332901000100100010","2013","KODE_NTPD", null)
                        .getRevPembayaran().getThn());
        assertEquals("KODE_NTPD",
                spDao.reversalPembayaran("332901000100100010","2013", "KODE_NTPD", null)
                        .getRevPembayaran().getNtpd());

    }

    /**
     * @TODO: unit test StoreProceduresDaoImpl untuk skenario reversal gagal karna nihil
     */
    @Test
    public void testRevNihil() {}

    /**
     * @TODO: unit test StoreProceduresDaoImpl untuk skenario reversal gagal karna data ganda
     */
    @Test
    public void testRevDouble() {}

    /**
     * @TODO: unit test StoreProceduresDaoImpl untuk skenario reversal gagal karna server error
     */
    @Test
    public void testRevError() {}

}
