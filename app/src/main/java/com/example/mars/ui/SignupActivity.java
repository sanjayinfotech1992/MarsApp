package com.example.mars.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.example.mars.R;
import com.example.mars.data.UserData;
import com.example.mars.util.Constants;
import com.example.mars.util.MyPreferences;
import com.example.mars.util.Validation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SignupActivity extends AppCompatActivity {
    EditText et_f_name, et_l_name, et_email, et_password, et_con_password;
    TextView tv_signup;
    Button btn_signup;
    boolean iStatus;
    FirebaseAuth firebaseAuth;
    RelativeLayout loading_view;
    ImageView add_image;
    ChoosePhotoHelper choosePhotoHelper;
    String imagePath = "";
    String pathImageFirebase = "";

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    MyPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        et_f_name = findViewById(R.id.et_f_name);
        et_l_name = findViewById(R.id.et_l_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_con_password = findViewById(R.id.et_con_password);
        tv_signup = findViewById(R.id.tv_signup);
        btn_signup = findViewById(R.id.btn_signup);
        add_image = findViewById(R.id.add_image);
        loading_view = findViewById(R.id.loading_view);
        preferences = new MyPreferences(this);

        firebaseAuth = FirebaseAuth.getInstance();
        btn_signup.setOnClickListener(v -> {
            if (imagePath.equalsIgnoreCase("")) {
                showToast("Please select image!!");
                return;
            }
            if (Validation.isEmptyData(et_f_name)) {
                if (Validation.isEmptyData(et_l_name)) {
                    if (Validation.isEmptyData(et_email)) {
                        if (Validation.isEmptyData(et_password)) {
                            if (Validation.isEmptyData(et_con_password)) {
                                if (Validation.isValidEmail(et_email)) {
                                    if (Validation.isPasswordMatch(et_password, et_con_password, SignupActivity.this)) {
                                        storagePics();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        tv_signup.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

        add_image.setOnClickListener(v -> {
            choosePhotoHelper = ChoosePhotoHelper.with(SignupActivity.this)
                    .asFilePath()
                    .build(photo -> {
                        Log.d("Picc", photo);
                        Uri myUri = Uri.fromFile(new File(photo));
                        preferences.saveString(MyPreferences.USER_IMAGE,photo);
                        try {
                            Bitmap oribitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
                            add_image.setImageBitmap(oribitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        Glide.with(SignupActivity.this)
//                                .load(photo)
//                                .into(add_image);
                        imagePath = photo;

//                        storePicss();
                    });

            choosePhotoHelper.showChooser();
        });
    }

    private void storagePics() {

        loading_view.setVisibility(View.VISIBLE);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        Uri filePath = Uri.fromFile(new File(imagePath));
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                showToast("Image Uploaded");
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        pathImageFirebase = uri.toString();
                        addUserInfoToFirebase(EncodeString(et_email.getText().toString()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Image uploading Failed!!");
                        Log.d("ImageUPload22", e.getMessage());

                        loading_view.setVisibility(View.GONE);

                        pathImageFirebase = imagePath;
                        addUserInfoToFirebase(EncodeString(et_email.getText().toString()));
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ImageUPload", e.getMessage());
                loading_view.setVisibility(View.GONE);
                pathImageFirebase = imagePath;
                addUserInfoToFirebase(EncodeString(et_email.getText().toString()));
//                storagePics();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        choosePhotoHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (choosePhotoHelper != null)
            choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    private void addUserInfoToFirebase(String child) {
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        ArrayList<UserData> userList = new ArrayList<>();
        iStatus = false;

        firebaseDatabase.child(Constants.DB_USER_TABLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    HashMap<String, String> users = new HashMap();
                    users = (HashMap<String, String>) snap.getValue();
//                    String  s= users.get(0).get("dd");
                    UserData userData = new UserData(users.get("strImagePath"), users.get("firstName"), users.get("lastName"), users.get("email"), users.get("password"));
                    userList.add(userData);
                    if (userData.getEmail() != null) {
                        if (userData.getEmail().equals(et_email.getText().toString())) {
                            showToast("Email already exist");
                            iStatus = true;
                        }
                    }
                }
                if (!iStatus) {
                    firebaseDatabase.child(Constants.DB_USER_TABLE).child(child).child("firstName").setValue(et_f_name.getText().toString());
                    firebaseDatabase.child(Constants.DB_USER_TABLE).child(child).child("lastName").setValue(et_l_name.getText().toString());
                    firebaseDatabase.child(Constants.DB_USER_TABLE).child(child).child("email").setValue(et_email.getText().toString());
                    firebaseDatabase.child(Constants.DB_USER_TABLE).child(child).child("password").setValue(et_password.getText().toString());
                    firebaseDatabase.child(Constants.DB_USER_TABLE).child(child).child("strImagePath").setValue(pathImageFirebase);
                    Toast.makeText(SignupActivity.this,"Signup Successfully!!",Toast.LENGTH_SHORT).show();
                    finish();
                }

                loading_view.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
