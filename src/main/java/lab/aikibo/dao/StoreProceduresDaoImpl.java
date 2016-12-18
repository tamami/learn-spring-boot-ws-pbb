package lab.aikibo.dao;

import com.jolbox.bonecp.BoneCPDataSource;
import lab.aikibo.constant.StatusRespond;
import lab.aikibo.controller.RootController;
import lab.aikibo.model.*;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;

/**
 * Created by tamami on 18/12/16.
 */
public class StoreProceduresDaoImpl implements StoreProceduresDao {

    @Autowired
    private BoneCPDataSource boneCPDs;

    private CallableStatement callable;
    private Sppt sppt;
    private PembayaranSppt pembayaranSppt;
    private ReversalPembayaran revPembayaran;
    private StatusInq status;
    private StatusTrx statusTrx;
    private StatusRev statusRev;

    @Override
    public StatusInq getDataSppt(String nop, String thn, String ipClient) {
        callable = null;
        sppt = null;
        status = null;

        try {
            callable = boneCPDs.getConnection().prepareCall("call sppt_terhutang(?,?,?)");
            callable.registerOutParameter(1, OracleTypes.CURSOR);
            callable.setString(2, nop);
            callable.setString(3, thn);
            callable.executeUpdate();
            ResultSet rs = (ResultSet) callable.getObject(1);
            ResultSetMetaData rsmd = rs.getMetaData();
            for(int i=1; i<rsmd.getColumnCount(); i++) {
                RootController.getLogger().debug(" >>> GET DATA FROM DB: " + i + " > " + rsmd.getColumnName(i));
            }
            sppt = new Sppt();

            while(rs.next()) {
                String nama = rs.getString("NAMA");
                String alamatOp = rs.getString("ALAMAT_OP");
                BigInteger pokok = rs.getBigDecimal("POKOK").toBigInteger();
                BigInteger denda = rs.getBigDecimal("DENDA").toBigInteger();
                RootController.getLogger().debug(" >>> NAMA : " + nama);
                RootController.getLogger().debug(" >>> ALAMAT OP : " + alamatOp);
                RootController.getLogger().debug(" >>> POKOK : " + pokok);
                RootController.getLogger().debug(" >>> DENDA : " + denda);
                sppt.setNop(nop);
                sppt.setThn(thn);
                sppt.setNama(nama);
                sppt.setAlamatOp(alamatOp);
                sppt.setPokok(pokok);
                sppt.setDenda(denda);
            }

            if(sppt.getNop() == null) {
                status = new StatusInq(StatusRespond.DATA_INQ_NIHIL, "Data Tidak Ditemukan", null);
                RootController.getLogger().debug(" >>> INQ ERROR >>> " + status);
                return status;
            }

            status = new StatusInq(StatusRespond.APPROVED, "Data Ditemukan", sppt);
            RootController.getLogger().debug(" >>> INQ SUKSES >>> " + status);
            return status;
        } catch(Exception e) {
            status = new StatusInq(StatusRespond.DATABASE_ERROR, "Kesalahan DB", null);
            RootController.getLogger().debug(" >>> INQ ERROR >>> " + status);
            return status;
        }
    }

    @Override
    public StatusTrx prosesPembayaran(String nop, String thn, Date tglBayar, String ipClient) {
        callable = null;
        pembayaranSppt = null;
        statusTrx = null;

        try {
            callable = boneCPDs.getConnection().prepareCall("call proses_pembayaran(?,?,?,?,?)");
            callable.registerOutParameter(1, OracleTypes.CURSOR);
            callable.setString(2, nop);
            callable.setString(3, thn);
            callable.setDate(4, new java.sql.Date(tglBayar.getTime()));
            callable.setString(5, ipClient);
            callable.executeUpdate();

            ResultSet rs = (ResultSet) callable.getObject(1);
            ResultSetMetaData rsMeta = rs.getMetaData();
            pembayaranSppt = new PembayaranSppt();
            while(rs.next()) {
                if(!rsMeta.getColumnName(1).equals("KODE_ERROR")) {
                    RootController.getLogger().debug(" >>> DATA PEMBAYARAN ADA: " + rs.getString("nop"));
                    pembayaranSppt.setNop(rs.getString("NOP"));
                    pembayaranSppt.setThn(rs.getString("THN"));
                    pembayaranSppt.setNtdp(rs.getString("NTPD"));
                    pembayaranSppt.setMataAnggaranPokok(rs.getString("MATA_ANGGARAN_POKOK"));
                    pembayaranSppt.setPokok(rs.getBigDecimal("POKOK").toBigInteger());
                    pembayaranSppt.setMataAnggaranSanksi(rs.getString("MATA_ANGGARAN_SANKSI"));
                    pembayaranSppt.setSanksi(rs.getBigDecimal("SANKSI").toBigInteger());
                    pembayaranSppt.setNamaWp(rs.getString("NAMA_WP"));
                    pembayaranSppt.setAlamatOp(rs.getString("ALAMAT_OP"));
                } else {
                    String infoSp = rs.getString("KODE_ERROR");
                    if(infoSp.equals("01")) {
                        statusTrx = new StatusTrx(StatusRespond.TAGIHAN_TELAH_TERBAYAR,
                                "Tagihan Telah Terbayar atau Pokok Pajak Nihil.", null);
                    } else if(infoSp.equals("02")) {
                        // not used
                    } else if(infoSp.equals("03")) {
                        statusTrx = new StatusTrx(StatusRespond.TAGIHAN_TELAH_TERBAYAR,
                                "Tagihan Telah Terbayar", null);
                    } else if(infoSp.equals("04")) {
                        statusTrx = new StatusTrx(StatusRespond.JUMLAH_SETORAN_NIHIL,
                                "Tagihan SPPT Telah Dibatalkan", null);
                    }
                    RootController.getLogger().debug(" >>> ERROR >>> " + statusTrx);
                    return statusTrx;
                }
            }
            statusTrx = new StatusTrx(StatusRespond.APPROVED, "Pembayaran Telah Tercatat", pembayaranSppt);
        } catch(Exception e) {
            statusTrx = new StatusTrx(StatusRespond.DATABASE_ERROR, "Kesalahan Server", null);
            RootController.getLogger().debug(" >>> HASIL EXCEPTION : " + e + " DENGAN KESALAHAN : " + statusTrx);
            return statusTrx;
        }
        RootController.getLogger().debug(" >>> TRX BERHASIL : " + statusTrx);
        return statusTrx;
    }

    @Override
    public StatusRev reversalPembayaran(String nop, String thn, String ntpd, String ipClient) {
        callable = null;
        revPembayaran = null;
        statusRev = null;

        try {
            callable = boneCPDs.getConnection().prepareCall("call reversal_pembayaran(?,?,?,?,?)");
            callable.registerOutParameter(1, OracleTypes.CURSOR);
            callable.setString(2, nop);
            callable.setString(3, thn);
            callable.setString(4, ntpd);
            callable.setString(5, ipClient);
            callable.executeUpdate();

            ResultSet rs = (ResultSet) callable.getObject(1);
            ResultSetMetaData rsMeta = rs.getMetaData();
            ReversalPembayaran revBayar = new ReversalPembayaran();
            while(rs.next()) {
                if(!rsMeta.getColumnName(1).equals("KODE_ERROR")) {
                    revBayar.setNop(rs.getString("NOP"));
                    revBayar.setThn(rs.getString("THN"));
                    revBayar.setNtpd(rs.getString("NTPD"));
                    statusRev = new StatusRev(StatusRespond.APPROVED, "Proses Reversal Berhasil", revBayar);
                } else {
                    String infoSp = rs.getString("KODE_ERROR");
                    if(infoSp.equals("01")) {
                        statusRev = new StatusRev(StatusRespond.DATA_INQ_NIHIL, "Data Yang Diminta Tidak Ada",
                                null);
                    } else if(infoSp.equals("02")) {
                        statusRev = new StatusRev(StatusRespond.DATABASE_ERROR, "Data Tersebut Ganda",
                                null);
                    }
                    RootController.getLogger().debug(" >>> REVERSAL ERROR >>> " + statusRev);
                    return statusRev;
                }
            }
        } catch(Exception e) {
            statusRev = new StatusRev(StatusRespond.DATABASE_ERROR, "Kesalahan Server", null);
            RootController.getLogger().debug(" >>> REV ERROR >>> " + statusRev);
            return statusRev;
        }
        return statusRev;
    }
}
