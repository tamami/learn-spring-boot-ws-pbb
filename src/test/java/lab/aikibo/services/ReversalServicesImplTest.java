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

    /**
     * @TODO: buat unit test untuk skenario reversal yang berhasil
     */
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
    }

    /**
     * @TODO: buat unit test untuk skenario reversal gagal karna data nihil
     */
    public void testRevNihil() {}

    /**
     * @TODO: buat unit test untuk skenario reversal gagal karna data ganda
     */
    public void testRevGanda(){}

    /**
     * @TODO: buat unit test untuk skenario reversal gagal karena kesalahan DB
     */
    public void testRevError() {}

}
