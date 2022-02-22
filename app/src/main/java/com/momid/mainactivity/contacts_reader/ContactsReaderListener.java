package com.momid.mainactivity.contacts_reader;

public interface ContactsReaderListener {

    public void alreadyStored();

    public void readStart();

    public void readEnd();

    public void permissionNeeded();
}
