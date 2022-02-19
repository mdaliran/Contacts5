package com.momid.mainactivity.contacts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "CONTACTS")
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String contactId;
    @Ignore
    private String firstName;
    @Ignore
    private String lastName;
    @ColumnInfo(name = "full_name")
    private String fullName;
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    @Ignore
    private String address;
    @ColumnInfo(name = "image_uri")
    private String imageUri;
    @Ignore
    private String firstLetter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        this.firstLetter = String.valueOf(fullName.charAt(0));
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }
}
