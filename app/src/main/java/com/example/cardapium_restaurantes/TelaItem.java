package com.example.cardapium_restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Interfaces.OnGetDataListener;
import Model.CategoriaCardapioModel;
import Model.ProdutoCardapioModel;

public class TelaItem extends AppCompatActivity {

    public ArrayAdapter<String> adapterSpinner;
    private static CategoriaCardapioModel c;
    private FirebaseAuth mAuth;
    private Spinner spinnerCategorias;
    private EditText editTextNameProd, editTextPrecoProd;
    private Button btnAddItem;
    private ImageView imageItem;
    private int image = 0;
    private int indexChange = -1;
    private FirebaseDatabase database;

    String a[] = {"Pizzas salgadas", "Pizzas doces", "Lanches", "Bebidas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar_item);

        spinnerCategorias = findViewById(R.id.spinnerCategoria);
        imageItem = findViewById(R.id.imageItemCriacao);
        adapterSpinner = new ArrayAdapter<>(TelaItem.this, android.R.layout.simple_spinner_item, a);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapterSpinner);
        editTextNameProd = findViewById(R.id.editTextNomeProd);
        editTextPrecoProd = findViewById(R.id.editTextPrecoProd);
        btnAddItem = findViewById(R.id.btnAddItem);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            Drawable myDrawable = getResources().getDrawable(extras.getInt("img"));
            imageItem.setImageDrawable(myDrawable);

            int indexFather = getIndex((String) extras.getString("cardFather"));
            spinnerCategorias.setSelection(indexFather);

            editTextNameProd.setText(extras.getString("title"));
            editTextPrecoProd.setText(extras.getString("preco"));
        }

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

    private int getIndex(String s) {
        int i = 0;
        for (String b : a) {
            if(b.equals(s))
                return i;
            i++;
        }
        return -1;
    }

    private int setImage(int index) {

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
        getData(new OnGetDataListener() {

            @Override
            public void onSuccess(Iterable<DataSnapshot> dataSnapshotValue) {
                return;
            }

            @Override
            public void onSingleSuccess(DataSnapshot dataSnapshot) {

                String id = dataSnapshot.getKey();
                String nome = dataSnapshot.child("name").getValue(String.class);
                int img = dataSnapshot.child("image").getValue(Integer.class);
                c = new CategoriaCardapioModel(id, nome, img);
                for (DataSnapshot child : dataSnapshot.child("foods").getChildren()) {
                    String titleFood = child.child("titleFood").getValue(String.class);
                    int image = child.child("image").getValue(Integer.class);
                    String preco = child.child("preco").getValue(String.class);

                    c.foods.add(new ProdutoCardapioModel(titleFood, image, preco));
                }
                setImage(index);
                ProdutoCardapioModel pc = new ProdutoCardapioModel(editTextNameProd.getText().toString(), image, editTextPrecoProd.getText().toString());
                Bundle extras = getIntent().getExtras();

                if(extras != null) {
                  indexChange = extras.getInt("indexChange");
                  c.foods.set(indexChange, pc);
                } else{
                    c.foods.add(pc);
                }

                c.salvar(mAuth.getUid());
                finish();
            }
        }, index.toString());
    }

    private void getData(final OnGetDataListener listener, String index) {
        DatabaseReference ref = database.getReference().child("Cardapios").child(mAuth.getUid()).child(index);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            listener.onSingleSuccess(snapshot);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getApplicationContext(), "Ocorreu um erro na recuperação dos dados, tente novamente", Toast.LENGTH_LONG).show();
        finish();
        }
    }); }
}