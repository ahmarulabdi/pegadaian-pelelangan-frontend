package com.kuansing.rndmjck.pelelangan.activity.administrator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.adapter.DataBarangRViewAdapter;
import com.kuansing.rndmjck.pelelangan.model.Barang;
import com.kuansing.rndmjck.pelelangan.response.DataBarangResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelolaBarangActivity extends AppCompatActivity {

    DataBarangRViewAdapter dataBarangRViewAdapter;
    ApiInterface apiInterface;
    @BindView(R.id.recycler_view_kelola_barang)
    RecyclerView recyclerViewKelolaBarang;
    @BindView(R.id.btn_barang_baru)
    Button buttonBarangBaru;
    @BindView(R.id.btn_barang_proses_lelang)
    Button buttonBarangProsesLelang;
    @BindView(R.id.btn_barang_sold_out)
    Button buttonBarangSoldOut;
    @BindView(R.id.swipe_refresh_kelola_barang)
    SwipeRefreshLayout swipeRefreshLayoutKelolaBarang;
    @BindView(R.id.title_keterangan_barang)
    TextView textViewTitleKeteranganBarang;
    String keterangan = "";
    private static final String TAG = "KelolaBarangActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tambah_barang);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TambahBarangActivity.class);
                startActivity(intent);
            }
        });


        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        if (getIntent().getStringExtra("keterangan").equals("proses lelang")) {
            keterangan = getIntent().getStringExtra("keterangan");
        }else if (getIntent().getStringExtra("keterangan").equals("sold out")){
            keterangan = getIntent().getStringExtra("keterangan");
        }else {
            keterangan = "";
        }
        getDataBarang();
        setTitleListData();

        buttonBarangBaru.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                keterangan = "";
                getDataBarang();
                setTitleListData();
            }
        });
        buttonBarangProsesLelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keterangan = "proses lelang";
                getDataBarang();
                setTitleListData();
            }
        });
        buttonBarangSoldOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keterangan = "sold out";
                getDataBarang();
                setTitleListData();
            }
        });


        swipeRefreshLayoutKelolaBarang.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataBarang();
                swipeRefreshLayoutKelolaBarang.setRefreshing(false);
            }
        });


    }

    private void setTitleListData() {
        if (keterangan.isEmpty()) {
            textViewTitleKeteranganBarang.setText("barang baru ");
        } else {
            textViewTitleKeteranganBarang.setText("barang " + keterangan);
        }
    }

    private void getDataBarang() {
        apiInterface.dataBarangByKeterangan(keterangan).enqueue(new Callback<DataBarangResponse>() {
            @Override
            public void onResponse(Call<DataBarangResponse> call, Response<DataBarangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<Barang> barangs = response.body().getData();
                        dataBarangRViewAdapter = new DataBarangRViewAdapter(barangs, getApplicationContext());
                        recyclerViewKelolaBarang.setAdapter(dataBarangRViewAdapter);
                        recyclerViewKelolaBarang.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataBarangResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
