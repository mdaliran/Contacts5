package com.momid.mainactivity.contacts.view;

import com.momid.mainactivity.contacts.Contact;

public interface OnItemClick {

    public void onItemClick(Contact contact);

    public void onCallClick(Contact contact);

    public void onSmsClick(Contact contact);
}
