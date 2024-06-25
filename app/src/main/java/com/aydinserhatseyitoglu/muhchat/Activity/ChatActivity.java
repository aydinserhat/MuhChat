package com.aydinserhatseyitoglu.muhchat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aydinserhatseyitoglu.muhchat.Adapters.MessageAdapter;
import com.aydinserhatseyitoglu.muhchat.Fragment.AnasayfaFragment;
import com.aydinserhatseyitoglu.muhchat.Fragment.UserProfile;
import com.aydinserhatseyitoglu.muhchat.Models.MessageModel;
import com.aydinserhatseyitoglu.muhchat.R;
import com.aydinserhatseyitoglu.muhchat.Utils.getDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    TextView chat_username_textview;
    ImageView geridonbuton;

    EditText messageTextEdittext;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FloatingActionButton sendMessageButton;
    List<MessageModel> messageModelList;
    RecyclerView chat_recy_view;
    MessageAdapter messageAdapter;
    List<String> keylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tanimla();
        action();
        loadMessage();
    }

    public String getUserName() {
        Bundle bundle = getIntent().getExtras();
        String userName = bundle.getString("userName");
        return userName;
    }

    public String getId() {
        String id = getIntent().getExtras().getString("id").toString();
        return id;
    }
    public void tanimla() {
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        geridonbuton = (ImageView)findViewById(R.id.geridonbuton);

        chat_username_textview = (TextView)findViewById(R.id.chat_username_textview);
        chat_username_textview.setText(getUserName());
        sendMessageButton = (FloatingActionButton)findViewById(R.id.sendMessageButton);
        messageTextEdittext = (EditText) findViewById(R.id.messageTextEdittext);
        messageModelList = new ArrayList<>();
        keylist = new ArrayList<>();
        chat_recy_view = (RecyclerView) findViewById(R.id.chat_recy_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ChatActivity.this,1);
        chat_recy_view.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(keylist,ChatActivity.this,ChatActivity.this,messageModelList);
        chat_recy_view.setAdapter(messageAdapter);

    }

    public void action() {


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = messageTextEdittext.getText().toString();
                messageTextEdittext.setText("");
                sendMessage(firebaseUser.getUid(),getId(),"text", getDate.getDate(),false,message);
            }
        });
        geridonbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ChatActivity.this, AnaActivity.class);
                startActivity(intent3);
            }
        });

    }
    public void sendMessage(String userId, String otherId,String textType,String date,Boolean seen,String messageText) {

        String mesajId = reference.child("Mesajlar").child(userId).child(otherId).push().getKey();
        Map messageMap = new HashMap();
        messageMap.put("type",textType);
        messageMap.put("seen",seen);
        messageMap.put("time",date);
        messageMap.put("text",messageText);
        messageMap.put("from",userId);

        reference.child("Mesajlar").child(userId).child(otherId).child(mesajId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Mesajlar").child(otherId).child(userId).child(mesajId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });



    }
    public void loadMessage() {
        reference.child("Mesajlar").child(firebaseUser.getUid()).child(getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel= snapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                keylist.add(snapshot.getKey());
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