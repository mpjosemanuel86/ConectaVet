package com.mpjosemanuel86.conectavet.ui.activities;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.mpjosemanuel86.conectavet.adapter.MyAdapter;
import com.mpjosemanuel86.conectavet.model.Cita;
import com.mpjosemanuel86.conectavet.model.Cliente;
import com.mpjosemanuel86.conectavet.model.Mascota;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GestionCitaActivity extends AppCompatActivity {

    Button buttonSeleccionarFecha, buttonSeleccionarHora, buttonGuardarCita;
    TextView textViewFechaSeleccionada, textViewHoraSeleccionada;
    Spinner spinnerMascotas, clientesSpinner;

    Calendar calendar;
    SimpleDateFormat dateFormatter, timeFormatter;

    FirebaseFirestore mFirestore;
    FirebaseUser currentUser;
    String userFirebaseUID;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cita);

        mFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userFirebaseUID = getIntent().getStringExtra("USER_ID");
        docRef = mFirestore.collection("users").document(userFirebaseUID);


        clientesSpinner = findViewById(R.id.spinnerClientes);
        buttonSeleccionarFecha = findViewById(R.id.buttonSeleccionarFecha);
        buttonSeleccionarHora = findViewById(R.id.buttonSeleccionarHora);
        buttonGuardarCita = findViewById(R.id.buttonGuardarCita);
        textViewFechaSeleccionada = findViewById(R.id.textViewFechaSeleccionada);
        textViewHoraSeleccionada = findViewById(R.id.textViewHoraSeleccionada);
        spinnerMascotas = findViewById(R.id.spinnerMascotas);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        buttonSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });

        buttonSeleccionarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorHora();
            }
        });

        buttonGuardarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCita();
            }
        });
        getClientesUsuarioSelect();
        consultarMascotas();
    }

    private void consultarMascotas() {
        CollectionReference mascotasRef = mFirestore.collection("pet");
        mascotasRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> nombresMascotas = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String nombreMascota = document.getString("nombreMascota");
                        nombresMascotas.add(nombreMascota);
                    }

                    // Llenar el Spinner con los nombres de las mascotas
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(GestionCitaActivity.this,
                            android.R.layout.simple_spinner_item, nombresMascotas);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMascotas.setAdapter(adapter);
                } else {
                    Log.d("TAG", "Error al obtener las mascotas: ", task.getException());
                }
            }
        });
    }

    private void mostrarSelectorFecha() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                textViewFechaSeleccionada.setText(dateFormatter.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void mostrarSelectorHora() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                textViewHoraSeleccionada.setText(timeFormatter.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void guardarCita() {
        String nombreCliente = clientesSpinner.getSelectedItem().toString();
        String fechaCita = textViewFechaSeleccionada.getText().toString().trim();
        String horaCita = textViewHoraSeleccionada.getText().toString().trim();

        if (nombreCliente.isEmpty() || fechaCita.isEmpty() || horaCita.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Verificar disponibilidad del intervalo horario
            String intervaloInicio = "09:00"; // Hora de inicio del intervalo
            String intervaloFin = "17:00";   // Hora de fin del intervalo
            String horaCitaInicio = horaCita.substring(0, 5); // Extraer solo la hora de la cita

            Log.d("horaCitaInicio", fechaCita);
            Log.d("intervaloInicio", intervaloInicio);
            Log.d("intervaloFin", intervaloFin);
            if (horaCitaInicio.compareTo(intervaloInicio) < 0 || horaCitaInicio.compareTo(intervaloFin) > 0) {
                Toast.makeText(this, "La cita debe estar dentro del intervalo horario de 09:00 a 17:00", Toast.LENGTH_SHORT).show();
                return;
            }
            CollectionReference refCli = docRef.collection("clientes");

            refCli.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Cliente obj_cliente = documentSnapshot.toObject(Cliente.class);
                        String cliente_id = documentSnapshot.getString("nombreCliente");
                        if (nombreCliente.equals(cliente_id)) {
                            if (obj_cliente != null && obj_cliente.getNombreCliente() != null) {
                                Log.d("cliente", obj_cliente.getNombreCliente());
                                Log.d("documentSnapshot", documentSnapshot.getId());
                                DocumentReference docRefClient = refCli.document(documentSnapshot.getId());

                                CollectionReference subColRefCita = docRefClient.collection("citas");
                                Cita nuevaCita = new Cita();
                                nuevaCita.setCliente(obj_cliente);
                                nuevaCita.setMascota(null);
                                nuevaCita.setFechaCita(fechaCita); // Establecer la fecha de la cita
                                nuevaCita.setHoraCita(horaCita);
                                nuevaCita.setTipoCita("Rápida");
                                // Añadir la nueva cita a la colección de citas
                                subColRefCita.add(nuevaCita)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("SUCCESS", "Cita añadida con ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("ERROR", "Error al añadir cita: " + e.getMessage());
                                    }
                                });
                            }

                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("ERROR", "Error al obtener datos: " + e.getMessage());
                }
            });
        }
    }

    public void getClientesUsuarioSelect(){
        DocumentReference docRef = mFirestore.collection("users").document(currentUser.getUid());
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(GestionCitaActivity.this,
                            android.R.layout.simple_spinner_item, clientes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    clientesSpinner.setAdapter(adapter);
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}