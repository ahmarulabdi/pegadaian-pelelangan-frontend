package com.kuansing.rndmjck.pelelangan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.activity.administrator.TambahJadwalLelangActivity;
import com.kuansing.rndmjck.pelelangan.adapter.JadwalLelangRViewAdapter;
import com.kuansing.rndmjck.pelelangan.model.JadwalLelang;
import com.kuansing.rndmjck.pelelangan.response.DataJadwalLelangResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class JadwalLelangFragment extends Fragment {

    private static final String TAG = "JadwalLelangFragment";
    ApiInterface apiInterface;
    @BindView(R.id.recycler_view_jadwal_lelang_container)
    RecyclerView recyclerView;
    JadwalLelangRViewAdapter jadwalLelangRViewAdapter;
    @BindView(R.id.swipe_refresh_jadwal_lelang)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.btn_tambah_jadwal_lelang)
    Button buttonTambahJadwalLelang;
    SessionManager sessionManager;

    public JadwalLelangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_jadwal_lelang, container, false);
        ButterKnife.bind(this, view);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sessionManager = new SessionManager(getActivity().getApplicationContext());
        if (sessionManager.getUserDetail().get("hak_akses").equals("penawar")){
            buttonTambahJadwalLelang.setVisibility(View.GONE);
        }




        buttonTambahJadwalLelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahJadwalLelangActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                getActivity().finish();
            }
        });
        getJadwalLelang();
        return view ;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJadwalLelang();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    private void getJadwalLelang() {
        Log.d(TAG, "getJadwalLelang: ");
        apiInterface.jadwalLelang().enqueue(new Callback<DataJadwalLelangResponse>() {
            @Override
            public void onResponse(Call<DataJadwalLelangResponse> call, Response<DataJadwalLelangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<JadwalLelang> jadwalLelangs = response.body().getData();
                        jadwalLelangRViewAdapter = new JadwalLelangRViewAdapter(jadwalLelangs, getContext());
                        recyclerView.setAdapter(jadwalLelangRViewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataJadwalLelangResponse> call, Throwable t) {
                Toast.makeText(getContext(), "gagal koneksi ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
