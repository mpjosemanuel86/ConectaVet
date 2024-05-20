package com.mpjosemanuel86.conectavet.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.model.Cliente;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CrearMascotaFragment extends DialogFragment {

    Button btnGuardarDatos;
    EditText nombreMascota, especieMascota, razaMascota, tamanioMascota, sexoMascota, fechaNacimientoMascota, colorMascota;
    Spinner clientesSpinner;
    private FirebaseFirestore mfirestore;
    ArrayAdapter<Cliente> clientesAdapter;

    private OnMascotaSavedListener listener;

    public interface OnMascotaSavedListener {
        void onMascotaSaved();
    }

    public void setOnMascotaSavedListener(OnMascotaSavedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crear_mascota, container, false);
        mfirestore = FirebaseFirestore.getInstance();

        nombreMascota = v.findViewById(R.id.editTextNombreMascota);
        especieMascota = v.findViewById(R.id.editTextEspecieMascota);
        razaMascota = v.findViewById(R.id.editTextRazaMascota);
        tamanioMascota = v.findViewById(R.id.editTextTamanioMascota);
        sexoMascota = v.findViewById(R.id.editTextSexoMascota);
        fechaNacimientoMascota = v.findViewById(R.id.editTextFechaNacimientoMascota);
        colorMascota = v.findViewById(R.id.editTextColorMascota);
        clientesSpinner = v.findViewById(R.id.clientesSpinner);

        clientesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        clientesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientesSpinner.setAdapter(clientesAdapter);

        cargarClientes();

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
                Cliente clienteSeleccionado = (Cliente) clientesSpinner.getSelectedItem();

                if (nombreMascotaPet.isEmpty() || especieMascotaPet.isEmpty() || razaMascotaPet.isEmpty() || tamanioMascotaPet.isEmpty() || fechaNacimientoMascotaPet.isEmpty() || colorMascotaPet.isEmpty()) {
                    Toast.makeText(getContext(), "Ingresar todo los datos de la mascota", Toast.LENGTH_SHORT).show();
                } else {
                    postPet(nombreMascotaPet, especieMascotaPet, razaMascotaPet, tamanioMascotaPet, fechaNacimientoMascotaPet, colorMascotaPet, clienteSeleccionado.getUid());
                }
            }
        });

        return v;
    }

    private void cargarClientes() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String veterinarioUid = currentUser.getUid();

            FirebaseFirestore.getInstance().collection("cliente")
                    .whereEqualTo("uid", veterinarioUid)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String uid = document.getId();
                                    String nombreCliente = document.getString("nombreCliente");
                                    String direccionCliente = document.getString("direccionCliente");
                                    String telefonoCliente = document.getString("telefonoCliente");

                                    Cliente cliente = new Cliente(uid, nombreCliente, direccionCliente, telefonoCliente);
                                    clientesAdapter.add(cliente);
                                }
                                clientesAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("Error", "Error al obtener los clientes: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void postPet(String nombreMascotaPet, String especieMascotaPet, String razaMascotaPet, String tamanioMascotaPet, String fechaNacimientoMascotaPet, String colorMascotaPet, String clienteUid) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String veterinarioUid = currentUser.getUid(); // Obtener el UID del veterinario actual

            // Agregar la mascota a la colección "pet" con el UID del veterinario
            DocumentReference petsRef = mfirestore.collection("pet").document();
            Map<String, Object> mascotaData = new HashMap<>();
            mascotaData.put("nombreMascota", nombreMascotaPet);
            mascotaData.put("especieMascota", especieMascotaPet);
            mascotaData.put("razaMascota", razaMascotaPet);
            mascotaData.put("tamanioMascota", tamanioMascotaPet);
            mascotaData.put("fechaNacimientoMascota", fechaNacimientoMascotaPet);
            mascotaData.put("colorMascota", colorMascotaPet);
            mascotaData.put("uid", veterinarioUid); // Incluir el UID del veterinario

            petsRef.set(mascotaData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Mascota creada exitosamente en la colección pets", Toast.LENGTH_SHORT).show();
                            if (listener != null) {
                                listener.onMascotaSaved(); // Notificar al listener
                            }
                            getDialog().dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error al crear mascota en la colección pets", Toast.LENGTH_SHORT).show();
                            Log.e("ERROR", "Error al crear mascota en la colección pets: " + e.getMessage());
                        }
                    });

            // Agregar la mascota a la subcolección de mascotas del cliente seleccionado
            DocumentReference clienteRef = mfirestore.collection("cliente").document(clienteUid);
            clienteRef.collection("mascotas").add(mascotaData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getContext(), "Mascota creada exitosamente para el cliente", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error al crear mascota para el cliente", Toast.LENGTH_SHORT).show();
                            Log.e("ERROR", "Error al crear mascota para el cliente: " + e.getMessage());
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
