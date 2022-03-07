package com.momid.mainactivity.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactsClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://79.175.174.26:32418/")
                .addConverterFactory(GsonConverterFactory.create(gson))
//                .client(client)
                .build();

        return retrofit;
    }

}
