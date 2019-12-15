package com.kmeoung.mapstore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.kmeoung.mapstore.base.BaseActivity
import com.truevalue.dreamappeal.http.BaseHttpCallback
import com.truevalue.dreamappeal.http.BaseHttpParams
import com.truevalue.dreamappeal.http.BaseOkhttpClient
import com.truevalue.dreamappeal.http.HttpType
import com.truevalue.dreamappeal.utils.Comm_Prefs
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Call
import org.json.JSONObject

class ActivityLogin : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Comm_Prefs.init(this@ActivityLogin)

        val id = "nzwfbx"
        val pw = "zx78776230"
        et_email.setText(id)
        et_password.setText(pw)

        onClickView()
    }

    private fun onClickView() {
        val listener = View.OnClickListener {
            when (it) {
                btn_login -> {
                    login()
                }
                tv_register -> {
                    val intent = Intent(this@ActivityLogin, ActivityRegister::class.java)
                    startActivity(intent)
                }
            }
        }
        btn_login.setOnClickListener(listener)
        tv_register.setOnClickListener(listener)
    }

    private fun login() {

        val params = BaseHttpParams()
        params.put("id", et_email.text.toString())
        params.put("pw", et_password.text.toString())

        if(et_email.text.toString().isNullOrEmpty() || et_password.text.toString().isNullOrEmpty()){
            Toast.makeText(this@ActivityLogin, "아이디 / 비밀번호를 제대로 입력해주세요", Toast.LENGTH_SHORT)
                .show()
            return
        }

        BaseOkhttpClient.request(
            HttpType.GET,
            Comm_Params.URL_SELECT_MEMBER,
            null,
            params,
            object : BaseHttpCallback {
                override fun onResponse(
                    call: Call,
                    serverCode: Int,
                    body: String
                    ) {
                    val json = JSONObject(body)
                    val error = json.getString("error");
                    if (error.isNullOrEmpty()) { // 데이터가 있음
                        val idm = json.getInt("idm")
                        val isManager = json.getInt("is_manager")
                        val name = json.getString("m_name")
                        val addr = json.getString("address")
                        val id = json.getString("id")

                        Comm_Prefs.setIdm(idm)

                        Toast.makeText(this@ActivityLogin, "${name}님 환영합니다", Toast.LENGTH_SHORT)
                            .show()

                        val intent = Intent(this@ActivityLogin, ActivityMain::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@ActivityLogin,
                            "아이디 / 비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

        )
    }
}