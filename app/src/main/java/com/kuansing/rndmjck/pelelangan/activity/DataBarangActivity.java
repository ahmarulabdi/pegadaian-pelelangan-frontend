package com.kuansing.rndmjck.pelelangan.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class DataBarangActivity extends AppCompatActivity {

    private static final String TAG = "DataBarangActivity";
    @BindView(R.id.recycler_view_data_barang_container)
    RecyclerView recyclerView;
    ApiInterface apiInterface;

    private String jenisBarang;
    @BindView(R.id.swipe_refresh_data_barang)
    SwipeRefreshLayout swipeRefreshLayout;
    String keterangan;
    DataBarangRViewAdapter dataBarangRViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        jenisBarang = getIntent().getStringExtra("jenis_barang");
        getSupportActionBar().setTitle("Data Barang " + jenisBarang);



        getDataBarang();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataBarang();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getDataBarang() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.dataBarang(jenisBarang).enqueue(new Callback<DataBarangResponse>() {
            @Override
            public void onResponse(Call<DataBarangResponse> call, Response<DataBarangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<Barang> barangs = response.body().getData();
                        dataBarangRViewAdapter = new DataBarangRViewAdapter(barangs, getApplicationContext());
                        recyclerView.setAdapter(dataBarangRViewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
