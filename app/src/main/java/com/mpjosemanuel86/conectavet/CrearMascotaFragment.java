package com.mpjosemanuel86.conectavet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CrearMascotaFragment extends DialogFragment{

    Button btnGuardarDatos;
    EditText nombreCliente, direccionCliente, telefonoCliente, nombreMascota, especieMascota, razaMascota, tamanioMascota, sexoMascota, fechaNacimientoMascota, colorMascota;
    private FirebaseFirestore mfirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crear_mascota, container, false);
        mfirestore = FirebaseFirestore.getInstance();

        nombreMascota = v.findViewById(R.id.editTextNombreMascota);
        especieMascota = v.findViewById(R.id.editTextEspecieMascota);
        razaMascota = v.findViewById(R.id.editTextRazaMascota);
        tamanioMascota = v.findViewById(R.id.editTextTamanioMascota);
        sexoMascota = v.findViewById(R.id.editTextSexoMascota);
        fechaNacimientoMascota = v.findViewById(R.id.editTextFechaNacimientoMascota);
        colorMascota = v.findViewById(R.id.editTextColorMascota);

        btnGuardarDatos = v.findViewById(R.id.buttonGuardarDatos);
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
                    Toast.makeText(getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                } else {
                    postPet(nombreMascotaPet, especieMascotaPet, razaMascotaPet, tamanioMascotaPet, fechaNacimientoMascotaPet, colorMascotaPet);
                }
            }
        });


        return v;
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
                Toast.makeText(getContext(),"Creado exitosamente", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al ingresar", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "Error al ingresar datos: " + e.getMessage());
            }
        });
    }
}