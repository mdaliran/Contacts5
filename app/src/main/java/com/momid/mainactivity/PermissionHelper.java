package com.momid.mainactivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class PermissionHelper {

//    @Inject
    public Context context;

    public PermissionHelper(Context context) {

        this.context = context;
    }

    public boolean hasContactsPermission() {

        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
}
