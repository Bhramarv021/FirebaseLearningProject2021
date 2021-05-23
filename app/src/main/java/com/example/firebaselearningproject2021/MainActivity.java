package com.example.firebaselearningproject2021;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText userName;
    EditText userRollNumber;
    EditText userCourse;
    EditText userDuration;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void process(View view) {
        userRollNumber = findViewById(R.id.rollno);
        userName = findViewById(R.id.name);
        userCourse = findViewById(R.id.course);
        userDuration = findViewById(R.id.duration);
        submitButton = findViewById(R.id.submit);

        String roll = userRollNumber.getText().toString().trim();
        String name = userName.getText().toString().trim();
        String course = userCourse.getText().toString().trim();
        String duration = userDuration.getText().toString().trim();

        StudentData studentData = new StudentData(name,course,duration);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference studentRef = db.getReference("Student");

        studentRef.child(roll).setValue(studentData);

        clearEditText();
        Toast.makeText(getApplicationContext(), "Inserted", Toast.LENGTH_LONG).show();
    }

    private void clearEditText() {
        userName.setText("");
        userRollNumber.setText("");
        userCourse.setText("");
        userDuration.setText("");
    }
}