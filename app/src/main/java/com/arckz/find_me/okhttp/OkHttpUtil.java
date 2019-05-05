package com.arckz.find_me.okhttp;

import android.app.Application;
import android.content.Context;
import com.arckz.find_me.base.BaseApplication;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OkHttpUtil {
    private static OkHttpClient mOkHttpClient = null;

    public static void initOKGO(Application appCtx){

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cookieJar(new CookieJarImpl(new DBCookieStore(BaseApplication.Companion.getINSTANCE().getApplicationContext())))
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                ;

//        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
//        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//        params.put("commonParamsKey2", "这里支持中文参数");

        OkGo.getInstance().init(appCtx)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);

        mOkHttpClient = builder.build();
    }

    public static OkHttpClient getOkHttpClient(){
        return  mOkHttpClient;
    }

    /**
     * 开启异步线程POST访问网络
     * @param requestData
     * @param url
     * @param responseCallback
     */
    public static void newCallPost(IRequestData requestData, String url, ICallback responseCallback){
        Request request = new Request.Builder()
                .url(url)
                .post(requestData.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程GET访问网络
     * @param requestData
     * @param url
     * @param responseCallback
     */
    public static void newCallGet(IRequestData requestData, String url, ICallback responseCallback){
        Request request = new Request.Builder()
                .url(url)
                .post(requestData.build())
                .method("GET", null)
                .build();
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

//    public static PostRequest post(String url){
//        PostRequest request = OkGo.post(url);
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        if(user != null){
//            request.headers("Authorization",user.getToken());
//        }
//        return request;
//    }
//
//    /**
//     * 开启异步线程GET访问网络
//     * @param requestData
//     * @param url
//     * @param responseCallback
//     */
//    public static void patch(String url, IRequestData requestData, ICallback responseCallback){
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        Request request = new Request.Builder()
//                .url(url)
//                .patch(requestData.build())
//                .header("Authorization" , user.getToken())
//                .build();
//        mOkHttpClient.newCall(request).enqueue(responseCallback);
//    }
//
//    /**
//     * 开启异步线程GET访问网络
//     * @param requestData
//     * @param url
//     * @param responseCallback
//     */
//    public static void put(String url, IRequestData requestData, ICallback responseCallback){
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        Request request = new Request.Builder()
//                .url(url)
//                .put(requestData.build())
//                .header("Authorization" , user.getToken())
//                .build();
//        mOkHttpClient.newCall(request).enqueue(responseCallback);
//    }
//
//    /**
//     * 开启异步线程GET访问网络
//     * @param url
//     * @param responseCallback
//     */
//    public static void put(String url, RequestBody body, ICallback responseCallback){
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        Request request = new Request.Builder()
//                .url(url)
//                .put(body)
//                .header("Authorization" , user.getToken())
//                .build();
//        mOkHttpClient.newCall(request).enqueue(responseCallback);
//    }
//
//    /**
//     * 开启异步线程GET访问网络
//     * @param url
//     * @param responseCallback
//     */
//    public static void delete(String url, ICallback responseCallback){
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        Request request = new Request.Builder()
//                .url(url)
//                .delete()
//                .header("Authorization" , user.getToken())
//                .build();
//        mOkHttpClient.newCall(request).enqueue(responseCallback);
//    }
//
//    public static GetRequest get(String url){
//        GetRequest request = OkGo.get(url);
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        if(user != null){
//            request.headers("Authorization",user.getToken());
//        }
//        return request;
//    }
//
//
//    public static Request getRequest(String url){
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        if(user != null){
//            return new Request.Builder()
//                    .url(url)
//                    .addHeader("Authorization",user.getToken())
//                    .build();
//        }
//        return new Request.Builder()
//                .url(url)
//                .build();
//    }
//
//    public static Request getRequest(String url, RequestBody formBody){
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        if(user != null){
//            return new Request.Builder()
//                    .url(url)
//                    .post(formBody)
//                    .addHeader("Authorization",user.getToken())
//                    .build();
//        }
//        return new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();
//    }
//
//    public static Request getRequest(String url, Map<String,Object> mapData){
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        JSONObject data = CommonUtil.getJSONObject(mapData);
//        RequestBody requestBody = RequestBody.create(JSON, data.toString());
//
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        if(user != null){
//            return new Request.Builder()
//                    .url(url)
//                    .post(requestBody)
//                    .addHeader("Authorization",user.getToken())
//                    .build();
//        }
//        return new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//    }
//
//    public static Request getFileRequest(String url, File file){
//        UserInfo user = MyApplication.getApplication().getUserInfo();
//        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
//        builder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.jpg\";Authorization=\""+user.getToken()+"\";"),
//                RequestBody.create(MediaType.parse("image/png"),file)
//            ).build();
//        RequestBody body = builder.build();
//        return  new Request.Builder().url(url).post(body).build();
//
//    }
}
