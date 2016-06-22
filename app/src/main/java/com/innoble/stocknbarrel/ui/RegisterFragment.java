
package com.innoble.stocknbarrel.ui;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.innoble.stocknbarrel.R;

import java.util.Map;
import java.util.TreeMap;

public  class RegisterFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private final String LOG_TAG = getClass().getSimpleName();
    private View rootView;
    private Map<String,String> userData;

    public RegisterFragment(){

    }

    @Override
    public void onClick(View v) {
        userData.put(RegisterActivity.USERNAME,((EditText)rootView.findViewById(R.id.register_name)).getText().toString());
        userData.put(RegisterActivity.EMAIL,((EditText)rootView.findViewById(R.id.register_email)).getText().toString());
        userData.put(RegisterActivity.BUDGET,((EditText)rootView.findViewById(R.id.register_budget)).getText().toString());
        ((Callback)getActivity()).onUserRegister(userData);
    }

    public  interface Callback{
        void onUserRegister(Map<String,String> data);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userData= new TreeMap<>();

        rootView = inflater.inflate(R.layout.register_fragment,container,false);

        ((Button)rootView.findViewById(R.id.registerButton)).setOnClickListener(this);
        return rootView;

    }
}