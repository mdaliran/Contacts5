package com.momid.mainactivity.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.momid.mainactivity.data_model.Contact;

import java.util.List;

@Dao
public interface ContactsDao {

    @Query("SELECT * FROM Contacts ORDER BY full_name ASC LIMIT (:page - 1) * :pageSize, :pageSize")
    public List<Contact> getAllContacts(int page, int pageSize);

    @Query("SELECT COUNT(*) FROM Contacts")
    public Integer getAllContactsCount();

    @Query("SELECT * FROM Contacts WHERE full_name LIKE '%' || :searchQuery || '%' LIMIT (:page - 1) * :pageSize, :pageSize")
    public List<Contact> searchContacts(String searchQuery, int page, int pageSize);

    @Query("SELECT COUNT(*) FROM Contacts WHERE full_name LIKE '%' || :searchQuery || '%'")
    public Integer searchContactsCount(String searchQuery);

    @Insert
    public void addContact(Contact contact);

    @Insert
    public void addAllContacts(List<Contact> contacts);

    @Update
    public void updateContact(Contact contact);

    @Update
    public void updateAll(List<Contact> contacts);
}
