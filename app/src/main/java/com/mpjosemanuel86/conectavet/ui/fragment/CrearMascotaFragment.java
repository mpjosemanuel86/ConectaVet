package com.mpjosemanuel86.conectavet.ui.fragment;

import static android.content.ContentValues.TAG;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.model.Cliente;
import com.mpjosemanuel86.conectavet.ui.activities.GestionCitaActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearMascotaFragment extends DialogFragment {

    String id_mascota;
    Button btnGuardarDatos;
    EditText nombreMascota, especieMascota, razaMascota, tamanioMascota, sexoMascota, fechaNacimientoMascota, colorMascota;
    Spinner clientesSpinner;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(getArguments() != null) {
            id_mascota = getArguments().getString("id_mascota");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_crear_mascota, container, false);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        clientesSpinner = v.findViewById(R.id.clientesSpinner);
        nombreMascota = v.findViewById(R.id.editTextNombreMascota);
        especieMascota = v.findViewById(R.id.editTextEspecieMascota);
        razaMascota = v.findViewById(R.id.editTextRazaMascota);
        tamanioMascota = v.findViewById(R.id.editTextTamanioMascota);
        sexoMascota = v.findViewById(R.id.editTextSexoMascota);
        fechaNacimientoMascota = v.findViewById(R.id.editTextFechaNacimientoMascota);
        colorMascota = v.findViewById(R.id.editTextColorMascota);

        btnGuardarDatos = v.findViewById(R.id.buttonGuardarDatos);
        getClientesUsuarioSelect();
        if (id_mascota == null || id_mascota.equals("")) {
            btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreMascotaPet = nombreMascota.getText().toString().trim();
                    String especieMascotaPet = especieMascota.getText().toString().trim();
                    String razaMascotaPet = razaMascota.getText().toString().trim();
                    String tamanioMascotaPet = tamanioMascota.getText().toString().trim();
                    String sexoMascotaPet = sexoMascota.getText().toString().trim();
                    String fechaNacimientoMascotaPet = fechaNacimientoMascota.getText().toString().trim();
                    String colorMascotaPet = colorMascota.getText().toString().trim();
                    String selectedValueCliente = clientesSpinner.getSelectedItem().toString();

                    if (nombreMascotaPet.isEmpty() || especieMascotaPet.isEmpty() || razaMascotaPet.isEmpty() || tamanioMascotaPet.isEmpty() || sexoMascotaPet.isEmpty() || fechaNacimientoMascotaPet.isEmpty() || colorMascotaPet.isEmpty()) {
                        Toast.makeText(getContext(), "Ingresar todos los datos", Toast.LENGTH_SHORT).show();
                    } else {
                        postMascota(nombreMascotaPet, especieMascotaPet, razaMascotaPet, tamanioMascotaPet, sexoMascotaPet, fechaNacimientoMascotaPet, colorMascotaPet, selectedValueCliente);
                    }
                }
            });

        } else {
            //getMascota();
            btnGuardarDatos.setText("update");
            btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreMascotaPet = nombreMascota.getText().toString().trim();
                    String especieMascotaPet = especieMascota.getText().toString().trim();
                    String razaMascotaPet = razaMascota.getText().toString().trim();
                    String tamanioMascotaPet = tamanioMascota.getText().toString().trim();
                    String sexoMascotaPet = sexoMascota.getText().toString().trim();
                    String fechaNacimientoMascotaPet = fechaNacimientoMascota.getText().toString().trim();
                    String colorMascotaPet = colorMascota.getText().toString().trim();

                    if (nombreMascotaPet.isEmpty() || especieMascotaPet.isEmpty() || razaMascotaPet.isEmpty() || tamanioMascotaPet.isEmpty() || sexoMascotaPet.isEmpty() || fechaNacimientoMascotaPet.isEmpty() || colorMascotaPet.isEmpty()) {
                        Toast.makeText(getContext(), "Ingresar todos los datos", Toast.LENGTH_SHORT).show();
                    } else {
                        updateMascota(nombreMascotaPet, especieMascotaPet, razaMascotaPet, tamanioMascotaPet, sexoMascotaPet, fechaNacimientoMascotaPet, colorMascotaPet);
                    }
                }
            });

        }

        return v;
    }

    private void updateMascota(String nombreMascotaPet, String especieMascotaPet, String razaMascotaPet, String tamanioMascotaPet, String sexoMascotaPet, String fechaNacimientoMascotaPet, String colorMascotaPet) {
        if (currentUser != null) {
            String uid = currentUser.getUid(); // UID del veterinario

            Map<String, Object> mascotaData = new HashMap<>();
            mascotaData.put("nombreMascota", nombreMascotaPet);
            mascotaData.put("especieMascota", especieMascotaPet);
            mascotaData.put("razaMascota", razaMascotaPet);
            mascotaData.put("tamanioMascota", tamanioMascotaPet);
            mascotaData.put("sexoMascota", sexoMascotaPet);
            mascotaData.put("fechaNacimientoMascota", fechaNacimientoMascotaPet);
            mascotaData.put("colorMascota", colorMascotaPet);
            //mascotaData.put("uid", uid); // Mantener el UID del veterinario

            // Actualizar mascota
            /*
            mfirestore.collection("pet").document(id_mascota).update(mascotaData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Mascota actualizada exitosamente", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al actualizar datos", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", "Error al ingresar datos: " + e.getMessage());
                }
            })
             */
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void postMascota(String nombreMascotaPet, String especieMascotaPet, String razaMascotaPet, String tamanioMascotaPet, String sexoMascotaPet, String fechaNacimientoMascotaPet, String colorMascotaPet, String selectedValueCliente) {
        if (currentUser != null) {
            String uid = currentUser.getUid(); // Obtener el UID del veterinario actual

            // Agregar la mascota a la colección "pet" con el UID del veterinario
            //DocumentReference petsRef = mfirestore.collection("pet").document();
            Map<String, Object> mascotaData = new HashMap<>();
            mascotaData.put("nombreMascota", nombreMascotaPet);
            mascotaData.put("especieMascota", especieMascotaPet);
            mascotaData.put("razaMascota", razaMascotaPet);
            mascotaData.put("tamanioMascota", tamanioMascotaPet);
            mascotaData.put("sexoMascota", sexoMascotaPet);
            mascotaData.put("fechaNacimientoMascota", fechaNacimientoMascotaPet);
            mascotaData.put("colorMascota", colorMascotaPet);

            DocumentReference docRef = mfirestore.collection("users").document(uid);
            CollectionReference subColRef = docRef.collection("clientes");
            subColRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("selectedValueCliente", selectedValueCliente);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String cliente_id = document.getString("nombreCliente");
                                    if(selectedValueCliente.equals(cliente_id)) {
                                        Log.d("Cliente_id", cliente_id);
                                        CollectionReference subColRef2 = document.getReference().collection("pets");
                                        subColRef2.add(mascotaData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                            public void onSuccess(DocumentReference clienteRef) {
                                                Log.d("postMascota", "Mascota creada con ID: " + clienteRef.getId());
                                                Toast.makeText(getContext(), "Mascota creada exitosamente", Toast.LENGTH_SHORT).show();
                                                getDialog().dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Error al ingresar datos", Toast.LENGTH_SHORT).show();
                                                Log.e("ERROR", "Error al ingresar datos: " + e.getMessage());
                                            }
                                        });
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    public void getClientesUsuarioSelect(){
        DocumentReference docRef = mfirestore.collection("users").document(currentUser.getUid());
        CollectionReference subColRef = docRef.collection("clientes");
        subColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> clientes = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String nombreCliente = document.getString("nombreCliente");
                        clientes.add(nombreCliente);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_spinner_item, clientes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientesSpinner.setAdapter(adapter);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }

    private void getMascota() {
        mfirestore.collection("pet").document(id_mascota).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombreMascotaText = documentSnapshot.getString("nombreMascota");
                String especieMascotaText = documentSnapshot.getString("especieMascota");
                String razaMascotaText = documentSnapshot.getString("razaMascota");
                String tamanioMascotaText = documentSnapshot.getString("tamanioMascota");
                String sexoMascotaText = documentSnapshot.getString("sexoMascota");
                String fechaNacimientoMascotaText = documentSnapshot.getString("fechaNacimientoMascota");
                String colorMascotaText = documentSnapshot.getString("colorMascota");

                nombreMascota.setText(nombreMascotaText);
                especieMascota.setText(especieMascotaText);
                razaMascota.setText(razaMascotaText);
                tamanioMascota.setText(tamanioMascotaText);
                sexoMascota.setText(sexoMascotaText);
                fechaNacimientoMascota.setText(fechaNacimientoMascotaText);
                colorMascota.setText(colorMascotaText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
