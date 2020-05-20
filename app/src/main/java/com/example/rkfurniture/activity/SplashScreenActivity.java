package com.example.rkfurniture.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.rkfurniture.R;
import com.example.rkfurniture.application.RkFurnitureApplication;
import com.example.rkfurniture.databinding.ActivitySplashScreenBinding;
import com.example.rkfurniture.fragments.EntryFragment;
import com.example.rkfurniture.fragments.LoginFragment;
import com.example.rkfurniture.fragments.OtpFragment;
import com.example.rkfurniture.fragments.SignUpFragment;
import com.example.rkfurniture.fragments.SplashFragment;
import com.example.rkfurniture.interfaces.LoginValidatorListner;
import com.example.rkfurniture.models.UserModel;
import com.example.rkfurniture.utility.Constants;
import com.example.rkfurniture.utility.CustomProgressDialog;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.rkfurniture.utility.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SplashScreenActivity extends AppCompatActivity implements LoginValidatorListner {
    private ActivitySplashScreenBinding mBindings;
    private CustomProgressDialog mDialog;
    private FirebaseAuth mAuth;
    private SharedPreferences mPref;
    private Editor mEditor;
    private DatabaseReference demoRef;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    String mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting Up View Binding;
        mBindings = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setTheme(R.style.fullScreenDesign);
        setContentView(mBindings.getRoot());
        mDialog = new CustomProgressDialog(SplashScreenActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mPref = Util.getSharePrefrences(this);
        mEditor = mPref.edit();
        mPref = Util.getSharePrefrences(SplashScreenActivity.this);
        mEditor = mPref.edit();

        loadFragment(new SplashFragment());

    }

    //method to Load any Fragment
    private void loadFragment(Fragment fragment){
        FragmentTransaction transactionManger = getSupportFragmentManager().beginTransaction();
        transactionManger.replace(R.id.frameContainer,fragment).commitAllowingStateLoss();
    }

    //Implemented Method to check for User Credentials
    @Override
    public void checkUserCredentials(String phone) {
        sendVerificationCode(phone);


    }
    // Creating new user
    @Override
    public void signUpNewUser(String email, String phone, String name) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        demoRef = RkFurnitureApplication.getFirebaseDBInstance().child("usersData").child(phone);
        demoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel = new UserModel();
                userModel.setUserName(name);
                userModel.setUserEmail(email);
                userModel.setUserPhone(phone);
                userModel.setUserUid(userId);
                userModel.setUserProfileImageURL("image");
                demoRef.setValue(userModel);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("firebaseError",""+databaseError);
            }
        });

    }


    @Override
    public void showLoginOrSignupScreen(String type) {
        if(type.equals(Constants.LOGIN_ACTION)){
            loadFragment(new LoginFragment());
        }else {
            loadFragment(new SignUpFragment());
        }

    }

    @Override
    public void fetchValuesFromFirebase() {
        //Function to fetch values from firebase when having Splash Screen and also exit this is no internet connection is there
        //After completing the fetching just load the new fragment login fragment/check for login
        checkAleadyLoggedIn();
    }

    @Override
    public void openLoginFragScreen() {
        loadFragment(new LoginFragment());
    }

    @Override
    public void openSignUpFragScreen() {
        loadFragment(new SignUpFragment());
    }

    private void checkAleadyLoggedIn() {
        boolean isLogin = mPref.getBoolean(Constants.LOGIN_STATUS,false);

        if(!isLogin) {
            loadFragment(new EntryFragment());
        }
        else{
            startActivity(new Intent(SplashScreenActivity.this, HomeScreenActivity.class));
            finish();
        }
    }

    //closing keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        return super.dispatchTouchEvent(ev);
    }

    //phone Authentication
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                loadFragment(new OtpFragment());
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(SplashScreenActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SplashScreenActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(SplashScreenActivity.this, HomeScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            mEditor.putBoolean(Constants.LOGIN_STATUS,true);
                            mEditor.commit();
                            mDialog.close();
                            Toast.makeText(SplashScreenActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
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

}
