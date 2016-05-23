package com.innoble.stocknbarrel;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends android.support.v4.app.Fragment implements  ListItemAdapter.ItemBtnClickListener{

    private List<ListItemAdapter.RowData>models;
    private ListItemAdapter shoppingListAdapter;

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
        double total = Double.parseDouble(((String) tcView.getText()).substring(1));
        total-= row.cost *row.qty;
        tcView.setText("$"+Double.toString(Math.round(total* 100.0) / 100.0));
        shoppingListAdapter.remove(row);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        View rootView =  inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.shopping_list_view);


        shoppingListAdapter = new ListItemAdapter(getActivity(),models,this);

        listView.setAdapter(shoppingListAdapter);


        TextView totalCostView = (TextView)rootView.findViewById(R.id.totalCost);

        double totalCost = 0;
        for(ListItemAdapter.RowData m : models){
            totalCost+= m.cost *m.qty;
        }
        totalCostView.setText("$"+ Double.toString(totalCost));



        return rootView;
    }



}
