package com.momid.mainactivity.database;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.momid.mainactivity.contacts_activity.Contact;

import java.util.List;

@Dao
public interface ContactsDao {

    @Query("SELECT * FROM Contacts ORDER BY full_name ASC")
    public DataSource.Factory<Integer, Contact> getAllContacts();

    @Query("SELECT COUNT(*) FROM Contacts")
    public LiveData<Integer> getAllContactsCount();

    @Query("SELECT * FROM Contacts WHERE full_name LIKE '%' || :searchQuery || '%'")
    public DataSource.Factory<Integer, Contact> searchContacts(String searchQuery);

    @Query("SELECT COUNT(*) FROM Contacts WHERE full_name LIKE '%' || :searchQuery || '%'")
    public LiveData<Integer> searchContactsCount(String searchQuery);

    @Insert
    public void addContact(Contact contact);

    @Insert
    public void addAllContacts(List<Contact> contacts);

    @Update
    public void updateContact(Contact contact);

    @Update
    public void updateAll(List<Contact> contacts);
}
