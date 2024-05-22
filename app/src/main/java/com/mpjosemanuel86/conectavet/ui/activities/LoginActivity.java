package com.mpjosemanuel86.conectavet.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mpjosemanuel86.conectavet.R;

public class LoginActivity extends AppCompatActivity {

    EditText CorreoLogin, PassLogin;
    Button Btn_login2;
    TextView UsuarioNuevoTXT;


    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;



    //Validar los datos
    String  correo = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Login");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        CorreoLogin = findViewById(R.id.CorreoLogin);
        PassLogin = findViewById(R.id.PassLogin);
        Btn_login2 = findViewById(R.id.Btn_login2);
        UsuarioNuevoTXT = findViewById(R.id.UsuarioNuevoTXT);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        Btn_login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
                
            }
        });

        UsuarioNuevoTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class ));
            }
        });
    }

    private void ValidarDatos() {
        
        correo = CorreoLogin.getText().toString();
        password = PassLogin.getText().toString();
        
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this,"Correo inválido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }
        else {
            LoginDeUsuario();
        }
    }

    private void LoginDeUsuario() {
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo,password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MenuPrincipal.class);
                            if(user != null) {
                                intent.putExtra("USER_ID", user.getUid());
                                startActivity(intent);
                            }

                            Toast.makeText(LoginActivity.this, "Bienvenid@: "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Verifique si el correo y la contraseña son correctos", Toast.LENGTH_SHORT).show();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}