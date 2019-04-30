package dev.raghav.sael;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import dev.raghav.sael.Connectivity.SessionManager;

public class Splash_Screen_Activity extends AppCompatActivity {

    SessionManager manager;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash__screen_);

        getSupportActionBar().hide();

        manager =new SessionManager(Splash_Screen_Activity.this);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try{
                    if (manager.isLoggedIn()) {

                        Intent intent = new Intent(Splash_Screen_Activity.this, Main2Activity.class);
                        startActivity(intent);
                       finish();
                    } else {
                        Intent intent = new Intent(Splash_Screen_Activity.this, Login_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e) {
                }

            }
        }, SPLASH_TIME_OUT);




    }
}
