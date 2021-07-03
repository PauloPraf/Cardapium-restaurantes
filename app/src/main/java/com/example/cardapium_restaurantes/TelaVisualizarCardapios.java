package com.example.cardapium_restaurantes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
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

public class TelaVisualizarCardapios extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_visualizar_cardapios);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView2);
        expandableListView.setIndicatorBounds(950, 0);
        ArrayList<CategoriaCardapioModel> cardapios = new ArrayList<>();
        getData(new OnGetDataListener() {
            @Override
            public void onSuccess(Iterable<DataSnapshot> dataSnapshotValue) {
                for(DataSnapshot data : dataSnapshotValue) {
                    if(data.getKey().equals(mAuth.getUid())) continue;
                    for(DataSnapshot snap : data.getChildren()) {
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
                }
                CustomAdapter adapter = new CustomAdapter(TelaVisualizarCardapios.this, cardapios);
                expandableListView.setAdapter(adapter);
            }

            @Override
            public void onSingleSuccess(DataSnapshot dataSnapshot) {

            }
        }, new Integer(0));
    }

    private void getData(final OnGetDataListener listener, Integer integer) {
        DatabaseReference ref = database.getReference().child("Cardapios");
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
}