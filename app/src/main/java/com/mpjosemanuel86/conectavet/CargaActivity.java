package com.mpjosemanuel86.conectavet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Esta actividad se encarga de mostrar una pantalla de carga al inicio de la aplicación
 * y luego inicia la actividad principal después de un cierto tiempo.
 */
public class CargaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establecer el diseño de la pantalla de carga
        setContentView(R.layout.activity_carga);

        // Duración de la pantalla de carga en milisegundos
        int Tiempo = 3000;

        // Utilizar un Handler para retrasar la apertura de la actividad principal
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Crear un Intent para iniciar la actividad principal
                Intent intent = new Intent(CargaActivity.this, MainActivity.class);

                // Iniciar la actividad principal
                startActivity(intent);

                // Finalizar la actividad actual (pantalla de carga)
                finish();
            }
        }, Tiempo);
    }
}
