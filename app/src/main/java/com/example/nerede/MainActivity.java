package com.example.nerede;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    Button button;
    ImageButton btn, filtre;

    private static final int REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private ActivityAdapter activityAdapter;
    private List<Etkinlik> activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logOut_btn);
        btn = findViewById(R.id.add_note_btn);
        filtre = findViewById(R.id.filtreleme);

        user = auth.getCurrentUser();

        recyclerView = findViewById(R.id.recyler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityList = new ArrayList<>();
        activityAdapter = new ActivityAdapter(this, activityList);
        recyclerView.setAdapter(activityAdapter);

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), GirisEkrani.class);
            startActivity(intent);
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), GirisEkrani.class);
                startActivity(intent);
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AktiviteOlusturma.class);
                startActivity(intent);
            }
        });

        filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        // Firebase'den verileri çekmek için metod çağrısı
        fetchDataFromFirebase();
    }

    // Filtreleme için pop-up ekranı göstermek
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrele");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.filter_dialog, null, false);
        final EditText input = viewInflated.findViewById(R.id.input);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String keyword = input.getText().toString().trim();
                filterDataFromFirebase(keyword);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // Firebase'den veri çekme ve RecyclerView'ı güncelleme
    private void fetchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("etkinlikler");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activityList.clear(); // Önceki verileri temizle
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Etkinlik etkinlik = snapshot.getValue(Etkinlik.class);
                    activityList.add(etkinlik);
                }
                activityAdapter.notifyDataSetChanged(); // RecyclerView'ı güncelle
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hata durumunda yapılacak işlemler
            }
        });
    }

    // Firebase'den filtrelenmiş verileri çekme ve RecyclerView'ı güncelleme
    private void filterDataFromFirebase(final String keyword) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("etkinlikler");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activityList.clear(); // Önceki verileri temizle
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Etkinlik etkinlik = snapshot.getValue(Etkinlik.class);
                    if (etkinlik.getIsim().toLowerCase().contains(keyword.toLowerCase()) ||
                            etkinlik.getAciklama().toLowerCase().contains(keyword.toLowerCase()) || etkinlik.getKonum().toLowerCase().contains(keyword.toLowerCase())) {
                        activityList.add(etkinlik);
                    }
                }
                activityAdapter.notifyDataSetChanged(); // RecyclerView'ı güncelle
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hata durumunda yapılacak işlemler
            }
        });
    }
}
