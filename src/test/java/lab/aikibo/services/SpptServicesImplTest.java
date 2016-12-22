package lab.aikibo.services;

import lab.aikibo.constant.StatusRespond;
import lab.aikibo.dao.StoreProceduresDaoImpl;
import lab.aikibo.model.Sppt;
import lab.aikibo.model.StatusInq;
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
 * Created by tamami on 22/12/16.
 */
@RunWith(SpringRunner.class)
public class SpptServicesImplTest {

    @InjectMocks
    private SpptServicesImpl spptServices = new SpptServicesImpl();

    @Mock
    private StoreProceduresDaoImpl spDao;

    private StatusInq statusSukses;
    private StatusInq statusInqGagalDataTidakAda;
    private StatusInq statusInqError;
    private Sppt sppt;

    @Before
    public void init() {
        sppt = new Sppt("332901000100100010", "2013", "FULAN", "BREBES",
                new BigInteger("10000"), new BigInteger("0"));
        statusSukses = new StatusInq(1, "Data Ditemukan", sppt);
        statusInqGagalDataTidakAda = new StatusInq(StatusRespond.DATA_INQ_NIHIL, "Data Tidak Ditemukan", null);
        statusInqError = new StatusInq(StatusRespond.DATABASE_ERROR, "Kesalahan DB", null);
    }

    @Test
    public void testInqSukses() {
        when(spDao.getDataSppt("332901000100100010","2013",null)).thenReturn(statusSukses);

        assertEquals(StatusRespond.APPROVED,
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getCode());
        assertEquals("Data Ditemukan",
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getMessage());
        assertEquals("332901000100100010",
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getSppt().getNop());
        assertEquals("2013",
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getSppt().getThn());
        assertEquals("FULAN",
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getSppt().getNama());
        assertEquals("BREBES",
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getSppt().getAlamatOp());
        assertEquals(new BigInteger("10000"),
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getSppt().getPokok());
        assertEquals(new BigInteger("0"),
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getSppt().getDenda());
    }

    /**
     * @TODO: unit testing untuk skenario inquiry nihil
     */
    @Test
    public void testInqNihil() {
        when(spDao.getDataSppt("332901000100100010","2013",null)).thenReturn(statusInqGagalDataTidakAda);

        assertEquals(StatusRespond.DATA_INQ_NIHIL,
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getCode());
        assertEquals("Data Tidak Ditemukan",
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getMessage());
        assertNull(spptServices.getSpptByNopThn("332901000100100010","2013",null).getSppt());
    }

    /**
     * @TODO: unit testing untuk skenario inquiry error server
     */
    @Test
    public void testInqError() {
        when(spDao.getDataSppt("332901000100100010","2013",null)).thenReturn(statusInqError);

        assertEquals(StatusRespond.DATABASE_ERROR,
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getCode());
        assertEquals("Kesalahan DB",
                spptServices.getSpptByNopThn("332901000100100010","2013",null).getMessage());
        assertNull(spptServices.getSpptByNopThn("332901000100100010","2013",null).getSppt());
    }

}
