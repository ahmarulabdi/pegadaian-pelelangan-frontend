package com.kuansing.rndmjck.pelelangan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.activity.DataBarangActivity;
import com.kuansing.rndmjck.pelelangan.activity.DataBarangSoldOutActivity;
import com.kuansing.rndmjck.pelelangan.activity.administrator.KelolaBarangActivity;
import com.kuansing.rndmjck.pelelangan.response.CountBarangResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuDataBarangFragment extends Fragment {


    public MenuDataBarangFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "MenuDataBarangFragment";
    @BindView(R.id.btn_lelang_emas)
    Button btnLelangEmas;
    @BindView(R.id.btn_lelang_elektronik)
    Button btnLelangElektronik;
    @BindView(R.id.btn_lelang_kendaraan)
    Button btnLelangKendaraan;
    @BindView(R.id.btn_lelang_semua)
    Button btnLelangSemua;
    @BindView(R.id.btn_kelola_barang)
    Button btnKelolaBarang;
    @BindView(R.id.btn_data_barang_sold_out)
    Button btnBarangSoldOut;

    @BindView(R.id.swipe_refresh_menu_data_barang)
    SwipeRefreshLayout swipeRefreshLayoutMenuDataBarang;
    SessionManager sessionManager;
    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_data_barang, container, false);
        ButterKnife.bind(this, view);
        sessionManager = new SessionManager(getContext());
        if (!sessionManager.getUserDetail().get("hak_akses").equals("administrator")) {
            btnKelolaBarang.setVisibility(View.GONE);
        } else {
            btnLelangEmas.setVisibility(View.GONE);
            btnLelangKendaraan.setVisibility(View.GONE);
            btnLelangElektronik.setVisibility(View.GONE);
            btnLelangSemua.setVisibility(View.GONE);
        }

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        countDataBarang();


        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        swipeRefreshLayoutMenuDataBarang.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                countDataBarang();
                swipeRefreshLayoutMenuDataBarang.setRefreshing(false);
            }
        });

        btnLelangEmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataBarangActivity.class);
                intent.putExtra("jenis_barang", "emas");
                startActivity(intent);
            }
        });
        btnLelangKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataBarangActivity.class);
                intent.putExtra("jenis_barang", "kendaraan");
                startActivity(intent);
            }
        });
        btnLelangElektronik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataBarangActivity.class);
                intent.putExtra("jenis_barang", "elektronik");
                startActivity(intent);
            }
        });
        btnLelangSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DataBarangActivity.class);
                intent.putExtra("jenis_barang", "");
                startActivity(intent);
            }
        });

        btnKelolaBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KelolaBarangActivity.class);
                intent.putExtra("keterangan", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        btnBarangSoldOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DataBarangSoldOutActivity.class);
                startActivity(intent);

            }
        });
    }

    private void countDataBarang() {
        apiInterface.countBarang().enqueue(new Callback<CountBarangResponse>() {
            @Override
            public void onResponse(Call<CountBarangResponse> call, Response<CountBarangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: getEmas" + response.body().getData().getEmas());
                        Log.d(TAG, "onResponse: getKendaraan" + response.body().getData().getKendaraan());
                        Log.d(TAG, "onResponse: getElektronik" + response.body().getData().getElektronik());
                        Log.d(TAG, "onResponse: getSemua" + response.body().getData().getSemua());
                        if (response.body().getData().getEmas() > 0) {
                            btnLelangEmas.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
                        } else {
                            btnLelangEmas.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                        if (response.body().getData().getKendaraan() > 0) {
                            btnLelangKendaraan.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
                        } else {
                            btnLelangKendaraan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                        if (response.body().getData().getElektronik() > 0) {
                            btnLelangElektronik.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
                        } else {
                            btnLelangElektronik.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                        if (response.body().getData().getSemua() > 0) {
                            btnLelangSemua.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
                        } else {
                            btnLelangSemua.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CountBarangResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
