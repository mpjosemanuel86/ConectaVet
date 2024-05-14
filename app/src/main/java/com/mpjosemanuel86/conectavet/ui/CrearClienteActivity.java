package com.mpjosemanuel86.conectavet.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mpjosemanuel86.conectavet.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CrearClienteActivity extends AppCompatActivity {


    Button btnGuardarDatos;
    EditText nombreCliente, direccionCliente, telefonoCliente, nombreMascota, especieMascota, razaMascota, tamanioMascota, sexoMascota, fechaNacimientoMascota, colorMascota;
    private FirebaseFirestore mfirestore;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);


        this.setTitle("Crear mascota");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mfirestore = FirebaseFirestore.getInstance();

        nombreCliente = findViewById(R.id.editTextNombreCliente);
        direccionCliente = findViewById(R.id.editTextDireccionCliente);
        telefonoCliente = findViewById(R.id.editTextTelefonoCliente);


        btnGuardarDatos = findViewById(R.id.buttonGuardarDatos);

        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreClientePet = nombreCliente.getText().toString().trim();
                String direccionClientePet = direccionCliente.getText().toString().trim();
                String telefonoClientePet = telefonoCliente.getText().toString().trim();


                if (nombreClientePet.isEmpty() && direccionClientePet.isEmpty() && telefonoClientePet.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                } else {
                    postPet(nombreClientePet, direccionClientePet, telefonoClientePet);
                }
            }
        });
    }

    private void postPet(String nombreClientePet, String direccionClientePet, String telefonoClientePet) {
        Map<String, Object> map =  new HashMap<>();
        map.put("nombreCliente", nombreClientePet);
        map.put("direccionCliente", direccionClientePet);
        map.put("telefonoCliente", telefonoClientePet);


    mfirestore.collection("pet").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Toast.makeText(getApplicationContext(),"Creado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
        Toast.makeText(getApplicationContext(),"Error al ingresar", Toast.LENGTH_SHORT).show();
            Log.e("ERROR", "Error al ingresar datos: " + e.getMessage());
        }
    });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}