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

public class HesapOlusturmaEkrani extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonRegister;
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
        setContentView(R.layout.activity_hesap_olusturma_ekrani);

        editTextEmail = findViewById(R.id.create_email_edit_text);
        editTextPassword = findViewById(R.id.create_password_edit_text);
        buttonRegister = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        textview = findViewById(R.id.login_text_view_btn);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth instance

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GirisEkrani.class);
                startActivity(intent);
                finish();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(HesapOlusturmaEkrani.this, "Lütfen mail adresini girin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(HesapOlusturmaEkrani.this, "Lütfen şifrenizi oluşturun", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(HesapOlusturmaEkrani.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HesapOlusturmaEkrani.this, "Kayıt Oluşturulamadı...", Toast.LENGTH_SHORT).show();
                        }
                    }//dakikka 17
                });
            }
        });
    }
}
