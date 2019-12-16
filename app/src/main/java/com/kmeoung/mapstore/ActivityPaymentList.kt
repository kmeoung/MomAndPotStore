package com.kmeoung.mapstore

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.kmeoung.mapstore.base.BaseActivity
import com.kmeoung.mapstore.base.BaseRecyclerViewAdapter2
import com.truevalue.dreamappeal.base.BaseViewHolder
import com.truevalue.dreamappeal.base.IORecyclerViewListener
import com.truevalue.dreamappeal.http.BaseHttpCallback
import com.truevalue.dreamappeal.http.BaseHttpParams
import com.truevalue.dreamappeal.http.BaseOkhttpClient
import com.truevalue.dreamappeal.http.HttpType
import com.truevalue.dreamappeal.utils.Comm_Prefs
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Call
import org.json.JSONArray
import org.json.JSONObject

class ActivityPaymentList : BaseActivity() {

    private var mAdapter: BaseRecyclerViewAdapter2<Any>? = null
    private var mViewType: String = ""

    companion object {
        val EXTRA_VIEW_TYPE = "EXTRA_VIEW_TYPE"
        val EXTRA_TYPE_ORDER_LIST = "EXTRA_TYPE_ORDER_LIST"
        val EXTRA_TYPE_PRODUCT_LIST = "EXTRA_TYPE_PRODUCT_LIST"
        val REQUEST_DATA = "REQUEST_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_list)

        initAdapter()
        initView()
    }

    private fun initView() {
        if (intent.getStringExtra(EXTRA_VIEW_TYPE) != null) {
            mViewType = intent.getStringExtra(EXTRA_VIEW_TYPE)

            when (mViewType) {
                EXTRA_TYPE_ORDER_LIST -> {
                    getOrder()
                }
                EXTRA_TYPE_PRODUCT_LIST -> {
                    getProducts()
                }
            }
        }
    }

    private fun getOrder() {

        val params = BaseHttpParams()
        params.put("idm", Comm_Prefs.getIdm())

        BaseOkhttpClient.request(
            HttpType.GET,
            Comm_Params.URL_SELECT_ORDER,
            null,
            params,
            object : BaseHttpCallback {
                override fun onResponse(
                    call: Call,
                    serverCode: Int,
                    body: String
                ) {

                    val json = JSONArray(body)
                    if(json.length() < 1) Toast.makeText(this@ActivityPaymentList,"데이터가 존재하지 않습니다",Toast.LENGTH_SHORT).show()
                    for (i in 0 until json.length()) {
                        val Object = json.getJSONObject(i)
                        val bean = Gson().fromJson<BeanOrder>(Object.toString(),BeanOrder::class.java)
                        mAdapter!!.add(bean)
                    }
                }
            }

        )
    }

    private fun getProducts() {

        val params = BaseHttpParams()

        BaseOkhttpClient.request(
            HttpType.GET,
            Comm_Params.URL_SELECT_PRODUCT,
            null,
            params,
            object : BaseHttpCallback {
                override fun onResponse(
                    call: Call,
                    serverCode: Int,
                    body: String
                ) {
                    mAdapter!!.clear()
                    val json = JSONArray(body)
                    for (i in 0 until json.length()) {
                        val Object = json.getJSONObject(i)
                        val idx = Object.getInt("idx")
                        val name = Object.getString("name")
                        val price = Object.getInt("price")
                        val count = Object.getInt("count")
                        val img = Object.getString("img")

                        val bean = BeanProduct(idx, name, price, count, img)
                        mAdapter!!.add(bean)
                    }
                }
            }

        )
    }


    private fun initAdapter() {
        mAdapter = BaseRecyclerViewAdapter2(object : IORecyclerViewListener {
            override val itemCount: Int
                get() = if (mAdapter != null) mAdapter!!.size() else 0

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return BaseViewHolder.newInstance(R.layout.listitem_menu, parent, false)
            }

            override fun onBindViewHolder(h: BaseViewHolder, i: Int) {
                val tvTitle = h.getItemView<TextView>(R.id.tv_title)
                val tvContents = h.getItemView<TextView>(R.id.tv_contents)
                val ivImage = h.getItemView<ImageView>(R.id.iv_img)
                val ivAdd = h.getItemView<ImageView>(R.id.iv_add)

                if(mViewType == EXTRA_TYPE_PRODUCT_LIST){
                    if(mAdapter != null){
                        val bean = mAdapter!!.get(i) as BeanProduct

                        ivImage.visibility = VISIBLE
                        ivAdd.visibility = VISIBLE
                        Glide.with(this@ActivityPaymentList)
                            .load(bean.img)
                            .into(ivImage)

                        tvTitle.text = bean.name
                        tvContents.text = String.format("%,d원 / %d개",bean.price,bean.count)

                        ivAdd.setOnClickListener(View.OnClickListener {
                            val intent = Intent()
                            intent.putExtra(REQUEST_DATA, bean)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        })
                    }
                }else if(mViewType == EXTRA_TYPE_ORDER_LIST){
                    if(mAdapter != null){
                        val bean = mAdapter!!.get(i) as BeanOrder

                        ivImage.visibility = GONE
                        ivAdd.visibility = GONE
                        tvTitle.text = bean.name
                        tvContents.text = String.format("%,d원 / %d개 \n %s",bean.price,bean.amount,bean.date)

                    }
                }
            }

            override fun getItemViewType(i: Int): Int {
                return 0
            }
        })

        rv_layout.adapter = mAdapter
        rv_layout.layoutManager = LinearLayoutManager(applicationContext)
    }
}