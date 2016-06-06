package com.innoble.stocknbarrel;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.innoble.stocknbarrel.database.StockNBarrelContentProvider;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.ShoppingList;
import com.innoble.stocknbarrel.model.ShoppingListItem;

public class ProductDetailActivity extends AppCompatActivity {

    private Intent thisIntent;
    private TextView prodNameTxt;
    private TextView retailerTxt;
    private TextView totalCostTxt;
    private TextView unitText;
    private int qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        thisIntent = getIntent();


        prodNameTxt = (TextView)findViewById(R.id.produt_detail_name);
        prodNameTxt.setText(thisIntent.getStringExtra("product_name"));


        retailerTxt = (TextView)findViewById(R.id.product_detail_retailer);
        retailerTxt.setText(thisIntent.getStringExtra("grocery_name"));

        totalCostTxt = (TextView)findViewById(R.id.produt_detail_cost);

        double totalCost = Math.round(thisIntent.getDoubleExtra("price",0.00)*100.0)/100.0;

        totalCostTxt.setText("$"+Double.toString(totalCost));

        unitText = (TextView)findViewById(R.id.unitTextView);
        unitText.setText(thisIntent.getStringExtra("unit"));



        final EditText qtyEdit  = (EditText)findViewById(R.id.qtyEditText);
        qty = Integer.parseInt(qtyEdit.getText().toString());

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
                    double cost = Math.round(qty * thisIntent.getDoubleExtra("price",0.00) * 100.00)/100.00;

                    totalCostTxt.setText("$"+cost);
                }
            }
        });





        Button addToCart = (Button)findViewById(R.id.add_to_cart_btn);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri shoppingListUri = StockNBarrelContentProvider.CONTENT_URI.buildUpon()
                        .appendPath(StockNBarrelContentProvider.SHOPPING_LISTS_PATH)
                        .build();

                Cursor shoppingListCurr = getContentResolver().query(shoppingListUri,null,null,null,null);
                shoppingListCurr.moveToFirst();
                long shoppingListID = shoppingListCurr.getLong(shoppingListCurr.getColumnIndex(ShoppingList.COLUMN_ID));

                ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListID,thisIntent.getLongExtra("grocery_stock_item_id",1),qty);
               shoppingListItem.insert(new StockNBarrelDatabaseHelper(ProductDetailActivity.this).getWritableDatabase());
                Toast.makeText(getBaseContext(),"Item has been added to shopping list",Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }


}
