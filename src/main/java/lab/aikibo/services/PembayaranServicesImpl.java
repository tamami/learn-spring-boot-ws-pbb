package lab.aikibo.services;

import lab.aikibo.dao.StoreProceduresDao;
import lab.aikibo.model.StatusTrx;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD

/**
 * Created by tamami on 16/12/16.
 */
@Service("pembayaranServices")
=======
/**
 * Created by tamami on 18/12/16.
 */
@Service("reversalServices")
>>>>>>> ca409175d62904c931f3002be02d014d8716a3cf
@Transactional
public class PembayaranServicesImpl implements PembayaranServices {

    @Autowired
    private StoreProceduresDao spDao;

    @Override
    public StatusTrx prosesPembayaran(String nop, String thn, DateTime tglBayar, String ipClient) {
        return spDao.prosesPembayaran(nop, thn, tglBayar.toDate(), ipClient);
    }
<<<<<<< HEAD

=======
>>>>>>> ca409175d62904c931f3002be02d014d8716a3cf
}
