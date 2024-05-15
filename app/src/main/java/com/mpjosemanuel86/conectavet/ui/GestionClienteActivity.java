package com.mpjosemanuel86.conectavet.ui;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mpjosemanuel86.conectavet.CrearClienteFragment;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.adapter.ClienteAdapter;
import com.mpjosemanuel86.conectavet.model.Cliente;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

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

        // Usar SnapshotListener para escuchar cambios en tiempo real
        Query query = mFirestore.collection("cliente");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Manejar errores de escucha
                    // Manejar errores de escucha
                    Toast.makeText(GestionClienteActivity.this, "Error al obtener los datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                List<Cliente> clientes = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Cliente cliente = documentSnapshot.toObject(Cliente.class);
                    clientes.add(cliente);
                }

                // Configurar el adaptador con los datos obtenidos de Firestore
                mAdapter = new ClienteAdapter(GestionClienteActivity.this, clientes);
                mRecycler.setAdapter(mAdapter);
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
                fm.show(getSupportFragmentManager(), "Navegar a fragment");
            }
        });
    }
}

