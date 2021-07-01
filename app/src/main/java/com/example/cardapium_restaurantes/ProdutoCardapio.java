package com.example.cardapium_restaurantes;

public class ProdutoCardapio {
    public String id;
    public String titleFood;
    public int image;

    public ProdutoCardapio() {}

    public ProdutoCardapio(String name, int image) {
        this.titleFood = name;
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitleFood(String titleFood) {
        this.titleFood = titleFood;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return titleFood;
    }

}
