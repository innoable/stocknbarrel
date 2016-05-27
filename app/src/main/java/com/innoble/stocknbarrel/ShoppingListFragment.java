package com.innoble.stocknbarrel;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.User;

import java.util.ArrayList;
import java.util.List;

import static android.R.color.holo_red_light;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends android.support.v4.app.Fragment
        implements  ListItemAdapter.ItemBtnClickListener,ListItemAdapter.ItemTotalChangeListener{

    private List<ListItemAdapter.RowData>models;
    private ListItemAdapter shoppingListAdapter;
    private ShoppingListAdapter cursorAdapter;
    private StockNBarrelDatabaseHelper db;

    private double total = 0;
    private User mUser;
    private double budget;
    private Cursor dataset;

    String[] web = {
            "Google Plus",
            "Twitter",
            "Windows",
            "Bing",
            "Itunes",
            "Wordpress",
            "Drupal"
    } ;

    int[] qty = {5,6,2,7,10,45,9};


    double[]cost ={4.30,10.31,32.57,94.0,46.4,78.9,1.50};

    boolean[] isInList={true,true,true,false,true,true,false};

    Integer[] imageId = {
            R.drawable.ic_action_add,
            R.drawable.ic_action_remove
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getContext().deleteDatabase(StockNBarrelDatabaseHelper.DATABASE_NAME);
        this.db = new StockNBarrelDatabaseHelper(getActivity());
        this.mUser = db.getUser();
        this.budget = mUser.getBudget();
        this.dataset = db.getShoppingList();
    }

    public ShoppingListFragment() {
        // Required empty public constructor
        models = new ArrayList<>();

        for(int i=0; i<web.length; i++){
            models.add(new ListItemAdapter.RowData(
                    web[i],
                    qty[i],
                    cost[i],
                    isInList[i]
            ));
        };

    }


    @Override
    public void onBtnClick(int position) {
        ListItemAdapter.RowData row = shoppingListAdapter.getItem(position);
        TextView tcView = (TextView)getView().findViewById(R.id.totalCost);
        total = Double.parseDouble(((String) tcView.getText()).substring(1));
        total-= Math.round(row.cost *row.qty * 100.0)/100.0;
        tcView.setText("$"+Double.toString(total));

        if(total > mUser.getBudget()){
            tcView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else{
            tcView.setTextColor(getResources().getColor(android.R.color.white));
        }
        if(shoppingListAdapter.getCount() <= 1 )
            shoppingListAdapter.clear();
        else
            shoppingListAdapter.remove(row);

    }

    @Override
    public void onItemCostChange(double oldVal,double newVal,int position) {
        TextView tcView = (TextView)getView().findViewById(R.id.totalCost);
        double oldTotal = Double.parseDouble(tcView.getText().toString().substring(1));
        double newTotal = Math.round(((oldTotal + (newVal - oldVal))*100.0)/100.0);
        tcView.setText("$"+Double.toString(newTotal));

        if (newTotal > budget){
            tcView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else{
            tcView.setTextColor(getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       // shoppingListAdapter = new ListItemAdapter(getActivity(),models,this,this);
        cursorAdapter = new ShoppingListAdapter(getActivity(),db.getShoppingList());


        View rootView =  inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.shopping_list_view);


        listView.setAdapter(cursorAdapter);


        TextView totalCostView = (TextView)rootView.findViewById(R.id.totalCost);

        double totalCost = 0;
        for(ListItemAdapter.RowData m : models){
            totalCost+= Math.round(m.cost *m.qty *100.0)/100.0;
        }
        totalCostView.setText("$"+ Double.toString(totalCost));
        if(totalCost > budget){
            totalCostView.setTextColor(getResources().getColor(holo_red_light));
        }
        else {
            totalCostView.setTextColor(getResources().getColor(android.R.color.white));
        }

        TextView budgetView = (TextView)rootView.findViewById(R.id.budget);
        budgetView.setText("$"+Double.toString(budget));






        return rootView;
    }





}
