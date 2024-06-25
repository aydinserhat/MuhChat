package com.aydinserhatseyitoglu.muhchat.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aydinserhatseyitoglu.muhchat.Activity.ChatActivity;
import com.aydinserhatseyitoglu.muhchat.Models.Users;
import com.aydinserhatseyitoglu.muhchat.R;
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


public class UserProfile extends Fragment {


    TextView userProfileCinsiyet,userProfileHakkında,userProfileSınıf,userProfileOkulNumarası,userProfileBolum,userProfileNameText,userProfileMesageİstegi,userProfileArkadasİstegi;
   ImageView userProfileAddFriendİcon,userProfileMesajicon;
    View view;
    String otherId,userId;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference, reference_Arkadaslik;
    String kontrol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        tanimla();
        action();
        return view;
    }

    public void tanimla() {
        kontrol="";
        otherId = getArguments().getString("userid");

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        reference_Arkadaslik = firebaseDatabase.getReference().child("Arkadaslik_Istek");
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userId = user.getUid();
        userProfileNameText = (TextView) view.findViewById(R.id.userProfileNameText);
        userProfileBolum = (TextView) view.findViewById(R.id.userProfileBolum);
        userProfileOkulNumarası = (TextView) view.findViewById(R.id.userProfileOkulNumarası);
        userProfileSınıf = (TextView) view.findViewById(R.id.userProfileSınıf);
        userProfileHakkında = (TextView) view.findViewById(R.id.userProfileHakkında);
        userProfileCinsiyet = (TextView) view.findViewById(R.id.userProfileCinsiyet);

        userProfileAddFriendİcon = (ImageView) view.findViewById(R.id.userProfileAddFriendİcon);
        userProfileMesajicon = (ImageView) view.findViewById(R.id.userProfileMesajicon);
        reference_Arkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)) {
                    kontrol = snapshot.child(userId).child("tip").getValue().toString();
                    userProfileAddFriendİcon.setImageResource(R.drawable.accepted);
                } else {
                    userProfileAddFriendİcon.setImageResource(R.drawable.addfriend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void action() {
            reference.child("Users").child(otherId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users kl = snapshot.getValue(Users.class);
                    userProfileNameText.setText(kl.getKullaniciismi());
                    userProfileBolum.setText("Bölüm:" +kl.getBolum());
                    userProfileOkulNumarası.setText("Okul Numarası: "+kl.getOkul_numarasi());
                    userProfileSınıf.setText("Sınıf:"+kl.getSinif());
                    userProfileHakkında.setText("Hakkında: "+kl.getHakkinda());
                    userProfileCinsiyet.setText("Cinsiyet: "+kl.getCinsiyet());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        userProfileAddFriendİcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kontrol != null && !kontrol.equals("")) {
                    arkadasIptalEt(otherId, userId);
                } else {
                    arkadasEkle(otherId, userId);
                }

            }
        });


    }

    public void arkadasEkle(final String otherId,final String userId) {
        reference_Arkadaslik.child(userId).child(otherId).child("tip").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    reference_Arkadaslik.child(otherId).child(userId).child("tip").setValue("aldi").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                kontrol="aldi";
                                Toast.makeText(getContext(),"İstek Başarılı",Toast.LENGTH_LONG).show();
                                userProfileAddFriendİcon.setImageResource(R.drawable.accepted);
                            } else {
                                Toast.makeText(getContext(),"İstek Problemi",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(),"İstek Problemi",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void arkadasIptalEt(String otherId,String userId) {
        reference_Arkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference_Arkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        kontrol="" ;
                        userProfileAddFriendİcon.setImageResource(R.drawable.addfriend);
                        Toast.makeText(getContext(),"İstek İptal edildi",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}