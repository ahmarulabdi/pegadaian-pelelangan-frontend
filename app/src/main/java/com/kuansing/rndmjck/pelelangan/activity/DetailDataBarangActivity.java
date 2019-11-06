package com.kuansing.rndmjck.pelelangan.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.adapter.DataPenawaranRViewAdapter;
import com.kuansing.rndmjck.pelelangan.config.ServerConfig;
import com.kuansing.rndmjck.pelelangan.model.Penawaran;
import com.kuansing.rndmjck.pelelangan.response.DataPenawaranResponse;
import com.kuansing.rndmjck.pelelangan.response.DetailBarangResponse;
import com.kuansing.rndmjck.pelelangan.response.DetailPenawaranResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDataBarangActivity extends AppCompatActivity {

    private static final String TAG = "DetailDataBarangActivit";
    @BindView(R.id.gambar_barang_detail)
    ImageView gambarBarangDetail;
    @BindView(R.id.kode_barang_detail)
    TextView kodeBarangDetail;
    @BindView(R.id.nama_barang_detail)
    TextView namaBarangDetail;
    @BindView(R.id.harga_barang_detail)
    TextView hargaBarangDetail;
    @BindView(R.id.keterangan_barang_detail)
    TextView keteranganBarangDetail;
    @BindView(R.id.jenis_barang_detail)
    TextView jenisBarangDetail;
    @BindView(R.id.kosong)
    TextView kosong;
    @BindView(R.id.text_input_tawar_harga)
    TextInputEditText tawarHarga;
    @BindView(R.id.text_input_tawar_harga_container)
    TextInputLayout tawarHargaContainer;
    @BindView(R.id.kirim_penawaran)
    Button btnKirimPenawaran;
    @BindView(R.id.label_nama_pemenang_detail)
    TextView labelNamaPemenangDetail;
    @BindView(R.id.nama_pemenang_detail)
    TextView namaPemenangDetail;
    @BindView(R.id.titik_dua_nama_pemenang_detail)
    TextView titikDuaNamaPemenang;



    ApiInterface apiInterface;
    @BindView(R.id.recycler_view_data_penawaran)
    RecyclerView recyclerView;


    private DataPenawaranRViewAdapter dataPenawaranRViewAdapter;

    private String status;
    @BindView(R.id.swipe_refresh_detail_barang)
    SwipeRefreshLayout swipeRefreshLayout;
    private String kodeBarang;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(getApplicationContext());
        kodeBarang = getIntent().getStringExtra("kode_barang");


        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataDetailBarangDataPenawaran();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnKirimPenawaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimPenawaran();
            }
        });

        getDataDetailBarangDataPenawaran();




    }

    private void kirimPenawaran() {
        apiInterface.kirimPenawaran(sessionManager.getUserDetail().get("id_users"), kodeBarang, tawarHarga.getText().toString())
                .enqueue(new Callback<DetailPenawaranResponse>() {
                    @Override
                    public void onResponse(Call<DetailPenawaranResponse> call, Response<DetailPenawaranResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus()) {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                getDataDetailBarangDataPenawaran();
                            } else {
                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailPenawaranResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getDataDetailBarangDataPenawaran() {
        getDataBarang();
        getDataPenawaran();
    }

    private void getDataBarang() {

        apiInterface.detailBarang(kodeBarang).enqueue(new Callback<DetailBarangResponse>() {
            @Override
            public void onResponse(Call<DetailBarangResponse> call, Response<DetailBarangResponse> response) {

                kodeBarangDetail.setText(response.body().getData().getKodeBarang());
                namaBarangDetail.setText(response.body().getData().getNamaBarang());
                hargaBarangDetail.setText("Rp. " + response.body().getData().getHargaBarang());
                keteranganBarangDetail.setText(response.body().getData().getKeterangan());
                jenisBarangDetail.setText(response.body().getData().getJenisBarang());

                if (sessionManager.getUserDetail().get("hak_akses").equals("administrator")) {
                    tawarHargaContainer.setVisibility(View.GONE);
                }


                if (keteranganBarangDetail.getText().toString().equals("sold out")) {
                    tawarHargaContainer.setVisibility(View.GONE);
                    namaPemenangDetail.setText(response.body().getData().getNamaLengakap());

                } else {

                    labelNamaPemenangDetail.setVisibility(View.GONE);
                    namaPemenangDetail.setVisibility(View.GONE);
                    titikDuaNamaPemenang.setVisibility(View.GONE);
                }
                getSupportActionBar().setTitle("Detail Barang " + response.body().getData().getNamaBarang());

                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(ServerConfig.IMAGE_FOLDER_BARANG + response.body().getData().getGambar())
                        .into(gambarBarangDetail);
            }

            @Override
            public void onFailure(Call<DetailBarangResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataPenawaran() {
        apiInterface.dataPenawarankodeBarang(kodeBarang).enqueue(new Callback<DataPenawaranResponse>() {
            @Override
            public void onResponse(Call<DataPenawaranResponse> call, Response<DataPenawaranResponse> response) {
                Log.d(TAG, "onResponse: cek data");
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<Penawaran> penawarans = response.body().getData();
                        sessionManager = new SessionManager(getApplicationContext());
                        for (int i = 0; i < penawarans.size(); i++) {
                            Penawaran penawaran = penawarans.get(i);
                            String idUsers = penawaran.getIdUsers();
                            String ses_idUsers = sessionManager.getUserDetail().get("id_users");
                            Log.d(TAG, "onResponse: session users" + ses_idUsers);
                            Log.d(TAG, "onResponse: users" + idUsers);
                            if (idUsers.equals(ses_idUsers)) {
                                tawarHargaContainer.setVisibility(View.GONE);
                            }
                        }

                        DataPenawaranRViewAdapter dataPenawaranRViewAdapter = new DataPenawaranRViewAdapter(getApplicationContext(), penawarans);
                        recyclerView.setAdapter(dataPenawaranRViewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        kosong.setVisibility(View.GONE);
                    } else {
                        kosong.setText(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<DataPenawaranResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal ", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (sessionManager.getUserDetail().get("hak_akses").equals("administrator")){
//            Intent intent = new Intent(getApplicationContext(), KelolaBarangActivity.class);
//            intent.putExtra("keterangan","proses lelang");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            startActivity(intent);
//        }
//    }
}
