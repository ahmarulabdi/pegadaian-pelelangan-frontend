package com.kuansing.rndmjck.pelelangan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kuansing.rndmjck.pelelangan.R;
import com.kuansing.rndmjck.pelelangan.Utility.SessionManager;
import com.kuansing.rndmjck.pelelangan.config.ServerConfig;
import com.kuansing.rndmjck.pelelangan.fragment.HubungiKamiFragment;
import com.kuansing.rndmjck.pelelangan.fragment.JadwalLelangFragment;
import com.kuansing.rndmjck.pelelangan.fragment.MenuDataBarangFragment;
import com.kuansing.rndmjck.pelelangan.fragment.MenuHomeFragment;
import com.kuansing.rndmjck.pelelangan.fragment.TataCaraFragment;
import com.kuansing.rndmjck.pelelangan.fragment.TentangKamiFragment;
import com.kuansing.rndmjck.pelelangan.fragment.UsersFragment;

public class ActivityNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ActivityNavigation";
    SessionManager sessionManager;
    RelativeLayout LayoutContainer;
    MenuHomeFragment menuHomeFragment;
    FragmentManager fragmentManager;
    JadwalLelangFragment jadwalLelangFragment;
    TataCaraFragment tataCaraFragment;
    TentangKamiFragment tentangKamiFragment;
    HubungiKamiFragment hubungiKamiFragment;
    UsersFragment usersFragment;


    ImageView fotoProfile;
    TextView namaProfile;
    TextView hakAkses;
    private MenuDataBarangFragment menuDataBarangFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        if (sessionManager.getUserDetail().get("hak_akses").equals("penawar")) {
            navigationView.getMenu().findItem(R.id.nav_user).setVisible(false);
        }

        View header = navigationView.getHeaderView(0);

        Log.d(TAG, "onCreate: fotopath " + ServerConfig.IMAGE_FOLDER_USERS + sessionManager.getUserDetail().get("foto_ktp"));

        fotoProfile = header.findViewById(R.id.nav_header_foto_profile);
        namaProfile = header.findViewById(R.id.nav_header_nama_profile);
        hakAkses = header.findViewById(R.id.nav_header_hak_akses);
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(ServerConfig.IMAGE_FOLDER_USERS + sessionManager.getUserDetail().get("foto_ktp"))
                .into(fotoProfile);
        namaProfile.setText(sessionManager.getUserDetail().get("nama_lengkap"));
        hakAkses.setText(sessionManager.getUserDetail().get("hak_akses"));


        LayoutContainer = findViewById(R.id.layout_container);

        menuHomeFragment = new MenuHomeFragment();
        jadwalLelangFragment = new JadwalLelangFragment();
        fragmentManager = getSupportFragmentManager();

        if (getIntent().hasExtra("navigate")) {
            if (getIntent().getStringExtra("navigate").equals("jadwal lelang")) {

                getSupportActionBar().setTitle("Menu Jadwal Lelang");
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.layout_container, jadwalLelangFragment)
                        .commit();
            }
        } else {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.layout_container, menuHomeFragment)
                    .commit();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            sessionManager.logoutUser();
            Intent intent = new Intent(ActivityNavigation.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle("Home");
            menuHomeFragment = new MenuHomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.layout_container, menuHomeFragment).commit();
        } else if (id == R.id.nav_user) {
            getSupportActionBar().setTitle("Data Pengguna");
            usersFragment = new UsersFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.layout_container, usersFragment)
                    .commit();

        } else if (id == R.id.nav_lelang) {
            getSupportActionBar().setTitle("Menu Lelang");
            menuDataBarangFragment = new MenuDataBarangFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.layout_container, new MenuDataBarangFragment()).commit();
        } else if (id == R.id.nav_jadwal_lelang)

        {
            getSupportActionBar().setTitle("Menu Jadwal Lelang");
            jadwalLelangFragment = new JadwalLelangFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.layout_container, jadwalLelangFragment)
                    .commit();
        } else if (id == R.id.nav_tata_cara_lelang)

        {
            getSupportActionBar().setTitle("Tata Cara Lelang");
            tataCaraFragment = new TataCaraFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.layout_container, tataCaraFragment)
                    .commit();

        } else if (id == R.id.nav_tentang_kami)

        {
            getSupportActionBar().setTitle("Tentang Kami");
            tentangKamiFragment = new TentangKamiFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.layout_container, tentangKamiFragment)
                    .commit();

        } else if (id == R.id.nav_hubungi_kami)

        {
            getSupportActionBar().setTitle("Hubungi Kami");
            hubungiKamiFragment = new HubungiKamiFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.layout_container, hubungiKamiFragment)
                    .commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
