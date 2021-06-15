package com.example.cardapium_restaurantes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cardapium_restaurantes.R;

public class MainActivity extends AppCompatActivity {
    private Button btt_login, btt_cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btt_login = findViewById(R.id.btt_login);
        btt_cadastrar = findViewById(R.id.btt_Cadastrar);

        btt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaLogin.class));
            }
        });

        btt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaCadastro.class));
            }
        });
    }
}