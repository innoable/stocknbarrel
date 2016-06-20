package com.innoble.stocknbarrel.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.innoble.stocknbarrel.R;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by Kemron on 31/05/2016.
 */
public class SearchResultListAdapter extends CursorAdapter{
    public SearchResultListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_result_item,parent,false);
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView)view.findViewById(R.id.result_item_name_textView);
        holder.price = (TextView)view.findViewById(R.id.result_item_price_textView);
        holder.grocery = (TextView)view.findViewById(R.id.result_item_grocery_textView);
        holder.thumbnail = (ImageView)view.findViewById(R.id.result_thumbnail);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder)view.getTag();
        holder.name.setText(cursor.getString(cursor.getColumnIndex("product_name")));

        String unitCost = new StringBuilder()
                .append("$")
                .append(new BigDecimal(cursor.getDouble(cursor.getColumnIndex("price")), MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING).toString())
                .append(" per ")
                .append(cursor.getString(cursor.getColumnIndex("unit")))
                .toString();

        holder.price.setText(unitCost);
        holder.grocery.setText(cursor.getString(cursor.getColumnIndex("grocery_name")));
        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex("product_thumbnail")))
                .into(holder.thumbnail);
    }



    class ViewHolder{
        TextView name;
        TextView price;
        TextView grocery;
        ImageView thumbnail;
    }
}
