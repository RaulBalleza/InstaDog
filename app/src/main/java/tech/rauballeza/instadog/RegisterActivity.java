package tech.rauballeza.instadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    // Date components
    private int mYear,mMonth,mDay;

    // Views
    EditText editTextNombre, editTextApellidos, editTextTelefono, editTextEmail, editTextPassword;
    TextView textViewFechaNac, textViewSelectedGender;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    ProgressDialog progressDialog;

    // Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Getting the birthdate
        final Button pickDate = (Button) findViewById(R.id.pick_date);
        final TextView textViewDate = (TextView) findViewById(R.id.birthdate);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textViewDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year < mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear, mMonth, mDay);

                                textViewDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        // Getting the value of radio gender selected
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);

        final TextView textViewGender = findViewById(R.id.selectedGender);
        textViewGender.setText(radioSexButton.getText());

        // Init values
        editTextNombre = findViewById(R.id.editTextPersonName);
        editTextApellidos = findViewById(R.id.editTextLastName);
        editTextTelefono = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textViewFechaNac = findViewById(R.id.birthdate);
        textViewSelectedGender = findViewById(R.id.selectedGender);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");

        // Handle register btn click
        btnRegistrar = findViewById(R.id.buttonRegister);

        btnRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Input fields
                String nombre = editTextNombre.getText().toString().trim();
                String apellido = editTextApellidos.getText().toString().trim();
                String telefono = editTextTelefono.getText().toString().trim();
                String fechaNac = textViewFechaNac.getText().toString().trim();
                String genero = textViewGender.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validate
                if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
                    // Set error and focus to email EditText
                    editTextEmail.setError("Email inválido");
                    editTextEmail.setFocusable(true);
                }
                else if( password.length()<6 ){
                    editTextPassword.setError("Contraseña inválida. Asegúrate de escribir por lo menos 6 caracteres");
                    editTextPassword.setFocusable(true);
                }
                else if( nombre.length()<2 ){
                    editTextNombre.setError("Nombre inválido. Asegúrate de escribir por lo menos 2 caracteres");
                    editTextNombre.setFocusable(true);
                }
                else if( apellido.length()<6 ){
                    editTextApellidos.setError("Apellido inválido. Asegúrate de escribir por lo menos 2 caracteres");
                    editTextApellidos.setFocusable(true);
                }
                else if( !Patterns.PHONE.matcher(telefono).matches() ){
                    editTextTelefono.setError("Número telefónico inválido.");
                    editTextTelefono.setFocusable(true);
                }
                else {

                    registerUser(nombre, apellido, telefono, fechaNac, genero, email, password);
                }
            }
        });
    }

    private void registerUser(String nombre, String apellido, String telefono, String fechaNac, String genero, String email, String password) {
        // Show progress dialog
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, dismiss dialog and start register activity
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Usuario registrado...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
<<<<<<< HEAD
=======
                            finish();
>>>>>>> 47d274b782662de3504a2460dc81f816b415700a
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Ha ocurrido un error. Por favor inténtelo nuevamente.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error, dismiss progress dialog and get and show a message to the user
                progressDialog.dismiss();

                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed(); // Va a la actitivity anterior
        return super.onSupportNavigateUp();
    }
}