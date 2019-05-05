package com.arckz.find_me.okhttp;

import android.app.Activity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by GJ on 2017/8/22.
 */

public abstract class ICallback implements Callback {

    public abstract void onResponse(final String s, final int code, final String msg);

    public Activity IAcitvity;

    public ICallback(Activity acitvity){
        IAcitvity = acitvity;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String str = response.body().string();
        IAcitvity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    onResponse(str,0,"");
                } catch (Exception e) {
                }
            }
        });
    }
}
