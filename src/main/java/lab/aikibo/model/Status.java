package lab.aikibo.model;

import lombok.Data;

/**
 * Created by tamami on 14/12/16.
 */
@Data
public class Status {

    private int code;
    private String message;

    public Status() {}

    public Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
