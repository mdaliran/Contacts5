package com.momid.mainactivity.contacts;

public interface OnItemClick {

    public void onItemClick(String phoneNumber);

    public void onCallClick(String phoneNumber);

    public void onSmsClick(String phoneNumber);
}
