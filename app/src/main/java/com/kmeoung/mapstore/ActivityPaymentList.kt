package com.kmeoung.mapstore

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmeoung.mapstore.base.BaseActivity
import com.kmeoung.mapstore.base.BaseRecyclerViewAdapter2
import com.truevalue.dreamappeal.base.BaseViewHolder
import com.truevalue.dreamappeal.base.IORecyclerViewListener
import kotlinx.android.synthetic.main.content_main.*

class ActivityPaymentList : BaseActivity(){

    private var mAdapter : BaseRecyclerViewAdapter2<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_list)

        initAdapter()
    }

    private fun initAdapter(){
        mAdapter = BaseRecyclerViewAdapter2(object : IORecyclerViewListener {
            override val itemCount: Int
                get() = if(mAdapter != null) mAdapter!!.size() else 0

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
                return BaseViewHolder.newInstance(R.layout.listitem_menu,parent,false)
            }

            override fun onBindViewHolder(h: BaseViewHolder, i: Int) {

            }

            override fun getItemViewType(i: Int): Int {
                return 0
            }
        })

        rv_layout.adapter = mAdapter
        rv_layout.layoutManager = LinearLayoutManager(applicationContext)
    }
}