package dev.raghav.sael.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.raghav.sael.Fragment.fragment_level;
import dev.raghav.sael.Fragment.fragment_start1;
import dev.raghav.sael.ModelClass.LevelListModel;
import dev.raghav.sael.R;

import static dev.raghav.sael.Fragment.fragment_level.LevelHashMap;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {

    private static final String TAG = "LevelAdapter";

    private ArrayList<LevelListModel> LevelList;
    public Context context;
    View viewlike;
    String PID="";


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt1;
        CardView cardeview;
        int pos;

        public ViewHolder(View view) {
            super(view);
            viewlike = view;

            txt1 = (TextView) viewlike.findViewById(R.id.tv_level);

            cardeview = (CardView)viewlike.findViewById(R.id.cardeview);

        }
    }

    public static Context mContext;
    public LevelAdapter(Context mContext, ArrayList<LevelListModel> levelList) {
        context = mContext;
        LevelList = levelList;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final LevelListModel levelListModel = LevelList.get(position);

        viewHolder.txt1.setText("Exercise "+levelListModel.getM_name());


        viewHolder.cardeview.setTag(viewHolder);
        viewHolder.pos = position;


        viewHolder.cardeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = position;
                PID =  LevelHashMap.get(i);

                Bundle args = new Bundle();
                args.putString("Level_id", PID);

                Fragment view_creat=new fragment_start1();
                view_creat.setArguments(args);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,view_creat);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();


            }
        });







    }
    @Override
        public int getItemCount() {
            return LevelList.size();
        }




        }