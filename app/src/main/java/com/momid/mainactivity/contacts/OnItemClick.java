package com.momid.mainactivity.contacts;

public interface OnItemClick {

    public void onItemClick(Contact contact);

    public void onCallClick(Contact contact);

    public void onSmsClick(Contact contact);
}
