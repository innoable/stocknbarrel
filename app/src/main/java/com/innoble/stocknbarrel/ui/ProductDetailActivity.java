package com.innoble.stocknbarrel.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.innoble.stocknbarrel.R;
import com.innoble.stocknbarrel.database.StockNBarrelContentProvider;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.ShoppingList;
import com.innoble.stocknbarrel.model.ShoppingListItem;

import java.math.BigDecimal;
import java.math.MathContext;

import static android.R.color.holo_red_light;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Fragment viewFragment;
        if(getIntent().getStringExtra("cart_item_id")!= null )
            viewFragment = new ProductEditRemoveFragment();

        else{
            viewFragment = new ProductAddFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,viewFragment)
                .commit();

    }







    public static class ProductEditRemoveFragment extends ProductFragment{
        private final Uri shoppingListItemUri = StockNBarrelContentProvider.CONTENT_URI.buildUpon()
                .appendPath(StockNBarrelContentProvider.SHOPPING_LIST_ITEMS_PATH)
                .build();

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.product_edit,menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case R.id.confirm:
                    Uri uri = shoppingListItemUri.buildUpon()
                            .appendPath(thisIntent.getStringExtra("cart_item_id"))
                            .build();

                    ContentResolver resolver = getActivity().getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(ShoppingListItem.COLUMN_QUANTITY,qty);
                    resolver.update(uri,values,null,null);
                    resolver.notifyChange(shoppingListItemUri,null);
                    activity.finish();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            qty = thisIntent.getIntExtra("qty",1);
            actionBtn.setText("Remove From Cart");
            actionBtn.setTextColor(getResources().getColor(android.R.color.white));
            actionBtn.setBackgroundColor(getResources().getColor(holo_red_light));
            qtyEdit.setText(Integer.toString(qty));
            BigDecimal cost = new BigDecimal(qty * thisIntent.getDoubleExtra("price",0.00), MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING);
            totalCostTxt.setText("$"+cost.toString());


            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri uri = shoppingListItemUri.buildUpon()
                            .appendPath(thisIntent.getStringExtra("cart_item_id"))
                            .build();
                    getActivity().getContentResolver().delete(uri,null,null);

                    Toast.makeText(activity,"Item has been removed from cart",Toast.LENGTH_SHORT).show();
                    activity.finish();

                }
            });

            return view;
        }


    }



    public static class ProductAddFragment extends  ProductFragment{
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view =  super.onCreateView(inflater, container, savedInstanceState);


            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri shoppingListUri = StockNBarrelContentProvider.CONTENT_URI.buildUpon()
                            .appendPath(StockNBarrelContentProvider.SHOPPING_LISTS_PATH)
                            .build();

                    Cursor shoppingListCurr = activity.getContentResolver().query(shoppingListUri,null,null,null,null);
                    shoppingListCurr.moveToFirst();
                    long shoppingListID = shoppingListCurr.getLong(shoppingListCurr.getColumnIndex(ShoppingList.COLUMN_ID));

                    ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListID,thisIntent.getLongExtra("grocery_stock_item_id",1),qty);
                    shoppingListItem.insert(new StockNBarrelDatabaseHelper(activity).getWritableDatabase());
                    Toast.makeText(activity,"Item has been added to shopping list",Toast.LENGTH_SHORT).show();
                    activity.finish();

                }
            });

            return  view;
        }
    }



    public static class ProductFragment extends android.support.v4.app.Fragment{
        protected Intent thisIntent;
        protected TextView prodNameTxt;
        protected TextView retailerTxt;
        protected TextView totalCostTxt;
        protected TextView unitText;
        protected EditText qtyEdit;
        protected Button actionBtn;
        protected int qty;
        protected BigDecimal total;
        protected AppCompatActivity activity;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            activity = (AppCompatActivity)getActivity();
            View view = inflater.inflate(R.layout.activity_product_detail,container,false);

            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);
            if (activity.getSupportActionBar() != null){
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            }



            thisIntent = activity.getIntent();


            prodNameTxt = (TextView)view.findViewById(R.id.produt_detail_name);
            prodNameTxt.setText(thisIntent.getStringExtra("product_name"));


            retailerTxt = (TextView)view.findViewById(R.id.product_detail_retailer);
            retailerTxt.setText(thisIntent.getStringExtra("grocery_name"));

            totalCostTxt = (TextView)view.findViewById(R.id.produt_detail_cost);

            final BigDecimal totalCost = new BigDecimal(thisIntent.getDoubleExtra("price",0.00),MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING);

            totalCostTxt.setText("$"+ totalCost.toString());

            unitText = (TextView)view.findViewById(R.id.unitTextView);
            unitText.setText(thisIntent.getStringExtra("unit"));



            qtyEdit  = (EditText)view.findViewById(R.id.qtyEditText);
            qty = Integer.parseInt(qtyEdit.getText().toString());

            actionBtn = (Button)view.findViewById(R.id.add_to_cart_btn);

            qtyEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String qtyTxt = s.toString();
                    if(qtyTxt.length() == 0){
                        totalCostTxt.setText("$"+0.00);
                    }
                    else{
                        qty = Integer.parseInt(qtyTxt);
                        total = new BigDecimal(qty * thisIntent.getDoubleExtra("price",0.00), MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING);
                        totalCostTxt.setText("$"+total.toString());
                    }
                }
            });
            return view;
        }
    }




}
