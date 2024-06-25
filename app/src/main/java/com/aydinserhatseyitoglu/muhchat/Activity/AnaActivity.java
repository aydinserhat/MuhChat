package com.aydinserhatseyitoglu.muhchat.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.aydinserhatseyitoglu.muhchat.Fragment.AnasayfaFragment;
import com.aydinserhatseyitoglu.muhchat.Fragment.KullaniciProfilFragment;
import com.aydinserhatseyitoglu.muhchat.Fragment.bildirimFragment;
import com.aydinserhatseyitoglu.muhchat.Utils.ChangeFragment;
import com.aydinserhatseyitoglu.muhchat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AnaActivity extends AppCompatActivity {

    private ChangeFragment changeFragment;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @SuppressLint("NonConstantResourceId")
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = (item) -> {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                changeFragment.change(new AnasayfaFragment());
                return true;
            case R.id.navigation_dashboard:
                changeFragment.change(new bildirimFragment());
                return true;
            case R.id.navigation_profile:
                changeFragment.change(new KullaniciProfilFragment());
                return true;

            case R.id.Exit:
                cik();
                return true;
        }
        return false;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tanimla();
        kontrol();

        changeFragment = new ChangeFragment(AnaActivity.this);
        changeFragment.change(new AnasayfaFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }
    public void cik(){
        auth.signOut();
        Intent intent= new Intent(AnaActivity.this, GirisActivity.class);
        startActivities(new Intent[]{intent});
        finish();
    }
    public void tanimla(){

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
    public void kontrol() {
        if (user == null) {
            Intent intent = new Intent(AnaActivity.this, GirisActivity.class);
            startActivity(intent);
            finish();
        }
    }

}