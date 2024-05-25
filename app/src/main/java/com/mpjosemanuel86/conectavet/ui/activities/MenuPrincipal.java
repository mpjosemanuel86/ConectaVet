package com.mpjosemanuel86.conectavet.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mpjosemanuel86.conectavet.R;

public class MenuPrincipal extends AppCompatActivity {

    Button CerrarSesion, botonClientes, botonMascotas, botonCitas, botonVerCitas;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView NombresPrincipal, CorreoPrincipal;
    ProgressBar progressBarDatos;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        NombresPrincipal = findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);
        progressBarDatos = findViewById(R.id.progressBarDatos);

        CerrarSesion = findViewById(R.id.CerrarSesion);
        botonClientes = findViewById(R.id.btnClientes);
        botonMascotas = findViewById(R.id.btnMascotas);
        botonCitas = findViewById(R.id.btnCitas);
        botonVerCitas = findViewById(R.id.btnVerCitas);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        botonClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, GestionClienteActivity.class);
                intent.putExtra("USER_ID", user.getUid());
                startActivity(intent);
            }
        });
        botonMascotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, GestionMascotaActivity.class);
                intent.putExtra("USER_ID", user.getUid());
                startActivity(intent);
            }
        });
        botonCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, GestionCitaActivity.class);
                intent.putExtra("USER_ID", user.getUid());
                startActivity(intent);
            }
        });
        botonVerCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuPrincipal.this, AgendaCitaActivity.class);
                intent.putExtra("USER_ID", user.getUid());
                startActivity(intent);
            }
        });
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalirAplicacion();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ComprobarInicioSesion();
    }

    private void ComprobarInicioSesion() {
        if (user != null) {
            // El usuario ha iniciado sesión
            CargaDeDatos();
        } else {
            // Lo dirigirá al MainActivity
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }

    private void CargaDeDatos() {
        DocumentReference docRef = db.collection("usuarios").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // El progressbar se oculta
                    progressBarDatos.setVisibility(View.GONE);
                    // Los TextView se muestran
                    NombresPrincipal.setVisibility(View.VISIBLE);
                    CorreoPrincipal.setVisibility(View.VISIBLE);

                    // Obtener los datos
                    String nombres = documentSnapshot.getString("nombres");
                    String correo = documentSnapshot.getString("correo");

                    // Setear los datos en los respectivos TextView
                    NombresPrincipal.setText(nombres);
                    CorreoPrincipal.setText(correo);

                    Log.d("MenuPrincipal", "Datos del usuario cargados correctamente");
                } else {
                    Log.e("MenuPrincipal", "El documento del usuario no existe");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar errores aquí
                Log.e("MenuPrincipal", "Error al obtener datos del usuario", e);
                Toast.makeText(MenuPrincipal.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                progressBarDatos.setVisibility(View.GONE); // Oculta el progressBar en caso de error
            }
        });
    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesión con éxito", Toast.LENGTH_SHORT).show();
    }
}
