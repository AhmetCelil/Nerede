package com.example.nerede;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GirisEkrani extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button buttonGiris;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textview;

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekrani);

        editTextEmail = findViewById(R.id.email_edit_text);
        editTextPassword = findViewById(R.id.password_edit_text);
        buttonGiris = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar_giris);
        textview = findViewById(R.id.create_account_text_view_btn);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth instance

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HesapOlusturmaEkrani.class);
                startActivity(intent);
                finish();
            }
        });

        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(GirisEkrani.this, "Lütfen mail adresini girin", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(GirisEkrani.this, "Lütfen şifrenizi girin", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(GirisEkrani.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(GirisEkrani.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(GirisEkrani.this, "Giriş Yapılamadı...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
