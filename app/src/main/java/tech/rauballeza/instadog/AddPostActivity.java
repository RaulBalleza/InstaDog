package tech.rauballeza.instadog;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddPostActivity extends AppCompatActivity {

    //CAMERA PERMISSIONS
    private static int CAMERA = 100;
    //GALLERY PERMISSIONS
    private static int GALLERY = 200;

    //PERMISSIONS ARRAYS
    String[] cameraPermissions;
    String[] galleryPermissions;

    //Firebase Auth
    FirebaseAuth firebaseAuth;

    //View Elements
    ImageView back;
    EditText title, desc;
    RelativeLayout image;
    Button add_post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        galleryPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();

        back = findViewById(R.id.addpost_btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.editTextTitle);

        desc = findViewById(R.id.editTextDesc);

        image = findViewById(R.id.iv_rl);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Seleccione imagen de post", Toast.LENGTH_SHORT).show();
                showPickImageDialog();
            }
        });

        add_post = findViewById(R.id.btn_add_post);
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _title = title.getText().toString().trim();
                String _desc = desc.getText().toString().trim();


            }
        });
    }

    private boolean checkStoragePermissions(){

        boolean camera =

        return camera && storage;
    }

    private void requestStoragePermissions(){
        ActivityCompat.requestPermissions(this, galleryPermissions, GALLERY);
    }

    private void showPickImageDialog() {
        String[] options = {"Camera", "Gallery"};

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose post image from...");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //Camera Clicked
                }

                if (which==1){
                    //Gallery clicked
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}
