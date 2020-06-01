package com.veridetta.kutipantokoh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.veridetta.kutipantokoh.db.DBHelper;


public class MainActivity extends AppCompatActivity {
    private static int CODE_WRITE_SETTINGS_PERMISSION;
    private AdView mAdView;
    Toolbar toolbar;
    ActionBar actionBar;
    DBHelper dbHelper;
    boolean doubleBackToExitPressedOnce = false;
    int io = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        /*
        if (TextUtils.isEmpty(getString(R.string.banner_home_footer))) {
            Toast.makeText(getApplicationContext(), "Please mention your Banner Ad ID in strings.xml", Toast.LENGTH_LONG).show();
            return;
        }
        */
        isWriteStoragePermissionGranted();
        isReadStoragePermissionGranted();
        if(isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()){
            loadFragment(new HomeFragment());
        }

        //mAdView = (AdView) findViewById(R.id.adView);
        /* AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("725F7196D12AFC68048ED82BD5C6F3A8")
                .build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        mAdView.loadAd(adRequest); */
        //actionBar = getSupportActionBar();
        //actionBar.setTitle("");
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment ;
            switch (item.getItemId()) {
                case R.id.nav_beranda:
                    //mTopToolbar.setNavigationIcon(R.drawable.search);
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_cari:
                    //mTopToolbar.setNavigationIcon(R.drawable.search);
                    fragment = new CariFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_fav:
                    //mTopToolbar.setNavigationIcon(R.drawable.search);
                    fragment = new FavFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.nav_kategori:
                    //mTopToolbar.setNavigationIcon(R.drawable.search);
                    fragment = new KategoriFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    @Override
    public void onPause() {
       // if (mAdView != null) {
        //    mAdView.pause();
        //}
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (mAdView != null) {
        //    mAdView.resume();
       // }
    }

    @Override
    public void onDestroy() {
       // if (mAdView != null) {
       //     mAdView.destroy();
       // }
        super.onDestroy();
    }
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("MainActivity","Permission is granted1");
                return true;
            } else {

                Log.v("MainActivity","Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("MainActivity","Permission is granted1");
            return true;
        }
    }
    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("MainActivity","Permission is granted2");
                return true;
            } else {

                Log.v("MainActivity","Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("MainActivity","Permission is granted2");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("MainActivity", "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("MainActivity","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    loadFragment(new HomeFragment());
                    io++;
                }else{

                }
                break;

            case 3:
                Log.d("MainActivity", "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("MainActivity","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    loadFragment(new HomeFragment());
                }else{

                }
                break;
        }
    }
    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.CODE_WRITE_SETTINGS_PERMISSION && Settings.System.canWrite(this)){
            Log.d("\"MainActivity\"", "MainActivity.CODE_WRITE_SETTINGS_PERMISSION success");
            //do your code
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
