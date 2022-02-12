package com.momid.mainactivity.recycler_adapter;

import com.momid.mainactivity.data_model.Contact;

public interface OnItemClick {

    public void onItemClick(Contact contact);

    public void onCallClick(Contact contact);

    public void onSmsClick(Contact contact);
}
