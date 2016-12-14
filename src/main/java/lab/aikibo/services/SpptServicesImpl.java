package lab.aikibo.services;

import lab.aikibo.dao.StoreProceduresDao;
import lab.aikibo.model.StatusInq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by tamami on 14/12/16.
 */
@Service("spptServices")
@Transactional
public class SpptServicesImpl implements SpptServices {

    @Autowired
    private StoreProceduresDao spDao;

    @Override
    public StatusInq getSpptByNopThn(String nop, String thn, String ipClient) {
        return spDao.getDataSppt(nop, thn, ipClient);
    }

}
