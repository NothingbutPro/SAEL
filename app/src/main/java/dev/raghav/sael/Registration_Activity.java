package dev.raghav.sael;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registration_Activity extends AppCompatActivity {

    Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);

        getSupportActionBar().hide();
        button_register=findViewById(R.id.button_signin);


        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Registration_Activity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
