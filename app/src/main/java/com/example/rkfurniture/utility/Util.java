package com.example.rkfurniture.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rkfurniture.R;


public class Util {


    public static SharedPreferences getSharePrefrences(Context context){
        return context.getApplicationContext().getSharedPreferences(
                context.getString(R.string.PREFERENCE_FILE_KEY), Context.MODE_PRIVATE);
    }



}
