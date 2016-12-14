package lab.aikibo.model;

import lab.aikibo.constant.SerialConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by tamami on 14/12/16.
 */
@Data
public class ReversalPembayaran implements Serializable {

    private static final long serialVersionUID = SerialConstant.SERIAL_REVERSAL_PEMBAYARAN;

    private String nop;
    private String thn;
    private String ntpd;

    public ReversalPembayaran() {}

    public ReversalPembayaran(String nop, String thn, String ntpd) {
        this.nop = nop;
        this.thn = thn;
        this.ntpd = ntpd;
    }

}
