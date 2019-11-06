package com.kuansing.rndmjck.pelelangan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.model.Users;
import com.kuansing.rndmjck.pelelangan.response.LoginResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etnomortelepon)
    EditText etnomortelepon;


    @BindView(R.id.etpassword)
    TextInputEditText etpassword;

    @BindView(R.id.btnlogin)
    Button btnlogin;


    @BindView(R.id.btnregistrasi)
    Button btnregistrasi;

    @BindView(R.id.cvlogin)
    CardView cvlogin;

    SessionManager sessionManager;
    ApiInterface apiInterface;


    String nomortelepon, password;

    public final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //init
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        cvlogin.getBackground().setAlpha(150);




        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btnregistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });


    }

    private void login() {
        nomortelepon = etnomortelepon.getText().toString();
        password = etpassword.getText().toString();

        apiInterface.login(nomortelepon, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
//                    cek response status berhasil
                    if (!response.body().getStatus()) {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Users users = response.body().getData();
                        sessionManager.createLoginSession(users);

                        Intent i = new Intent(getApplicationContext(), SplashScreenActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "koneksi gagal", Toast.LENGTH_LONG).show();
                Log.e(TAG, "gagal" + t.getLocalizedMessage());
            }
        });

    }

}
