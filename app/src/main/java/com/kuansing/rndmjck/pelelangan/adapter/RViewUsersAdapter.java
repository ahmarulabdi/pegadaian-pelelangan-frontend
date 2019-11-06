package com.kuansing.rndmjck.pelelangan.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.config.ServerConfig;
import com.kuansing.rndmjck.pelelangan.model.Users;

import java.util.List;

/**
 * Created by rndmjck on 17/08/18.
 */

public class RViewUsersAdapter extends RecyclerView.Adapter<RViewUsersAdapter.ViewHolderUsers>{

    private List<Users>users;
    private Context context;

    public RViewUsersAdapter(List<Users> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_data_users,parent,false);
        ViewHolderUsers viewHolderUsers = new ViewHolderUsers(view);

        return viewHolderUsers;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUsers holder, int position) {
        String imageLocation = ServerConfig.IMAGE_FOLDER_USERS + users.get(position).getFotoKtp();
        Log.d(String.valueOf(context.getApplicationContext()), "onBindViewHolder: "+imageLocation);
        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(imageLocation)
                .into(holder.imageViewFotoKTP);

        holder.textViewNamaLengkap.setText(" : "+users.get(position).getNamaLengkap());
        holder.textViewNomorTelepon.setText(" : "+users.get(position).getNomorTelepon());
        holder.textViewAlamat.setText(" : "+users.get(position).getAlamat());
        holder.textViewNomorKTP.setText(" : "+users.get(position).getNomorKtp());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolderUsers extends RecyclerView.ViewHolder {
        ImageView imageViewFotoKTP;
        TextView textViewNamaLengkap;
        TextView textViewNomorTelepon;
        TextView textViewAlamat;
        TextView textViewNomorKTP;

        public ViewHolderUsers(View itemView) {
            super(itemView);
            textViewNamaLengkap = itemView.findViewById(R.id.text_view_nama_lengkap_data_users);
            textViewNomorTelepon = itemView.findViewById(R.id.text_view_nomor_telepon_data_users);
            textViewAlamat = itemView.findViewById(R.id.text_view_alamat_data_users);
            textViewNomorKTP = itemView.findViewById(R.id.text_view_nomor_ktp_data_users);
            imageViewFotoKTP = itemView.findViewById(R.id.image_view_foto_ktp_data_users);

        }
    }
}
