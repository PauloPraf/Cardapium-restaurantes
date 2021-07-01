package com.example.cardapium_restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Interfaces.OnGetDataListener;
import Model.UserModel;

public class TelaListagemCardapio extends AppCompatActivity {
    private FloatingActionButton fabMore;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabAdd;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private StorageReference storageReference;

    public boolean clicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_listagem_cardapio);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        fabMore = findViewById(R.id.fabMoreAction);
        fabAdd = findViewById(R.id.fabAdd);
        fabEdit = findViewById(R.id.fabEdit);
        fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persistir();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setIndicatorBounds(950, 0);
        ArrayList<CategoriaCardapio> cardapios = new ArrayList<>();
        getData(new OnGetDataListener() {
            @Override
            public void onSuccess(Iterable<DataSnapshot> dataSnapshotValue) {
                for(DataSnapshot snap : dataSnapshotValue) {
                    String nome = snap.child("name").getValue(String.class);
                    int img = snap.child("image").getValue(Integer.class);
                    CategoriaCardapio card = new CategoriaCardapio(nome, img);
                    for(DataSnapshot child : snap.child("foods").getChildren()) {
                        String titleFood = child.child("titleFood").getValue(String.class);
                        int image = child.child("image").getValue(Integer.class);

                        card.foods.add(new ProdutoCardapio(titleFood, image));
                    }

                    cardapios.add(card);
                }
                CustomAdapter adapter = new CustomAdapter(TelaListagemCardapio.this, cardapios);
                expandableListView.setAdapter(adapter);
                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        Toast.makeText(getApplicationContext(), "Ryan", Toast.LENGTH_SHORT);
                        return false;
                    }
                });
            }
        });


    }

    private void getData(final OnGetDataListener listener) {
        DatabaseReference ref = database.getReference().child("Cardapios").child(mAuth.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listener.onSuccess(snapshot.getChildren());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Ocorreu um erro na recuperação dos dados, tente novamente", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void persistir() {
        CategoriaCardapio categoria1 = new CategoriaCardapio("Pizzas salgadas", R.drawable.pizza_salgada);
        categoria1.foods.add(new ProdutoCardapio("Calabresa", R.drawable.pizza_salgada));
        categoria1.foods.add(new ProdutoCardapio("Moda da casa", R.drawable.pizza_salgada));
        categoria1.foods.add(new ProdutoCardapio("Quatro queijos", R.drawable.pizza_salgada));
        categoria1.foods.add(new ProdutoCardapio("Marguerita", R.drawable.pizza_salgada));
        categoria1.foods.add(new ProdutoCardapio("Bacon", R.drawable.pizza_salgada));

        CategoriaCardapio categoria2 = new CategoriaCardapio("Pizzas doces",R.drawable.pizza_doce);
        categoria2.foods.add(new ProdutoCardapio("Brigadeiro", R.drawable.pizza_doce));
        categoria2.foods.add(new ProdutoCardapio("Beijinho", R.drawable.pizza_doce));
        categoria2.foods.add(new ProdutoCardapio("Confete", R.drawable.pizza_doce));
        categoria2.foods.add(new ProdutoCardapio("Dois amores", R.drawable.pizza_doce));
        categoria2.foods.add(new ProdutoCardapio("Romeu e julieta", R.drawable.pizza_doce));

        ArrayList<CategoriaCardapio> allCategories = new ArrayList<CategoriaCardapio>();
        allCategories.add(categoria1);
        allCategories.add(categoria2);

        for( CategoriaCardapio c : allCategories) {
            c.salvar(mAuth.getUid());
        }
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);

        if(!clicked) {
            clicked = true;
        } else {
            clicked = false;
        }
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            fabEdit.setVisibility(View.VISIBLE);
            fabAdd.setVisibility(View.VISIBLE);
        }else {
            fabEdit.setVisibility(View.INVISIBLE);
            fabAdd.setVisibility(View.INVISIBLE);
        }
    }
}