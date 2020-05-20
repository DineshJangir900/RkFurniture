package com.example.rkfurniture.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rkfurniture.R;
import com.example.rkfurniture.databinding.FragmentOtpBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends Fragment {
    private FragmentOtpBinding mFragBindings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragBindings = FragmentOtpBinding.inflate(inflater);
        return mFragBindings.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
