package com.example.cardapium_restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.UserModel;

public class TelaDoUsuario extends AppCompatActivity {
    private Button logout;
    private TextView ola;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_do_usuario);

        ola = findViewById(R.id.txtOlaUser);
        logout = findViewById(R.id.bttLogout);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getCurrentUserInfo();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), "Até mais!", Toast.LENGTH_LONG);
            }
        });
    }

    private void getCurrentUserInfo() {
        DatabaseReference ref = database.getReference().child("Users").child(mAuth.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nome = snapshot.child("nome").getValue(String.class);
                String sobrenome = snapshot.child("sobrenome").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                currentUser = new UserModel(mAuth.getUid(), nome, sobrenome, email);
                ola.setText("Olá, " + currentUser.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro na recuperação dos seus dados, tente novamente", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}