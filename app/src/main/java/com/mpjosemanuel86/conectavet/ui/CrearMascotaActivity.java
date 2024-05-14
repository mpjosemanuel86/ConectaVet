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

public class CrearMascotaActivity extends AppCompatActivity {


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

        nombreMascota = findViewById(R.id.editTextNombreMascota);
        especieMascota = findViewById(R.id.editTextEspecieMascota);
        razaMascota = findViewById(R.id.editTextRazaMascota);
        tamanioMascota = findViewById(R.id.editTextTamanioMascota);
        sexoMascota = findViewById(R.id.editTextSexoMascota);
        fechaNacimientoMascota = findViewById(R.id.editTextFechaNacimientoMascota);
        colorMascota = findViewById(R.id.editTextColorMascota);

        btnGuardarDatos = findViewById(R.id.buttonGuardarDatos);

        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreMascotaPet = nombreMascota.getText().toString().trim();
                String especieMascotaPet = especieMascota.getText().toString().trim();
                String razaMascotaPet = razaMascota.getText().toString().trim();
                String tamanioMascotaPet = tamanioMascota.getText().toString().trim();
                String fechaNacimientoMascotaPet = fechaNacimientoMascota.getText().toString().trim();
                String colorMascotaPet = colorMascota.getText().toString().trim();

                if (nombreMascotaPet.isEmpty() && especieMascotaPet.isEmpty() && razaMascotaPet.isEmpty() && tamanioMascotaPet.isEmpty() && fechaNacimientoMascotaPet.isEmpty() && colorMascotaPet.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                } else {
                    postPet(nombreMascotaPet, especieMascotaPet, razaMascotaPet, tamanioMascotaPet, fechaNacimientoMascotaPet, colorMascotaPet);
                }
            }
        });
    }

    private void postPet(String nombreMascotaPet, String especieMascotaPet, String razaMascotaPet, String tamanioMascotaPet, String fechaNacimientoMascotaPet, String colorMascotaPet) {
        Map<String, Object> map =  new HashMap<>();
        map.put("nombreMascota", nombreMascotaPet);
        map.put("especieMascota", especieMascotaPet);
        map.put("razaMascota", razaMascotaPet);
        map.put("tamanioMascota", tamanioMascotaPet);
        map.put("fechaNacimientoMascota", fechaNacimientoMascotaPet);
        map.put("colorMascota", colorMascotaPet);

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