package com.kuansing.rndmjck.pelelangan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rndmjck on 12/08/18.
 */

public class CountBarang {
    @SerializedName("emas")
    @Expose
    private Integer emas;
    @SerializedName("kendaraan")
    @Expose
    private Integer kendaraan;
    @SerializedName("elektronik")
    @Expose
    private Integer elektronik;
    @SerializedName("semua")
    @Expose
    private Integer semua;

    public Integer getEmas() {
        return emas;
    }

    public void setEmas(Integer emas) {
        this.emas = emas;
    }

    public Integer getKendaraan() {
        return kendaraan;
    }

    public void setKendaraan(Integer kendaraan) {
        this.kendaraan = kendaraan;
    }

    public Integer getElektronik() {
        return elektronik;
    }

    public void setElektronik(Integer elektronik) {
        this.elektronik = elektronik;
    }

    public Integer getSemua() {
        return semua;
    }

    public void setSemua(Integer semua) {
        this.semua = semua;
    }
}
