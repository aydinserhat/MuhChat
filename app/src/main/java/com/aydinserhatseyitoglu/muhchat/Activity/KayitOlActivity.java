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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity {
    EditText input_kullaniciadi;
    EditText input_password;
    Button registerButon;
    FirebaseAuth auth;
    TextView hesapvar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        tanimla();
    }

    public void tanimla(){

        input_kullaniciadi = (EditText) findViewById(R.id.input_kullaniciadi);
        input_password = (EditText) findViewById(R.id.input_password);
        registerButon=(Button) findViewById(R.id.registerButon);
        auth= FirebaseAuth.getInstance();
        hesapvar = (TextView)findViewById(R.id.hesapvar);
        registerButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kullanici_adi = input_kullaniciadi.getText().toString();
                String pass = input_password.getText().toString();
                if (!kullanici_adi.equals("") && !pass.equals("")) {
                    input_kullaniciadi.setText("");
                    input_password.setText("");
                    kayitOl(kullanici_adi,pass);
                }else {
                    Toast.makeText(getApplicationContext(),"Bilgileri boş giremezsiniz",Toast.LENGTH_LONG).show();
                }

            }
        });
        hesapvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitOlActivity.this,GirisActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void kayitOl(String email, String pass){
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    firebaseDatabase= FirebaseDatabase.getInstance();
                    reference = firebaseDatabase.getReference().child("Users").child(auth.getUid());
                    Map map  = new HashMap();
                    map.put("profile_image","");
                    map.put("kullaniciismi","null");
                    map.put("okul_numarasi","null");
                    map.put("bolum","null");
                    map.put("sinif","null");
                    map.put("hakkinda","null");
                    map.put("cinsiyet","null");
                    reference.setValue(map);

                    Intent intent= new Intent(KayitOlActivity.this, AnaActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Kayıt olma hatası",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}