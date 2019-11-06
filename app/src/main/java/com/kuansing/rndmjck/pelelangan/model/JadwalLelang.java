package com.kuansing.rndmjck.pelelangan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rndmjck on 12/08/18.
 */

public class JadwalLelang {
    @SerializedName("id_jadwal_lelang")
    @Expose
    private String idJadwalLelang;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;

    @SerializedName("tanggalFormater")
    @Expose
    private String tanggalFormater;

    @SerializedName("waktu")
    @Expose
    private String waktu;

    public String getIdJadwalLelang() {
        return idJadwalLelang;
    }

    public void setIdJadwalLelang(String idJadwalLelang) {
        this.idJadwalLelang = idJadwalLelang;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTanggalFormater() {
        return tanggalFormater;
    }

    public void setTanggalFormater(String tanggalFormater) {
        this.tanggalFormater = tanggalFormater;
    }
}
