package dev.raghav.sael;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import dev.raghav.sael.Connectivity.Connectivity;
import dev.raghav.sael.Connectivity.SessionManager;
import dev.raghav.sael.Connectivity.SharedPref;

public class Registration_Activity extends AppCompatActivity {

    Button button_register;
    EditText et_name,et_email,et_mobile,et_pass;
    String Et_Name,Et_Email,Et_Mobile,Et_Pass;

    SessionManager manager;
     String user_id,name,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);

        getSupportActionBar().hide();
        manager=new SessionManager(Registration_Activity.this);

        button_register=findViewById(R.id.button_signin);
        et_email=findViewById(R.id.et_email);
        et_name=findViewById(R.id.et_fullname);
        et_mobile=findViewById(R.id.et_mobile);
        et_pass=findViewById(R.id.et_pw);


        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Registration_Activity.this,Main2Activity.class);
//                startActivity(intent);

                Et_Name=et_name.getText().toString();
                Et_Pass=et_pass.getText().toString();
                Et_Email=et_email.getText().toString();
                Et_Mobile=et_mobile.getText().toString();
                if (Connectivity.isNetworkAvailable(Registration_Activity.this)){

                    if (!Et_Name.isEmpty() && !Et_Mobile.isEmpty() && !Et_Pass.isEmpty() && !Et_Email.isEmpty()){
                        new RegistrationExcute().execute();
                    }else {
                        Toast.makeText(Registration_Activity.this, "All Field Are Required", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Registration_Activity.this, "Please Check Internet", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    private class RegistrationExcute extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Registration_Activity.this);
            dialog.setMessage("Processing...");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://jntrcpl.com/staracademy/Api/registration");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name",Et_Name);
                postDataParams.put("mobile",Et_Mobile);
                postDataParams.put("password",Et_Pass);
                postDataParams.put("email",Et_Email);

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

                    JSONObject data= new JSONObject(result).getJSONObject("userdata");
                     user_id=data.getString("user_id");
                     name=data.getString("name");
                     email=data.getString("email");
                    String mobile=data.getString("mobile");
                    String show_password=data.getString("show_password");


                    SharedPref.setUserid(Registration_Activity.this,user_id);
                    SharedPref.setFirstname(Registration_Activity.this,name);
                    SharedPref.setEmail(Registration_Activity.this,email);

                        manager.setLogin(true);

                        Intent intent = new Intent(Registration_Activity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();

                        Toast.makeText(Registration_Activity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                    } else {
                        String error = object.getString("error");
                        Toast.makeText(Registration_Activity.this, ""+error, Toast.LENGTH_SHORT).show();

                        Toast.makeText(Registration_Activity.this, "Some Problem, Please Try Again", Toast.LENGTH_SHORT).show();
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
