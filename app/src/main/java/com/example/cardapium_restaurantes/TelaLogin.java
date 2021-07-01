package com.example.cardapium_restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TelaLogin extends AppCompatActivity {
    private EditText edtTextTextEmailAddress, edtTextTextPassword;
    private Button bttLogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);
        edtTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        edtTextTextPassword = findViewById(R.id.editTextTextPassword);
        bttLogin = findViewById(R.id.bttLogin);
        mAuth = FirebaseAuth.getInstance();

        bttLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCampos();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Toast.makeText(getApplicationContext(), "Existe uma sessão ativa", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(), TelaDoUsuario.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        edtTextTextPassword.setText("");
    }

    private void validarCampos() {
        if(edtTextTextEmailAddress.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Informe o E-mail", Toast.LENGTH_SHORT).show();
            return;
        }

        if(edtTextTextPassword.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Informe a senha", Toast.LENGTH_SHORT).show();
            return;
        }

        login();
    }

    private void login() {
        mAuth.signInWithEmailAndPassword(edtTextTextEmailAddress.getText().toString(), edtTextTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(null, "signInWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Login efetuado com sucesso!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), TelaDoUsuario.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(null, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Usuário ou senha incorretos",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
    }
}