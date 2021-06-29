package com.example.cardapium_restaurantes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TelaListagemCardapio extends AppCompatActivity {
    private FloatingActionButton fabMore;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabAdd;

//    private Animation rotateOpen = AnimationUtils.loadAnimation(TelaListagemCardapio.this, R.anim.rotate_open_anim);
//    private Animation rotateClose = AnimationUtils.loadAnimation(TelaListagemCardapio.this, R.anim.rotate_close_anim);
//    private Animation fromBottom = AnimationUtils.loadAnimation(TelaListagemCardapio.this, R.anim.from_bottom_anim);
//    private Animation toBottom = AnimationUtils.loadAnimation(TelaListagemCardapio.this, R.anim.to_bottom_anim);

    public boolean clicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_listagem_cardapio);
        fabMore = findViewById(R.id.fabMoreAction);
        fabAdd = findViewById(R.id.fabAdd);
        fabEdit = findViewById(R.id.fabEdit);
        fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent it = new Intent(TelaListagemCardapio.this, TelaCriarCategoria.class);
//                startActivity(it);
                onAddButtonClicked();
            }
        });
//        if (fabMore.getVisibility() == View.VISIBLE) {
//            fabEdit.setAnimation(AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim));
//            fabAdd.setAnimation(AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim));
//            fabMore.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim));
//        }else {
//            fabEdit.setAnimation(AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim));
//            fabAdd.setAnimation(AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim));
//            fabMore.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim));
//        }
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

    private void onAddButtonClicked() {
        setVisibility(clicked);
//        setAnimation(clicked);
        if(!clicked) {
            clicked = true;
        } else {
            clicked = false;
        }
    }
//
//    private void setAnimation(boolean clicked) {
//      if (!clicked) {
//          fabEdit.startAnimation(fromBottom);
//          fabAdd.startAnimation(fromBottom);
//          fabMore.startAnimation(rotateOpen);
//      }else {
//          fabEdit.startAnimation(toBottom);
//          fabAdd.startAnimation(toBottom);
//          fabMore.startAnimation(rotateClose);
//      }
//    }
//
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