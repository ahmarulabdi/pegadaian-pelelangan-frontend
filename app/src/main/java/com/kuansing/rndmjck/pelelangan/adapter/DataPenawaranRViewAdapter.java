package com.kuansing.rndmjck.pelelangan.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.activity.administrator.KelolaBarangActivity;
import com.kuansing.rndmjck.pelelangan.model.Penawaran;
import com.kuansing.rndmjck.pelelangan.response.DataPenawaranResponse;
import com.kuansing.rndmjck.pelelangan.response.DetailBarangResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rndmjck on 07/08/18.
 */

public class DataPenawaranRViewAdapter extends RecyclerView.Adapter<DataPenawaranRViewAdapter.ViewHolderDataPenawaran> {

    private List<Penawaran> penawarans;
    private List<ListView> containerListViewPenawaran;
    private Context context;
    private static final String TAG = "DataPenawaranRViewAdapt";
    private ProgressDialog progressDialog;
    private String newStatus;
    private String idPenawaran;
    private String idUsers;
    private SessionManager sessionManager;
    private String status;
    public String newHarga;
    public EditText etNewHarga;

    public DataPenawaranRViewAdapter(Context context, List<Penawaran> penawarans) {
        this.penawarans = penawarans;
        this.context = context;
    }

    @Override
    public ViewHolderDataPenawaran onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_data_penawaran, parent, false);
        ViewHolderDataPenawaran viewHolderDataPenawaran = new ViewHolderDataPenawaran(view);
        sessionManager = new SessionManager(context.getApplicationContext());
        return viewHolderDataPenawaran;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDataPenawaran holder, final int position) {

        Log.d(TAG, "onBindViewHolder: position " + position + " nama penawar " + penawarans.get(position).getIdPenawaran());

        holder.namaPenawar.setText("Nama Penawar " + penawarans.get(position).getNamaLengkap());

        holder.status.setText(penawarans.get(position).getStatus());
        String harga = penawarans.get(position).getHarga();
        holder.harga.setText("Harga " + harga);
        if (penawarans.get(position).getIdUsers().equals(sessionManager.getUserDetail().get("id_users"))) {
            holder.linearLayout.setBackgroundColor(R.color.colorMyListPenawaran);
        }
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                idPenawaran = penawarans.get(position).getIdPenawaran();
                idUsers = penawarans.get(position).getIdUsers();
                Log.d(TAG, "onClick: status " + holder.status.getText());

                if (holder.status.getText().equals("tunda")) {
                    newStatus = "tawar harga";
                } else {
                    newStatus = "tunda";
                }
                Log.d(TAG, "onBindViewHolder: users" + idUsers + " session users " + sessionManager.getUserDetail().get("id_users"));
                if (idUsers.equals(sessionManager.getUserDetail().get("id_users"))) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getRootView().getContext());
                    alertDialog.setTitle("rubah status");
                    alertDialog.setMessage("rubah status jadi " + newStatus);
                    alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setStatusPenawaran(idPenawaran, newStatus, holder.status);

                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: tidak");
                        }
                    });

                    alertDialog.show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "anda tidak bisa merubah status penawaran orang lain", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.harga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPenawaran = penawarans.get(position).getIdPenawaran();
                idUsers = penawarans.get(position).getIdUsers();
                Log.d(TAG, "onClick: harga");
                Log.d(TAG, "onBindViewHolder: users" + idUsers + " session users " + sessionManager.getUserDetail().get("id_users"));
                if (idUsers.equals(sessionManager.getUserDetail().get("id_users"))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    LinearLayout layout = new LinearLayout(context.getApplicationContext());
                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(parms);

                    layout.setGravity(Gravity.CLIP_VERTICAL);
                    layout.setPadding(2, 2, 2, 2);

                    etNewHarga = new EditText(context.getApplicationContext());
                    etNewHarga.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etNewHarga.setText(penawarans.get(position).getHarga());
                    etNewHarga.setTextColor(R.color.colorInfo);
                    layout.addView(etNewHarga, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                    Log.d(TAG, "onClick: newHarga " + newHarga);
                    builder.setView(layout);
                    builder.setTitle("rubah harga");
                    builder.setMessage("rubah harga penawaran?");
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newHarga = etNewHarga.getText().toString();
                            Log.d(TAG, "onClick: newHarga " + newHarga);
                            setHargaPenawaran(idPenawaran, newHarga, holder.harga);
                        }
                    }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "onClick: tidak");
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(context.getApplicationContext(), "anda tidak bisa merubah status penawaran orang lain", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!sessionManager.getUserDetail().get("hak_akses").equals("administrator")) {
            holder.setPemenang.setVisibility(View.GONE);
        }

            holder.setPemenang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setTitle("konfirmasi");
                    builder.setMessage("tambahkan sebagai pemenang ?");
                    builder.setPositiveButton("ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            setSebagaiPemenang(penawarans.get(position).getKodeBarang(), penawarans.get(position).getIdUsers());
                        }
                    }).setNegativeButton("tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
            });


    }

    private void setSebagaiPemenang(String kodeBarang, String idUsers) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.setPemenang(kodeBarang, idUsers).enqueue(new Callback<DetailBarangResponse>() {
            @Override
            public void onResponse(Call<DetailBarangResponse> call, Response<DetailBarangResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context.getApplicationContext(), KelolaBarangActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.putExtra("keterangan", "sold out");
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

    private void setStatusPenawaran(String idPenawaran, final String status, final TextView textViewStatus) {
        Log.d(TAG, "setStatusPenawaran: id_penawaran = " + idPenawaran);
        Log.d(TAG, "setStatusPenawaran: status = " + status);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(context.getApplicationContext());
        apiInterface.setStatusPenawaran(idPenawaran, status).enqueue(new Callback<DataPenawaranResponse>() {
            @Override
            public void onResponse(Call<DataPenawaranResponse> call, Response<DataPenawaranResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Log.d(TAG, "onResponse: set Status berhasil");
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        textViewStatus.setText(newStatus);
                    } else {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataPenawaranResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "gagal rubah Status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setHargaPenawaran(String idPenawaran, final String harga, final TextView textViewHarga) {
        Log.d(TAG, "setHargaPenawaran: id_penawaran = " + idPenawaran);
        Log.d(TAG, "setHargaPenawaran: harga = " + harga);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(context.getApplicationContext());
        apiInterface.setHargaPenawaran(idPenawaran, harga).enqueue(new Callback<DataPenawaranResponse>() {
            @Override
            public void onResponse(Call<DataPenawaranResponse> call, Response<DataPenawaranResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        Log.d(TAG, "onResponse: set harga berhasil");
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        textViewHarga.setText("Harga " + newHarga);
                    } else {
                        Toast.makeText(context.getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataPenawaranResponse> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "gagal rubah harga", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return penawarans.size();
    }

    class ViewHolderDataPenawaran extends RecyclerView.ViewHolder {

        private static final String TAG = "ViewHolderDataPenawaran";
        TextView namaPenawar;
        TextView status;
        TextView harga;
        LinearLayout linearLayout;
        Button setPemenang;


        public ViewHolderDataPenawaran(View itemView) {
            super(itemView);

            namaPenawar = itemView.findViewById(R.id.nama_penawar);
            status = itemView.findViewById(R.id.status);
            harga = itemView.findViewById(R.id.harga);
            linearLayout = itemView.findViewById(R.id.container_text_list_view_data_penawaran);
            setPemenang = itemView.findViewById(R.id.set_pemenang);
        }
    }


}
