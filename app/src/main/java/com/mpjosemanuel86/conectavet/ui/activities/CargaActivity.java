package com.mpjosemanuel86.conectavet.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mpjosemanuel86.conectavet.R;

/**
 * Esta actividad se encarga de mostrar una pantalla de carga al inicio de la aplicación
 * y luego inicia la actividad principal después de un cierto tiempo.
 */
public class CargaActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establecer el diseño de la pantalla de carga
        setContentView(R.layout.activity_carga);

        firebaseAuth = FirebaseAuth.getInstance();

        // Duración de la pantalla de carga en milisegundos
        int Tiempo = 3000;

        // Utilizar un Handler para retrasar la apertura de la actividad principal
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Crear un Intent para iniciar la actividad principal
                Intent intent = new Intent(CargaActivity.this, MainActivity.class);

                // Iniciar la actividad principal
                startActivity(intent);

                // Finalizar la actividad actual (pantalla de carga)
                finish();*/
                VerificarUsuario();
            }
        }, Tiempo);
    }
    private void    VerificarUsuario(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser == null){
                    startActivity(new Intent(CargaActivity.this, MainActivity.class));
                    finish();

                }else {
                    String uid = firebaseUser.getUid();
                    Log.d("prueba", uid);
                    Intent intent = new Intent(CargaActivity.this, MenuPrincipal.class);
                    intent.putExtra("USER_ID", uid); // Pasa el UID como extra
                    startActivity(intent);
                    finish();
                }
    }
}
