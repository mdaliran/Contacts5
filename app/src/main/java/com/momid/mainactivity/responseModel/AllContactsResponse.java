package com.momid.mainactivity.responseModel;

import com.momid.mainactivity.data_model.Contact;

import java.util.ArrayList;
import java.util.List;

public class AllContactsResponse {

    private List<Contact> contacts;
    private int totalPages;

    public AllContactsResponse() {
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
