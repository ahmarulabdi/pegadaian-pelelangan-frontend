package com.kuansing.rndmjck.pelelangan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rndmjck on 01/08/18.
 */

public class Users {
    @SerializedName("id_users")
    @Expose
    private String idUsers;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengkap;
    @SerializedName("nomor_telepon")
    @Expose
    private String nomorTelepon;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("nomor_ktp")
    @Expose
    private String nomorKtp;
    @SerializedName("foto_ktp")
    @Expose
    private String fotoKtp;
    @SerializedName("hak_akses")
    @Expose
    private String hakAkses;

    public String getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(String idUsers) {
        this.idUsers = idUsers;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomorKtp() {
        return nomorKtp;
    }

    public void setNomorKtp(String nomorKtp) {
        this.nomorKtp = nomorKtp;
    }

    public String getFotoKtp() {
        return fotoKtp;
    }

    public void setFotoKtp(String fotoKtp) {
        this.fotoKtp = fotoKtp;
    }

    public String getHakAkses() {
        return hakAkses;
    }

    public void setHakAkses(String hakAkses) {
        this.hakAkses = hakAkses;
    }
}
