package lab.aikibo.services;

import lab.aikibo.constant.StatusRespond;
import lab.aikibo.dao.StoreProceduresDaoImpl;
import lab.aikibo.model.PembayaranSppt;
import lab.aikibo.model.StatusTrx;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Created by tamami on 21/12/16.
 */
@RunWith(SpringRunner.class)
public class PembayaranServicesImplTest {

    @InjectMocks
    private PembayaranServicesImpl byrServices = new PembayaranServicesImpl();

    @Mock
    private StoreProceduresDaoImpl spDao;

    private StatusTrx statusTrxBerhasil;
    private StatusTrx statusTrxNihil;
    private StatusTrx statusTrxTerbayar;
    private StatusTrx statusTrxBatal;
    private StatusTrx statusTrxError;
    private StatusTrx statusTrxhnPajakBukanAngka;
    private StatusTrx statusTrxWaktuBayarLdWaktuCatat;
    private PembayaranSppt byrSppt;

    @Before
    public void init() {
        byrSppt = new PembayaranSppt("332901000100100010","2013","KODE_NTPD","4.1.1.12.1",
                new BigInteger("10000"), "4.1.1.12.2", new BigInteger("0"), "FULAN",
                "BREBES");
        statusTrxBerhasil = new StatusTrx(StatusRespond.APPROVED, "Transaksi Telah Tercatat", byrSppt);
        statusTrxNihil = new StatusTrx(StatusRespond.JUMLAH_SETORAN_NIHIL, "Data Yang Diminta Tidak Ada",
                null);
        statusTrxTerbayar = new StatusTrx(StatusRespond.TAGIHAN_TELAH_TERBAYAR, "Tagihan Telah Terbayar", null);
        statusTrxBatal = new StatusTrx(StatusRespond.JUMLAH_SETORAN_NIHIL, "Tagihan Telah Dibatalkan", null);
        statusTrxError = new StatusTrx(StatusRespond.DATABASE_ERROR, "Kesalahan Server", null);
    }

    @Test
    public void testTrxBerhasil() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0).toDate(), null)).thenReturn(statusTrxBerhasil);

        assertEquals(StatusRespond.APPROVED,
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getCode());
        assertEquals("Transaksi Telah Tercatat",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getMessage());
        assertEquals("332901000100100010",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getByrSppt().getNop());
        assertEquals("2013",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getByrSppt().getThn());
        assertEquals("KODE_NTPD",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getByrSppt().getNtpd());
        assertEquals("4.1.1.12.1",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0),null).getByrSppt().getMataAnggaranPokok());
        assertEquals(new BigInteger("10000"),
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0),null).getByrSppt().getPokok());
        assertEquals("4.1.1.12.2",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getByrSppt().getMataAnggaranSanksi());
        assertEquals(new BigInteger("0"),
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getByrSppt().getSanksi());
        assertEquals("FULAN",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0),null).getByrSppt().getNamaWp());
        assertEquals("BREBES",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0),null).getByrSppt().getAlamatOp());

    }

    @Test
    public void testTrxNihil() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0).toDate(), null)).thenReturn(statusTrxNihil);

        assertEquals(StatusRespond.JUMLAH_SETORAN_NIHIL,
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getCode());
        assertEquals("Data Yang Diminta Tidak Ada",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getMessage());
        assertNull(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0),null).getByrSppt());
    }

    @Test
    public void testTrxTerbayar() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0).toDate(), null)).thenReturn(statusTrxTerbayar);

        assertEquals(StatusRespond.TAGIHAN_TELAH_TERBAYAR,
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0),null).getCode());
        assertEquals("Tagihan Telah Terbayar",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getMessage());
        assertNull(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0), null).getByrSppt());
    }

    @Test
    public void testTrxBatal() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0).toDate(), null)).thenReturn(statusTrxBatal);

        assertEquals(StatusRespond.JUMLAH_SETORAN_NIHIL,
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getCode());
        assertEquals("Tagihan Telah Dibatalkan",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0),null).getMessage());
        assertNull(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0), null).getByrSppt());
    }

    @Test
    public void testTrxError() {
        when(spDao.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0).toDate(), null)).thenReturn(statusTrxError);

        assertEquals(StatusRespond.DATABASE_ERROR,
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getCode());
        assertEquals("Kesalahan Server",
                byrServices.prosesPembayaran("332901000100100010","2013",
                        new DateTime(2016,12,20,10,0), null).getMessage());
        assertNull(byrServices.prosesPembayaran("332901000100100010","2013",
                new DateTime(2016,12,20,10,0),null).getByrSppt());
    }

}
