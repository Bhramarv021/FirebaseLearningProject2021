package com.example.firebaselearningproject2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView userProfileImage;
    EditText userName;
    EditText userCourse;
    EditText userRollNumber;
    EditText userDuration;
    Button browse;
    Button signup;

    Uri imagePath;
    Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.name);
        userRollNumber = findViewById(R.id.roll_number);
        userDuration = findViewById(R.id.duration);
        userCourse = findViewById(R.id.course);
        browse = findViewById(R.id.browse_button);
        signup = findViewById(R.id.signup_button);
        userProfileImage = findViewById(R.id.imageView);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(MainActivity.this)
                        .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Choose profile photo"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToFirebase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imagePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imagePath);
                imgBitmap = BitmapFactory.decodeStream(inputStream);
                userProfileImage.setImageBitmap(imgBitmap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadDataToFirebase() {

        String name = userName.getText().toString();
        String roll_no = userRollNumber.getText().toString();
        String duration = userDuration.getText().toString();
        String course = userCourse.getText().toString();

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        //Adding image to firebase storage
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference uploader = firebaseStorage.getReference("Image1231" + new Random().nextInt(50));

        uploader.putFile(imagePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploader.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                dialog.dismiss();
                                //Now we get all student data include image reference in string
                                //Its time to upload form data except image as image already uploaded
                                FirebaseDatabase db = FirebaseDatabase.getInstance();
                                DatabaseReference rootReference = db.getReference("Student");

                                StudentDataWithImage studentDataWithImage = new StudentDataWithImage(course, duration, name, uri.toString());
                                rootReference.child(roll_no).setValue(studentDataWithImage);

                                Toast.makeText(MainActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                resetForm();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot snapshot) {
                        long percentage = (100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded : "+percentage+"%");
                    }
                });
    }

    private void resetForm() {
        userName.setText("");
        userCourse.setText("");
        userRollNumber.setText("");
        userDuration.setText("");
        userProfileImage.setImageResource(R.drawable.ic_launcher_foreground);
    }

}