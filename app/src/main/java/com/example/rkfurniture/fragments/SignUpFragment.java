package com.example.rkfurniture.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rkfurniture.R;
import com.example.rkfurniture.databinding.FragmentLoginBinding;
import com.example.rkfurniture.databinding.FragmentSignUpBinding;
import com.example.rkfurniture.interfaces.LoginValidatorListner;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    private FragmentSignUpBinding mFragBindings;
    private LoginValidatorListner mCallbacks;
    private String userName,userPhone,userEmail;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (LoginValidatorListner) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragBindings = FragmentSignUpBinding.inflate(inflater);
        return mFragBindings.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        userName = mFragBindings.nameET.getText().toString();
        userEmail = mFragBindings.nameET.getText().toString();
        userPhone = mFragBindings.phoneNumberET.getText().toString();

        mCallbacks.signUpNewUser(userEmail, userPhone, userPhone);

        mFragBindings.loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openLoginFragScreen();
            }
        });

    }
}
