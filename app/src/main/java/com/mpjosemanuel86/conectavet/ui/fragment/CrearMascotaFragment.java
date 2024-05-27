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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mpjosemanuel86.conectavet.R;
import com.mpjosemanuel86.conectavet.model.Cliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrearMascotaFragment extends DialogFragment {

    String id_mascota;
    Button btnGuardarDatos;
    EditText nombreMascota, especieMascota, razaMascota, tamanioMascota, sexoMascota, fechaNacimientoMascota, colorMascota;
    private FirebaseFirestore mfirestore;
    private static final String TAG = "CrearMascotaFragment";
    private FirebaseAuth mAuth;
    private Spinner spinnerDocumentos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        nombreMascota = v.findViewById(R.id.editTextNombreMascota);
        especieMascota = v.findViewById(R.id.editTextEspecieMascota);
        razaMascota = v.findViewById(R.id.editTextRazaMascota);
        tamanioMascota = v.findViewById(R.id.editTextTamanioMascota);
        sexoMascota = v.findViewById(R.id.editTextSexoMascota);
        fechaNacimientoMascota = v.findViewById(R.id.editTextFechaNacimientoMascota);
        colorMascota = v.findViewById(R.id.editTextColorMascota);

        btnGuardarDatos = v.findViewById(R.id.buttonGuardarDatos);
        spinnerDocumentos = v.findViewById(R.id.clientesSpinner);


        List<String> nombresClientes = new ArrayList<>();


        Spinner spinnerDocumentos = v.findViewById(R.id.clientesSpinner);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid(); // Obtener el UID del usuario actual

            // Realizar la consulta condicional en Firestore
            mfirestore.collection("cliente")
                    .whereEqualTo("uid", uid) // Filtrar por UID del usuario actual
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<String> nombresClientes = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String nombreCliente = document.getString("nombreCliente");
                                nombresClientes.add(nombreCliente);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, nombresClientes);
                            spinnerDocumentos.setAdapter(adapter);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error al obtener documentos", e);
                        }
                    });
        } else {
            // Manejar el caso en que el usuario no esté autenticado
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }


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

                    if (nombreMascotaPet.isEmpty() || especieMascotaPet.isEmpty() || razaMascotaPet.isEmpty() || tamanioMascotaPet.isEmpty() || sexoMascotaPet.isEmpty() || fechaNacimientoMascotaPet.isEmpty() || colorMascotaPet.isEmpty()) {
                        Toast.makeText(getContext(), "Ingresar todos los datos", Toast.LENGTH_SHORT).show();
                    } else {
                        postMascota(nombreMascotaPet, especieMascotaPet, razaMascotaPet, tamanioMascotaPet, sexoMascotaPet, fechaNacimientoMascotaPet, colorMascotaPet);
                    }
                }
            });

        } else {
            getMascota();
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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
            mascotaData.put("uid", uid); // Mantener el UID del veterinario

            // Actualizar mascota
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
            });
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void postMascota(String nombreMascotaPet, String especieMascotaPet, String razaMascotaPet, String tamanioMascotaPet, String sexoMascotaPet, String fechaNacimientoMascotaPet, String colorMascotaPet) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid(); // Obtener el UID del veterinario actual

            // Obtener el nombre del cliente seleccionado en el Spinner
            String nombreClienteSeleccionado = spinnerDocumentos.getSelectedItem().toString();

            // Buscar el documento del cliente correspondiente en Firestore
            mfirestore.collection("cliente")
                    .whereEqualTo("nombreCliente", nombreClienteSeleccionado)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // Suponiendo que solo hay un cliente con el mismo nombre
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot clienteDocumentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                                // Obtener la referencia al documento del cliente
                                String clienteId = clienteDocumentSnapshot.getId();

                                // Crear un mapa con los datos de la mascota
                                Map<String, Object> mascotaData = new HashMap<>();
                                mascotaData.put("nombreMascota", nombreMascotaPet);
                                mascotaData.put("especieMascota", especieMascotaPet);
                                mascotaData.put("razaMascota", razaMascotaPet);
                                mascotaData.put("tamanioMascota", tamanioMascotaPet);
                                mascotaData.put("sexoMascota", sexoMascotaPet);
                                mascotaData.put("fechaNacimientoMascota", fechaNacimientoMascotaPet);
                                mascotaData.put("colorMascota", colorMascotaPet);
                                mascotaData.put("uid", uid); // Incluir el UID del veterinario
                                mascotaData.put("clienteId", clienteId); // Incluir la referencia al documento del cliente

                                // Guardar la información de la mascota en un nuevo documento en la colección "pet"
                                mfirestore.collection("pet")
                                        .add(mascotaData)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference mascotaRef) {
                                                Log.d(TAG, "Mascota creada con ID: " + mascotaRef.getId());
                                                Toast.makeText(getContext(), "Mascota creada exitosamente", Toast.LENGTH_SHORT).show();
                                                getDialog().dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Error al ingresar datos", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "Error al ingresar datos de la mascota: " + e.getMessage());
                                            }
                                        });
                            } else {
                                Log.e(TAG, "No se encontró ningún cliente con el nombre seleccionado: " + nombreClienteSeleccionado);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error al obtener datos del cliente", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error al obtener datos del cliente: " + e.getMessage());
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
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
