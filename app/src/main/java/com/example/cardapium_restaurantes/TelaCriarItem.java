package com.example.cardapium_restaurantes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class TelaCriarItem extends AppCompatActivity {

    public ArrayAdapter<String> adapterSpinner;
    private FirebaseAuth mAuth;
    private Spinner spinnerCategorias;
    private EditText editTextNameProd, editTextPrecoProd;
    private Button btnAddItem;
    private ImageView imageItem;
    private int image = 0;
    String a[] = {"Pizzas salgadas", "Pizzas doces", "Lanches", "Bebidas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar_item);

        spinnerCategorias = findViewById(R.id.spinnerCategoria);
        imageItem = findViewById(R.id.imageItemCriacao);
        adapterSpinner = new ArrayAdapter<>(TelaCriarItem.this, android.R.layout.simple_spinner_item, a);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapterSpinner);
        editTextNameProd = findViewById(R.id.editTextNomeProd);
        editTextPrecoProd = findViewById(R.id.editTextPrecoProd);
        btnAddItem = findViewById(R.id.btnAddItem);
        mAuth = FirebaseAuth.getInstance();

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Drawable myDrawable = getResources().getDrawable(setImage(position));
                imageItem.setImageDrawable(myDrawable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDados();
            }
        });

    }

    private void validarDados() {
        if (editTextNameProd.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "O nome é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editTextPrecoProd.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "O preço é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        addItem();
    }

    private int setImage(int index) {

        int image = 0;
        switch (index) {
            case 0:
                image = R.drawable.pizza_salgada;
                break;
            case 1:
                image = R.drawable.pizza_doce;
                break;
            case 2:
                image = R.drawable.lanche;
                break;
            case 3:
                image = R.drawable.bebida;
                break;
        }

        return image;
    }
    private void addItem() {
        Integer index = spinnerCategorias.getSelectedItemPosition();
        setImage(index);
        CategoriaCardapio c = new CategoriaCardapio(index.toString(), a[index], image);
        ProdutoCardapio pc = new ProdutoCardapio(editTextNameProd.getText().toString(), image, editTextPrecoProd.getText().toString());
        c.foods.add(pc);
        c.salvar(mAuth.getUid());
        finish();
    }
}