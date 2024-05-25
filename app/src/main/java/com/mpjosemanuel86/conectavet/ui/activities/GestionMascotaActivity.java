package com.mpjosemanuel86.conectavet.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.adapter.MascotaAdapter;
import com.mpjosemanuel86.conectavet.adapter.MyAdapter;
import com.mpjosemanuel86.conectavet.model.Cliente;
import com.mpjosemanuel86.conectavet.model.Mascota;
import com.mpjosemanuel86.conectavet.ui.fragment.CrearMascotaFragment;

import java.util.ArrayList;

public class GestionMascotaActivity extends AppCompatActivity {

    private Button btn_add_fragment;
    private RecyclerView mRecycler;
    //private MascotaAdapter mAdapter;
    private MyAdapter myAdapter;
    private FirebaseFirestore mFirestore;
    private FirebaseUser currentUser;
    ArrayList<Mascota> array_mascota;
    ArrayList<Cliente> array_cliente;
    ArrayList<Mascota> mascotas;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_mascota);

        mFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Configurar RecyclerView
        mRecycler = findViewById(R.id.rvMascotas);

        // Crear datos de ejemplo
        /*
        mascotas = new ArrayList<>();
        mascotas.add(new Mascota("Mascota 1"));
        mascotas.add(new Mascota("Mascota 2"));
        mascotas.add(new Mascota("Mascota 3"));
         */


        // Crear un MascotaAdapter
        //mAdapter = new MascotaAdapter(mascotas);

        // Establecer el MascotaAdapter en el RecyclerView
        //mRecycler.setAdapter(mAdapter);

        // Establecer un LayoutManager en el RecyclerView
        //mRecycler.setLayoutManager(new LinearLayoutManager(this));


        DocumentReference docRef = mFirestore.collection("users").document(currentUser.getUid());
        CollectionReference subColRef = docRef.collection("clientes");

        subColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                array_cliente = new ArrayList<>();
                array_mascota = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Cliente obj_cliente = documentSnapshot.toObject(Cliente.class);
                    if(obj_cliente != null && obj_cliente.getNombreCliente() != null){
                        Log.d("cliente", obj_cliente.getNombreCliente());
                        Log.d("documentSnapshot", documentSnapshot.getId());
                        DocumentReference docRefClient = subColRef.document(documentSnapshot.getId());
                        CollectionReference subColRefPets = docRefClient.collection("pets");
                        subColRefPets.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                array_cliente.add(obj_cliente);
                                for(DocumentSnapshot documentSnapshot1: queryDocumentSnapshots){
                                    Mascota obj_mascota = documentSnapshot1.toObject(Mascota.class);
                                    if(obj_mascota != null && obj_mascota.getNombreMascota() != null){
                                        Log.d("mascota", obj_mascota.getNombreMascota());
                                        array_mascota.add(obj_mascota);
                                    }
                                }

                                myAdapter = new MyAdapter(array_mascota, getApplicationContext());
                                mRecycler.setAdapter(myAdapter);
                                mRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                //mRecycler.setAdapter(mAdapter);
                            }
                            }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ERROR", "Error al obtener datos: " + e.getMessage());
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ERROR", "Error al obtener datos: " + e.getMessage());
            }
        });



        if (currentUser != null) {
            /*
            String uid = currentUser.getUid();
            Query query = mFirestore.collection("pet").whereEqualTo("uid", uid);

            FirestoreRecyclerOptions<Mascota> firestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Mascota>().setQuery(query, Mascota.class).build();

            mAdapter = new MascotaAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
            mAdapter.notifyDataSetChanged();
            mRecycler.setAdapter(mAdapter);
            */

            //Log.d("UID", uid);
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
        //mAdapter.startListening();
        }


    @Override
    protected void onStop() {
        super.onStop();
        //mAdapter.stopListening();
    }
}


