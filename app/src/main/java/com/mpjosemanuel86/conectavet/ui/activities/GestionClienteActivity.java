package com.mpjosemanuel86.conectavet.ui.activities;




import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    String userFirebaseUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cliente);

        mFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userFirebaseUID = getIntent().getStringExtra("USER_ID");
        DocumentReference docRef = mFirestore.collection("users").document(userFirebaseUID);
        mRecycler = findViewById(R.id.rvClientes);
        if(userFirebaseUID != null)
            Log.d("userFirebaseUID", userFirebaseUID);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query subColRef = docRef.collection("clientes");
        FirestoreRecyclerOptions<Cliente> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Cliente>().setQuery(subColRef, Cliente.class).build();

        mAdapter = new ClienteAdapter(firestoreRecyclerOptions, this, getSupportFragmentManager());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

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

