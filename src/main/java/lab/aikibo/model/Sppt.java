package lab.aikibo.model;

import lab.aikibo.constant.SerialConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by tamami on 14/12/16.
 */
@Data
public class Sppt implements Serializable {

    private static final long serialVersionUID = SerialConstant.SERIAL_SPPT;

    private String nop;
    private String thn;

}
