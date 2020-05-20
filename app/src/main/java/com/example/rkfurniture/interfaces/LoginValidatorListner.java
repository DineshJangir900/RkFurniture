package com.example.rkfurniture.interfaces;

public interface LoginValidatorListner {
    void checkUserCredentials(String phone);
    void showLoginOrSignupScreen(String type);
    void signUpNewUser(String email, String phone, String name);
    void fetchValuesFromFirebase();

    void openLoginFragScreen();
    void openSignUpFragScreen();
}
