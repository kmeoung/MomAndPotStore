package com.kmeoung.mapstore

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kmeoung.mapstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class ActivityRegister : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        onClickView()
    }

    private fun onClickView(){
        val listener = View.OnClickListener{
            when(it){
                btn_register->{
                    finish()
                }
            }
        }
        btn_register.setOnClickListener(listener)
    }
}