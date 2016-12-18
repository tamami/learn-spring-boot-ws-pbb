package lab.aikibo.services;

import lab.aikibo.model.StatusRev;

/**
 * Created by tamami on 18/12/16.
 */
public interface ReversalServices {

    public StatusRev prosesReversal(String nop, String thn, String ntpd, String ipClient);

}
