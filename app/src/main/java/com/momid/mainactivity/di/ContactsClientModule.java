package com.momid.mainactivity.di;

import com.momid.mainactivity.network.ContactsClient;
import com.momid.mainactivity.network.ContactsNetworkService;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(ViewModelComponent.class)
public class ContactsClientModule {

    @Provides
    public ContactsNetworkService provideContactsNetworkService() {

        Retrofit retrofit = ContactsClient.getClient();
        ContactsNetworkService contactsNetworkService = retrofit.create(ContactsNetworkService.class);
        return contactsNetworkService;
    }
}
