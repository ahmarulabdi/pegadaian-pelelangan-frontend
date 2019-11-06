package com.kuansing.rndmjck.pelelangan.activity.administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.activity.ActivityNavigation;
import com.kuansing.rndmjck.pelelangan.response.JadwalLelangResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahJadwalLelangActivity extends AppCompatActivity {

    private static final String TAG = "TambahJadwalLelangActiv";
    ApiInterface apiInterface;
    @BindView(R.id.tambah_jadwal_lelang)
    Button buttonSubmitJadwalLelang;
    @BindView(R.id.dp_tanggal_lelang)
    DatePicker datePickerTanggalLelang;
    @BindView(R.id.tp_waktu_lelang)
    TimePicker timePickerWaktuLelang;
    private String tanggalLelang;
    private String waktuLelang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_jadwal_lelang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Jadwal Lelang");
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityNavigation.class);
                intent.putExtra("navigate","jadwal lelang");
                startActivity(intent);
            }
        });

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        buttonSubmitJadwalLelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggalLelang = datePickerTanggalLelang.getYear() + "-" + (datePickerTanggalLelang.getMonth() + 1) + "-" + datePickerTanggalLelang.getDayOfMonth() ;
                waktuLelang = timePickerWaktuLelang.getCurrentHour() + ":" + timePickerWaktuLelang.getCurrentMinute();

                if (tanggalLelang.isEmpty() || waktuLelang.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "data belum lengkap", Toast.LENGTH_SHORT).show();
                } else {
                    tambahJadwalLelang();
                }
            }
        });
    }
    private void tambahJadwalLelang() {
        Log.d(TAG, "tambahJadwalLelang: tanggalLelang "+tanggalLelang);
        Log.d(TAG, "tambahJadwalLelang: waktuLelang "+waktuLelang);
        apiInterface.tambahJadwalLelang(tanggalLelang, waktuLelang).enqueue(new Callback<JadwalLelangResponse>() {
            @Override
            public void onResponse(Call<JadwalLelangResponse> call, Response<JadwalLelangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: sampai sini");
                        Intent intent = new Intent(getApplicationContext(), ActivityNavigation.class);
                        intent.putExtra("navigate","jadwal lelang");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<JadwalLelangResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
