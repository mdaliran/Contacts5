package com.momid.mainactivity.repository;

public interface DataCallback<T> {

    public void onFinish(T t);

    public void onFail(String error);
}
