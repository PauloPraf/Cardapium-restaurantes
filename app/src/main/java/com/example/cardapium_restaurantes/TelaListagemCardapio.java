package com.example.cardapium_restaurantes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TelaListagemCardapio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_listagem_cardapio);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setIndicatorBounds(950, 0);
        ArrayList<CategoriaCardapio> categorias = getData();
        CustomAdapter adapter = new CustomAdapter(this, categorias);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), "Ryan", Toast.LENGTH_SHORT);
                return false;
            }
        });
    }
    private ArrayList<CategoriaCardapio> getData() {
        CategoriaCardapio categoria1 = new CategoriaCardapio("Pizzas salgadas", R.drawable.pizza_salgada);
        categoria1.foods.add(new ProdutoCardapio("Calabresa", R.drawable.pizza_salgada));
        categoria1.foods.add(new ProdutoCardapio("Moda da casa", R.drawable.pizza_salgada));
        categoria1.foods.add(new ProdutoCardapio("Quatro queijos", R.drawable.pizza_salgada));
        categoria1.foods.add(new ProdutoCardapio("Marguerita", R.drawable.pizza_salgada));
        categoria1.foods.add(new ProdutoCardapio("Bacon", R.drawable.pizza_salgada));

        CategoriaCardapio categoria2 = new CategoriaCardapio("Pizzas doces", R.drawable.pizza_doce);
        categoria2.foods.add(new ProdutoCardapio("Brigadeiro", R.drawable.pizza_doce));
        categoria2.foods.add(new ProdutoCardapio("Beijinho", R.drawable.pizza_doce));
        categoria2.foods.add(new ProdutoCardapio("Confete", R.drawable.pizza_doce));
        categoria2.foods.add(new ProdutoCardapio("Dois amores", R.drawable.pizza_doce));
        categoria2.foods.add(new ProdutoCardapio("Romeu e julieta", R.drawable.pizza_doce));

        ArrayList<CategoriaCardapio> allCategories = new ArrayList<CategoriaCardapio>();
        allCategories.add(categoria1);
        allCategories.add(categoria2);
        return allCategories;
    }
}