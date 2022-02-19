package com.momid.mainactivity.di;

import com.momid.mainactivity.contacts.ContactsRepository;
import com.momid.mainactivity.contacts.ContactsRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public abstract class ContactsRepositoryModule {

    @Binds
    public abstract ContactsRepository bindContactsRepository(ContactsRepositoryImpl contactsRepositoryImpl);
}
