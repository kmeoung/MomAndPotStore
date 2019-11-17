package com.kmeoung.mapstore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.kmeoung.mapstore.base.BaseActivity

class ActivityIntro : BaseActivity() {

    private val DELAY: Long = 1000 * 1
    private val handler = Handler(Handler.Callback {
        val intent = Intent(this@ActivityIntro,ActivityLogin::class.java)
        startActivity(intent)
        finish()
        false
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }

    override fun onResume() {
        super.onResume()
        handler.sendEmptyMessageDelayed(0, DELAY)
    }

    override fun onPause() {
        super.onPause()
        handler.removeMessages(0)
    }
}