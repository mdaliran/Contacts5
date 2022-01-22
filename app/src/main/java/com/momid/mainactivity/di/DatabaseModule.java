package com.momid.mainactivity.di;

import android.content.Context;

import androidx.room.Room;

import com.momid.mainactivity.database.ContactsDao;
import com.momid.mainactivity.database.ContactsDatabase;
import com.momid.mainactivity.repository.ContactsRepository;
import com.momid.mainactivity.repository.ContactsRepositoryInterface;

import javax.inject.Inject;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

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
