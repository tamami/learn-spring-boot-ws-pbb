package lab.aikibo.services;

import lab.aikibo.dao.StoreProceduresDao;
import lab.aikibo.model.StatusTrx;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by tamami on 16/12/16.
 */
@Service("pembayaranServices")
@Transactional
public class PembayaranServicesImpl implements PembayaranServices {

    @Autowired
    private StoreProceduresDao spDao;

    @Override
    public StatusTrx prosesPembayaran(String nop, String thn, DateTime tglBayar, String ipClient) {
        return spDao.prosesPembayaran(nop, thn, tglBayar.toDate(), ipClient);
    }

}
