package tech.rauballeza.instadog;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void openRegisterPage(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
    private void openFeed() {
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
    }
}
