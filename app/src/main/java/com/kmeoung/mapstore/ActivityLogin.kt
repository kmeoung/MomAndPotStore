package com.kmeoung.mapstore

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kmeoung.mapstore.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        onClickView()
    }
    
    private fun onClickView(){
        val listener = View.OnClickListener{
            when(it){
                btn_login->{
                    val intent = Intent(this@ActivityLogin,ActivityMain::class.java)
                    startActivity(intent)
                    finish()
                }
                tv_register->{
                    val intent = Intent(this@ActivityLogin,ActivityRegister::class.java)
                    startActivity(intent)
                }
                tv_forgot_password->{
                    // todo : 비밀번호 찾기 페이지 연동
                }
            }
        }
        btn_login.setOnClickListener(listener)
        tv_register.setOnClickListener(listener)
        tv_forgot_password.setOnClickListener(listener)
    }
}