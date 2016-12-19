package lab.aikibo.model;

import lombok.Data;

/**
 * Created by tamami on 14/12/16.
 */
@Data
public class StatusInq extends Status {

    private Sppt sppt;

    public StatusInq() {};

    public StatusInq(int code, String message, Sppt sppt) {
        super(code, message);
        this.sppt = sppt;
    }

}
