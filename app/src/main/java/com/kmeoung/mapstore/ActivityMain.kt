package com.kmeoung.mapstore

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kmeoung.mapstore.base.BaseActivity
import com.kmeoung.mapstore.base.BaseRecyclerViewAdapter2
import com.truevalue.dreamappeal.base.BaseViewHolder
import com.truevalue.dreamappeal.base.IORecyclerViewListener

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class ActivityMain : BaseActivity() {

    private var mAdapter : BaseRecyclerViewAdapter2<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initAdapter()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action",View.OnClickListener{
                    mAdapter!!.add("")
                }).show()
        }

        btn_purchase.setOnClickListener {
            view->Snackbar.make(view, "진짜 구매?", Snackbar.LENGTH_LONG)
            .setAction("YES",View.OnClickListener{
                mAdapter!!.clear()
                val intent = Intent(this@ActivityMain,ActivityPayment::class.java)
                startActivity(intent)
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

            }

            override fun getItemViewType(i: Int): Int {
                return 0
            }
        })

        rv_layout.adapter = mAdapter
        rv_layout.layoutManager = LinearLayoutManager(applicationContext)
    }
}
