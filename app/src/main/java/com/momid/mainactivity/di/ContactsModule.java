package com.momid.mainactivity.di;

import android.app.Application;
import android.content.Context;

import com.momid.mainactivity.ContactsReader;
import com.momid.mainactivity.ContactsReaderImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ViewModelComponent.class)
public abstract class ContactsModule {

    @Binds
    public abstract ContactsReader bindContactsReader(ContactsReaderImpl contactsReaderImpl);
}
