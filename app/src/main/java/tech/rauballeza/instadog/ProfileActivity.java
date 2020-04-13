package tech.rauballeza.instadog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProfileRecyclerAdapter profileRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //Storage
    StorageReference storageReference;
    //Path where images of user profile and cover will be stored
    String storagePath = "Users_Profile_Cover_Imgs/";

    ImageView avatarIv, coverIv,btn_more;
    TextView nameTv, emailTv, phoneTv;
    FloatingActionButton fab;

    //    Dialogo de progreso
    ProgressDialog pd;

    //Constantes de permisos
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //Array de permisos
    String cameraPermissions[];
    String storagePermissions[];

    //Uri of picked image
    Uri image_uri;

    //For checking profile or cover photo
    String profileOrCoverPhoto;

    private int[] posts = {
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve,
            R.drawable.steve, R.drawable.steve, R.drawable.steve};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        //Init array permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        recyclerView = findViewById(R.id.posts);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        profileRecyclerAdapter = new ProfileRecyclerAdapter(posts);
        recyclerView.setAdapter(profileRecyclerAdapter);

        //Init views
        avatarIv = findViewById(R.id.avatarIv);
        //coverIv = view.findViewById(R.id.coverIv);
        nameTv = findViewById(R.id.nameTv);
        //emailTv = view.findViewById(R.id.emailTv);
        //phoneTv = view.findViewById(R.id.phoneTv);
        fab = findViewById(R.id.fab);

        //Innit progress dialog
        pd = new ProgressDialog(this);

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Check until requiered data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();
                    String cover = "" + ds.child("cover").getValue();

                    //Set data
                    nameTv.setText(name);
                    //emailTv.setText(email);
                    //phoneTv.setText(phone);
                    try {
                        //If image is received then set
                        Picasso.get().load(image).into(avatarIv);
                    } catch (Exception e) {
                        //if there is any exception while getting image then set default
                        Picasso.get().load(R.drawable.ic_add_a_photo_black_24dp).into(avatarIv);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //fab button click
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        btn_more = findViewById(R.id.btn_more);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUserStatus();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(ProfileActivity.this, FeedActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.search_button:
                        return true;
                    case R.id.upload:
                        return true;
                    case R.id.likes:
                        return true;
                    case R.id.profile:
                        return true;

                }
                return false;
            }
        });
    }


    private boolean checkStoragePermission() {
        //Check if storage permission is enable or not
        //return 1 if enabled
        //return 2 if not enabled
        boolean result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_DENIED);
        return result;
    }

    private void requestStoragePermission() {
        //request runtime storage permission
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //Check if storage permission is enable or not
        //return 1 if enabled
        //return 2 if not enabled
        boolean result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_DENIED);

        boolean result1 = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_DENIED);

        return result && result1;
    }

    private void requestCameraPermission() {
        //request runtime storage permission
        requestPermissions(cameraPermissions, STORAGE_REQUEST_CODE);
    }


    private void showEditProfileDialog() {
        /*Show dialog containing options
         * 1) Edit Profile Picture
         * 2) Edit Cover Photo
         * 3) Edit Name
         * 4) Edit Phone
         * */

        //Options to show in dialog
        String options[] = {"Editar foto de perfil", "Editar foto de portada", "Editar nombre", "Editar teléfono"};
        //Dialogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        //Set titulo
        builder.setTitle("Selecciona una opción:");
        //Agregar items
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //Ediar Foto de perfil click
                        pd.setMessage("Actualización de imagen de perfil");
                        profileOrCoverPhoto = "image";
                        showImagePicDialog();
                        break;
                    case 1:
                        //Editar Portada click
                        pd.setMessage("Actualización de imagen de portada");
                        profileOrCoverPhoto = "cover";
                        showImagePicDialog();
                        break;
                    case 2:
                        //Editar nombre click
                        pd.setMessage("Actualización de nombre");
                        //Calling mthod and pass key "name" as parameters to update it's value in database
                        showNamePhoneUpdateDialog("name");
                        break;
                    case 3:
                        //Editar telefono click
                        pd.setMessage("Actualización de teléfono");
                        showNamePhoneUpdateDialog("phone");
                        break;

                }
            }

        });
        builder.create().show();
    }


    private void showNamePhoneUpdateDialog(final String key) {
        /*parameter "key" will contain value:
            either  "name" which is key in user's database is used to update user's name
            or      "phone" which is key in user's database is used to update user's phone
        * */
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Update " + key);
        //set layout of dialog
        LinearLayout linearLayout = new LinearLayout(ProfileActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        //add edit text
        final EditText editText = new EditText(ProfileActivity.this);
        editText.setHint("Enter " + key); //hint e.g. Edit name OR Edit phone
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add buttons in dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                String value = editText.getText().toString().trim();
                //validate if user has entered something or not
                if (!TextUtils.isEmpty(value)) {
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(ProfileActivity.this, "Actualizado...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(ProfileActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ProfileActivity.this, "Please enter " + key, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //add buttons in dialog to cancel
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //create and show dialog
        builder.create().show();
    }

    private void showImagePicDialog() {
        //Show dialog containing options camera and gallery to pick the image

        //Options to show in dialog
        String options[] = {"Camara", "Galeria"};
        //Dialogo de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        //Set titulo
        builder.setTitle("Tomar imagen desde:");
        //Agregar items
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //Click camara
                        if (!checkCameraPermission()) {
                            requestCameraPermission();
                        } else {
                            pickFromCamera();
                        }
                        break;
                    case 1:
                        //Seleccion de galeria
                        if (!checkStoragePermission()) {
                            requestStoragePermission();
                        } else {
                            pickFromGallery();
                        }
                        break;
                }
            }
        });
        builder.create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /*This method called when user press Allow or Deny from permission request dialog
         * here we will handle permission cases (allowed & denied)*/
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                //Picking rom camera, first check if camera and storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //Permissions enabled
                        pickFromCamera();
                    } else {
                        //permisions denied
                        Toast.makeText(ProfileActivity.this, "Habilite la cámara y el permiso de almacenamiento.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                //Picking from gallery, first check if storage permissions allowed or not
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //Permissions enabled
                        pickFromGallery();
                    } else {
                        //permisions denied
                        Toast.makeText(ProfileActivity.this, "Habilite el permiso de almacenamiento.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*This methow will be called after picking image from camera or gallery*/
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image is picked from gallery, get uri of image
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera, get uri of image
                uploadProfileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        //Show progress
        pd.show();

        //Path and name of image to be stored in firebase storage
        //e.g. Users_Profile_Cover_Imgs/image_351635432.jpg
        String filePathAndName = storagePath + "" + profileOrCoverPhoto + "_" + user.getUid();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Image is uploaded to storage, now get it's url and store in user's database
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();

                //Check if image is uploaded or not and uri is received
                if (uriTask.isSuccessful()) {
                    //Image uploaded
                    final HashMap<String, Object> results = new HashMap<>();
                    results.put(profileOrCoverPhoto, downloadUri.toString());

                    databaseReference.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Imagen cargada...", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(ProfileActivity.this, "Error al cargar imagen...", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(ProfileActivity.this, "Ha ocurrido algun error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //There were some error(s), get show error message, dismiss progress dialog
                pd.dismiss();
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickFromCamera() {
        //Intent of picking image form device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        //Put image uri
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //Intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        //Pick from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {

        } else {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

}
