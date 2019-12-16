package com.kmeoung.mapstore

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kmeoung.mapstore.base.BaseActivity
import com.truevalue.dreamappeal.http.BaseHttpCallback
import com.truevalue.dreamappeal.http.BaseHttpParams
import com.truevalue.dreamappeal.http.BaseOkhttpClient
import com.truevalue.dreamappeal.http.HttpType
import com.truevalue.dreamappeal.utils.Comm_Prefs
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.Call
import org.json.JSONObject

class ActivityRegister : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        onClickView()
    }

    private fun onClickView() {
        val listener = View.OnClickListener {
            when (it) {
                btn_register -> {
                    register()
                }
            }
        }
        btn_register.setOnClickListener(listener)
    }

    private fun register() {

        val name = et_name.text.toString()
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        val rePw = et_re_password.text.toString()
        val addr = et_address.text.toString()

        if (name.isNullOrEmpty() ||
            email.isNullOrEmpty() ||
            password.isNullOrEmpty() ||
            addr.isNullOrEmpty()
        ) {
            Toast.makeText(this@ActivityRegister, "데이터를 제대로 채워주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (!password.equals(rePw)) {
            Toast.makeText(this@ActivityRegister, "비밀번호가 서로 다릅니다", Toast.LENGTH_SHORT).show()
            return
        }

        val params = BaseHttpParams()
        val idm = Comm_Prefs.getCreateIdm() + 1
        params.put("idm", idm)
        Comm_Prefs.setCreateIdm(idm)
        params.put("m_name", name)
        params.put("is_manager", 0)
        params.put("address", addr)
        params.put("id", email)
        params.put("pw", password)
        //    params.put("memberJson",json.toString())
        BaseOkhttpClient.request(
            HttpType.GET,
            Comm_Params.URL_INSERT_MEMBER,
            null,
            params,
            object : BaseHttpCallback {
                override fun onResponse(
                    call: Call,
                    serverCode: Int,
                    body: String
                ) {
                    val json = JSONObject(body)
                    var isError = false
                    try {
                        val error = json.getString("error")
                        isError = true
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ActivityRegister,
                            "회원가입에 실패하였습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } finally {
                        if (!isError) { // 데이터가 있음
                            val success = json.getString("success")

                            if (success.equals("success")) {
                                Toast.makeText(this@ActivityRegister, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT)
                                    .show()

                                finish()
                            }
                        }
                    }
                }
            }

        )
    }

}