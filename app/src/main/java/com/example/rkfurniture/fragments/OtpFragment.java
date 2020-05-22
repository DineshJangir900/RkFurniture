package com.example.rkfurniture.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rkfurniture.R;
import com.example.rkfurniture.activity.HomeScreenActivity;
import com.example.rkfurniture.activity.SplashScreenActivity;
import com.example.rkfurniture.databinding.FragmentOtpBinding;
import com.example.rkfurniture.interfaces.OtpValidatorListner;
import com.example.rkfurniture.utility.Constants;
import com.example.rkfurniture.utility.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends Fragment implements OtpValidatorListner {
    private FragmentOtpBinding mFragBindings;
    private FirebaseAuth mAuth;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragBindings = FragmentOtpBinding.inflate(inflater);
        mAuth = FirebaseAuth.getInstance();
        mPref = Util.getSharePrefrences(getContext());
        mEditor = mPref.edit();
        mPref = Util.getSharePrefrences(getContext());
        mEditor = mPref.edit();



        return mFragBindings.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        mFragBindings.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private void verifyVerificationCode(String mVerificationId,String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            mEditor.putBoolean(Constants.LOGIN_STATUS,true);
                            mEditor.commit();
                            Toast.makeText(getActivity(), "Login Successful.", Toast.LENGTH_SHORT).show();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    @Override
    public void otpVerification(String verficationID, String code) {
        mFragBindings.otpET.setText(code);
        Log.e("otp",""+verficationID+" "+code);
        verifyVerificationCode(verficationID,code);
    }
}
