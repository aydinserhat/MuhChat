package com.aydinserhatseyitoglu.muhchat.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aydinserhatseyitoglu.muhchat.Models.Users;
import com.aydinserhatseyitoglu.muhchat.R;
import com.aydinserhatseyitoglu.muhchat.Utils.ChangeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class KullaniciProfilFragment extends Fragment {

    String imageUrl;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    View view;
    EditText kullaniciismi, okul_numarasi, bolum, sinif, hakkinda, cinsiyet;
    CircleImageView profile_image;
    Button bilgiguncelle;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kullanici_profil, container, false);
        tanimla();
        bilgilerGetir();
        return view;
    }

    public void tanimla() {
        kullaniciismi = (EditText) view.findViewById(R.id.kullaniciismi);
        okul_numarasi = (EditText) view.findViewById(R.id.okul_numarasi);
        bolum = (EditText) view.findViewById(R.id.bolum);
        sinif = (EditText) view.findViewById(R.id.sinif);
        hakkinda = (EditText) view.findViewById(R.id.hakkinda);
        cinsiyet = (EditText) view.findViewById(R.id.cinsiyet);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        bilgiguncelle = (Button) view.findViewById(R.id.bilgiguncelle);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        bilgiguncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriAc();
            }
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users").child(user.getUid());
    }

    private void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            Uri filepath = data.getData();
            StorageReference ref = storageReference.child("photos").child("serhat.jpg");
            ref.putFile(filepath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                Toast.makeText(getContext(), "Güncelleme Başarılı", Toast.LENGTH_LONG).show();
                                guncelle();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Güncelleme Başarısız", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void bilgilerGetir() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users kl = snapshot.getValue(Users.class);
                kullaniciismi.setText(kl.getKullaniciismi());
                okul_numarasi.setText(kl.getOkul_numarasi());
                bolum.setText(kl.getBolum());
                sinif.setText(kl.getSinif());
                hakkinda.setText(kl.getHakkinda());
                cinsiyet.setText(kl.getCinsiyet());
                imageUrl = kl.getProfile_image();
                if (kl.getProfile_image() != null) {
                    Picasso.get().invalidate(kl.getProfile_image());
                    Picasso.get().load(kl.getProfile_image()).into(profile_image);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void guncelle() {
        String isim = kullaniciismi.getText().toString();
        String okul_no = okul_numarasi.getText().toString();
        String bolumu = bolum.getText().toString();
        String sinifi = sinif.getText().toString();
        String hakkimda = hakkinda.getText().toString();
        String cinsiyeti = cinsiyet.getText().toString();

        reference = database.getReference().child("Users").child(auth.getUid());
        Map<String, Object> map = new HashMap<>();

        map.put("kullaniciismi", isim);
        map.put("okul_numarasi", okul_no);
        map.put("bolum", bolumu);
        map.put("sinif", sinifi);
        map.put("hakkinda", hakkimda);
        map.put("cinsiyet", cinsiyeti);


        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ChangeFragment fragment = new ChangeFragment(getContext());
                    fragment.change(new KullaniciProfilFragment());
                    Toast.makeText(getContext(), "Güncelleme Başarılı", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Güncelleme Başarısız", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
