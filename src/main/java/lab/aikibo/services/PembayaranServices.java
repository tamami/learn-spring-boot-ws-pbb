package lab.aikibo.services;

import lab.aikibo.model.StatusTrx;
import org.joda.time.DateTime;

/**
 * Created by tamami on 18/12/16.
 */
public interface PembayaranServices {
    public StatusTrx prosesPembayaran(String nop, String thn, DateTime tglBayar, String ipClient);
}
