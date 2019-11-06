package com.kuansing.rndmjck.pelelangan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.activity.DetailDataBarangActivity;
import com.kuansing.rndmjck.pelelangan.activity.administrator.KelolaBarangActivity;
import com.kuansing.rndmjck.pelelangan.config.ServerConfig;
import com.kuansing.rndmjck.pelelangan.model.Barang;
import com.kuansing.rndmjck.pelelangan.response.DetailBarangResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rndmjck on 06/08/18.
 */

public class DataBarangRViewAdapter extends RecyclerView.Adapter<DataBarangRViewAdapter.ViewHolderDataBarang> {

    private static final String TAG = "DataBarangRViewAdapter";
    private List<Barang> barangs;
    private Context context;
    private SessionManager sessionManager;
    private String kodeBarang;
    private String keterangan;
    ApiInterface apiInterface;


    public DataBarangRViewAdapter(List<Barang> barang, Context context) {
        this.barangs = barang;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderDataBarang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_data_barang, parent, false);

        ViewHolderDataBarang viewHolderDataBarang = new ViewHolderDataBarang(view);
        sessionManager = new SessionManager(context.getApplicationContext());


        return viewHolderDataBarang;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDataBarang holder, final int position) {
        Log.d(TAG, "onBindViewHolder: position " + position);


        Glide.with(context)
                .asBitmap()
                .load(ServerConfig.IMAGE_FOLDER_BARANG + barangs.get(position).getGambar())
                .into(holder.gambarBarang);


        holder.kodeBarang.setText("Kode Barang " + barangs.get(position).getKodeBarang());
        holder.namaBarang.setText(barangs.get(position).getNamaBarang());
        holder.hargaBarang.setText("Rp. " + barangs.get(position).getHargaBarang());
        holder.keteranganBarang.setText(barangs.get(position).getKeterangan());
        holder.jenisBarang.setText("Jenis Barang " + barangs.get(position).getJenisBarang());


        if (!sessionManager.getUserDetail().get("hak_akses").equals("administrator")) {
            holder.aksiBarang.setVisibility(View.GONE);
            holder.hapusBarang.setVisibility(View.GONE);
        }

        if (holder.keteranganBarang.getText().toString().equals("")) {
            holder.aksiBarang.setText("Proses Lelang");
            holder.aksiBarang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getRootView().getContext());

                    alertDialog.setTitle("konfirmasi ?");
                    alertDialog.setMessage("tambahkan barang ke dalam penawaran lelang?");
                    alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            kodeBarang = barangs.get(position).getKodeBarang();
                            keterangan = "proses lelang";
                            setKeterangan();
                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            });
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context.getApplicationContext(), "barang belum dimasukkan kedalam proses lelang", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (!holder.keteranganBarang.getText().toString().equals("")) {
            holder.aksiBarang.setVisibility(View.GONE);
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), DetailDataBarangActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("kode_barang", barangs.get(position).getKodeBarang());
                    context.startActivity(intent);
                }
            });
        }


        holder.hapusBarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("konfirmasi");
                builder.setMessage("hapus data barang " + barangs.get(position).getNamaBarang() + " ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        kodeBarang = barangs.get(position).getKodeBarang();
                        hapusBarang();
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });


    }

    private void hapusBarang() {
        Log.d(TAG, "hapusBarang: kodeBarang " + kodeBarang);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.deleteBarang(kodeBarang).enqueue(new Callback<DetailBarangResponse>() {
            @Override
            public void onResponse(Call<DetailBarangResponse> call, Response<DetailBarangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context.getApplicationContext(), KelolaBarangActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        Log.d(TAG, "onResponse: getKeterangan " + response.body().getData().getKeterangan());

                        intent.putExtra("keterangan", "proses lelang");
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailBarangResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setKeterangan() {
        Log.d(TAG, "setKeterangan: kodeBarang " + kodeBarang);
        Log.d(TAG, "setKeterangan: keterangan " + keterangan);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.setKeterangan(kodeBarang, keterangan).enqueue(new Callback<DetailBarangResponse>() {
            @Override
            public void onResponse(Call<DetailBarangResponse> call, Response<DetailBarangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context.getApplicationContext(), KelolaBarangActivity.class);
                        intent.putExtra("keterangan", keterangan);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailBarangResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return barangs.size();
    }


    class ViewHolderDataBarang extends RecyclerView.ViewHolder {

        private TextView kodeBarang;
        private ImageView gambarBarang;
        private TextView namaBarang;
        private TextView hargaBarang;
        private TextView keteranganBarang;
        private TextView jenisBarang;
        private Button aksiBarang;
        private ImageButton hapusBarang;
        RelativeLayout parentLayout;

        public ViewHolderDataBarang(View itemView) {
            super(itemView);

            kodeBarang = itemView.findViewById(R.id.kode_barang);
            gambarBarang = itemView.findViewById(R.id.gambar_barang);
            namaBarang = itemView.findViewById(R.id.nama_barang);
            hargaBarang = itemView.findViewById(R.id.harga_barang);
            keteranganBarang = itemView.findViewById(R.id.keterangan_barang);
            jenisBarang = itemView.findViewById(R.id.jenis_barang);
            parentLayout = itemView.findViewById(R.id.list_view_data_barang_layout);
            aksiBarang = itemView.findViewById(R.id.list_view_data_barang_aksi);
            hapusBarang = itemView.findViewById(R.id.list_view_data_barang_hapus);


        }

    }
}
