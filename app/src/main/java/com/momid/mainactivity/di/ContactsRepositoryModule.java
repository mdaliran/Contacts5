package com.momid.mainactivity.di;

import com.momid.mainactivity.repository.ContactsRepository;
import com.momid.mainactivity.repository.ContactsRepositoryInterface;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public abstract class ContactsRepositoryModule {

    @Binds
    public abstract ContactsRepositoryInterface bindContactsRepository(ContactsRepository contactsRepository);
}
