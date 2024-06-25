package com.aydinserhatseyitoglu.muhchat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aydinserhatseyitoglu.muhchat.Fragment.KullaniciProfilFragment;
import com.aydinserhatseyitoglu.muhchat.Fragment.UserProfile;
import com.aydinserhatseyitoglu.muhchat.Models.Users;
import com.aydinserhatseyitoglu.muhchat.R;
import com.aydinserhatseyitoglu.muhchat.Utils.ChangeFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ObjectInputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public UserAdapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
    }

    //layout tanımla
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userlayout,parent,false);
        return new ViewHolder(view);
    }
    // view ayarı
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
       // holder.usernameTextView.setText(userKeysList.get(position).toString());
        reference.child("Users").child(userKeysList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users kl = snapshot.getValue(Users.class);
                holder.usernameTextView.setText(kl.getKullaniciismi());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.userAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment fragment = new ChangeFragment(context);
                fragment.changeWithParameter(new UserProfile(), userKeysList.get(position));
            }
        });
    }
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        CircleImageView userimage;
        LinearLayout userAnaLayout;
        ViewHolder(View itemView) {
            super(itemView);
            usernameTextView=(TextView) itemView.findViewById(R.id.usernameTextView);
            userimage =(CircleImageView) itemView.findViewById(R.id.userimage);
            userAnaLayout =(LinearLayout) itemView.findViewById(R.id.userAnaLayout);
        }
    }

}
