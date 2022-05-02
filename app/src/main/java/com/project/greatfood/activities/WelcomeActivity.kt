package com.project.greatfood.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.project.greatfood.R

class WelcomeActivity : AppCompatActivity() {
    var next: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    fun Ingresar(view: View?) {
        val ingresar = Intent(this, MainActivity::class.java)
        startActivity(ingresar)
    }
}