package com.example.cardapium_restaurantes;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoriaCardapio {
    private String id;
    public String name;
    public int image;
    public ArrayList<ProdutoCardapio> foods = new ArrayList<ProdutoCardapio>();
    public CategoriaCardapio(String id ,String name, int image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }
    @Override
    public String toString() {
        return name;
    }

    public void salvar(String uId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Cardapios").child(uId).child(this.id).setValue(this);
    }
}
