package com.momid.mainactivity.di;

import android.app.Application;
import android.content.Context;

import com.momid.mainactivity.ContactsGetter;
import com.momid.mainactivity.data_model.Contact;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ViewModelComponent.class)
public class ContactsModule {

    @Provides
    public ContactsGetter provideContactsGetter(@ApplicationContext Context context) {

        return new ContactsGetter((Application) context);
    }
}
