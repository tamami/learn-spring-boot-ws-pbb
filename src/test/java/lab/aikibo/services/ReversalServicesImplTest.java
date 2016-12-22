package lab.aikibo.services;

import lab.aikibo.constant.StatusRespond;
import lab.aikibo.dao.StoreProceduresDaoImpl;
import lab.aikibo.model.ReversalPembayaran;
import lab.aikibo.model.StatusRev;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Created by tamami on 21/12/16.
 */
@RunWith(SpringRunner.class)
public class ReversalServicesImplTest {

    @InjectMocks
    private ReversalServicesImpl revServices = new ReversalServicesImpl();

    @Mock
    private StoreProceduresDaoImpl spDao;

    private StatusRev statusRevBerhasil;
    private StatusRev statusRevNihil;
    private StatusRev statusRevGanda;
    private StatusRev statusRevError;
    private ReversalPembayaran revSppt;

    @Before
    public void init() {
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
    public void testRevBerhasil() {
        when(spDao.reversalPembayaran("332901000100100010","2013","KODE_NTPD", null))
                .thenReturn(statusRevBerhasil);

        assertEquals(StatusRespond.APPROVED,
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null).getCode());
        assertEquals("Reversal Telah Berhasil Dilakukan",
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD", null).getMessage());
        assertEquals("332901000100100010",
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getRevPembayaran().getNop());
        assertEquals("2013",
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getRevPembayaran().getThn());
        assertEquals("KODE_NTPD",
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getRevPembayaran().getNtpd());
    }

    @Test
    public void testRevNihil() {
        when(spDao.reversalPembayaran("332901000100100010","2013","KODE_NTPD",null))
                .thenReturn(statusRevNihil);

        assertEquals(StatusRespond.DATA_INQ_NIHIL,
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getCode());
        assertEquals("Data Yang Diminta Tidak Ada",
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getMessage());
        assertNull(revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                .getRevPembayaran());
    }

    @Test
    public void testRevGanda(){
        when(spDao.reversalPembayaran("332901000100100010","2013","KODE_NTPD",null))
                .thenReturn(statusRevGanda);

        assertEquals(StatusRespond.DATABASE_ERROR,
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getCode());
        assertEquals("Data Transaksi Tercatat Ganda",
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getMessage());
        assertNull(revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getRevPembayaran());
    }

    /**
     * @TODO: buat unit test untuk skenario reversal gagal karena kesalahan DB
     */
    @Test
    public void testRevError() {
        when(spDao.reversalPembayaran("332901000100100010","2013","KODE_NTPD",null))
                .thenReturn(statusRevError);

        assertEquals(StatusRespond.DATABASE_ERROR,
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getCode());
        assertEquals("Kesalahan Server",
                revServices.prosesReversal("332901000100100010","2013","KODE_NTPD", null)
                        .getMessage());
        assertNull(revServices.prosesReversal("332901000100100010","2013","KODE_NTPD",null)
                        .getRevPembayaran());
    }

}
