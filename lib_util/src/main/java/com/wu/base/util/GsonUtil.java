//package com.wu.base.util;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonSyntaxException;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @ describe:
// * @ author: Martin
// * @ createTime: 2018/12/13 14:10
// * @ version: 1.0
// */
//public class GsonUtil {
//
//    /** 请求结果集 */
//    public static final String RESULTS = "results";
//    /** 状态码 */
//    public static final String STATUS = "status";
//    /** 状态描述 */
//    public static final String MESSAGE = "message";
//
//    /**
//     * 获取状态描述
//     * @param json json串
//     * @return 状态描述
//     */
//    public static String getMessage(JsonObject json) {
//        try {
//            return json.get(MESSAGE).getAsString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "未知错误";
//        }
//    }
//
//    /**
//     * 获取状态描述
//     * @param json json串
//     * @return 状态描述
//     */
//    public static String getMessage(String json) {
//        try {
//            return new JsonParser().parse(json).getAsJsonObject().get(MESSAGE).getAsString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "未知错误";
//        }
//    }
//
//    /**
//     * 获取状态码
//     * @param json json串
//     * @return 获取状态码
//     */
//    public static int getStatus(JsonObject json) {
//        try {
//            return json.get(STATUS).getAsInt();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -2;
//        }
//    }
//
//    /**
//     * 获取状态码
//     * @param json json串
//     * @return 获取状态码
//     */
//    public static int getStatus(String json) {
//        try {
//            return new JsonParser().parse(json).getAsJsonObject().get(STATUS).getAsInt();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -2;
//        }
//    }
//
//
//    /**
//     *  获取结果集对象
//     * @param json 结果数据集合
//     * @param cls 类型
//     */
//    public static <T> T getResults(JsonObject json, Class<T> cls) {
//        try {
//            return new Gson().fromJson(json, cls);
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     *  获取结果集字符串
//     * @param json 结果数据集合
//     */
//    public static String getResultString(JsonObject json) {
//        try {
//            return json.get(RESULTS).getAsString();
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     *  获取结果集数组
//     * @param json 结果数据集合
//     * @param cls 类型
//     */
//    public static <T> List<T> getResultsArray(JsonObject json, Class<T> cls) {
//        try {
//            Gson gson = new Gson();
//            List<T> list = new ArrayList<T>();
//            JsonArray array = json.getAsJsonArray(RESULTS).getAsJsonArray();
//            for(final JsonElement elem : array){
//                list.add(gson.fromJson(elem, cls));
//            }
//            return list;
//        } catch (JsonSyntaxException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 获取状态描述
//     * @param json json串
//     * @return 状态描述
//     */
//    public static String getResults(JsonObject json, String key) {
//        try {
//            return json.getAsJsonArray(RESULTS).getAsJsonObject().get(key).getAsString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "未知错误";
//        }
//    }
//}
