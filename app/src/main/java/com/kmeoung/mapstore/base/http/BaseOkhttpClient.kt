package com.truevalue.dreamappeal.http

import android.os.Handler
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


object BaseOkhttpClient : OkHttpClient() {

    private val client: OkHttpClient
    private val handler: Handler

    init {
        client = OkHttpClient()
        handler = Handler()
    }

    fun request(
        http_type: Int,
        url: String,
        header: BaseHttpHeader?,
        params: BaseHttpParams?,
        callback: BaseHttpCallback?
    ) {
        val clientRequest = when (http_type) {
            HttpType.POST -> post(url, header, params)
            HttpType.GET -> get(url, header, params)
            HttpType.PATCH -> patch(url, header, params)
            HttpType.DELETE -> delete(url, header, params)
            else -> post(url, header, params)
        }
        if(params != null) Log.d("TEST", params.bodyParams().toString())
        val call = client.newCall(clientRequest)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (callback != null) {
                    callback.onFailure(call, e)
                } else return
            }

            override fun onResponse(call: Call, response: Response) {

                val strBody = response.body!!.string()
                Log.d("TEST", strBody)
                if (callback != null) {
                    handler.post(Runnable { callback.onResponse(call, response.code, strBody) })
                }

            }
        })
    }

    /**
     * Http GET
     */
    private fun get(
        url: String,
        header: BaseHttpHeader?,
        params: BaseHttpParams?
    ): Request {
        var requestUrl = url
        if (params != null) {
            requestUrl += params.urlParams()
        }
        var builder: Request.Builder = Request.Builder()
        if (header != null)
            builder = header.getBuilder()
        Log.d("TEST", requestUrl)
        return builder.url(requestUrl)
            .get()
            .build()
    }

    /**
     * Http POST
     */
    private fun post(
        url: String,
        header: BaseHttpHeader?,
        params: BaseHttpParams?
    ): Request {
        var requestBody: RequestBody = BaseHttpParams.toRequestType(JSONObject())
        if (params != null) {
            requestBody = params.bodyParams()
        }

        var builder: Request.Builder = Request.Builder()
        if (header != null)
            builder = header.getBuilder()

        return builder.url(url)
            .post(requestBody)
            .build()
    }

    /**
     * Http PATCH
     */
    private fun patch(
        url: String,
        header: BaseHttpHeader?,
        params: BaseHttpParams?
    ): Request {
        var requestBody: RequestBody = BaseHttpParams.toRequestType(JSONObject())
        if (params != null) {
            requestBody = params.bodyParams()
        }

        var builder: Request.Builder = Request.Builder()
        if (header != null)
            builder = header.getBuilder()

        return builder.url(url)
            .patch(requestBody)
            .build()
    }

    /**
     * Http DELETE
     */
    private fun delete(
        url: String,
        header: BaseHttpHeader?,
        params: BaseHttpParams?
    ): Request {
        var requestBody: RequestBody = BaseHttpParams.toRequestType(JSONObject())
        if (params != null) {
            requestBody = params.bodyParams()
        }

        var builder: Request.Builder = Request.Builder()
        if (header != null)
            builder = header.getBuilder()

        return builder.url(url)
            .delete(requestBody)
            .build()
    }

}