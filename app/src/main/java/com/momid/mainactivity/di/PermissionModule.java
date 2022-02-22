package com.momid.mainactivity.di;

import android.content.Context;

import com.momid.mainactivity.PermissionHelper;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ViewModelComponent.class)
public class PermissionModule {

    @Provides
    public PermissionHelper providePermissionHelper(@ApplicationContext Context context) {

        return new PermissionHelper(context);
    }
}
