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

        if(name.isNullOrEmpty() ||
                email.isNullOrEmpty() ||
                password.isNullOrEmpty() ||
                addr.isNullOrEmpty()){
            Toast.makeText(this@ActivityRegister,"데이터를 제대로 채워주세요",Toast.LENGTH_SHORT).show()
            return
        }

        if(!password.equals(rePw)){
            Toast.makeText(this@ActivityRegister,"비밀번호가 서로 다릅니다",Toast.LENGTH_SHORT).show()
            return
        }

        val params = BaseHttpParams()
        val idm = Comm_Prefs.getCreateIdm() +1
        val json = JSONObject()
        json.put("idm", idm)
        Comm_Prefs.setCreateIdm(idm)
        json.put("m_name", name)
        json.put("is_manager", 0)
        json.put("address",addr)
        json.put("id",email)
        json.put("pw",password)
        val parentJson = JSONObject()
        parentJson.put("memberJson",json)
        params.put(parentJson)
        BaseOkhttpClient.request(
            HttpType.POST,
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
                    val error = json.getString("error");
                    if (error.isNullOrEmpty()) { // 데이터가 있음
                        Toast.makeText(this@ActivityRegister, json.toString(), Toast.LENGTH_SHORT)
                            .show()

                        val idm = json.getInt("idm")
                        val isManager = json.getInt("is_manager")
                        val addr = json.getString("address")
                        val id = json.getString("id")
                        val password = json.getString("pw")

//                        val intent = Intent(this@ActivityRegister, ActivityMain::class.java)
//                        startActivity(intent)
//                        finish()
                    } else {
                        Toast.makeText(
                            this@ActivityRegister,
                            "회원가입에 실패하였습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

        )
    }

}