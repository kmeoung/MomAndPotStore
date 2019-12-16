package com.truevalue.dreamappeal.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.kmeoung.mapstore.Comm_Params

object Comm_Prefs {

    private var mContext: Context? = null
    private var prefs: SharedPreferences?

    init {
        prefs = null
    }

    /**
     * App 실행 시 한번만 실행
     */
    fun init(context: Context) {
        mContext = context
        prefs = mContext!!.getSharedPreferences(Comm_Params.APP_NAME, MODE_PRIVATE)
        if(getCreateIdm() == -1){
            setCreateIdm(10)
        }
        if(getCreateIdo() == -1){
            setCreateIdo(100)
        }
    }

    // 현재코드
    fun setIdm(token : Int){
        prefs!!.edit().putInt(Comm_Prefs_Param.PREFS_USER_IDM,token).commit()
    }

    fun getIdm() : Int{
        return prefs!!.getInt(Comm_Prefs_Param.PREFS_USER_IDM,-1)
    }

    // 생산코드
    fun setCreateIdm(idx : Int){
        prefs!!.edit().putInt(Comm_Prefs_Param.PREFS_CREATE_USER_IDM,idx).commit()
    }

    fun getCreateIdm() : Int{
        return prefs!!.getInt(Comm_Prefs_Param.PREFS_CREATE_USER_IDM,-1)
    }

    // 생산코드
    fun setCreateIdo(idx : Int){
        prefs!!.edit().putInt(Comm_Prefs_Param.PREFS_CREATE_USER_IDM,idx).commit()
    }

    fun getCreateIdo() : Int{
        return prefs!!.getInt(Comm_Prefs_Param.PREFS_CREATE_USER_IDM,-1)
    }





}