package com.kuansing.rndmjck.pelelangan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuansing.rndmjck.pelelangan.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TataCaraFragment extends Fragment {


    public TataCaraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tata_cara, container, false);

    }

}