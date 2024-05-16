package com.mpjosemanuel86.conectavet.ui.activities;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mpjosemanuel86.conectavet.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GestionCitaActivity extends AppCompatActivity {

    EditText editTextNombreCliente;
    Button buttonSeleccionarFecha, buttonSeleccionarHora, buttonGuardarCita;
    TextView textViewFechaSeleccionada, textViewHoraSeleccionada;
    Spinner spinnerMascotas;

    Calendar calendar;
    SimpleDateFormat dateFormatter, timeFormatter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cita);

        db = FirebaseFirestore.getInstance();

        editTextNombreCliente = findViewById(R.id.editTextNombreCliente);
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
        consultarMascotas();
    }

    private void consultarMascotas() {
        CollectionReference mascotasRef = db.collection("pet");
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
        String nombreCliente = editTextNombreCliente.getText().toString().trim();
        String fechaCita = textViewFechaSeleccionada.getText().toString().trim();
        String horaCita = textViewHoraSeleccionada.getText().toString().trim();

        if (nombreCliente.isEmpty() || fechaCita.isEmpty() || horaCita.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            // Verificar disponibilidad del intervalo horario
            String intervaloInicio = "09:00"; // Hora de inicio del intervalo
            String intervaloFin = "17:00";   // Hora de fin del intervalo
            String horaCitaInicio = horaCita.substring(0, 5); // Extraer solo la hora de la cita

            if (horaCitaInicio.compareTo(intervaloInicio) < 0 || horaCitaInicio.compareTo(intervaloFin) > 0) {
                // La cita no está dentro del intervalo horario permitido
                Toast.makeText(this, "La cita debe estar dentro del intervalo horario de 09:00 a 17:00", Toast.LENGTH_SHORT).show();
                return;
            }

            // Consultar citas existentes para la fecha y el intervalo horario
            CollectionReference citasRef = db.collection("citas");
            citasRef.whereEqualTo("fechaCita", fechaCita)
                    .whereGreaterThanOrEqualTo("horaCita", intervaloInicio)
                    .whereLessThanOrEqualTo("horaCita", intervaloFin)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    // El intervalo horario está disponible, guardar la cita
                                    Map<String, Object> cita = new HashMap<>();
                                    cita.put("nombreCliente", nombreCliente);
                                    cita.put("fechaCita", fechaCita);
                                    cita.put("horaCita", horaCita);

                                    db.collection("citas")
                                            .add(cita)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(GestionCitaActivity.this, "Cita guardada correctamente", Toast.LENGTH_SHORT).show();
                                                        // Limpiar los campos después de guardar la cita
                                                        editTextNombreCliente.setText("");
                                                        textViewFechaSeleccionada.setText("");
                                                        textViewHoraSeleccionada.setText("");
                                                    } else {
                                                        Toast.makeText(GestionCitaActivity.this, "Error al guardar la cita", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // El intervalo horario está completo, mostrar mensaje
                                    Toast.makeText(GestionCitaActivity.this, "El intervalo horario de " + intervaloInicio + " a " + intervaloFin + " ya está completo para esta fecha", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Error al consultar citas existentes
                                Log.e("TAG", "Error al consultar citas existentes", task.getException());
                                Toast.makeText(GestionCitaActivity.this, "Error al consultar citas existentes", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}