package lab.aikibo.config;

import lab.aikibo.constant.StatusRespond;
import lab.aikibo.model.PembayaranSppt;
import lab.aikibo.model.StatusInq;
import lab.aikibo.services.PembayaranServices;
import lab.aikibo.services.SpptServices;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tamami on 14/12/16.
 */
@Controller
public class RootController {

    static final Logger logger = Logger.getLogger(RootController.class);

    @Autowired
    SpptServices spptServices;

    @Autowired
    PembayaranServices pembayaranServices;

    @Autowired
    ReversalServices reversalServices;

    @RequestMapping(value="/sppt/{nop}/{thn}", method= RequestMethod.GET)
    public StatusInq getSppt(@PathVariable("nop") String nop, @PathVariable("thn") String thnPajak,
                             HttpServletRequest request) {
        logger.debug("NOP: " + nop + "; THN: " + thnPajak);
        String ipClient = request.getHeader("X-FORWARDED-FOR");
        if(ipClient == null) {
            ipClient = request.getRemoteAddr();
        }
        StatusInq status = null;

        try {
            Integer.parseInt(thnPajak);
        } catch(NumberFormatException ex) {
            status = new StatusInq(StatusRespond.THN_PAJAK_BUKAN_ANGKA, "Tahun Pajak Mengandung Karakter Bukan Angka", null);
            return status;
        }

        try {
            status = spptServices.getSpptByNopThn(nop, thnPajak, ipClient);
        } catch(Exception e) {
            logger.error(e);
        }
        return status;
    }

}
