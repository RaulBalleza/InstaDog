package tech.rauballeza.instadog;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
<<<<<<< HEAD
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
=======
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
>>>>>>> 47d274b782662de3504a2460dc81f816b415700a

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button login;
    EditText username;
    EditText pass;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
=======
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.editTextUsername);
        pass = findViewById(R.id.editTextPassword);
>>>>>>> 47d274b782662de3504a2460dc81f816b415700a
        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                openFeed();
            }
        });

        TextView txt_register = (TextView) findViewById(R.id.textViewRegister);
        txt_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });

=======
                String email = username.getText().toString();
                String passw = pass.getText().toString();
                loginUser(email, passw);
            }
        });

        TextView txt_register = (TextView) findViewById(R.id.textViewRegister);
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });

>>>>>>> 47d274b782662de3504a2460dc81f816b415700a
        // Dirigiendo el hipervínculo a la activity de Registro
//        String text = "Regístrate.";
//        SpannableString spanableRegister = new SpannableString(text);
//
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View v) {
//                // Enviar al activity de registro
//                openRegisterPage();
//            }
//        };
//
//        spanableRegister.setSpan(clickableSpan, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        txt_register.setText(spanableRegister);
//        txt_register.setMovementMethod(LinkMovementMethod.getInstance());
    }

<<<<<<< HEAD
    private void openRegisterPage(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
    private void openFeed() {
=======
    private void openRegisterPage() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    private void loginUser(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this, FeedActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

>>>>>>> 47d274b782662de3504a2460dc81f816b415700a
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
    }
}
