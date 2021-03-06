package com.example.loginfirebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PreferenceHelper {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "User";
    public PreferenceHelper(Context context)
    {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void saveArrayList(ArrayList<User> list, String key)  //contactlist
    {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
        Log.i("My pref data = ","saved");
    }

    public ArrayList<User> getArrayList(String key) //contactlist
    {
        Gson gson = new Gson();
        String json = pref.getString(key, null);
        Type type = new TypeToken<ArrayList<User>>(){}.getType();
        Log.i("My pref getArrayList = ","called");
        return gson.fromJson(json, type);
    }

    public boolean deleteUserById(int id)
    {
        boolean n = editor.remove(id+"").commit();
        return n;
    }

    public boolean deleteUser()
    {
        boolean n = editor.clear().commit();
        return n;
    }
}
