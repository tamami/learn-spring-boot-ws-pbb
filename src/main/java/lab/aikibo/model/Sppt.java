package lab.aikibo.model;

import lab.aikibo.constant.SerialConstant;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by tamami on 14/12/16.
 */
@Data
public class Sppt implements Serializable {

    private static final long serialVersionUID = SerialConstant.SERIAL_SPPT;

    private String nop;
    private String thn;
    private String nama;
    private String alamatOp;
    private BigInteger pokok;
    private BigInteger denda;

    public Sppt() {}

    public Sppt(String nop, String thn, String nama, String alamatOp, BigInteger pokok, BigInteger denda) {
        this.nop = nop;
        this.thn = thn;
        this.nama = nama;
        this.alamatOp = alamatOp;
        this.pokok = pokok;
        this.denda = denda;
    }

}
