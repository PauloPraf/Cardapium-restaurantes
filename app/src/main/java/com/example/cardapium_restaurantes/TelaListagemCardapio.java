package com.example.cardapium_restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Interfaces.OnGetDataListener;
import Model.CategoriaCardapioModel;
import Model.ProdutoCardapioModel;

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
        fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TelaItem.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setIndicatorBounds(950, 0);
        ArrayList<CategoriaCardapioModel> cardapios = new ArrayList<>();
        getData(new OnGetDataListener() {
            @Override
            public void onSuccess(Iterable<DataSnapshot> dataSnapshotValue) {
                for(DataSnapshot snap : dataSnapshotValue) {
                    String id = snap.child("id").getValue(String.class);
                    String nome = snap.child("name").getValue(String.class);
                    int img = snap.child("image").getValue(Integer.class);
                    CategoriaCardapioModel card = new CategoriaCardapioModel(id, nome, img);
                    for(DataSnapshot child : snap.child("foods").getChildren()) {
                        String titleFood = child.child("titleFood").getValue(String.class);
                        int image = child.child("image").getValue(Integer.class);
                        String preco = child.child("preco").getValue(String.class);

                        card.foods.add(new ProdutoCardapioModel(titleFood, image, preco));
                    }

                    cardapios.add(card);
                }
                CustomAdapter adapter = new CustomAdapter(TelaListagemCardapio.this, cardapios);
                expandableListView.setAdapter(adapter);
                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        Intent it = new Intent(TelaListagemCardapio.this, TelaItem.class);
                        String card = cardapios.get(groupPosition).name;
                        String titleFood = cardapios.get(groupPosition).foods.get(childPosition).titleFood;
                        int image = cardapios.get(groupPosition).foods.get(childPosition).image;
                        String preco = cardapios.get(groupPosition).foods.get(childPosition).preco;
                        it.putExtra("cardFather", card);
                        it.putExtra("title", titleFood);
                        it.putExtra("img", image);
                        it.putExtra("preco", preco);
                        it.putExtra("indexChange", childPosition);
                        startActivity(it);
                        return true;
                    }
                });
            }
            public void onSingleSuccess(DataSnapshot data){

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
            fabAdd.setVisibility(View.VISIBLE);
        }else {
            fabAdd.setVisibility(View.INVISIBLE);
        }
    }
}