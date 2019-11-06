package com.kuansing.rndmjck.pelelangan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rndmjck on 07/08/18.
 */

public class Penawaran {

    @SerializedName("id_penawaran")
    @Expose
    private String idPenawaran;
    @SerializedName("kode_barang")
    @Expose
    private String kodeBarang;
    @SerializedName("nama_barang")
    @Expose
    private String namaBarang;
    @SerializedName("id_users")
    @Expose
    private String idUsers;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengkap;
    @SerializedName("nomor_telepon")
    @Expose
    private String nomorTelepon;
    @SerializedName("harga")
    @Expose
    private String harga;
    @SerializedName("status")
    @Expose
    private String status;

    public String getIdPenawaran() {
        return idPenawaran;
    }

    public void setIdPenawaran(String idPenawaran) {
        this.idPenawaran = idPenawaran;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

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

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
