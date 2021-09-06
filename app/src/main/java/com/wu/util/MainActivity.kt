package com.wu.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wu.util.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        initView();
    }

    private fun initView() {

        binding!!.rvContent.layoutManager = LinearLayoutManager(this)

        var mAdapter = MainAdapter(this);

        var userList = ArrayList<UserInfo>()

        for (index in 0..20) {
            var userInfo = UserInfo()
            userInfo.name = "测试" + index
            userInfo.phoneNum = "1853853738" + index
            userList.add(userInfo)
        }
        binding!!.rvContent.adapter = mAdapter
        mAdapter.addItems(userList)

    }
}