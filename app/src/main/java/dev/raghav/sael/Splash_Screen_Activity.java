package dev.raghav.sael;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash_Screen_Activity extends AppCompatActivity {

  //  SessionManager manager;

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

       // manager =new SessionManager(SplashActivity.this);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try{
                    Intent intent = new Intent(Splash_Screen_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();

//                    if (manager.isLoggedIn()) {
//
//                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        SplashActivity.this.finish();
//                    } else {
//                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        SplashActivity.this.finish();
//                    }
                }catch (Exception e) {
                }
//                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(i);

                // close this activity
//                    finish();
            }
        }, SPLASH_TIME_OUT);




    }
}
