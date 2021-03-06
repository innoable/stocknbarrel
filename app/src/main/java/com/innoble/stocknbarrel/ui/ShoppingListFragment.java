package com.innoble.stocknbarrel.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.innoble.stocknbarrel.R;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.ShoppingListItem;
import com.innoble.stocknbarrel.model.User;
import com.innoble.stocknbarrel.provider.ProductDetailParcelable;
import com.innoble.stocknbarrel.provider.StockNBarrelContentProvider;

import java.math.BigDecimal;
import java.math.MathContext;

import static android.R.color.holo_red_light;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends android.support.v4.app.Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, ShoppingListAdapter.ItemBtnClickListener, ShoppingListAdapter.ItemTotalChangeListener {

    private static final int SHOPPING_LIST_LOADER_ID = 0;

    private Uri shoppingListItemQuery;
    private ShoppingListAdapter cursorAdapter;
    private StockNBarrelDatabaseHelper db;
    private TextView tcView;
    private TextView budgetView;
    private ImageButton budgetEditBtn;

    private BigDecimal total = new BigDecimal(0, MathContext.DECIMAL64);
    private User mUser;
    private BigDecimal budget;

    public ShoppingListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        shoppingListItemQuery = StockNBarrelContentProvider.CONTENT_URI.buildUpon()
                .appendPath(StockNBarrelContentProvider.SHOPPING_LIST_ITEMS_PATH)
                .build();
        this.db = new StockNBarrelDatabaseHelper(getActivity());
        this.mUser = db.getUser();
        this.budget = new BigDecimal(mUser.getBudget(), MathContext.DECIMAL64).setScale(2, BigDecimal.ROUND_CEILING);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(SHOPPING_LIST_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        cursorAdapter = new ShoppingListAdapter(getActivity(), null, this, this);

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.shopping_list_view);
        tcView = (TextView) rootView.findViewById(R.id.totalCost);
        budgetView = (TextView) rootView.findViewById(R.id.budget);
        budgetView.setText("$" + budget.toString());
        budgetEditBtn = (ImageButton) rootView.findViewById(R.id.budgetEditBtn);

        budgetEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.single_edit_dialog, (ViewGroup) v.getParent(), false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Budget");

                // Set up the dialog input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                //Specify layout to use for dialog
                builder.setView(viewInflated);


                // Set up the buttons

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String inputStr = input.getText().toString();
                        budget = new BigDecimal(inputStr, MathContext.DECIMAL64).setScale(2, BigDecimal.ROUND_CEILING);
                        mUser.setBudget(budget.doubleValue());
                        mUser.update(db.getWritableDatabase());
                        budgetView.setText("$" + budget.toString());
                        if (total.compareTo(budget) > 0) {
                            tcView.setTextColor(getResources().getColor(holo_red_light));
                        } else {
                            tcView.setTextColor(getResources().getColor(android.R.color.white));
                        }
                    }
                });

                builder.show();
            }
        });


        listView.setAdapter(cursorAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                Cursor cur = cursorAdapter.getCursor();
                cur.moveToPosition(position);


                ProductDetailParcelable parcelable = new ProductDetailParcelable();

                parcelable.shortDescription = cur.getString(cur.getColumnIndex("short_description"));
                parcelable.longDescription = cur.getString(cur.getColumnIndex("long_description"));
                parcelable.productName = cur.getString(cur.getColumnIndex("product_name"));
                parcelable.price = cur.getDouble(cur.getColumnIndex("price"));
                parcelable.unit = cur.getString(cur.getColumnIndex("unit"));
                parcelable.vendorName = cur.getString(cur.getColumnIndex("vendor_name"));
                parcelable.vendorPhone = cur.getString(cur.getColumnIndex("vendor_phone"));
                parcelable.vendorLocation = cur.getString(cur.getColumnIndex("vendor_location"));
                parcelable.productThumbnail = cur.getString(cur.getColumnIndex("product_thumbnail"));
                parcelable.itemCartID = cur.getString(cur.getColumnIndex("_id"));
                parcelable.qty = cur.getInt(cur.getColumnIndex("quantity"));
                intent.putExtra(Intent.EXTRA_TEXT, parcelable);

                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(SHOPPING_LIST_LOADER_ID, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shopping_cart_menu, menu);
        MenuItem item = menu.findItem(R.id.clear_cart);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_cart:
                getActivity().getContentResolver().delete(shoppingListItemQuery, null, null);
                getLoaderManager().restartLoader(SHOPPING_LIST_LOADER_ID, null, this);
                tcView.setTextColor(getResources().getColor(android.R.color.white));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateViewTotal(Cursor cursor) {

        if (cursor == null) {
            tcView.setText("$" + Double.toString(0.00));
            tcView.setTextColor(getResources().getColor(android.R.color.white));
            return;
        }

        BigDecimal newTotal = new BigDecimal(0.00, MathContext.DECIMAL64);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            newTotal = newTotal.add(new BigDecimal(cursor.getInt(cursor.getColumnIndex("quantity")) * cursor.getDouble(cursor.getColumnIndex("price")), MathContext.DECIMAL64)
                    .setScale(2, BigDecimal.ROUND_CEILING));
        }
        tcView.setText("$" + newTotal.toString());

        if (newTotal.compareTo(budget) > 0) {
            tcView.setTextColor(getResources().getColor(holo_red_light));
        } else {
            tcView.setTextColor(getResources().getColor(android.R.color.white));
        }

        total = newTotal;

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                shoppingListItemQuery,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap cursor information into Adapter
        cursorAdapter.swapCursor(data);
        updateViewTotal(data);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        //Remove Cursor information into adapter
        cursorAdapter.swapCursor(null);
    }

    @Override
    public void onBtnClick(long itemId) {
        Uri uri = shoppingListItemQuery.buildUpon()
                .appendPath(Long.toString(itemId))
                .build();
        getActivity().getContentResolver().delete(uri, null, null);
        getLoaderManager().restartLoader(SHOPPING_LIST_LOADER_ID, null, this);
    }

    @Override
    public void onItemCostChange(BigDecimal oldVal, BigDecimal newVal,
                                 int newQty, int cursorIdx) {
        if (oldVal.equals(newVal))
            return;

        total = total.subtract(oldVal).add(newVal);
        tcView.setText("$" + total.toString());
        if (total.compareTo(budget) > 0) {
            tcView.setTextColor(getResources().getColor(holo_red_light));
        } else {
            tcView.setTextColor(getResources().getColor(android.R.color.white));
        }

        Cursor cur = cursorAdapter.getCursor();

        if (cursorIdx >= cur.getCount())
            return;
        cur.moveToPosition(cursorIdx);
        long itemId = cur.getLong(cur.getColumnIndexOrThrow("_id"));


        Uri uri = shoppingListItemQuery.buildUpon()
                .appendPath(Long.toString(itemId))
                .build();

        ContentResolver resolver = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ShoppingListItem.COLUMN_QUANTITY, newQty);
        resolver.update(uri, values, null, null);
        resolver.notifyChange(shoppingListItemQuery, null);
    }
}
