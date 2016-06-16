package com.innoble.stocknbarrel.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.innoble.stocknbarrel.R;
import com.innoble.stocknbarrel.database.StockNBarrelContentProvider;
import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.ShoppingList;
import com.innoble.stocknbarrel.model.ShoppingListItem;
import com.squareup.picasso.Picasso;

import org.atteo.evo.inflector.English;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;

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


    /**
     * ProductEditRemoveFragment displays product details fragment with interaction icons specific
     * to editing and removal for items currently held in users cart
     *
     * @Author Kemron Glasgow
     */
    public static class ProductEditRemoveFragment extends ProductFragment{
        private final Uri shoppingListItemUri = StockNBarrelContentProvider.CONTENT_URI.buildUpon()
                .appendPath(StockNBarrelContentProvider.SHOPPING_LIST_ITEMS_PATH)
                .build();

        private final double ACTION_BUTTON_DPS = 250;
        private  float DISPLAY_SCALE;
        private Drawable actionIcon;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            DISPLAY_SCALE = getResources().getDisplayMetrics().density;
            actionIcon = ResourcesCompat.getDrawable(getResources(),android.R.drawable.ic_menu_delete, null);
            actionIcon.setColorFilter(Color.argb(230, 255, 0, 0), PorterDuff.Mode.SRC_ATOP);
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

                    ContentResolver resolver = mActivity.getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(ShoppingListItem.COLUMN_QUANTITY,qty);
                    resolver.update(uri,values,null,null);
                    resolver.notifyChange(shoppingListItemUri,null);
                    mActivity.finish();
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
            actionBtn.setTextColor(getResources().getColor(holo_red_light));
            actionBtn.setBackgroundColor(Color.TRANSPARENT);
            int pixels = (int) (ACTION_BUTTON_DPS * DISPLAY_SCALE + 0.5f);
            //ViewGroup.LayoutParams params = actionBtn.getLayoutParams();
            //params.width = pixels;
            //actionBtn.setLayoutParams(params);
            actionBtn.setCompoundDrawablesWithIntrinsicBounds(actionIcon,null,null,null);
            qtyEdit.setText(Integer.toString(qty));
            BigDecimal cost = new BigDecimal(qty * thisIntent.getDoubleExtra("price",0.00), MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING);
            totalCostTxt.setText("$"+cost.toString());

            // Display removal confirmation dialog
            actionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
                    dialogBuilder.setMessage("Are you sure you want to delete this entry?")
                            .setNegativeButton("Cancel",null)
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Uri uri = shoppingListItemUri.buildUpon()
                                            .appendPath(thisIntent.getStringExtra("cart_item_id"))
                                            .build();
                                    mActivity.getContentResolver().delete(uri,null,null);

                                    Toast.makeText(mActivity,"Item has been removed from cart",Toast.LENGTH_SHORT).show();
                                    mActivity.finish();

                                }
                            }).create().show();



                }
            });

            return view;
        }



    }


    /**
     * ProductAddFragment displays products product details view with controls specific for reviewing
     * items and adding them to the user's cart.
     * @Author Kemron Glasgow
     */
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

                    Cursor shoppingListCurr = mActivity.getContentResolver().query(shoppingListUri,null,null,null,null);
                    shoppingListCurr.moveToFirst();
                    long shoppingListID = shoppingListCurr.getLong(shoppingListCurr.getColumnIndex(ShoppingList.COLUMN_ID));

                    ShoppingListItem shoppingListItem = new ShoppingListItem(shoppingListID,thisIntent.getLongExtra("grocery_stock_item_id",1),qty);
                    shoppingListItem.insert(new StockNBarrelDatabaseHelper(mActivity).getWritableDatabase());
                    Toast.makeText(mActivity,"Item has been added to shopping list",Toast.LENGTH_SHORT).show();
                    mActivity.finish();

                }
            });

            return  view;
        }
    }


    /**
     * ProductFragment is manages loading and display of the fundamental items within the product
     * details view
     * @Author Kemron Glasgow
     */
    public abstract static class ProductFragment extends android.support.v4.app.Fragment{
        protected Intent thisIntent;
        protected TextView prodNameTxt;
        protected TextView retailerTxt;
        protected TextView totalCostTxt;
        protected TextView unitText;
        protected EditText qtyEdit;
        protected Button actionBtn;
        protected TextView descTextView;
        protected int qty;
        protected BigDecimal total;
        protected AppCompatActivity mActivity;
        protected TextView longDescriptionLink;
        protected RecyclerView gallery;
        protected GalleryAdapter galleryAdapter;


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mActivity = (AppCompatActivity)getActivity();
            thisIntent = mActivity.getIntent();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


            View view = inflater.inflate(R.layout.activity_product_detail,container,false);

            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            mActivity.setSupportActionBar(toolbar);
            if (mActivity.getSupportActionBar() != null){
                mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            prodNameTxt = (TextView)view.findViewById(R.id.produt_detail_name);
            prodNameTxt.setText(thisIntent.getStringExtra("product_name"));

            retailerTxt = (TextView)view.findViewById(R.id.product_detail_retailer);
            retailerTxt.setText(thisIntent.getStringExtra("grocery_name"));

            totalCostTxt = (TextView)view.findViewById(R.id.produt_detail_cost);
            final BigDecimal totalCost = new BigDecimal(thisIntent.getDoubleExtra("price",0.00),MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING);
            totalCostTxt.setText("$"+ totalCost.toString());

            descTextView = (TextView)view.findViewById(R.id.descriptTextView);
            descTextView.setText(thisIntent.getStringExtra("product_short_description"));

            actionBtn = (Button)view.findViewById(R.id.add_to_cart_btn);

            unitText = (TextView)view.findViewById(R.id.unitTextView);
            unitText.setText(English.plural(thisIntent.getStringExtra("unit"),qty));

            longDescriptionLink = (TextView)view.findViewById(R.id.longDetailsLink);
            longDescriptionLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity,DescriptionActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT,thisIntent.getStringExtra("product_long_description"));
                    intent.putExtra("SHORT_DESCRIPTION",thisIntent.getStringExtra("product_short_description"));
                    intent.putExtra("PRODUCT_NAME",thisIntent.getStringExtra("product_name"));
                    startActivity(intent);

                }
            });

            // specify an adapter (see also next example)
            gallery = (RecyclerView) view.findViewById(R.id.details_gallery);
            gallery.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mActivity);
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            gallery.setLayoutManager(llm);
            gallery.setItemAnimator(new DefaultItemAnimator());
            List<String> images = Arrays.asList(
                    "https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
                    "https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
                    "https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/Another%252520Rockaway%252520Sunset.jpg",
                    "https://lh3.googleusercontent.com/--L0Km39l5J8/URquXHGcdNI/AAAAAAAAAbs/3ZrSJNrSomQ/s1024/Antelope%252520Butte.jpg",
                    "https://lh6.googleusercontent.com/-8HO-4vIFnlw/URquZnsFgtI/AAAAAAAAAbs/WT8jViTF7vw/s1024/Antelope%252520Hallway.jpg",
                    "https://lh4.googleusercontent.com/-WIuWgVcU3Qw/URqubRVcj4I/AAAAAAAAAbs/YvbwgGjwdIQ/s1024/Antelope%252520Walls.jpg",
                    "https://lh6.googleusercontent.com/-UBmLbPELvoQ/URqucCdv0kI/AAAAAAAAAbs/IdNhr2VQoQs/s1024/Apre%2525CC%252580s%252520la%252520Pluie.jpg",
                    "https://lh3.googleusercontent.com/-s-AFpvgSeew/URquc6dF-JI/AAAAAAAAAbs/Mt3xNGRUd68/s1024/Backlit%252520Cloud.jpg",
                    "https://lh5.googleusercontent.com/-bvmif9a9YOQ/URquea3heHI/AAAAAAAAAbs/rcr6wyeQtAo/s1024/Bee%252520and%252520Flower.jpg",
                    "https://lh5.googleusercontent.com/-n7mdm7I7FGs/URqueT_BT-I/AAAAAAAAAbs/9MYmXlmpSAo/s1024/Bonzai%252520Rock%252520Sunset.jpg",
                    "https://lh6.googleusercontent.com/-4CN4X4t0M1k/URqufPozWzI/AAAAAAAAAbs/8wK41lg1KPs/s1024/Caterpillar.jpg",
                    "https://lh3.googleusercontent.com/-rrFnVC8xQEg/URqufdrLBaI/AAAAAAAAAbs/s69WYy_fl1E/s1024/Chess.jpg",
                    "http://vignette4.wikia.nocookie.net/kingdomhearts/images/f/f7/Sora_(KHIIFM)_KHIIHD.png/revision/latest?cb=20140813042326"
            );

            galleryAdapter = new GalleryAdapter(mActivity,images);
            gallery.setAdapter(galleryAdapter);
            qtyEdit  = (EditText)view.findViewById(R.id.qtyEditText);
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
                        total = new BigDecimal(qty * thisIntent.getDoubleExtra("price",0.00), MathContext.DECIMAL64).setScale(2,BigDecimal.ROUND_CEILING);
                        totalCostTxt.setText("$"+total.toString());
                        unitText.setText(English.plural(thisIntent.getStringExtra("unit"),qty));
                    }
                }
            });
            return view;
        }
    }





    public static class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

        private List<String> mImageUriList;
        private Context mContext;

         class ViewHolder extends RecyclerView.ViewHolder{
             private ImageView imageView;
            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.details_gallery_image);
            }
        }

        public GalleryAdapter(Context context,List<String> imageUriList) {
            this.mImageUriList = imageUriList;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.details_gallery_item,parent,false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Picasso.with(mContext)
                    .load(mImageUriList.get(position))
                    .fit()
                    .centerInside()
                    .into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return mImageUriList.size();
        }
    }







}
