package com.momid.mainactivity.database;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.momid.mainactivity.contacts.Contact;

import java.util.List;

@Dao
public interface ContactsDao {

    public static final String LOCAL = "LOCAL";
    public static final String REMOTE = "REMOTE";

    @Query("SELECT * FROM Contacts ORDER BY full_name ASC")
    public PagingSource<Integer, Contact> getAllContacts();

    @Query("SELECT COUNT(*) FROM Contacts")
    public LiveData<Integer> getAllContactsCount();

    @Query("SELECT * FROM Contacts WHERE full_name LIKE '%' || :searchQuery || '%'")
    public PagingSource<Integer, Contact> searchContacts(String searchQuery);

    @Query("SELECT COUNT(*) FROM Contacts WHERE full_name LIKE '%' || :searchQuery || '%'")
    public LiveData<Integer> searchContactsCount(String searchQuery);

    @Insert
    public void addContact(Contact contact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addAllContacts(List<Contact> contacts);

    @Update
    public void updateContact(Contact contact);

    @Update
    public void updateAll(List<Contact> contacts);

    @Query("SELECT COUNT(*) FROM Contacts")
    public int getContactsCount();

    @Query("DELETE FROM Contacts")
    public void removeAll();

    @Query("DELETE FROM Contacts WHERE id NOT IN(:ids) AND Source = :source")
    public void removeIfNotInIds(String source, List<String> ids);
}
