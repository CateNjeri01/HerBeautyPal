package com.example.mercie.example.fragments.beautyshop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercie.example.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeautyShopEditProfileFragment extends Fragment {


    public BeautyShopEditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

}
