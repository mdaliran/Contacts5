package com.momid.mainactivity.network;

import com.momid.mainactivity.contacts.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkService {

    @GET("Contact")
    public Call<List<Contact>> getContacts();
}
