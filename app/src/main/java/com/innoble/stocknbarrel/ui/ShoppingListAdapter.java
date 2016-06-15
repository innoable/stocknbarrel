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

import org.atteo.evo.inflector.English;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by Kemron on 27/05/2016.
 */
public class ShoppingListAdapter extends CursorAdapter implements View.OnClickListener{
    private Context context;
    private ItemBtnClickListener mItemBtnClickListener;
    private ItemTotalChangeListener mItemTotalChangeListener;

    public ShoppingListAdapter(Context context, Cursor cursor,ItemBtnClickListener mItemBtnClickListener,ItemTotalChangeListener tcListener){
        super(context,cursor,0);
        this.context = context;
        this.mItemBtnClickListener = mItemBtnClickListener;
        this.mItemTotalChangeListener = tcListener;
    }




    @Override
    // Executed when a new view is being created (should setup ViewHolder pattern initalization here)
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        this.context = context;
        View view =  LayoutInflater.from(context).inflate(R.layout.shopping_list_single,parent,false);
        ViewHolder holder;
        holder = new ViewHolder();
        holder.itemTitle = (TextView) view.findViewById(R.id.shopping_item_name);
        holder.itemTotal = (TextView) view.findViewById(R.id.shopping_item_cost_textView);
        holder.txtQty = (TextView) view.findViewById(R.id.qty_txtView);
        holder.vendorTitle = (TextView)view.findViewById(R.id.shopping_item_vendor);
        holder.unitText = (TextView)view.findViewById(R.id.unitTxtView);
        holder.thumbnail =(ImageView) view.findViewById(R.id.thumbnail);
        holder.position = cursor.getPosition();
        holder.txtQty.setOnFocusChangeListener(new OnQtyChangeListener(holder));
        view.setTag(holder);
        return view;
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolder holder = (ViewHolder) view.getTag();

        String title = cursor.getString(cursor.getColumnIndex("product_name"));
        double cost = cursor.getDouble(cursor.getColumnIndex("price"));
        int qty = cursor.getInt(cursor.getColumnIndex("quantity"));
        qty = qty >=0 ? qty : 1;

        holder.txtQty.setText(Integer.toString(qty));

        holder.txtQty.setTag(new Double(cost));

        String unitText = cursor.getString(cursor.getColumnIndex("unit"));

        holder.unitText.setText(English.plural(unitText,qty));

        holder.itemTotal.setText(new StringBuilder().append("$")
                .append(new BigDecimal(qty*cost, MathContext.DECIMAL64)
                        .setScale(2,BigDecimal.ROUND_CEILING)
                        .toString())
                .toString());

        holder.vendorTitle.setText(cursor.getString(cursor.getColumnIndex("vendor_name")));
        holder.itemTitle.setText(title);
        holder.position = cursor.getPosition();




    }


    @Override
    public void onClick(View v) {
        if(mItemBtnClickListener!= null)
            mItemBtnClickListener.onBtnClick((long)v.getTag());
    }


    public interface ItemBtnClickListener {
        void onBtnClick(long itemId);
    }

    public interface ItemTotalChangeListener {
        void onItemCostChange(BigDecimal oldVal, BigDecimal newVal,int newQty,int cursorIdx);
    }



    private class ViewHolder {
        TextView txtQty;
        TextView itemTotal;
        TextView itemTitle;
        TextView vendorTitle;
        TextView unitText;
        ImageView thumbnail;
        int position;
    }


    private class OnQtyChangeListener implements View.OnFocusChangeListener{

        ViewHolder viewHolder;
        OnQtyChangeListener(ViewHolder viewHolder){
            this.viewHolder = viewHolder;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                String qtyStr = ((EditText)v).getText().toString();
                if(qtyStr.length() > 0 ){
                    int qty = Integer.parseInt(qtyStr);
                    BigDecimal newCost = new BigDecimal(qty *((Double)v.getTag()).doubleValue(),MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING);
                  //  double newCost = Math.round( qty*((Double)v.getTag()).doubleValue() * 100.0) / 100.0;
                    BigDecimal oldCost = new BigDecimal(viewHolder.itemTotal.getText().toString().substring(1),MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING);
                    viewHolder.itemTotal.setText("$"+newCost.toString());
                    mItemTotalChangeListener.onItemCostChange(oldCost,newCost,qty,viewHolder.position);
                }

            }
        }
    }




}
