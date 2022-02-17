package com.momid.mainactivity.contacts_activity;

import com.momid.mainactivity.contacts_activity.Contact;

public interface OnItemClick {

    public void onItemClick(Contact contact);

    public void onCallClick(Contact contact);

    public void onSmsClick(Contact contact);
}
