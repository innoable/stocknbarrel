package com.innoble.stocknbarrel;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.add(R.id.container,new LoginFragment()).commit();



    }




    public static class LoginFragment extends Fragment{
         public LoginFragment(){

         }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.login_fragment,container,false);

            Button registerBtn = (Button)rootView.findViewById(R.id.registerButton);

            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return rootView;

        }
    }

}
