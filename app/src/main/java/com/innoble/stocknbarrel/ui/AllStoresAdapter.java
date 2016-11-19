package com.innoble.stocknbarrel.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.innoble.stocknbarrel.R;
import com.squareup.picasso.Picasso;

import org.atteo.evo.inflector.English;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by Kemron on 27/05/2016.
 */
public class AllStoresAdapter extends CursorAdapter implements View.OnClickListener {
    private Context context;


    public AllStoresAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.context = context;
    }


    @Override
    // Executed when a new view is being created (should setup ViewHolder pattern initalization here)
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.all_stores_list_item, parent, false);
        ViewHolder holder;
        holder = new ViewHolder();
        holder.storeLogo = (ImageView) view.findViewById(R.id.store_logo);
        view.setTag(holder);
        return view;
    }


    // Attach data to inflated view items
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.storeId = cursor.getLong(cursor.getColumnIndex("id"));
        Picasso.with(context)
                .load(cursor.getString(cursor.getColumnIndex("store_logo")))
                .into(holder.storeLogo);


    }


    @Override
    public void onClick(View v) {

    }


    public interface ItemBtnClickListener {
        void onBtnClick(long itemId);
    }

    public interface ItemTotalChangeListener {
        void onItemCostChange(BigDecimal oldVal, BigDecimal newVal, int newQty, int cursorIdx);
    }


    private class ViewHolder {
        ImageView storeLogo;
        long storeId;
    }
}
