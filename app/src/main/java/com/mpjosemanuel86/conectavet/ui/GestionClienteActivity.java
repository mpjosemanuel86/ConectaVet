package com.mpjosemanuel86.conectavet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mpjosemanuel86.conectavet.R;

public class GestionClienteActivity extends AppCompatActivity {

    Button btnAgregarCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cliente);

        btnAgregarCliente = findViewById(R.id.btnAgregarCliente);

        btnAgregarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GestionClienteActivity.this, CrearClienteActivity.class));
            }
        });

    }
}