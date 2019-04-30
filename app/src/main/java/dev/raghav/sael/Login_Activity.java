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
import android.widget.TextView;
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

public class Login_Activity extends AppCompatActivity {

   Button button_signin;
   TextView new_reg, forget_pw;

   EditText et_email, et_password;
   String Et_Email, Et_Password;

    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        getSupportActionBar().hide();
        manager=new SessionManager(Login_Activity.this);

        button_signin=findViewById(R.id.button_signin);
        et_email=findViewById(R.id.edit_email);
        et_password=findViewById(R.id.edit_pw);
        forget_pw=findViewById(R.id.tv_forgot);

        new_reg=findViewById(R.id.new_reg);

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Et_Email=et_email.getText().toString();
                Et_Password=et_password.getText().toString();

//                Intent intent=new Intent(Login_Activity.this,Main2Activity.class);
//                startActivity(intent);
                if (Connectivity.isNetworkAvailable(Login_Activity.this)){

                  if (!Et_Email.isEmpty() && !Et_Password.isEmpty()){
                      new LoginExcute().execute();
                  }else {
                      if (Et_Email.isEmpty()){
                          et_email.setError("Please Enter Email or Mobile");
                      }if (Et_Password.isEmpty()){
                          et_password.setError("Please Enter Password");
                      }
                  }

                }else {
                    Toast.makeText(Login_Activity.this, "Please Check Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        new_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_Activity.this,Registration_Activity.class);
                startActivity(intent);
            }
        });

        forget_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_Activity.this,Forgot_Password.class);
                startActivity(intent);
            }
        });
    }

    private class LoginExcute extends AsyncTask<String, Integer, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Login_Activity.this);
            dialog.setMessage("Processing...");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                URL url = new URL("https://jntrcpl.com/staracademy/Api/user_login");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username",Et_Email);
                postDataParams.put("password",Et_Password);

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
                   String user_id=data.getString("user_id");
                    String name=data.getString("name");
                    String email=data.getString("email");
                    String mobile=data.getString("mobile");
                    String show_password=data.getString("show_password");


                   // AppPreference.setFirstname(Login_Activity.this,firstname);

                        manager.setLogin(true);

                        Intent intent = new Intent(Login_Activity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Login_Activity.this, "login success", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(Login_Activity.this, "Invalid details login error", Toast.LENGTH_SHORT).show();
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
