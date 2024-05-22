package com.mpjosemanuel86.conectavet.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.adapter.MascotaAdapter;
import com.mpjosemanuel86.conectavet.model.Mascota;
import com.mpjosemanuel86.conectavet.ui.fragment.CrearMascotaFragment;

public class GestionMascotaActivity extends AppCompatActivity {

    private Button btn_add_fragment;
    private RecyclerView mRecycler;
    private MascotaAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_mascota);

        // Inicializar Firebase Firestore
        mFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Configurar RecyclerView
        mRecycler = findViewById(R.id.rvMascotas);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Comprobar si el usuario está autenticado
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Crear la consulta para obtener las mascotas del usuario actual
            Query query = mFirestore.collection("pet").whereEqualTo("uid", uid);

            // Configurar FirestoreRecyclerOptions
            FirestoreRecyclerOptions<Mascota> firestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Mascota>().setQuery(query, Mascota.class).build();

            // Inicializar el adaptador de las mascotas
            mAdapter = new MascotaAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
            mAdapter.notifyDataSetChanged();
            mRecycler.setAdapter(mAdapter);
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        // Configurar el botón para agregar mascotas
        btn_add_fragment = findViewById(R.id.btnAgregarMascota2);
        btn_add_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el fragmento para agregar mascotas
                CrearMascotaFragment fm = new CrearMascotaFragment();
                fm.show(getSupportFragmentManager(), "Navegar a fragment");
            }
        });
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

