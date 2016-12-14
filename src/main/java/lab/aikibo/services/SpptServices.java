package lab.aikibo.services;

import lab.aikibo.model.StatusInq;

/**
 * Created by tamami on 14/12/16.
 */
public interface SpptServices {

    public StatusInq getSpptByNopThn(String nop, String thn, String ipClient);

}
