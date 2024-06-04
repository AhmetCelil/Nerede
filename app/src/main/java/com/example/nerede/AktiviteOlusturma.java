package com.example.nerede;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AktiviteOlusturma extends AppCompatActivity {

    private EditText isimEditText, aciklamaEditText, konumEditText, kapasiteEditText, saatEditText;
    private DatePicker datePicker;
    private ImageButton saveButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivite_olusturma);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("etkinlikler");

        // Get references to UI elements
        isimEditText = findViewById(R.id.notes_title_text);
        aciklamaEditText = findViewById(R.id.notes_content_text);
        konumEditText = findViewById(R.id.konum_title_text);
        kapasiteEditText = findViewById(R.id.kapasite_title_text);
        saatEditText = findViewById(R.id.saat_content_text);
        datePicker = findViewById(R.id.date_time_picker);
        saveButton = findViewById(R.id.save_aktivite_btn);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEtkinlik();
            }
        });
    }

    private void saveEtkinlik() {
        String isim = isimEditText.getText().toString().trim();
        String aciklama = aciklamaEditText.getText().toString().trim();
        String konum = konumEditText.getText().toString().trim();
        String kapasite = kapasiteEditText.getText().toString().trim();
        String saat = saatEditText.getText().toString().trim();
        String zaman = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
        long timestamp = System.currentTimeMillis();

        if (isim.isEmpty() || aciklama.isEmpty() || konum.isEmpty() || kapasite.isEmpty() || saat.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        String rezerveSayisi = "0";  // Initialize as 0 for a new event

        Etkinlik etkinlik = new Etkinlik(isim, aciklama, konum, zaman, kapasite, rezerveSayisi, saat, timestamp);
        String key = databaseReference.push().getKey();
        if (key != null) {
            etkinlik.setKey(key); // Etkinlik nesnesine anahtar değerini ekle
            databaseReference.child(key).setValue(etkinlik)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Etkinlik başarıyla oluşturuldu", Toast.LENGTH_SHORT).show();
                        finish(); // Activity'yi kapat
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Etkinlik oluşturulurken hata oluştu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AktiviteOlusturma", "Etkinlik oluşturulurken hata oluştu: " + e.getMessage());
                    });
        }
    }
}
