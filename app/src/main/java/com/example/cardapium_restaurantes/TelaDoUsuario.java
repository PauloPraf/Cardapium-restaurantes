package com.example.cardapium_restaurantes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TelaDoUsuario extends AppCompatActivity {

    Button bttGerCardapio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_do_usuario);

        bttGerCardapio = findViewById(R.id.bttGerCardapio);
        bttGerCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(TelaDoUsuario.this, TelaListagemCardapio.class);
                startActivity(it);
            }
        });
    }
}
