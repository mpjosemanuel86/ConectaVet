package com.mpjosemanuel86.conectavet.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.adapter.MascotaAdapter;
import com.mpjosemanuel86.conectavet.model.Mascota;
import com.mpjosemanuel86.conectavet.ui.fragment.CrearMascotaFragment;

import java.util.ArrayList;
import java.util.List;

public class GestionMascotaActivity extends AppCompatActivity implements CrearMascotaFragment.OnMascotaSavedListener {

    Button btn_add_fragment_mascota;
    RecyclerView mRecycler;
    MascotaAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseUser currentUser;
    List<Mascota> todasLasMascotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_mascota);

        mFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRecycler = findViewById(R.id.rvMascotas);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        todasLasMascotas = new ArrayList<>();

        if (currentUser != null) {
            obtenerTodasLasMascotas();
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        btn_add_fragment_mascota = findViewById(R.id.btnAgregarMascota2);

        btn_add_fragment_mascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearMascotaFragment fm = new CrearMascotaFragment();
                fm.setOnMascotaSavedListener(GestionMascotaActivity.this); // Asignar el listener
                fm.show(getSupportFragmentManager(), "Navegar a fragment");
            }
        });
    }

    private void obtenerTodasLasMascotas() {
        mFirestore.collection("cliente").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot clienteSnapshot : task.getResult()) {
                    DocumentReference clienteRef = clienteSnapshot.getReference();
                    CollectionReference mascotasRef = clienteRef.collection("mascotas");

                    mascotasRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (DocumentSnapshot mascotaSnapshot : task1.getResult()) {
                                Mascota mascota = mascotaSnapshot.toObject(Mascota.class);
                                todasLasMascotas.add(mascota);
                            }
                            // Configurar el adaptador después de obtener todas las mascotas
                            configurarAdaptador();
                        } else {
                            Toast.makeText(this, "Error al obtener mascotas", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Error al obtener clientes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configurarAdaptador() {
        mAdapter = new MascotaAdapter(todasLasMascotas);
        mRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    // Implementar el método onMascotaSaved de la interfaz OnMascotaSavedListener
    @Override
    public void onMascotaSaved() {
        // Limpiar la lista y volver a obtener todas las mascotas
        todasLasMascotas.clear();
        obtenerTodasLasMascotas();
    }
}
