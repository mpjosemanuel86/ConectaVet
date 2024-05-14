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


public class CrearClienteFragment extends DialogFragment{

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
        View v = inflater.inflate(R.layout.fragment_crear_cliente, container, false);
        mfirestore = FirebaseFirestore.getInstance();

        nombreCliente = v.findViewById(R.id.editTextNombreCliente);
        direccionCliente = v.findViewById(R.id.editTextDireccionCliente);
        telefonoCliente = v.findViewById(R.id.editTextTelefonoCliente);

        btnGuardarDatos = v.findViewById(R.id.btnGuardarDatos2);
        btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreClientePet = nombreCliente.getText().toString().trim();
                String direccionClientePet = direccionCliente.getText().toString().trim();
                String telefonoClientePet = telefonoCliente.getText().toString().trim();


                if (nombreClientePet.isEmpty() && direccionClientePet.isEmpty() && telefonoClientePet.isEmpty()) {
                    Toast.makeText(getContext(), "Ingresar los datos", Toast.LENGTH_SHORT).show();
                } else {
                    postCliente(nombreClientePet, direccionClientePet, telefonoClientePet);
                }
            }
        });


        return v;
    }
    private void postCliente(String nombreClientePet, String direccionClientePet, String telefonoClientePet) {
        Map<String, Object> map =  new HashMap<>();
        map.put("nombreCliente", nombreClientePet);
        map.put("direccionCliente", direccionClientePet);
        map.put("telefonoCliente", telefonoClientePet);


        mfirestore.collection("cliente").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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