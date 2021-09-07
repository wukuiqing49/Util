package com.wu.util


/**
 * @author wkq
 *
 * @date 2021年09月06日 15:28
 *
 *@des
 *
 */

class UserInfo() {
    var name: String=""
    var icon: String=""
    var age: Int=0
    var phoneNum: String=""
    var id: Int=0
    override fun toString(): String {
        return "UserInfo(name='$name', icon='$icon', age=$age, phoneNum='$phoneNum', id=$id)"
    }

}