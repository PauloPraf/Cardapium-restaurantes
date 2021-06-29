package com.example.cardapium_restaurantes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomAdapter extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<CategoriaCardapio> categoriaCardapio;
    private LayoutInflater inflater;

    public CustomAdapter(Context c, ArrayList<CategoriaCardapio> categoriaCardapio) {
        this.c = c;
        this.categoriaCardapio = categoriaCardapio;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return categoriaCardapio.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoriaCardapio.get(groupPosition).foods.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoriaCardapio.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categoriaCardapio.get(groupPosition).foods.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_categoria, null);
        }
        // set category
        CategoriaCardapio categoriaCardapio = (CategoriaCardapio) getGroup(groupPosition);

        // set category items
        TextView titleFood = (TextView) convertView.findViewById(R.id.titleCategory);
        ImageView imageFood = (ImageView) convertView.findViewById(R.id.imageCategory);

        String nameFood =  categoriaCardapio.name;
        int pathImage = categoriaCardapio.image;
        titleFood.setText(nameFood);

        //// fazer ficar dinamico de acordo com a categoria:: pode usar if's/else, porem fica feio
        imageFood.setImageResource(pathImage);
        convertView.setBackgroundColor(Color.parseColor("#F27405"));

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.activity_food, null);
        }
        ProdutoCardapio child = (ProdutoCardapio) getChild(groupPosition, childPosition);
        Log.i(child.toString(), "child");
        TextView titleFood = (TextView) convertView.findViewById(R.id.titleFood);
        ImageView imageFood = (ImageView) convertView.findViewById(R.id.imageFood);
//        String nameTest = categoriaCardapio
        imageFood.setImageResource(child.image);

        titleFood.setText(child.titleFood);


//        String categoryName = getGroup(groupPosition).toString();

//        imageFood.setImageResource(R.drawable.mini_logo_cardapium);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
