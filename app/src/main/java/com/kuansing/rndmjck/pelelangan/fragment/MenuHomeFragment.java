package com.kuansing.rndmjck.pelelangan.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.activity.administrator.EditUsersActivity;
import com.kuansing.rndmjck.pelelangan.config.ServerConfig;
import com.kuansing.rndmjck.pelelangan.model.Users;
import com.kuansing.rndmjck.pelelangan.response.UsersResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuHomeFragment extends Fragment {



    @BindView(R.id.home_profile_image)
    ImageView profileImage;
    @BindView(R.id.home_profile_name)
    TextView profileName;
    @BindView(R.id.button_edit_users)
    Button buttonEditUsers;
    SessionManager sessionManager;
    ApiInterface apiInterface;


    public MenuHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_home, container, false);

        ButterKnife.bind(this,view);
        sessionManager = new SessionManager(view.getContext());

        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonEditUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditUsersActivity.class);
                startActivity(intent);
            }
        });

        getProfilData();
    }

    private void getProfilData() {
        apiInterface.getUsers(sessionManager.getUserDetail().get("id_users")).enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()){
                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        Users users = response.body().getData();


                        String imageLocation = ServerConfig.IMAGE_FOLDER_USERS + users.getFotoKtp();
                        Log.d(String.valueOf(getActivity().getApplicationContext()), "onResponse: imageLocation "+imageLocation);
                        Glide.with(getActivity().getApplicationContext())
                                .asBitmap()
                                .load(imageLocation)
                                .into(profileImage);
                        profileName.setText(users.getNamaLengkap());
                    }else {
                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {
                Toast.makeText(getActivity(),"koneksi gagal",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
