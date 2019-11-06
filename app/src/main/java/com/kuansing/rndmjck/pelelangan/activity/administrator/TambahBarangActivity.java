package com.kuansing.rndmjck.pelelangan.activity.administrator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.response.DetailBarangResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahBarangActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.et_nama_barang)
    EditText editTextNamaBarang;
    @BindView(R.id.et_harga_barang)
    EditText editTextHargaBarang;
    @BindView(R.id.spin_jenis_barang)
    Spinner spinnerJenisBarang;
    @BindView(R.id.ib_gambar_barang)
    ImageButton imageButtonGambarBarang;
    @BindView(R.id.iv_gambar_barang)
    ImageView imageViewgambarBarang;
    @BindView(R.id.btn_submit_barang)
    Button buttonSubmitBarang;
    ProgressDialog progressDialog;

    private Integer REQUEST_GALLERY = 101;


    private static final String TAG = "TambahBarangActivity";
    private Bitmap bitmap;
    private Uri uri;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        String[] arrJenisBarang = {"emas", "kendaraan", "elektronik", "lain-lain"};
        ArrayAdapter<String> jenisBarangAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrJenisBarang);
        jenisBarangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisBarang.setAdapter(jenisBarangAdapter);

        imageButtonGambarBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukagallery();
            }
        });

        buttonSubmitBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: tes");
                new DoTambahBarang().execute();
            }
        });


    }

    private void bukagallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "open gallery"), REQUEST_GALLERY);
        Log.d(TAG, "bukagallery: tes");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                uri = data.getData();
                if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(getApplicationContext(), "gambar muncul", Toast.LENGTH_SHORT).show();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageViewgambarBarang.setImageBitmap(bitmap);
                        buttonSubmitBarang.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    EasyPermissions.requestPermissions(this, "Access for storage",
                            REQUEST_GALLERY, galleryPermissions);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, TambahBarangActivity.this);
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


    class DoTambahBarang extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(TambahBarangActivity.this);
            progressDialog.setMessage("Proses Registrasi ...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String imageBase64 = imageToString();
            String imageTitle = editTextNamaBarang.getText().toString();
            String namaBarang = editTextNamaBarang.getText().toString();
            String hargaBarang = editTextHargaBarang.getText().toString();
            String jenisBarang = spinnerJenisBarang.getSelectedItem().toString();

            apiInterface.tambahBarang(imageBase64, imageTitle, namaBarang, hargaBarang, jenisBarang)
                    .enqueue(new Callback<DetailBarangResponse>() {
                        @Override
                        public void onResponse(Call<DetailBarangResponse> call, Response<DetailBarangResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus()) {
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),KelolaBarangActivity.class);
                                    intent.putExtra("keterangan","");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<DetailBarangResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
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
