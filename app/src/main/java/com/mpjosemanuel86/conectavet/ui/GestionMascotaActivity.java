package com.mpjosemanuel86.conectavet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mpjosemanuel86.conectavet.CrearClienteFragment;
import com.mpjosemanuel86.conectavet.CrearMascotaFragment;
import com.mpjosemanuel86.conectavet.R;

public class GestionMascotaActivity extends AppCompatActivity {

    Button btnAgregarMascota, btn_add_fragment_mascota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_mascota);

        btnAgregarMascota = findViewById(R.id.btnAgregarMascota);
        btn_add_fragment_mascota = findViewById(R.id.btnAgregarMascota2);

        btnAgregarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GestionMascotaActivity.this, CrearMascotaActivity.class));
            }
        });

        btn_add_fragment_mascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearMascotaFragment fm = new CrearMascotaFragment();
                fm.show(getSupportFragmentManager(),"Navegar a fragment");

            }
        });
    }
}