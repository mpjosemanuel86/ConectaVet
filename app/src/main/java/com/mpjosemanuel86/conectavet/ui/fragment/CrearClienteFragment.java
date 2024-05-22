package com.mpjosemanuel86.conectavet.ui.fragment;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mpjosemanuel86.conectavet.R;
import java.util.HashMap;
import java.util.Map;

public class CrearClienteFragment extends DialogFragment {

    String id_cliente;
    Button btnGuardarDatos;
    EditText nombreCliente, direccionCliente, telefonoCliente;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            id_cliente = getArguments().getString("id_cliente");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_crear_cliente, container, false);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        nombreCliente = v.findViewById(R.id.editTextNombreCliente);
        direccionCliente = v.findViewById(R.id.editTextDireccionCliente);
        telefonoCliente = v.findViewById(R.id.editTextTelefonoCliente);
        btnGuardarDatos = v.findViewById(R.id.btnGuardarDatos2);

        if(id_cliente == null||id_cliente==""){
            btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreClientePet = nombreCliente.getText().toString().trim();
                    String direccionClientePet = direccionCliente.getText().toString().trim();
                    String telefonoClientePet = telefonoCliente.getText().toString().trim();

                    if (nombreClientePet.isEmpty() || direccionClientePet.isEmpty() || telefonoClientePet.isEmpty()) {
                        Toast.makeText(getContext(), "Ingresar todos los datos", Toast.LENGTH_SHORT).show();
                    } else {
                        postCliente(nombreClientePet, direccionClientePet, telefonoClientePet);
                    }
                }
            });

        }else{
            getcliente();
            btnGuardarDatos.setText("update");

            btnGuardarDatos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreClientePet = nombreCliente.getText().toString().trim();
                    String direccionClientePet = direccionCliente.getText().toString().trim();
                    String telefonoClientePet = telefonoCliente.getText().toString().trim();

                    if (nombreClientePet.isEmpty() || direccionClientePet.isEmpty() || telefonoClientePet.isEmpty()) {
                        Toast.makeText(getContext(), "Ingresar todos los datos", Toast.LENGTH_SHORT).show();
                    } else {
                        updateCliente(nombreClientePet, direccionClientePet, telefonoClientePet);
                    }
                }
            });
        }


        return v;
    }




    private void updateCliente(String nombreClientePet, String direccionClientePet, String telefonoClientePet) {

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String uid = currentUser.getUid(); // UID del veterinario

                Map<String, Object> clienteData = new HashMap<>();
                clienteData.put("nombreCliente", nombreClientePet);
                clienteData.put("direccionCliente", direccionClientePet);
                clienteData.put("telefonoCliente", telefonoClientePet);
                //clienteData.put("uid", uid); // Mantener el UID del veterinario

                // Crear cliente

                mfirestore.collection("cliente").document(id_cliente).update(clienteData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Cliente actualizado exitosamente", Toast.LENGTH_SHORT).show();
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
    private void postCliente(String nombreClientePet, String direccionClientePet, String telefonoClientePet) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid(); // UID del veterinario

            Map<String, Object> clienteData = new HashMap<>();
            clienteData.put("nombreCliente", nombreClientePet);
            clienteData.put("direccionCliente", direccionClientePet);
            clienteData.put("telefonoCliente", telefonoClientePet);
            //clienteData.put("uid", uid); // Mantener el UID del veterinario

            // Crear cliente
            DocumentReference docRef = mfirestore.collection("users").document(uid);
            CollectionReference subColRef = docRef.collection("clientes");
            subColRef.add(clienteData)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("postCliente", "Cliente creado con ID: " + documentReference.getId());
                    Toast.makeText(getContext(), "Cliente creado exitosamente", Toast.LENGTH_SHORT).show();
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
            /*
            mfirestore.collection("cliente")
                    .add(clienteData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference clienteRef) {
                            Log.d("postCliente", "Cliente creado con ID: " + clienteRef.getId());
                            Toast.makeText(getContext(), "Cliente creado exitosamente", Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error al ingresar datos", Toast.LENGTH_SHORT).show();
                            Log.e("ERROR", "Error al ingresar datos: " + e.getMessage());
                        }
                    })
             */
        } else {
            Toast.makeText(getContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
    }

    private void getcliente() {
        mfirestore.collection("cliente").document(id_cliente).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombreClienteText = documentSnapshot.getString("nombreCliente");
                String direccionClienteText = documentSnapshot.getString("direccionCliente");
                String telefonoClienteText = documentSnapshot.getString("telefonoCliente");

                nombreCliente.setText(nombreClienteText);
                direccionCliente.setText(direccionClienteText);
                telefonoCliente.setText(telefonoClienteText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    }



