package com.example.prohieu.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prohieu.NavigationDrawer;
import com.example.prohieu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> memberProfile;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private FirebaseDatabase data;
    private DatabaseReference dataRef;
    private Button editBtn, backBtn;
    private ArrayAdapter arrayAdapter;
    private Member member;
    private TextView userName;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ImageView ImgUserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        editBtn = (Button) findViewById(R.id.editBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        listView = (ListView) findViewById(R.id.list_view);
        userName = (TextView) findViewById(R.id.userName);
        memberProfile = new ArrayList<>();
        member = new Member();
        ImgUserPhoto = findViewById(R.id.regUserPhoto1);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        data = FirebaseDatabase.getInstance();
        dataRef = data.getReference("Member").child(user.getDisplayName());
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        userName.setText(user.getDisplayName());
        memberProfile.add("Age: ");
        memberProfile.add("Gender: ");
        memberProfile.add("Height: ");
        memberProfile.add("Weight: ");

        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, memberProfile);
        listView.setAdapter(arrayAdapter);
    }

    public void onStart() {
        super.onStart();
        /*
        storageRef.child("users_photos/"+ user.getDisplayName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImgUserPhoto.setImageURI(uri);
                Log.i("ProfilePage", uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/
        Glide.with(this).load(user.getPhotoUrl()).into(ImgUserPhoto);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Member object and use the values to update the UI
                member = dataSnapshot.getValue(Member.class);
                if (member != null) {
                    memberProfile.clear();
                    userName.setText(member.getName());
                    memberProfile.add("Age: " + member.getAge().toString());
                    memberProfile.add("Gender: " + member.getGender());
                    memberProfile.add("Height: " + member.getHeight().toString());
                    memberProfile.add("Weight: " + member.getWeight().toString());
                }
                arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, memberProfile);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Error", "loadPost:onCancelled", databaseError.toException());
            }
        };
        dataRef.addValueEventListener(postListener);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileReg.class);
                startActivity(intent);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
                startActivity(intent);
            }
        });
    }


}
