package com.kmeoung.mapstore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kmeoung.mapstore.base.BaseActivity
import com.kmeoung.mapstore.base.BaseRecyclerViewAdapter2
import com.truevalue.dreamappeal.base.BaseViewHolder
import com.truevalue.dreamappeal.base.IORecyclerViewListener
import com.truevalue.dreamappeal.http.BaseHttpCallback
import com.truevalue.dreamappeal.http.BaseHttpParams
import com.truevalue.dreamappeal.http.BaseOkhttpClient
import com.truevalue.dreamappeal.http.HttpType
import com.truevalue.dreamappeal.utils.Comm_Prefs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Call
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ActivityMain : BaseActivity() {

    private var mAdapter : BaseRecyclerViewAdapter2<Any>? = null
    private val REQUEST_ADD_MENU = 1004
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initAdapter()

        fab.setOnClickListener {
            val intent = Intent(this@ActivityMain,ActivityPaymentList::class.java)
            intent.putExtra(ActivityPaymentList.EXTRA_VIEW_TYPE,ActivityPaymentList.EXTRA_TYPE_PRODUCT_LIST)
            startActivityForResult(intent,REQUEST_ADD_MENU)
        }

        btn_purchase.setOnClickListener {
            view->Snackbar.make(view, "진짜 구매?", Snackbar.LENGTH_LONG)
            .setAction("YES",View.OnClickListener{

                if(mAdapter!!.size() < 1) {
                    Toast.makeText(this@ActivityMain,"장바구니를 채워주세요",Toast.LENGTH_SHORT).show()
                }else{
                    val ido = Comm_Prefs.getCreateIdo() + 1
                    Comm_Prefs.setCreateIdo(ido)
                    for(i in 0 until mAdapter!!.size()) {
                        payment(mAdapter!!.get(i) as BeanProduct,i,ido)
                    }
                }

            }).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val intent = Intent(this@ActivityMain,ActivityPaymentList::class.java)
        intent.putExtra(ActivityPaymentList.EXTRA_VIEW_TYPE,ActivityPaymentList.EXTRA_TYPE_ORDER_LIST)
        startActivity(intent)


        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initAdapter(){
        mAdapter = BaseRecyclerViewAdapter2(object : IORecyclerViewListener{
            override val itemCount: Int
                get() = if(mAdapter != null) mAdapter!!.size() else 0

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return BaseViewHolder.newInstance(R.layout.listitem_menu,parent,false)
            }

            override fun onBindViewHolder(h: BaseViewHolder, i: Int) {
                val tvTitle = h.getItemView<TextView>(R.id.tv_title)
                val tvContents = h.getItemView<TextView>(R.id.tv_contents)
                val ivImage = h.getItemView<ImageView>(R.id.iv_img)
                val ivAdd = h.getItemView<ImageView>(R.id.iv_add)

                if(mAdapter != null){
                    val bean = mAdapter!!.get(i) as BeanProduct

                    ivImage.visibility = View.VISIBLE
                    ivAdd.visibility = View.GONE
                    Glide.with(this@ActivityMain)
                        .load(bean.img)
                        .into(ivImage)

                    tvTitle.text = bean.name
                    tvContents.text = String.format("%,d원 / %d개",bean.price,bean.count)

                    ivAdd.setOnClickListener(View.OnClickListener {
                        val intent = Intent()
                        intent.putExtra(ActivityPaymentList.REQUEST_DATA, bean)
                        setResult(Activity.RESULT_OK, intent)
                    })
                }

            }

            override fun getItemViewType(i: Int): Int {
                return 0
            }
        })

        rv_layout.adapter = mAdapter
        rv_layout.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_ADD_MENU){
                val bean = data!!.getSerializableExtra(ActivityPaymentList.REQUEST_DATA) as BeanProduct
                mAdapter!!.add(bean)
            }
        }
    }

    private fun payment(bean : BeanProduct,position : Int,ido:Int) {

        val params = BaseHttpParams()

        val ido: Int = ido
        val idx: Int = bean.idx
        val amount: Int = 1
        val buyer: Int = Comm_Prefs.getIdm()
        val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: String = dateTime.format(Date())
        val state: Int = 1

        params.put("ido", ido)
        params.put("idx", idx)
        params.put("amount", amount)
        params.put("buyer", buyer)
        params.put("date", date)
        params.put("state", state)
        //    params.put("memberJson",json.toString())
        BaseOkhttpClient.request(
            HttpType.GET,
            Comm_Params.URL_INSERT_ORDER,
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
                        e.printStackTrace()
                    } finally {
                        if (!isError) { // 데이터가 있음
                            val success = json.getString("success")
                        }
                    }
                    if(position == mAdapter!!.size() - 1){
                        Toast.makeText(this@ActivityMain, "주문에 성공하셨습니다.", Toast.LENGTH_SHORT)
                            .show()
                        mAdapter!!.clear()
                        val intent = Intent(this@ActivityMain,ActivityPayment::class.java)
                        startActivity(intent)
                    }
                }
            }

        )
    }

}
