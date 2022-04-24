package com.app.greatfood.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.greatfood.R;
import com.app.greatfood.activities.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void Ingresar (View view) {
        Intent ingresar = new Intent (this, MainActivity.class);
        startActivity(ingresar);
    }

    }


