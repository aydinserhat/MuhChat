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

import com.aydinserhatseyitoglu.muhchat.Adapters.friend_req_adapter;
import com.aydinserhatseyitoglu.muhchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class bildirimFragment extends Fragment {
    View view;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String userId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> friend_req_key_list;
    RecyclerView friend_req_recy;
    friend_req_adapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bildirim, container, false);
        tanimla();
        istekler();
        return view;
    }

    public void tanimla() {
        auth= FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId=firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Arkadaslik_Istek");
        friend_req_key_list = new ArrayList<>();
        friend_req_recy =(RecyclerView) view.findViewById(R.id.friend_req_recy);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        friend_req_recy.setLayoutManager(layoutManager);
        adapter = new friend_req_adapter(friend_req_key_list,getActivity(),getContext());
        friend_req_recy.setAdapter(adapter);
    }
    public void istekler() {

        reference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {




                friend_req_key_list.add(snapshot.getKey());
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                friend_req_key_list.remove(snapshot.getKey());
                adapter.notifyDataSetChanged();
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