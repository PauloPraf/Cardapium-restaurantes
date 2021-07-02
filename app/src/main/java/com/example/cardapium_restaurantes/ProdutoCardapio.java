package com.example.cardapium_restaurantes;

public class ProdutoCardapio {
    public String id;
    public String titleFood;
    public String preco;
    public int image;

    public ProdutoCardapio() {}

    public ProdutoCardapio(String name, int image, String preco) {
        this.titleFood = name;
        this.image = image;
        this.preco = preco;
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
