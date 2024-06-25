package com.aydinserhatseyitoglu.muhchat.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aydinserhatseyitoglu.muhchat.Adapters.UserAdapter;
import com.aydinserhatseyitoglu.muhchat.Models.Users;
import com.aydinserhatseyitoglu.muhchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AnasayfaFragment extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> userKeysList;
    RecyclerView userListRecylerView;
    View view;
    UserAdapter userAdapter;
    FirebaseUser user;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ana fonksiyon
        view = inflater.inflate(R.layout.fragment_anasayfa, container, false);
        tanimla();
        kullaniciGetir();
        return view;
    }

    //Kullanılacak değişkenlerin tannımlanması
    public void tanimla() {
        userKeysList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        userListRecylerView= view.findViewById(R.id.userListRecylerView);
        RecyclerView.LayoutManager mng = new GridLayoutManager(getContext(),2);
        userListRecylerView.setLayoutManager(mng);
        userAdapter = new UserAdapter(userKeysList,getActivity(),getContext());
        userListRecylerView.setAdapter(userAdapter);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }


    public void kullaniciGetir() {
        reference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                reference.child("Users").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users kl = snapshot.getValue(Users.class);
                        //Kullanıcı adı girmemiş kullanıcıları listelememe ve kendini görmeme
                        if (!kl.getKullaniciismi().equals("null")&&!snapshot.getKey().equals(user.getUid())) {
                            userKeysList.add(snapshot.getKey());
                            userAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}