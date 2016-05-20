package com.innoble.stocknbarrel;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kemron on 20/05/2016.
 */

// Shopping List Custom Adapter
public class ListItemAdapter extends ArrayAdapter <String>{
    private final Activity context;
    private Model[] models;

    private final int MIN_VAL = 1;
    private final int MAX_VAL = 100;




    public ListItemAdapter(Activity context, Model[] models) {
        super(context, R.layout.shopping_list_single);
        List<String> texts = new ArrayList<>();
        for(Model m: models){
            texts.add(m.title);
        }
        this.addAll(texts);
        this.context = context;
        this.models= models;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View rootView = view;
        NumberPicker numberPicker;
        if(rootView == null){ // check if view is new and not recycled
            rootView = View.inflate(context,R.layout.shopping_list_single,null);
            numberPicker=
                    (NumberPicker) rootView.findViewById(R.id.shopping_item_qty_picker);

            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(100);

        }

        TextView txtTitle = (TextView) rootView.findViewById(R.id.shopping_item_name);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.shopping_item_img);

        txtTitle.setText(models[position].title);

        Integer imageId = (models[position].inShoppingList)?
                R.drawable.ic_action_remove : R.drawable.ic_action_add;

        imageView.setImageResource(imageId);

        NumberPicker qty = (NumberPicker) rootView.findViewById(R.id.shopping_item_qty_picker);
        int qtyVal = (models[position].qty >= MIN_VAL && models[position].qty <= MAX_VAL)?
                models[position].qty:  0;
        qty.setValue(qtyVal);

        TextView cost = (TextView) rootView.findViewById(R.id.shopping_item_cost_textView);
        cost.setText(String.valueOf(
                Math.ceil(models[position].cost * models[position].qty)
        ));

        return rootView;
    }


    public static class Model{
        String title;
        int qty;
        double cost;
        boolean inShoppingList;
        public Model(String title, int qty, double cost, boolean inShoppingList) {
            this.title = title;
            this.qty = qty;
            this.cost = cost;
            this.inShoppingList = inShoppingList;
        }





    }


}