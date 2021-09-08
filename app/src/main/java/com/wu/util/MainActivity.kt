package com.wu.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wu.base.util.screen.ScreenShotUtil
import com.wu.util.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var binding: ActivityMainBinding? = null

    var userList = ArrayList<UserInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        initView()
        binding!!.onClick = this
    }

    var mAdapter: MainAdapter? = null
    private fun initView() {

        binding!!.rvContent.layoutManager = LinearLayoutManager(this)

        mAdapter = MainAdapter(this);


        for (index in 0..5) {
            var userInfo = UserInfo()
            userInfo.id = index
            userInfo.name = "测试" + index
            userInfo.phoneNum = "1853853738" + index
            userList.add(userInfo)
        }
        binding!!.rvContent.adapter = mAdapter
        mAdapter!!.addItems(userList)

//        binding!!.btRf.setOnClickListener {
//            setNewData()
//        }
//        binding!!.btAdd.setOnClickListener {
//            setAddData()
//        }
//        binding!!.btRemove.setOnClickListener {
//            setNewData()
//        }

    }

    private fun setAddData() {
        var userList = ArrayList<UserInfo>()
        var lengh = mAdapter!!.getItems()!!.size
        userList.addAll(mAdapter!!.getItems()!!)
        for (index in 0..10) {
            var userInfo = UserInfo()
            userInfo.id = (index + lengh)
            userInfo.name = "测试" + userInfo.id
            userInfo.phoneNum = "1853853738" + userInfo.id
            userList.add(userInfo)
        }

        var result =
            DiffUtil.calculateDiff(RecyclerViewDiffCallBack(mAdapter!!.getItems()!!, userList))
        mAdapter!!.updateItems(userList)
        result.dispatchUpdatesTo(mAdapter!!)

    }

    private fun setNewData() {
        var userList = ArrayList<UserInfo>()
        for (index in 0..mAdapter!!.getItems()!!.size) {
            var userInfo = UserInfo()
            if (index == 0) {
                userInfo.name = "开始"
            } else if (index == mAdapter!!.getItems()!!.size) {
                userInfo.name = "结束"
            } else {
                userInfo.name = "测试" + index
            }
            userInfo.id = index
            userInfo.phoneNum = "1853853738" + index
            userList.add(userInfo)
        }
        var result = DiffUtil.calculateDiff(RecyclerViewDiffCallBack(mAdapter!!.getItems()!!, userList))

        mAdapter!!.setDatas(userList)

        result.dispatchUpdatesTo(mAdapter!!)
    }


    private fun removeData() {

        var userList = ArrayList<UserInfo>()
        userList.addAll(mAdapter!!.getItems()!!)
        if (userList == null || userList.size == 0) return
        userList.removeAt(0)
        var result =
            DiffUtil.calculateDiff(RecyclerViewDiffCallBack(mAdapter!!.getItems()!!, userList))
        mAdapter!!.updateItems(userList)
        result.dispatchUpdatesTo(mAdapter!!)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_rf -> {
                setNewData()
            }
            R.id.bt_add -> {
                setAddData()
            }
            R.id.bt_remove -> {
                removeData()
            }
            R.id.bt_ald -> {
                var intent = Intent(this, AsyncListDifferActivity::class.java)
                startActivity(intent);
            }
            R.id.bt_screen -> {
              showLarge();
            }
        }
    }

    private fun showLarge() {
        binding!!.ivLarge.visibility=View.VISIBLE

       var bitmap= ScreenShotUtil.getScreenPath(binding!!.rvContent);

        binding!!.ivLarge.setBitmap(bitmap)

    }


}