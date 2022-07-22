package com.wu.base.util

import android.net.Uri
import android.text.TextUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.HttpURLConnection
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Url 处理工具类
 */

object UriUtil {


    /**
     * 判断是否是Url
     */
    fun isUrl(url: String): Boolean {
        if (TextUtils.isEmpty(url)) return false
        return url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://")
    }


    /**
     * 获取查询参数名称
     */
    fun getQueryParameterNames(url: String): List<String> {
        if (TextUtils.isEmpty(url)) return emptyList()
        var uri = Uri.parse(url)

        if (uri.isOpaque()) {
            return emptyList()
        }
        var parameterNames = uri.queryParameterNames

        if (parameterNames == null) {
            return emptyList()
        } else {
            return parameterNames.toMutableList()
        }
    }

    /**
     * 获取Url的原来参数值
     */
    fun getQueryParameterValue(url: String, key: String): String? {

        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(key)) return url
        var uri = Uri.parse(url)
        if (uri.isOpaque()) {
            return url
        }
        //利用Map的唯一性拼接参数
        var parameterMap = getParameterMap(url)
        return parameterMap.get(key)

    }

    /**
     *  获取Url参数的值(Decode之后的)
     *
     *  getQueryParameter()  方法是默认Decode的
     *
     */
    fun getQueryParameterDecodeValue(url: String, key: String): String? {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(key)) return ""
        var uri = Uri.parse(url)
        if (uri.isOpaque()) {
            return ""
        } else {
            return Uri.parse(url).getQueryParameter(key)
        }
    }

    /**
     * url 添加参数
     */
    fun addQueryParameterValue(url: String, key: String, value: String): String {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(key)) return url
        var uri = Uri.parse(url)
        if (uri.isOpaque()) {
            return url
        }

        if (url.contains("#")) {
            url.replace("#", "%23")
        }
        if (!url.contains("?")) {
            url + "?"
        }
        var urlStart = url.split("?").get(0)
        //利用Map的唯一性拼接参数
        var parameterMap = getParameterMap(url)
        //利用map的唯一性 存储或者更新值
        parameterMap.put(key, value)
        //参数的map
        var appendUrl = appendMapParameter(parameterMap)
        var division = "?"
        return "$urlStart$division$appendUrl"
    }

    /**
     * 删除Encode 的参数
     */
    fun deleteQueryParameterDecodeValue(url: String, key: String): String {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(key)) return url
        var uri = Uri.parse(url)
        if (uri.isOpaque()) {
            return url
        }
        if (!url.contains("?")) {
            url + "?"
        }
        var urlStart = url.split("?").get(0)
        //利用Map的唯一性拼接参数
        var parameterMap = getParameterMap(url)
        //利用map的唯一性 存储或者更新值
        if (parameterMap.containsKey(key)){
            parameterMap.remove(key)
        }
        //参数的map
        var appendUrl = appendMapParameter(parameterMap)
        var division = "?"
        return "$urlStart$division$appendUrl"
    }

    /**
     * 田间Encode 的参数
     */
    fun addQueryParameterDecodeValue(url: String, key: String, value: String): String {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(key)) return url
        var uri = Uri.parse(url)
        if (uri.isOpaque()) {
            return url
        }
        if (!url.contains("?")) {
            url + "?"
        }
        var urlStart = url.split("?").get(0)
        //利用Map的唯一性拼接参数
        var parameterMap = getParameterMap(url)
        //利用map的唯一性 存储或者更新值
        parameterMap.put(key, Uri.encode(value))
        //参数的map
        var appendUrl = appendMapParameter(parameterMap)
        var division = "?"
        return "$urlStart$division$appendUrl"
    }


    /**
     * map 拼接成字符串
     */
    private fun appendMapParameter(parameterMap: HashMap<String, String>): String {
        var stringBuilder = StringBuilder()

        parameterMap.keys.forEach {
            stringBuilder.append(it).append("=").append(parameterMap.get(it)).append("&")
        }
        if (stringBuilder.length > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        }
        return stringBuilder.toString()
    }

    /**
     * 把url 的参数转为Map存储
     */
    private fun getParameterMap(
        url: String
    ): HashMap<String, String> {
        var map: HashMap<String, String> = HashMap<String, String>()
        // 参数名字的列表
        var parameter = Uri.parse(url).queryParameterNames
        parameter.forEach {
            if (getQueryParameterDecodeValue(url, it) != null)
                map.put(it, getQueryParameterDecodeValue(url, it)!!)
        }
        return map
    }

    var isEncode = false

    //获取重定向后的真实地址
    fun getRedirectUrl(path: String?): String? {
        var path = path

        if (TextUtils.isEmpty(path)) {
            return ""
        }
        if (UrlRedirectUrlUtil.findEnd(path)) {
            return path
        }
        try {
            if (path!!.contains("#")) {
                path = path!!.replace("#", URLEncoder.encode("#"))
                isEncode = true
            }
            val mOkHttpClient = OkHttpClient()
            val request = Request.Builder()
                .addHeader(
                    "Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"
                )
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Connection", "keep-alive")
                .addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Linux; Android 5.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36"
                )
                .url(path)
                .build()
            val mCall = mOkHttpClient.newCall(request)
            val response = mCall.execute()
            val backUrl = response.header("Location")
            if (response.code() != HttpURLConnection.HTTP_MOVED_TEMP && response.code() != HttpURLConnection.HTTP_MOVED_PERM) {
                var requestPath = response.request().url().toString()
                if (isEncode) {
                    requestPath = requestPath.replace("%23", URLDecoder.decode("%23"))
                    isEncode = false
                }
                return requestPath
            } else {
                return getRedirectUrl(backUrl)
            }

        } catch (e: Exception) {
            return path
        }

    }


}