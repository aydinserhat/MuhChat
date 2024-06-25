package com.aydinserhatseyitoglu.muhchat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aydinserhatseyitoglu.muhchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GirisActivity extends AppCompatActivity {
    private EditText input_password_login, input_kullaniciadi_login;
    private Button loginButon;
    private FirebaseAuth auth;
    private TextView kayıtol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        tanimla();
    }
    public void tanimla() {
        input_kullaniciadi_login = (EditText) findViewById(R.id.input_kullaniciadi_login);
        input_password_login = (EditText) findViewById(R.id.input_password_login);
        loginButon = (Button) findViewById(R.id.loginButon);
        auth = FirebaseAuth.getInstance();
        kayıtol = (TextView) findViewById(R.id.kayıtol);
        loginButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = input_kullaniciadi_login.getText().toString();
                String pass = input_password_login.getText().toString();
                if (!email.equals("") && !pass.equals("")) {
                    sistemeGiris(email, pass);
                } else {
                    Toast.makeText(getApplicationContext(),"Boş Girilemez",Toast.LENGTH_LONG).show();
                }
            }
        });
        kayıtol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisActivity.this,KayitOlActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void sistemeGiris(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(GirisActivity.this, AnaActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Hatalı Bilgi",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}