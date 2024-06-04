package com.example.nerede;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class ActivityDetail extends AppCompatActivity {

    private TextView titleTextView, capacityTextView, contentTextView, locationTextView, dateTextView, timeTextView, reserveTextView;
    private ImageButton increaseButton, decreaseButton;
    private DatabaseReference databaseReference;
    private Etkinlik activity;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Firebase veritabanı ve kimlik doğrulama referanslarını al
        databaseReference = FirebaseDatabase.getInstance().getReference("etkinlikler");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // XML dosyasındaki bileşenleri bağla
        titleTextView = findViewById(R.id.activity_title);
        capacityTextView = findViewById(R.id.activity_capacity);
        reserveTextView = findViewById(R.id.activity_rezerve);
        contentTextView = findViewById(R.id.activity_content);
        locationTextView = findViewById(R.id.activity_location);
        dateTextView = findViewById(R.id.activity_date);
        timeTextView = findViewById(R.id.activity_time);
        increaseButton = findViewById(R.id.katilimci_ekle);
        decreaseButton = findViewById(R.id.katilimci_cikar);

        // Intent'ten geçirilen Etkinlik nesnesini al
        activity = (Etkinlik) getIntent().getSerializableExtra("activity");

        // Etkinlik nesnesi alınmazsa, hata ver ve aktiviteyi sonlandır
        if (activity == null) {
            Log.e("ActivityDetail", "Activity nesnesi null. Intent'te geçerli bir activity olmayabilir.");
            finish();
            return;
        }

        // XML bileşenlerine Etkinlik nesnesinin özelliklerini yerleştir
        titleTextView.setText(activity.getIsim());
        capacityTextView.setText("Kapasite: " + activity.getEtkinlikKapasitesi());
        reserveTextView.setText("Rezerve edilen: " + activity.getRezerveSayisi());
        contentTextView.setText("Açıklama: " + activity.getAciklama());
        locationTextView.setText("Konum: " + activity.getKonum());
        dateTextView.setText("Tarih: " + activity.getZaman());
        timeTextView.setText("Saat: " + activity.getSaat());

        // Artırma ve azaltma düğmelerine tıklama olaylarını ekle
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseReservation();
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseReservation();
            }
        });
    }

    private void increaseReservation() {
        int currentReservation = Integer.parseInt(activity.getRezerveSayisi());
        currentReservation++;
        activity.setRezerveSayisi(String.valueOf(currentReservation));

        // Kullanıcının e-posta adresini açıklama kısmına ekle
        String currentDescription = activity.getAciklama();
        String userEmail = currentUser.getEmail();
        String newDescription = currentDescription + "\nRezerve eden: " + userEmail;
        activity.setAciklama(newDescription);

        updateReservationText();
        updateReservationInDatabase();
    }

    private void decreaseReservation() {
        int currentReservation = Integer.parseInt(activity.getRezerveSayisi());
        if (currentReservation > 0) {
            currentReservation--;
            activity.setRezerveSayisi(String.valueOf(currentReservation));

            // Kullanıcının e-posta adresini açıklama kısmından çıkar
            String currentDescription = activity.getAciklama();
            String userEmail = currentUser.getEmail();
            String newDescription = currentDescription.replace("\nRezerve eden: " + userEmail, "");
            activity.setAciklama(newDescription);

            updateReservationText();
            updateReservationInDatabase();
        }
    }

    private void updateReservationText() {
        reserveTextView.setText("Rezerve edilen: " + activity.getRezerveSayisi());
        contentTextView.setText("Açıklama: " + activity.getAciklama());
    }

    private void updateReservationInDatabase() {
        if (activity != null && activity.getKey() != null) {
            databaseReference.child(activity.getKey()).child("rezerveSayisi").setValue(activity.getRezerveSayisi());
            databaseReference.child(activity.getKey()).child("aciklama").setValue(activity.getAciklama())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ActivityDetail.this, "Rezervasyon sayısı güncellendi", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActivityDetail.this, "Rezervasyon sayısı güncellenirken hata oluştu", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.e("ActivityDetail", "Activity veya activity key null");
        }
    }
}
