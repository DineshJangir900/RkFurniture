package com.example.rkfurniture.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rkfurniture.databinding.FragmentLoginBinding;
import com.example.rkfurniture.interfaces.LoginValidatorListner;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding mFragBindings;
    private final String TAG = "LoginFragment//";
    private LoginValidatorListner mCallBack;
    private String mUserPhone, mUserCountryCode ,phoneNumber;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallBack = (LoginValidatorListner) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragBindings = FragmentLoginBinding.inflate(inflater);
        return mFragBindings.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mFragBindings.generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForLoginCredentials();
            }
        });

        mFragBindings.signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.openSignUpFragScreen();
            }
        });


    }
    private void checkForLoginCredentials(){
        mUserPhone = mFragBindings.phoneNumberTV.getText().toString();
        mUserCountryCode = mFragBindings.countryCodeTV.getText().toString();

        phoneNumber = "+" + mUserCountryCode + mUserPhone;

        if(TextUtils.isEmpty(mUserCountryCode) ){
            Toast.makeText(getContext(), "Please enter your Country Code... ", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(mUserPhone)){
            Toast.makeText(getContext(), "Please enter your phone Number...", Toast.LENGTH_SHORT).show();
        }else{
          mCallBack.checkUserCredentials(phoneNumber);
        }
    }
}
