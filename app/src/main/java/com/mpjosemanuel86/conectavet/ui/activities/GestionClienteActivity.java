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
import com.mpjosemanuel86.conectavet.ui.fragment.CrearClienteFragment;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.adapter.ClienteAdapter;
import com.mpjosemanuel86.conectavet.model.Cliente;

public class GestionClienteActivity extends AppCompatActivity {

    Button  btn_add_fragment;
    RecyclerView mRecycler;
    ClienteAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cliente);

        mFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRecycler = findViewById(R.id.rvClientes);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        if (currentUser != null) {
            String uid = currentUser.getUid();
            Query query = mFirestore.collection("cliente").whereEqualTo("uid", uid);
            FirestoreRecyclerOptions<Cliente> firestoreRecyclerOptions =
                    new FirestoreRecyclerOptions.Builder<Cliente>().setQuery(query, Cliente.class).build();

            mAdapter = new ClienteAdapter(firestoreRecyclerOptions, this);
            mAdapter.notifyDataSetChanged();
            mRecycler.setAdapter(mAdapter);
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }


        btn_add_fragment = findViewById(R.id.btnAgregarCliente2);

        btn_add_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearClienteFragment fm = new CrearClienteFragment();
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

