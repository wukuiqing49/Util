package com.wu.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.wu.util.databinding.ActivityAdlBinding

/**
 * DiffUtil 异步刷新
 */
class AsyncListDifferActivity : AppCompatActivity(), View.OnClickListener {

    var binding: ActivityAdlBinding? = null
    var userList = ArrayList<UserInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityAdlBinding>(this, R.layout.activity_adl)
        initView()

        binding!!.onClick = this
    }
    var mAdapter: AsyncListDifferAdapter? = null
    private fun initView() {
        binding!!.rvContent.layoutManager = LinearLayoutManager(this)
        mAdapter = AsyncListDifferAdapter(this)
        for (index in 0..10) {
            var userInfo = UserInfo()
            userInfo.id = index
            userInfo.name = "测试" + index
            userInfo.phoneNum = "1853853738" + index
            userList.add(userInfo)
        }
        binding!!.rvContent.adapter = mAdapter
        mAdapter!!.setDatas(userList)

        binding!!.btRf.setOnClickListener {
            setNewData()
        }
        binding!!.btAdd.setOnClickListener {
            setAddData()
        }
        binding!!.btRemove.setOnClickListener {
            setNewData()
        }

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
        mAdapter!!.setDatas(userList)

    }

    private fun setNewData() {
        var userList = ArrayList<UserInfo>()
        for (index in 0..10) {
            var userInfo = UserInfo()
            if (index == 0) {
                userInfo.name = "开始"
            } else if (index == 10) {
                userInfo.name = "结束"
            } else {
                userInfo.name = "测试" + index
            }
            userInfo.id = index
            userInfo.phoneNum = "1853853738" + index
            userList.add(userInfo)
        }
        mAdapter!!.setDatas(userList)
    }


    private fun removeData() {

        var userList = ArrayList<UserInfo>()
        userList.addAll(mAdapter!!.getItems()!!)
        if (userList == null || userList.size == 0) return
        userList.removeAt(0)
        mAdapter!!.setDatas(userList)
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
        }
    }


}