package dev.raghav.sael.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dev.raghav.sael.R;
import dev.raghav.sael.Registration_Activity;

public class fragment_text_home extends Fragment {

    Button btn_start, table_1,table_2,table_3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_text_start, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Learning English");

        btn_start=view.findViewById(R.id.button_start);
        table_1=view.findViewById(R.id.table_1);
        table_2=view.findViewById(R.id.table_2);
        table_3=view.findViewById(R.id.table_3);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment view_creat=new fragment_level();
                FragmentManager fragmentManager = getFragmentManager();
              //  fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,view_creat);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
        table_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment view_creat=new fragment_table1();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,view_creat);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
 table_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment view_creat=new fragment_table2();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,view_creat);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

 table_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment view_creat=new fragment_table3();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,view_creat);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

    }

}
