package com.kuansing.rndmjck.pelelangan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rndmjck on 06/08/18.
 */

public class Barang {
    @SerializedName("kode_barang")
    @Expose
    private String kodeBarang;
    @SerializedName("nama_barang")
    @Expose
    private String namaBarang;
    @SerializedName("harga_barang")
    @Expose
    private String hargaBarang;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("jenis_barang")
    @Expose
    private String jenisBarang;
    @SerializedName("id_users")
    @Expose
    private String idUsers;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengakap;

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

    public String getHargaBarang() {
        return hargaBarang;
    }

    public void setHargaBarang(String hargaBarang) {
        this.hargaBarang = hargaBarang;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getJenisBarang() {
        return jenisBarang;
    }

    public void setJenisBarang(String jenisBarang) {
        this.jenisBarang = jenisBarang;
    }

    public String getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(String idUsers) {
        this.idUsers = idUsers;
    }

    public String getNamaLengakap() {
        return namaLengakap;
    }

    public void setNamaLengakap(String namaLengakap) {
        this.namaLengakap = namaLengakap;
    }
}
