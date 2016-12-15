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
    private String ntdp;
    private String mataAnggaranPokok;
    private BigInteger pokok;
    private String mataAnggaranSanksi;
    private BigInteger sanksi;
    private String namaWp;
    private String alamatOp;

}
