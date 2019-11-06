package com.kuansing.rndmjck.pelelangan.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kuansing.rndmjck.pelelangan.model.Users;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Context _context;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String ID_USERS = "id_users";
    public static final String NOMOR_TELEPON = "nomor_telepon";
    public static final String NAMA_LENGKAP = "nama_lengkap";
    public static final String FOTO_KTP = "foto_ktp";
    public static final String HAK_AKSES = "hak_akses";

    public Context get_context() {
        return _context;
    }

    //constuctor
    public SessionManager(Context context) {
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(Users users) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(ID_USERS, users.getIdUsers());
        editor.putString(NOMOR_TELEPON, users.getNomorTelepon());
        editor.putString(NAMA_LENGKAP, users.getNamaLengkap());
        editor.putString(FOTO_KTP, users.getFotoKtp());
        editor.putString(HAK_AKSES, users.getHakAkses());
        editor.commit();
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(ID_USERS, sharedPreferences.getString(ID_USERS, null));
        user.put(NOMOR_TELEPON, sharedPreferences.getString(NOMOR_TELEPON, null));
        user.put(FOTO_KTP, sharedPreferences.getString(FOTO_KTP, null));
        user.put(NAMA_LENGKAP, sharedPreferences.getString(NAMA_LENGKAP, null));
        user.put(FOTO_KTP, sharedPreferences.getString(FOTO_KTP, null));
        user.put(HAK_AKSES, sharedPreferences.getString(HAK_AKSES, null));
        return user;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}
