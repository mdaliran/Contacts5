package com.momid.mainactivity.di;

import android.content.Context;

import androidx.room.Room;

import com.momid.mainactivity.database.ContactsDao;
import com.momid.mainactivity.database.ContactsDatabase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ViewModelComponent.class)
public class DatabaseModule {

    public DatabaseModule() {

    }

    @Provides
    public ContactsDatabase provideContactsDatabase(@ApplicationContext Context context) {

        return Room.databaseBuilder(context, ContactsDatabase.class, "contacts_database15").build();
    }

    @Provides
    public ContactsDao provideContactsDao(ContactsDatabase contactsDatabase) {

        return contactsDatabase.contactsDao();
    }
}
