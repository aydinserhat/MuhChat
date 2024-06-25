package com.aydinserhatseyitoglu.muhchat.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aydinserhatseyitoglu.muhchat.Fragment.UserProfile;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class friend_req_adapter extends RecyclerView.Adapter<friend_req_adapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    String userId;

    public friend_req_adapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userId = firebaseUser.getUid();
    }

    //layout tanımla
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_req_layout,parent,false);
        return new ViewHolder(view);
    }
    // view ayarı
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
       //holder.usernameTextView.setText(userKeysList.get(position).toString());
        reference.child("Users").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users kl = snapshot.getValue(Users.class);
                holder.friend_req_text.setText(kl.getKullaniciismi().toString());
                holder.friend_req_ekle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kabulEt(userId,userKeysList.get(position));
                    }
                });
                holder.friend_req_red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redEt(userId,userKeysList.get(position));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView friend_req_text;
        CircleImageView friend_req_image;
        Button friend_req_ekle,friend_req_red;

        ViewHolder(View itemView) {
            super(itemView);
            friend_req_text=(TextView) itemView.findViewById(R.id.friend_req_text);
            friend_req_image =(CircleImageView) itemView.findViewById(R.id.friend_req_image);
            friend_req_ekle =(Button) itemView.findViewById(R.id.friend_req_ekle);
            friend_req_red =(Button) itemView.findViewById(R.id.friend_req_red);

        }
    }

    public void kabulEt(String userId,String otherId) {


        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        final String reportDate = df.format(today);


        reference.child("Arkadaslar").child(userId).child(otherId).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Arkadaslar").child(otherId).child(userId).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context,"İstek Kabul Edildi.",Toast.LENGTH_LONG).show();
                            reference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    reference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
    }
    public void redEt(String userId,String otherId) {
        reference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"İstek Reddedildi",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });
    }

}
