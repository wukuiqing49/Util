package com.wu.base.util;

import android.text.TextUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author wkq
 * @date 2022年07月22日 16:15
 * @des 重定向Url处理工具
 */

public class UrlRedirectUrlUtil {
    //是否Encode
    private static boolean isEncode = false;
    private static Disposable callDisposable;


    //获取重定向后的真实地址
    public static String getRedirectUrl(String path) {

        boolean isEncode = false;
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        if (findEnd(path)) {
            return path;
        }
        if (isShortUrl(path))
            return path;
        try {
            if (path.contains("#")) {
                path = path.replace("#", URLEncoder.encode("#"));
                isEncode = true;
            }
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 5.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36")
                    .url(path)
                    .build();
            Call mCall = mOkHttpClient.newCall(request);
            Response response = mCall.execute();
            String backUrl = response.header("Location");
            if (response.code() != HttpURLConnection.HTTP_MOVED_TEMP && response.code() != HttpURLConnection.HTTP_MOVED_PERM) {
                String requestPath = response.request().url().toString();
                if (isEncode) {
                    requestPath = requestPath.replace("%23", URLDecoder.decode("%23"));
                    isEncode = false;
                }
                return requestPath;
            } else {
                return getRedirectUrl(backUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return path;
        }
    }


    //处理重定向的Url
    public static void getRedirectUrl(String url, ResponseCallBack callBack) {
        if (callBack == null) return;
        //取消上一个请求
        cancelRequest();
        if (TextUtils.isEmpty(url)) {
            callBack.getRedirectFail();
            return;
        }
        if (findEnd(url)) {
            callBack.getRedirectSuccess(url);
            return;
        }
        if (isShortUrl(url)) {
            callBack.getRedirectSuccess(url);
            return;
        }


        callDisposable = Observable
                .create((ObservableOnSubscribe<String>) emitter -> {
                    if (url.contains("#")) {
                        isEncode = true;
                        emitter.onNext(url.replace("#", URLEncoder.encode("#")));
                    } else {
                        emitter.onNext(url);
                    }
                    emitter.onComplete();
                })
                .flatMap((Function<String, ObservableSource<String>>) s -> new Observable<String>() {
                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {
                        Request.Builder builder = new Request.Builder();
                        builder.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                        builder.header("Accept-Encoding", "gzip, deflate, br");
                        builder.header("Accept-Language", "zh-CN,zh;q=0.9");
                        builder.header("Connection", "keep-alive");
                        builder.header("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36");
                        Request request = builder.url(s).get().build();

                        OkHttpClient client = new OkHttpClient()
                                .newBuilder()
                                .followRedirects(false)
                                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                                .writeTimeout(10, TimeUnit.SECONDS)
                                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                                .build();
                        client.writeTimeoutMillis();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                observer.onError(new Throwable("解析失败"));
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String path = url;

                                if (response.code() == HttpURLConnection.HTTP_MOVED_TEMP || response.code() == HttpURLConnection.HTTP_MOVED_PERM) {
                                    String location = response.headers().get("Location");
                                    if (!TextUtils.isEmpty(location)) {
                                        path = location;
                                    }
                                    if (isEncode) {
                                        path = path.replace("%23", URLDecoder.decode("%23"));
                                        isEncode = false;
                                    }
                                }
                                observer.onNext(path);
                                observer.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        path -> callBack.getRedirectSuccess(path),
                        throwable -> callBack.getRedirectFail());

    }

    //    取消请求
    public static void cancelRequest() {
        if (callDisposable != null && !callDisposable.isDisposed()) {
            callDisposable.dispose();
        }
    }

    /**
     * 判断是否是是讯云短连接
     *
     * @param url
     * @return
     */
    public static boolean isShortUrl(String url) {

        return false;

    }

    /**
     * 判断外部链接.MP4结尾
     */
    public static boolean findEnd(String url) {
        if (url.endsWith(".mp4")) {
            return true;
        }
        return false;
    }


    public interface ResponseCallBack {
        void getRedirectSuccess(String url);

        void getRedirectFail();
    }

}
