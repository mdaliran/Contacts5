package com.momid.mainactivity.contacts;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Random;

@Entity(tableName = "CONTACTS")
public class Contact {

    @PrimaryKey
    @SerializedName("_id")
    @NonNull
    private String id;
    @ColumnInfo
    private String contactId;
    @Ignore
    @SerializedName("firstName")
    private String firstName;
    @Ignore
    @SerializedName("lastName")
    private String lastName;
    @ColumnInfo(name = "full_name")
    private String fullName;
    @ColumnInfo(name = "phone_number")
    private String phoneNumber;
    @Ignore
    private String address;
    @ColumnInfo(name = "image_uri")
    @SerializedName("avatar")
    private String imageUri;

    private String source = "REMOTE";

    public Contact() {

//        fullName = firstName + lastName;
    }

    public void setUpForRemote() {

        fullName = firstName + lastName;
        source = "REMOTE";
//        id = new Random().nextInt(100000) + 1000;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        fullName = firstName + lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
        return String.valueOf(fullName.charAt(0));
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
