package lab.aikibo.model;

import lab.aikibo.constant.SerialConstant;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by tamami on 14/12/16.
 */
@Data
public class PembayaranSppt implements Serializable {

    private static final long serialVersionUID = SerialConstant.SERIAL_PEMBAYARAN_SPPT;

    private String nop;
    private String thn;
    private String ntpd;
    private String mataAnggaranPokok;
    private BigInteger pokok;
    private String mataAnggaranSanksi;
    private BigInteger sanksi;
    private String namaWp;
    private String alamatOp;

    public PembayaranSppt() {}

    public PembayaranSppt(String nop, String thn, String ntpd, String mataAnggaranPokok, BigInteger pokok,
                          String mataAnggaranSanksi, BigInteger sanksi, String namaWp, String alamatOp) {
        this.nop = nop;
        this.thn = thn;
        this.ntpd = ntpd;
        this.mataAnggaranPokok = mataAnggaranPokok;
        this.pokok = pokok;
        this.mataAnggaranSanksi = mataAnggaranSanksi;
        this.sanksi = sanksi;
        this.namaWp = namaWp;
        this.alamatOp = alamatOp;
    }

}
