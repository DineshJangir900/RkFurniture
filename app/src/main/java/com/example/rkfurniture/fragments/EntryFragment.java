package com.example.rkfurniture.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.rkfurniture.utility.Constants;
import com.example.rkfurniture.databinding.FragmentEntryBinding;
import com.example.rkfurniture.interfaces.LoginValidatorListner;


public class EntryFragment extends Fragment {
    private FragmentEntryBinding mFragBindings;
    private LoginValidatorListner mCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (LoginValidatorListner) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragBindings = FragmentEntryBinding.inflate(inflater);
        return mFragBindings.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mFragBindings.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showLoginOrSignupScreen(Constants.LOGIN_ACTION);
            }
        });

        mFragBindings.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.showLoginOrSignupScreen(Constants.SIGNUP_ACTION);
            }
        });
    }

}
