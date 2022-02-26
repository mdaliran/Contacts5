package com.momid.mainactivity.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class CallUtil {

    private Context context;

    @Inject
    public CallUtil(@ApplicationContext Context context) {

        this.context = context;
    }

    public void call(String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(callIntent);
    }

    public void message(String text, String phoneNumber) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber)); // This ensures only SMS apps respond
        intent.putExtra("sms_body", text);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
