package com.momid.mainactivity.response_model;

import com.momid.mainactivity.data_model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsListResponse {

    private List<Contact> contacts;
    private int totalPages;

    public ContactsListResponse() {
        contacts = new ArrayList<>();
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
