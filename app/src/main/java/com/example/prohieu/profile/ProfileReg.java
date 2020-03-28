package com.example.prohieu.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.prohieu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileReg extends AppCompatActivity {
    private EditText textName, textAge, textHeight, textWeight;
    private Spinner textGender;
    private Button buttonRegister;
    private ImageView ImgUserPhoto;
    private static int REQUESCODE = 1 ;
    private static int PReqCode = 1 ;
    private Uri pickedImgUri ;
    private ProgressBar progressBar;
    private Member member;
    private DatabaseReference dataRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_reg);

        textName = (EditText) findViewById(R.id.regName);
        textGender = (Spinner) findViewById(R.id.regGender);
        textAge = (EditText) findViewById(R.id.regAge);
        textHeight = (EditText) findViewById(R.id.regHeight);
        textWeight = (EditText) findViewById(R.id.regWeight);
        buttonRegister = (Button) findViewById(R.id.regBtn);
        progressBar = (ProgressBar)findViewById(R.id.regProgressBar);

        member = new Member();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        dataRef = FirebaseDatabase.getInstance().getReference("Member");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("users_photos/"+ user.getDisplayName());

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonRegister.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                String age = textAge.getText().toString().trim();
                String height = textHeight.getText().toString().trim();
                String weight = textWeight.getText().toString().trim();
                String name = textName.getText().toString().trim();
                String gender = textGender.getSelectedItem().toString().trim();

                if( age.isEmpty() || height.isEmpty() || weight.isEmpty() || name.isEmpty() || gender.isEmpty()) {
                    showLongMessage("Please verify all fields.");
                    buttonRegister.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    member.setName(name);
                    member.setGender(gender);
                    member.setAge(Integer.parseInt(age));
                    member.setHeight(Float.parseFloat(height));
                    member.setWeight(Float.parseFloat(weight));
                    dataRef.child(user.getDisplayName()).setValue(member);
                    showLongMessage("Successfully Update.");
                    progressBar.setVisibility(View.VISIBLE);
                    GoHomePage();
                }
            }
        });

        ImgUserPhoto = findViewById(R.id.regUserPhoto) ;
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }
            }
        });
    }

    private void updateUserInfo(Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded succesfully
                // get image url
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contain user image url
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                //.setDisplayName(textName.getText().toString().trim())
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // user info updated successfully
                                            //GoHomePage();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);
            showShortMessage("Profile image was changed");
            updateUserInfo(pickedImgUri, user);
            //uploadImage();
        }
    }

    private void showLongMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void showShortMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void GoHomePage() {
        Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
        startActivity(intent);
    }

    private void uploadImage() {
        if(pickedImgUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            storageRef.putFile(pickedImgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            showShortMessage("Uploaded");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            showShortMessage("Failed "+e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

}
