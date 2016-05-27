package com.innoble.stocknbarrel;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kemron on 20/05/2016.
 */

// Shopping List Custom Adapter
public class ListItemAdapter extends ArrayAdapter<ListItemAdapter.RowData> implements View.OnClickListener {
    private final Activity context;
    private List<RowData> models;
    private ItemBtnClickListener mItemBtnClickListener;
    private ItemTotalChangeListener mItemTotalChangeListener;


    public ListItemAdapter(Activity context, List<RowData> rowDataList, ItemBtnClickListener btnListener, ItemTotalChangeListener itemCostChangeListener) {
        super(context, R.layout.shopping_list_single, rowDataList);
        this.models = new ArrayList<>();
        this.models = rowDataList;
        this.context = context;
        this.mItemBtnClickListener = btnListener;
        this.mItemTotalChangeListener = itemCostChangeListener;

    }








    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if (view == null) { // check if view is new and not recycled
            view = View.inflate(context, R.layout.shopping_list_single, null);
            holder = new ViewHolder();
            holder.itemTitle = (TextView) view.findViewById(R.id.shopping_item_name);
            holder.addRemoveBtn = (ImageButton) view.findViewById(R.id.add_remote_btn);
            holder.itemTotal = (TextView) view.findViewById(R.id.shopping_item_cost_textView);
            holder.editQty = (EditText) view.findViewById(R.id.edit_qty);
            holder.addRemoveBtn.setOnClickListener(this);

            holder.editQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(getCount() <= 0)
                        return;
                    if (!hasFocus) {
                        String qtyStr = holder.editQty.getText().toString();
                        if (qtyStr.length() == 0) {
                            double oldItemCost = Double.parseDouble(holder.itemTotal.getText().toString().substring(1));
                            holder.itemTotal.setText("$" + String.valueOf(0.0));
                            holder.editQty.setText("0");
                            if(mItemTotalChangeListener!= null)
                                 mItemTotalChangeListener.onItemCostChange(oldItemCost, 0, holder.position);
                        } else {
                            int qty = Integer.parseInt(qtyStr);
                            double oldItemCost = Double.parseDouble(holder.itemTotal.getText().toString().substring(1));
                            double newItemCost = Math.round(qty * models.get(holder.position).cost * 100.0) / 100.0;
                            holder.itemTotal.setText("$" + String.valueOf(newItemCost));
                            if(mItemTotalChangeListener!= null)
                                mItemTotalChangeListener.onItemCostChange(oldItemCost, newItemCost, holder.position);
                        }
                    }
                }
            });


            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        int qty = (models.get(position).qty >= 1) ?
                models.get(position).qty : 1;

        holder.editQty.setText(Integer.toString(qty));

        holder.itemTotal.setText(new StringBuilder().append("$")
                .append(Double.toString(
                        Math.round(qty * models.get(position).cost * 100.0) / 100.0)
                ).toString());


        holder.itemTitle.setText(models.get(position).title);
        holder.addRemoveBtn.setTag(position);
        holder.position = position;


        return view;
    }


    @Override
    public void onClick(View v) {
        if (mItemBtnClickListener != null) {
            mItemBtnClickListener.onBtnClick((Integer) v.getTag());
        }
    }


    static class ViewHolder {
        EditText editQty;
        TextView itemTotal;
        TextView itemTitle;
        ImageButton addRemoveBtn;
        int position;


    }


    public interface ItemBtnClickListener {
        void onBtnClick(int position);
    }

    public interface ItemTotalChangeListener {
        void onItemCostChange(double oldVal, double newVal, int position);
    }


    public static class RowData {
        String title;
        int qty;
        double cost;
        boolean inShoppingList;

        public RowData(String title, int qty, double cost, boolean inShoppingList) {
            this.title = title;
            this.qty = qty;
            this.cost = cost;
            this.inShoppingList = inShoppingList;
        }

        @Override
        public String toString() {
            return this.title;
        }
    }


}