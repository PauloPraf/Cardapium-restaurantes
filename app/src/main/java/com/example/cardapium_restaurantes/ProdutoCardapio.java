package com.example.cardapium_restaurantes;

import java.util.ArrayList;

public class ProdutoCardapio {
    public String titleFood;
    public int image;

    public ProdutoCardapio(String name, int image) {
        this.titleFood = name;
        this.image = image;
    }
    @Override
    public String toString() {
        return titleFood;
    }

}
