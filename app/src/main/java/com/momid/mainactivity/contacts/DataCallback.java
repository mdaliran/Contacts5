package com.momid.mainactivity.contacts;

public interface DataCallback<T> {

    public void onFinish(T t);

    public void onFail(String error);
}
