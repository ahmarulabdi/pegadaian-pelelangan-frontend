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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.activity.LoginActivity;
import com.kuansing.rndmjck.pelelangan.config.ServerConfig;
import com.kuansing.rndmjck.pelelangan.model.Users;
import com.kuansing.rndmjck.pelelangan.response.RegisterResponse;
import com.kuansing.rndmjck.pelelangan.response.UsersResponse;
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

public class EditUsersActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_GALLERY_CODE = 9544;
    private static final String TAG = "EditUsersActivity";

    @BindView(R.id.edit_text_nama_lengkap_edit_users)
    EditText editTextnamaLengkap;
    @BindView(R.id.edit_text_alamat_edit_users)
    EditText editTextAlamat;
    @BindView(R.id.edit_text_nomor_ktp_edit_users)
    EditText editTextNomorKTP;
    @BindView(R.id.edit_text_nomor_telepon_edit_users)
    EditText editTextNomorTelepon;
    @BindView(R.id.edit_text_password_edit_users)
    EditText editTextPassword;
    @BindView(R.id.edit_text_password_konfirmasi_edit_users)
    EditText editTextPasswordKonfirmasi;

    @BindView(R.id.image_button_select_gambar)
    ImageButton imageButtonSelectGambar;
    @BindView(R.id.image_view_foto_ktp_edit_users)
    ImageView imageViewFotoKTP;
    @BindView(R.id.button_submit_edit_users)
    Button buttonSubmit;

    Bitmap bitmap;


    ApiInterface apiInterface;
    SessionManager sessionManager;
    private Uri uri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Profil Anda");

        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(getApplicationContext());

        imageButtonSelectGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bukagallery();
            }
        });


        getProfileData();


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DoEditData().execute();
            }
        });


    }

    private void getProfileData() {
        apiInterface.getUsers(sessionManager.getUserDetail().get("id_users")).enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Users users = response.body().getData();
                        editTextnamaLengkap.setText(users.getNamaLengkap());
                        editTextAlamat.setText(users.getAlamat());
                        editTextNomorKTP.setText(users.getNomorKtp());
                        editTextNomorTelepon.setText(users.getNomorTelepon());


                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(ServerConfig.IMAGE_FOLDER_USERS + users.getFotoKtp())
                                .into(imageViewFotoKTP);

                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                        imageViewFotoKTP.setImageBitmap(bitmap);
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

    private void bukagallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "open gallery"), REQUEST_GALLERY_CODE);
        Log.d(TAG, "bukagallery: tes");
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

    class DoEditData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EditUsersActivity.this);
            progressDialog.setMessage("Proses Edit Profil ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String imageBase64 = imageToString();
            String imageTitle = editTextNomorKTP.getText().toString();
            String namaLengkap = editTextnamaLengkap.getText().toString();
            String alamat = editTextAlamat.getText().toString();
            String nomorKTP = editTextNomorKTP.getText().toString();
            String nomorTelepon = editTextNomorTelepon.getText().toString();
            String password = editTextPassword.getText().toString();

            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            apiInterface.editProfile(
                    sessionManager.getUserDetail().get("id_users"),
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
                            Toast.makeText(getApplicationContext(), "Silahkan login kembali dengan profil yang sudah diperbarui", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            sessionManager.logoutUser();
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<RegisterResponse> call, Throwable t) {

                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

}
