package lab.aikibo.model;

import lombok.Data;

/**
 * Created by tamami on 14/12/16.
 */
@Data
public class StatusTrx extends Status {

    private PembayaranSppt byrSppt;

    public StatusTrx() {}

    public StatusTrx(int code, String message, PembayaranSppt byrSppt) {
        super(code, message);
        this.byrSppt = byrSppt;
    }

}
