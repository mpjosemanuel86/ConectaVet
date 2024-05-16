package com.mpjosemanuel86.conectavet.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mpjosemanuel86.conectavet.adapter.ClienteAdapter;
import com.mpjosemanuel86.conectavet.adapter.MascotaAdapter;
import com.mpjosemanuel86.conectavet.model.Cliente;
import com.mpjosemanuel86.conectavet.model.Mascota;
import com.mpjosemanuel86.conectavet.ui.fragment.CrearMascotaFragment;
import com.mpjosemanuel86.conectavet.R;

public class GestionMascotaActivity extends AppCompatActivity {

    Button btn_add_fragment_mascota;
    RecyclerView mRecycler;
    MascotaAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_mascota);

        mFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRecycler = findViewById(R.id.rvMascotas);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        if (currentUser != null) {
            String uid = currentUser.getUid();
            Query query = mFirestore.collection("pet").whereEqualTo("uid", uid);
            FirestoreRecyclerOptions<Mascota> firestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Mascota>().setQuery(query, Mascota.class).build();

            mAdapter = new MascotaAdapter(firestoreRecyclerOptions, this);
            mAdapter.notifyDataSetChanged();
            mRecycler.setAdapter(mAdapter);

        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }



        btn_add_fragment_mascota = findViewById(R.id.btnAgregarMascota2);



        btn_add_fragment_mascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearMascotaFragment fm = new CrearMascotaFragment();
                fm.show(getSupportFragmentManager(),"Navegar a fragment");

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
