package dev.raghav.sael.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import dev.raghav.sael.Adapter.LevelAdapter;
import dev.raghav.sael.Connectivity.Connectivity;
import dev.raghav.sael.Connectivity.HttpHandler;
import dev.raghav.sael.ModelClass.LevelListModel;
import dev.raghav.sael.Profile_Update;
import dev.raghav.sael.R;

public class fragment_level extends Fragment {

    RecyclerView recycler_level;
    String server_url;
    ArrayList<LevelListModel> LevelList=new ArrayList<>();
    private LevelAdapter levelAdapter;

   public static HashMap<Integer,String>LevelHashMap=new HashMap<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_level, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Learning English");

   recycler_level=view.findViewById(R.id.level_recycler);

        if (Connectivity.isNetworkAvailable(view.getContext())){

            try{

                if(LevelList.size() !=0)
                {
                    LevelList.clear();

                    recycler_level.setAdapter(null);
                    levelAdapter.notifyDataSetChanged();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            new Level_Get_Excute().execute();

        }else {
            Toast.makeText(view.getContext(), "Please Check Internet", Toast.LENGTH_SHORT).show();
        }


    }

    private class Level_Get_Excute extends AsyncTask<String, Void, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Processing");
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://jntrcpl.com/staracademy/Api/get_level";


            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            //   Log.e("getcomment_url", output);
            System.out.println("getcomment_url" + output);
            return output;
        }


        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();

                    JSONObject object = new JSONObject(output);
                    String res = object.getString("responce");
                    JSONObject dataJsonObject = new JSONObject(output);

                    if (res.equals("true")) {

                        JSONArray Data_array = dataJsonObject.getJSONArray("userdata");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject c = Data_array.getJSONObject(i);

                            String masterdata_id = c.getString("masterdata_id");
                            String m_name = c.getString("m_name");
                            String type = c.getString("type");



                            LevelList.add(i, new LevelListModel(masterdata_id,m_name,type));
                            LevelHashMap.put(i , masterdata_id);
                        }


                        levelAdapter = new LevelAdapter(getContext(), LevelList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recycler_level.setLayoutManager(mLayoutManager);
                        recycler_level.setItemAnimator(new DefaultItemAnimator());
                        recycler_level.setAdapter(levelAdapter);


                    }else {
                        LevelList.clear();
                        levelAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "No Level Found", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(output);
            }

        }

    }

}