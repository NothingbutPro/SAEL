package dev.raghav.sael.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;

import dev.raghav.sael.Connectivity.Connectivity;
import dev.raghav.sael.Connectivity.SharedPref;
import dev.raghav.sael.Login_Activity;
import dev.raghav.sael.Main2Activity;
import dev.raghav.sael.ModelClass.LevelListModel;
import dev.raghav.sael.ModelClass.QuestionListModel;
import dev.raghav.sael.R;

public class fragment_start1 extends Fragment {

    Button btn_start2;
    Button button_check;
    TextView tv_answer,tv_question, tv_ques_no;
     String LEVEL_ID;
     EditText et_text;
    ArrayList<QuestionListModel> QuestionList=new ArrayList<>();

    public static int intNext=0;

   public static LinkedList <QuestionListModel>link_level_question = new LinkedList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_text_start1, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Learning English");

        btn_start2=view.findViewById(R.id.button_start);
        button_check=view.findViewById(R.id.button_check);
        tv_answer=view.findViewById(R.id.tv_answer);
        tv_question=view.findViewById(R.id.tv_question);
        tv_ques_no=view.findViewById(R.id.ques_no);
        et_text=view.findViewById(R.id.et_text);

        Bundle b = getArguments();
        try {
            LEVEL_ID = b.getString("Level_id");
        }catch (Exception e){
            int x = intNext +1;
           try {
               if(link_level_question.get(x).getQuestion().length() !=0) {
                   tv_ques_no.setText("Question: "+link_level_question.get(intNext).getSr_no());
                   tv_question.setText(link_level_question.get(intNext).getQuestion());
                   tv_answer.setText(link_level_question.get(intNext).getAnswer());
               }
           }catch (Exception e1){
               btn_start2.setText("Complete");
               tv_ques_no.setText("Question: "+link_level_question.get(intNext).getSr_no());
               tv_question.setText(link_level_question.get(intNext).getQuestion());
               tv_answer.setText(link_level_question.get(intNext).getAnswer());
           }

        }

       // Toast.makeText(getContext(), "bb "+LEVEL_ID, Toast.LENGTH_SHORT).show();

        if (Connectivity.isNetworkAvailable(view.getContext())){
            new Question_Get_Excute().execute();

        }else {
            Toast.makeText(view.getContext(), "Please Check Internet", Toast.LENGTH_SHORT).show();
        }

        btn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = intNext +1;

             if (btn_start2.getText().equals("Complete")){
                 Fragment view_creat=new fragment_complete();
                 FragmentManager fragmentManager = getFragmentManager();
                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                 fragmentTransaction.replace(R.id.content_frame,view_creat);
                 //fragmentTransaction.addToBackStack(null);
                 fragmentTransaction.commit();
             }else {
                 intNext++;
                 Fragment view_creat=new fragment_start1();
                 FragmentManager fragmentManager = getFragmentManager();
                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                 fragmentTransaction.replace(R.id.content_frame,view_creat);
                // fragmentTransaction.addToBackStack(null);
                 fragmentTransaction.commit();
             }

            }
        });

        button_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et_text.getText().toString().isEmpty()){
                    tv_answer.setVisibility(View.VISIBLE);
                }else {
                    et_text.setError("Please type here");
                }


            }
        });

        //***********************************





    }


    private class Question_Get_Excute extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Processing...");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://jntrcpl.com/staracademy/Api/get_question");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("level",LEVEL_ID);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            }
            catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                try {
                    //  Toast.makeText(LoginActivity.this, "result is" + result, Toast.LENGTH_SHORT).show();


                    JSONObject object = new JSONObject(result);
                    String res = object.getString("responce");

                    if (res.equals("true")) {

                        JSONArray Data_array = object.getJSONArray("userdata");
                        for (int i = 0; i < Data_array.length(); i++) {
                            JSONObject data = Data_array.getJSONObject(i);

                            String q_id = data.getString("q_id");
                            String question = data.getString("question");
                            String answer = data.getString("answer");
                            String level = data.getString("level");
                            String sr_no = data.getString("sr_no");

                            QuestionList.add(i, new QuestionListModel(q_id,question,answer,level,sr_no));
                            link_level_question.add( new QuestionListModel(q_id,question,answer,level,sr_no));
//                            if(i==0)
//                            {
//                            tv_question.setText(link_level_question.get(i).getQuestion());
//
//                            tv_answer.setText(link_level_question.get(i).getAnswer());
//                            }


                        }
                        try {
                            if (link_level_question.get(intNext+1).getQuestion().length()!=0){
                                tv_ques_no.setText("Question: "+link_level_question.get(intNext).getSr_no());
                                tv_question.setText(link_level_question.get(intNext).getQuestion());
                                tv_answer.setText(link_level_question.get(intNext).getAnswer());
                            }
                        }catch (Exception e){
                            btn_start2.setText("Complete");
                            tv_ques_no.setText("Question: "+link_level_question.get(intNext).getSr_no());
                            tv_question.setText(link_level_question.get(intNext).getQuestion());
                            tv_answer.setText(link_level_question.get(intNext).getAnswer());
                        }




                    } else {
                        Toast.makeText(getContext(), "No Question Found", Toast.LENGTH_SHORT).show();

                        Fragment view_creat=new fragment_level();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame,view_creat);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext()).setTitle("Learning English")
                                .setMessage("No Question Found");

                        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

//                        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                //exitLauncher();
//                            }

                       // });
                        final AlertDialog alert = dialog.create();
                        alert.show();



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


}
