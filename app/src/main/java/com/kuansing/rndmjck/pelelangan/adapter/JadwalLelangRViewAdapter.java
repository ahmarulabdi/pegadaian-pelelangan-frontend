package com.kuansing.rndmjck.pelelangan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.model.JadwalLelang;
import com.kuansing.rndmjck.pelelangan.response.DataJadwalLelangResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rndmjck on 12/08/18.
 */

public class JadwalLelangRViewAdapter extends RecyclerView.Adapter<JadwalLelangRViewAdapter.ViewHolderJadwalLelang> {

    private List<JadwalLelang> jadwalLelangs;
    private Context context;
    private SessionManager sessionManager;
    private ApiInterface apiInterface;

    private static final String TAG = "JadwalLelangRViewAdapte";

    public JadwalLelangRViewAdapter(List<JadwalLelang> jadwalLelangs, Context context) {
        this.jadwalLelangs = jadwalLelangs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderJadwalLelang onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_jadwal_lelang, parent, false);

        JadwalLelangRViewAdapter.ViewHolderJadwalLelang viewHolderJadwalLelang = new JadwalLelangRViewAdapter.ViewHolderJadwalLelang(view);
        sessionManager = new SessionManager(context.getApplicationContext());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        return viewHolderJadwalLelang;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderJadwalLelang holder, final int position) {

        Log.d(TAG, "onBindViewHolder: jadwal lelang "+position);
        holder.tanggalLelang.setText(jadwalLelangs.get(position).getTanggalFormater());
        holder.waktuLelang.setText(jadwalLelangs.get(position).getWaktu());

        if (sessionManager.getUserDetail().get("hak_akses").equals("penawar")){
            holder.hapusJadwalLelang.setVisibility(View.GONE);
        }
        holder.hapusJadwalLelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("konfirmasi");
                builder.setMessage("apakah anda yakin hapus jadwal ini ?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idJadwalLelang = jadwalLelangs.get(position).getIdJadwalLelang();

                        hapusJadwalLelang(idJadwalLelang);

                        jadwalLelangs.remove(position);
                        notifyDataSetChanged();

                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }


    private void hapusJadwalLelang(String idJadwallelang) {
        Log.d(TAG, "hapusJadwalLelang: idJadwalLelang " + idJadwallelang);
        apiInterface.deleteJadwalLelang(idJadwallelang).enqueue(new Callback<DataJadwalLelangResponse>() {
            @Override
            public void onResponse(Call<DataJadwalLelangResponse> call, Response<DataJadwalLelangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataJadwalLelangResponse> call, Throwable t) {
                Toast.makeText(context, "koneksi gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jadwalLelangs.size();
    }


    class ViewHolderJadwalLelang extends RecyclerView.ViewHolder {

        TextView tanggalLelang;
        TextView waktuLelang;
        ImageButton hapusJadwalLelang;

        public ViewHolderJadwalLelang(View itemView) {
            super(itemView);

            tanggalLelang = itemView.findViewById(R.id.tanggal_lelang);
            waktuLelang = itemView.findViewById(R.id.waktu_lelang);
            hapusJadwalLelang = itemView.findViewById(R.id.list_view_jadwal_lelang_hapus);
        }

    }
}
