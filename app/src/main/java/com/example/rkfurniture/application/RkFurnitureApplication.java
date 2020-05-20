package com.example.rkfurniture.application;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RkFurnitureApplication extends Application {
    private static RkFurnitureApplication mInstance;
    private static StorageReference mStorageRef;
    private static DatabaseReference rootRef;

    @Override
    public void onCreate() {
        super.onCreate();
        try{
            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
                    e.printStackTrace();
                    Log.e(""+e.getLocalizedMessage(), "\n" + e.getMessage());
                    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
                    FirebaseCrashlytics.getInstance().log(e.getMessage());
                    System.out.print(e);
                }
            });

        }catch (Exception ae){
            ae.printStackTrace();
        }

        mInstance = this;
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        rootRef = firebaseDatabase.getReference();
        mStorageRef = firebaseStorage.getReference();
    }

    public static synchronized RkFurnitureApplication getInstance() {
        return mInstance;
    }

    public static synchronized StorageReference getStorageReferenceInstance() {
        return mStorageRef;
    }

    public static synchronized DatabaseReference getFirebaseDBInstance() {
        return rootRef;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
