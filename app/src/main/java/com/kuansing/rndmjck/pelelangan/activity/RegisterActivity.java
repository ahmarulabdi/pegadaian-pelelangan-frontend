package com.kuansing.rndmjck.pelelangan.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.response.RegisterResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends Activity implements EasyPermissions.PermissionCallbacks {


    private static final int REQUEST_GALLERY_CODE = 9544;

    private final String TAG = RegisterActivity.class.getSimpleName();

    @BindView(R.id.btnsubmit)
    Button btnSubmit;

    @BindView(R.id.etnamalengkap)
    EditText etNamaLengkap;
    @BindView(R.id.etalamat)
    EditText etAlamat;
    @BindView(R.id.etnomorktp)
    EditText etNomorKTP;
    @BindView(R.id.etnomortelepon)
    EditText etNomorTelepon;
    @BindView(R.id.etpassword)
    EditText etPassword;
    @BindView(R.id.etpasswordkonfirmasi)
    EditText etPasswordKonfirmasi;
    @BindView(R.id.selectgambar)
    ImageButton selectGambar;
    @BindView(R.id.ivfotoktp)
    ImageView ivFotoKTP;
    private Uri uri;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: tes");
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);


        selectGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukagallery();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etPassword.getText().toString().equals(etPasswordKonfirmasi.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "password dan konfirmasi password tidak sama", Toast.LENGTH_SHORT).show();
                } else {
                    new DoKirimData().execute();
                }
            }
        });
    }


    private void bukagallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "open gallery"), REQUEST_GALLERY_CODE);
        Log.d(TAG, "bukagallery: tes");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: tes ");
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE) {
                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                uri = data.getData();
                if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(), "gambar muncul", Toast.LENGTH_SHORT).show();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        ivFotoKTP.setImageBitmap(bitmap);
                        btnSubmit.setEnabled(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    EasyPermissions.requestPermissions(this, "Access for storage",
                            REQUEST_GALLERY_CODE, galleryPermissions);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, RegisterActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "akses gambar gallery diterima");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "akses gambar gallery ditolak");
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);

        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    class DoKirimData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Proses Registrasi ...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String imageBase64 = imageToString();
            String imageTitle = etNomorKTP.getText().toString();
            String namaLengkap = etNamaLengkap.getText().toString();
            String alamat = etAlamat.getText().toString();
            int nomorKTP = Integer.parseInt(etNomorKTP.getText().toString());
            int nomorTelepon = Integer.parseInt(etNomorTelepon.getText().toString());
            String password = etPassword.getText().toString();

            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.register(
                    imageBase64,
                    imageTitle,
                    namaLengkap,
                    alamat,
                    nomorKTP,
                    nomorTelepon,
                    password
            ).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(retrofit2.Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: "+t);
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }
}
