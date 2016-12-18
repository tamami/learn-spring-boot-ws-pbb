package lab.aikibo.controller;

import lab.aikibo.constant.StatusRespond;
import lab.aikibo.model.StatusInq;
import lab.aikibo.model.StatusRev;
import lab.aikibo.model.StatusTrx;
import lab.aikibo.services.PembayaranServices;
import lab.aikibo.services.ReversalServices;
import lab.aikibo.services.SpptServices;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

/**
 * Created by tamami on 14/12/16.
 */
@RestController
public class RootController {

    static final Logger logger = Logger.getLogger(RootController.class);

    @Autowired
    SpptServices spptServices;

    @Autowired
    PembayaranServices pembayaranServices;

    @Autowired
    ReversalServices reversalServices;

    @Getter
    static final Logger logger = Logger.getLogger(RootController.class);

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
        logger.debug(" >>> GET DATA SUCCESS >>> " + status);
        return status;
    }

    @RequestMapping(value="/bayar/{nop}/{thn}/{tglBayar}/{jamBayar}", method = RequestMethod.GET)
    public StatusTrx prosesPembayaran(@PathVariable("nop") String nop, @PathVariable("thn") String thnPajak,
                                      @PathVariable("tglBayar") String tglBayarString,
                                      @PathVariable("jamBayar") String jamBayarString, HttpServletRequest request) {
        StatusTrx status = null;
        BigInteger pokok = null;
        BigInteger denda = null;
        String ipClient = request.getHeader("X-FORWARDED-FOR");
        if(ipClient == null) {
            ipClient = request.getRemoteAddr();
        }

        // cek tanggal bayar, tidak boleh lebih dari
        int date = Integer.parseInt(tglBayarString.substring(0,2));
        int month = Integer.parseInt(tglBayarString.substring(2,4));
        int year = Integer.parseInt(tglBayarString.substring(4,8));
        int hour = Integer.parseInt(jamBayarString.substring(0,2));
        int min = Integer.parseInt(jamBayarString.substring(2,4));

        DateTime tglBayar = new DateTime(year, month, date, hour, min);
        if(tglBayar.isAfterNow()) {
            status = new StatusTrx(StatusRespond.TGL_JAM_BAYAR_LD_TGL_JAM_KIRIM,
                    "Tanggal atau jam pada saat dibayarkan melebihi tanggal dan jam saat ini", null);
            return status;
        }

        // proses pembayaran
        try {
            status = pembayaranServices.prosesPembayaran(nop, thnPajak, tglBayar, ipClient);
        } catch(Exception e) {
            logger.error(e);
            logger.debug(" >>> PROSES PENCATATAN PEMBAYARAN GAGAL >>> " + status);
        }
        logger.debug(" >>> PENCATATAN PEMBAYARAN BERHASIL >>> " + status);
        return status;
    }

    @RequestMapping(value="/reversal/{nop}/{thn}/{ntpd}", method = RequestMethod.GET)
    public StatusRev prosesReversal(@PathVariable("nop") String nop, @PathVariable("thn") String thn,
                                    @PathVariable("ntpd") String ntpd, HttpServletRequest request) {
        StatusRev status = null;

        // get ip client
        String ipClient = request.getHeader("X-FORWARDED-FOR");
        if(ipClient == null) {
            ipClient = request.getRemoteAddr();
        }
        logger.debug(" >>> IP CLIENT : " + ipClient + " > MEMBUKA KONEKSI");

        try {
            status = reversalServices.prosesReversal(nop, thn, ntpd, ipClient);
        } catch(Exception e) {
            logger.error(e);
            logger.debug(" >>> REVERSAL ERROR >>> " + status);
        }
        logger.debug(" >>> REVERSAL SUKSES >>> " + status);
        return status;
    }

}
