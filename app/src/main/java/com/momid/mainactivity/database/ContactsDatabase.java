package com.momid.mainactivity.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.momid.mainactivity.contacts.Contact;

@Database(entities = {Contact.class}, version = 3)
public abstract class ContactsDatabase extends RoomDatabase {

    public abstract ContactsDao contactsDao();
}
