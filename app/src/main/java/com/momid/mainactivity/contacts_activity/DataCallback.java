package com.momid.mainactivity.contacts_activity;

public interface DataCallback<T> {

    public void onFinish(T t);

    public void onFail(String error);
}
