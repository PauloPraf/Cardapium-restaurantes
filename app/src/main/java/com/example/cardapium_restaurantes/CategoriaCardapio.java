package com.example.cardapium_restaurantes;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class CategoriaCardapio {
    public String name;
    public int image;
    public ArrayList<ProdutoCardapio> foods = new ArrayList<ProdutoCardapio>();
    public CategoriaCardapio(String name, int image) {
        this.name = name;
        this.image = image;
    }
    @Override
    public String toString() {
        return name;
    }

}
