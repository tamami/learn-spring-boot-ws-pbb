package lab.aikibo.model;

import lombok.Data;

/**
 * Created by tamami on 14/12/16.
 */
@Data
public class StatusRev extends Status {

    private ReversalPembayaran revPembayaran;

    public StatusRev() {}

    public StatusRev(int code, String message, ReversalPembayaran revPembayaran) {
        super(code, message);
        this.revPembayaran = revPembayaran;
    }

}
