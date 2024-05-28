package com.mpjosemanuel86.conectavet.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.adapter.CitaAdapter;
import com.mpjosemanuel86.conectavet.adapter.ClienteAdapter;
import com.mpjosemanuel86.conectavet.model.Cita;
import com.mpjosemanuel86.conectavet.model.Cliente;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AgendaCitaActivity extends AppCompatActivity {

    RecyclerView mRecycler;
    CitaAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_cita);
        mFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRecycler = findViewById(R.id.rvCitas);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Convertir la fecha actual a formato "yyyyMMdd"
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            String fechaActual = dateFormat.format(new Date());

            Query citasQuery = mFirestore.collection("citas")
                    .whereEqualTo("uid", uid)
                    .whereGreaterThanOrEqualTo("fechaCita", fechaActual)
                    .orderBy("fechaCita", Query.Direction.ASCENDING);

            FirestoreRecyclerOptions<Cita> firestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Cita>().setQuery(citasQuery, Cita.class).build();

            mAdapter = new CitaAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
            mAdapter.notifyDataSetChanged();
            mRecycler.setAdapter(mAdapter);
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
