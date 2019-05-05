package com.arckz.find_me.okhttp;


import okhttp3.Call;


public class OKHttpRequestHandle implements RequestHandle {

    private final Call call;

    public OKHttpRequestHandle(Call call) {
        super();
        this.call = call;
    }

    @Override
    public void cancle() {
        call.cancel();
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}