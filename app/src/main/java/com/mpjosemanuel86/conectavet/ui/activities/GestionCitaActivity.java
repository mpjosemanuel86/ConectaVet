package com.mpjosemanuel86.conectavet.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.mpjosemanuel86.conectavet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GestionCitaActivity extends AppCompatActivity {

    EditText editTextNombreCliente;
    EditText editTextMotivoCita;
    Button buttonSeleccionarFecha, buttonGuardarCita;

    TextView textViewFechaSeleccionada;
    Spinner spinnerMascotas, spinnerHorarios, spinnerClientes;

    Calendar calendar;
    SimpleDateFormat dateFormatter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cita);

        db = FirebaseFirestore.getInstance();


        buttonSeleccionarFecha = findViewById(R.id.buttonSeleccionarFecha);
        buttonGuardarCita = findViewById(R.id.buttonGuardarCita);
        textViewFechaSeleccionada = findViewById(R.id.textViewFechaSeleccionada);
        spinnerMascotas = findViewById(R.id.spinnerMascotas);
        spinnerHorarios = findViewById(R.id.spinnerHorarios);
        spinnerClientes = findViewById(R.id.spinnerClientes);
        editTextMotivoCita = findViewById(R.id.editTextMotivoCita);


        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


        buttonSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });

        buttonGuardarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCita();
            }
        });

        consultarClientes(); // Llamada al método para cargar los clientes
        cargarHorariosEnSpinner();
    }


    private void consultarClientes() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String usuarioId = currentUser.getUid();

            CollectionReference clientesRef = db.collection("cliente");
            clientesRef.whereEqualTo("uid", usuarioId) // Filtrar por el UID del usuario autenticado
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> nombresClientes = new ArrayList<>();
                                final List<String> clientesIds = new ArrayList<>();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String nombreCliente = document.getString("nombreCliente");
                                    nombresClientes.add(nombreCliente);
                                    clientesIds.add(document.getId());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(GestionCitaActivity.this,
                                        android.R.layout.simple_spinner_item, nombresClientes);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerClientes.setAdapter(adapter);

                                spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        String clienteId = clientesIds.get(position);
                                        consultarMascotas(clienteId);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // Manejar la falta de selección si es necesario
                                    }
                                });
                            } else {
                                Log.d("TAG", "Error al obtener los clientes: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void consultarMascotas(String clienteId) {
        CollectionReference mascotasRef = db.collection("pet");
        mascotasRef.whereEqualTo("clienteId", clienteId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> nombresMascotas = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombreMascota = document.getString("nombreMascota");
                                nombresMascotas.add(nombreMascota);
                            }

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
    private void cargarHorariosEnSpinner() {
        List<String> horarios = new ArrayList<>();
        for (int hour = 9; hour < 17; hour++) {
            horarios.add(String.format("%02d:00", hour));
            horarios.add(String.format("%02d:30", hour));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, horarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHorarios.setAdapter(adapter);
    }

    private void guardarCita() {
        String nombreCliente = spinnerClientes.getSelectedItem().toString();
        String nombreMascota = spinnerMascotas.getSelectedItem().toString();
        String fechaCita = textViewFechaSeleccionada.getText().toString().trim();
        String horaCita = spinnerHorarios.getSelectedItem().toString().trim();
        String motivoCita = editTextMotivoCita.getText().toString().trim();

        if (nombreCliente.isEmpty() || nombreMascota.isEmpty() || fechaCita.isEmpty() || horaCita.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Convertir la fecha al formato "yyyyMMdd"
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

            try {
                Date date = originalFormat.parse(fechaCita);
                String formattedDate = targetFormat.format(date);

                // Verificar disponibilidad del intervalo horario
                String intervaloInicio = "09:00";
                String intervaloFin = "17:00";

                // Consultar citas existentes para la fecha y el intervalo horario
                CollectionReference citasRef = db.collection("citas");
                citasRef.whereEqualTo("fechaCita", formattedDate)
                        .whereGreaterThanOrEqualTo("horaCita", intervaloInicio)
                        .whereLessThanOrEqualTo("horaCita", intervaloFin)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    boolean horarioDisponible = true;

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String horaExistente = document.getString("horaCita");
                                        if (horaExistente.equals(horaCita)) {
                                            horarioDisponible = false;
                                            break;
                                        }
                                    }

                                    if (horarioDisponible) {
                                        // El horario está disponible, guardar la cita
                                        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        Map<String, Object> cita = new HashMap<>();
                                        cita.put("nombreCliente", nombreCliente);
                                        cita.put("nombreMascota", nombreMascota);
                                        cita.put("fechaCita", formattedDate); // almacenar fecha en formato yyyyMMdd
                                        cita.put("horaCita", horaCita);
                                        cita.put("tipoCita", motivoCita);
                                        cita.put("uid", currentUserID);

                                        db.collection("citas")
                                                .add(cita)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(GestionCitaActivity.this, "Cita guardada correctamente", Toast.LENGTH_SHORT).show();
                                                            editTextMotivoCita.setText("");
                                                            textViewFechaSeleccionada.setText("");
                                                            spinnerHorarios.setSelection(0);
                                                        } else {
                                                            Toast.makeText(GestionCitaActivity.this, "Error al guardar la cita", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(GestionCitaActivity.this, "El horario de " + horaCita + " ya está ocupado para esta fecha", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("TAG", "Error al consultar citas existentes", task.getException());
                                    Toast.makeText(GestionCitaActivity.this, "Error al consultar citas existentes", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al formatear la fecha", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

