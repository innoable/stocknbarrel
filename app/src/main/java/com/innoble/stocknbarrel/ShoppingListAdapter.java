package com.innoble.stocknbarrel;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
        holder.addRemoveBtn = (ImageButton) view.findViewById(R.id.add_remote_btn);
        holder.itemTotal = (TextView) view.findViewById(R.id.shopping_item_cost_textView);
        holder.editQty = (EditText) view.findViewById(R.id.edit_qty);
        holder.addRemoveBtn.setOnClickListener(this);
        holder.editQty.setOnFocusChangeListener(new OnQtyChangeListener(holder));
        view.setTag(holder);
        return view;
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();

        String title = cursor.getString(1);
        double cost = cursor.getDouble(2);
        int qty = cursor.getInt(3);
        qty = qty >=0 ? qty : 1;

        holder.editQty.setText(Integer.toString(qty));

        holder.editQty.setTag(new Double(cost));

        holder.itemTotal.setText(new StringBuilder().append("$")
                .append(Double.toString(
                        Math.round(qty *cost * 100.0) / 100.0)
                ).toString());


        holder.itemTitle.setText(title);
        holder.position = cursor.getPosition();
        holder.addRemoveBtn.setTag(cursor.getLong(cursor.getColumnIndex("_id")));

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
        void onItemCostChange(double oldVal, double newVal, int cursorIdx);
    }



    private class ViewHolder {
        EditText editQty;
        TextView itemTotal;
        TextView itemTitle;
        ImageButton addRemoveBtn;
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
                    double newCost = Math.round( qty*((Double)v.getTag()).doubleValue() * 100.0) / 100.0;
                    double oldCost = Double.parseDouble(viewHolder.itemTotal.getText().toString().substring(1));
                    viewHolder.itemTotal.setText("$"+Double.toString(newCost));
                    mItemTotalChangeListener.onItemCostChange(oldCost,newCost,viewHolder.position);
                }

            }
        }
    }




}
