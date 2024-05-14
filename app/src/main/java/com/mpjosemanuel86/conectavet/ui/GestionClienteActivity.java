package com.mpjosemanuel86.conectavet.ui;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mpjosemanuel86.conectavet.CrearClienteFragment;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.adapter.ClienteAdapter;
import com.mpjosemanuel86.conectavet.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class GestionClienteActivity extends AppCompatActivity {

    Button btnAgregarCliente, btn_add_fragment;
    RecyclerView mRecycler;
    ClienteAdapter mAdapter;
    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cliente);

        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.rvClientes);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Obtener datos de Firestore
        Query query = mFirestore.collection("cliente");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Cliente> clientes = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Cliente cliente = documentSnapshot.toObject(Cliente.class);
                    clientes.add(cliente);
                }

                // Configurar el adaptador con los datos obtenidos de Firestore
                mAdapter = new ClienteAdapter(GestionClienteActivity.this, clientes);
                mRecycler.setAdapter(mAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar errores de consulta a Firestore
            }
        });

        btnAgregarCliente = findViewById(R.id.btnAgregarCliente);
        btn_add_fragment = findViewById(R.id.btnAgregarCliente2);
        btnAgregarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GestionClienteActivity.this, CrearClienteActivity.class));
            }
        });
        btn_add_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearClienteFragment fm = new CrearClienteFragment();
                fm.show(getSupportFragmentManager(),"Navegar a fragment");
            }
        });
    }

}
