package com.arckz.find_me.okhttp;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GJ on 2017/8/22.
 */

public class IRequestData {

    private Map<String,String> mMap = null;

    public IRequestData(){
    }

    public IRequestData(String key, String value){
        put(key, value);
    }

    public void put(String key, String value){
        if(mMap == null){
            mMap = new HashMap<>();
        }
        mMap.put(key,value);
    }

    public RequestBody build(){
        FormBody.Builder build = new FormBody.Builder();
        for (String key : mMap.keySet()) {
            build.add(key,mMap.get(key));
        }
        return build.build();
    }

}
