package com.momid.mainactivity.di;

import com.momid.mainactivity.contacts_activity.ContactsReader;
import com.momid.mainactivity.contacts_activity.ContactsReaderImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public abstract class ContactsModule {

    @Binds
    public abstract ContactsReader bindContactsReader(ContactsReaderImpl contactsReaderImpl);
}
