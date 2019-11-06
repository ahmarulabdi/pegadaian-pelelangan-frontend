package com.kuansing.rndmjck.pelelangan.rest;


import android.support.annotation.Nullable;

import com.kuansing.rndmjck.pelelangan.response.CountBarangResponse;
import com.kuansing.rndmjck.pelelangan.response.DataBarangResponse;
import com.kuansing.rndmjck.pelelangan.response.DataJadwalLelangResponse;
import com.kuansing.rndmjck.pelelangan.response.DataPenawaranResponse;
import com.kuansing.rndmjck.pelelangan.response.DataUsersResponse;
import com.kuansing.rndmjck.pelelangan.response.DetailBarangResponse;
import com.kuansing.rndmjck.pelelangan.response.DetailPenawaranResponse;
import com.kuansing.rndmjck.pelelangan.response.JadwalLelangResponse;
import com.kuansing.rndmjck.pelelangan.response.LoginResponse;
import com.kuansing.rndmjck.pelelangan.response.RegisterResponse;
import com.kuansing.rndmjck.pelelangan.response.UsersResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiInterface {

    // di sini adalah penghubung antara android dan server

    // untuk login
    @FormUrlEncoded
    @POST("users/login")
    Call<LoginResponse> login(
            @Field("nomor_telepon") String nomortelepon,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("users/register")
    Call<RegisterResponse> register(
            @Field("image_base64") String imageBase64,
            @Field("image_title") String imageTitle,
            @Field("nama_lengkap") String namaLengkap,
            @Field("alamat") String alamat,
            @Field("nomor_ktp") int nomorKTP,
            @Field("nomor_telepon") int nomorTelepon,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("users/editprofil")
    Call<RegisterResponse> editProfile(
            @Field("id_users") String idUsers,
            @Field("image_base64") String imageBase64,
            @Field("image_title") String imageTitle,
            @Field("nama_lengkap") String namaLengkap,
            @Field("alamat") String alamat,
            @Field("nomor_ktp") String nomorKTP,
            @Field("nomor_telepon") String nomorTelepon,
            @Field("password") String password
    );

    @GET("users/datas")
    Call<DataUsersResponse> dataUsers();

    @GET("users/data/{id_users}")
    Call<UsersResponse> getUsers(
            @Path("id_users") String idUsers
    );

    @GET("barang/datas/{jenis_barang}")
    Call<DataBarangResponse> dataBarang(
            @Path("jenis_barang") String jenisBarang
    );
    @GET("barang/barangsoldout")
    Call<DataBarangResponse> dataBarangSoldOut();
    @Nullable
    @GET("barang/datasbyketerangan/{keterangan}")
    Call<DataBarangResponse> dataBarangByKeterangan(
            @Path("keterangan") String keterangan
    );
    @GET("barang/data/{kode_barang}")
    Call<DetailBarangResponse> detailBarang(
            @Path("kode_barang") String kodeBarang
    );
    @FormUrlEncoded
    @POST("barang/create")
    Call<DetailBarangResponse> tambahBarang(
            @Field("image_base64") String imageBase64,
            @Field("image_title") String imageTitle,
            @Field("nama_barang") String namaBarang,
            @Field("harga_barang") String hargaBarang,
            @Field("jenis_barang") String jenisBarang
    );
    @FormUrlEncoded
    @POST("barang/setketerangan")
    Call<DetailBarangResponse> setKeterangan(
            @Field("kode_barang") String kodeBarang,
            @Field("keterangan") String keterangan
    );
    @FormUrlEncoded
    @POST("barang/setpemenang")
    Call<DetailBarangResponse> setPemenang(
            @Field("kode_barang") String kodeBarang,
            @Field("id_users") String idUsers
    );
    @GET("barang/countdatabaranglelang")
    Call<CountBarangResponse> countBarang();

    @FormUrlEncoded
    @POST("barang/delete")
    Call<DetailBarangResponse> deleteBarang(
            @Field("kode_barang") String kodeBarang
    );


    @GET("penawaran/datas")
    Call<DataPenawaranResponse> dataPenawaran();



    @FormUrlEncoded
    @POST("penawaran/create")
    Call<DetailPenawaranResponse> kirimPenawaran(
            @Field("id_users") String idUsers,
            @Field("kode_barang") String kodeBarang,
            @Field("harga") String harga
    );

    @GET("penawaran/datakodes/{kode_barang}")
    Call<DataPenawaranResponse> dataPenawarankodeBarang (
            @Path("kode_barang") String kodeBarang
    );

    @FormUrlEncoded
    @POST("penawaran/setstatus")
    Call<DataPenawaranResponse> setStatusPenawaran(
      @Field("id_penawaran") String idPenawaran,
      @Field("status") String  status
    );
    @FormUrlEncoded
    @POST("penawaran/setharga")
    Call<DataPenawaranResponse> setHargaPenawaran(
            @Field("id_penawaran") String idPenawaran,
            @Field("harga") String harga
    );

    @GET("jadwal")
    Call<DataJadwalLelangResponse> jadwalLelang();

    @FormUrlEncoded
    @POST("jadwal/create")
    Call<JadwalLelangResponse> tambahJadwalLelang(
            @Field("tanggal") String tanggal,
            @Field("waktu") String waktu
    );

    @FormUrlEncoded
    @POST("jadwal/delete")
    Call<DataJadwalLelangResponse> deleteJadwalLelang(
            @Field("id_jadwal_lelang") String idJadwalLelang
    );







//    @GET("pengajuanta")
//    Call<ResponsePengajuanKor> pengajuanta();
//
//    //untuk signup
//    @FormUrlEncoded
//    @POST("signup.php")
//    Call<ResponseBody> signup(
//            @Field("username") String username,
//            @Field("password") String password,
//            @Field("nama_lengkap") String namaLengkap,
//            @Field("email") String email,
//            @Field("no_hp") String nomorHP
//    );

}
