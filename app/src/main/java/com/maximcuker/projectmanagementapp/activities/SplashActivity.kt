package com.maximcuker.projectmanagementapp.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.maximcuker.projectmanagementapp.R
import com.maximcuker.projectmanagementapp.firebase.FirestoreClass
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //use font from link https://www.1001fonts.com/cramaten-font.html#more
        val typeFace:Typeface = Typeface.createFromAsset(assets,"Cramaten.ttf")
        tv_app_name.typeface = typeFace

        Handler().postDelayed({
            var currentUserId = FirestoreClass().getCurrentUserId()
            if (currentUserId?.isNotEmpty() == true) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        },2500)
    }
}