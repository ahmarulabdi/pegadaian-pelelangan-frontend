package com.kuansing.rndmjck.pelelangan.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.adapter.RViewUsersAdapter;
import com.kuansing.rndmjck.pelelangan.model.Users;
import com.kuansing.rndmjck.pelelangan.response.DataUsersResponse;
import com.kuansing.rndmjck.pelelangan.rest.ApiClient;
import com.kuansing.rndmjck.pelelangan.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {
    private static final String TAG = "UsersFragment";
    ApiInterface apiInterface;
    SwipeRefreshLayout swipeRefreshLayout;
    RViewUsersAdapter rViewUsersAdapter;
    RecyclerView recyclerView;


    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_data_users);
        recyclerView = view.findViewById(R.id.recycler_view_data_users);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataUsers();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getDataUsers();


    }

    private void getDataUsers() {
        Log.d(TAG, "getDataUsers: tes");
        apiInterface.dataUsers().enqueue(new Callback<DataUsersResponse>() {
            @Override
            public void onResponse(Call<DataUsersResponse> call, Response<DataUsersResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        List<Users> users = response.body().getData();
                        rViewUsersAdapter = new RViewUsersAdapter(users,getContext());
                        recyclerView.setAdapter(rViewUsersAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT);
                    }else{
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onFailure(Call<DataUsersResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "koneksi gagal", Toast.LENGTH_SHORT);
            }
        });
    }
}
