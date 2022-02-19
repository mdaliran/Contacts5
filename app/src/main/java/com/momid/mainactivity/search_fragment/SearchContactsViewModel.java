package com.momid.mainactivity.search_fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.momid.mainactivity.contacts_activity.Contact;
import com.momid.mainactivity.contacts_activity.ContactsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SearchContactsViewModel extends ViewModel {

    private MutableLiveData<SearchContactsRequest> searchContactsRequest;
    private LiveData<PagedList<Contact>> searchContactsListLivedata;

    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();

    public MutableLiveData<String> errorMessageLiveDate = new MutableLiveData<>();

    public ContactsRepository contactsRepository;

    @Inject
    public SearchContactsViewModel(ContactsRepository contactsRepository) {

        if (searchContactsListLivedata == null) {

            this.contactsRepository = contactsRepository;

            searchContactsRequest = new MutableLiveData<>();

            searchContactsListLivedata = Transformations.switchMap(searchContactsRequest, new Function<SearchContactsRequest, LiveData<PagedList<Contact>>>() {
                @Override
                public LiveData<PagedList<Contact>> apply(SearchContactsRequest input) {
//                    return new LivePagedListBuilder<>(contactsRepository.searchContacts(searchContactsRequest.getValue()), 25).build();
                    return null;
                }
            });
        }
    }

    public LiveData<PagedList<Contact>> getSearchContactsListLivedata() {
        return searchContactsListLivedata;
    }

    public void searchContacts(SearchContactsRequest request) {

        this.searchContactsRequest.postValue(request);
    }

    public void onSearchViewTextChange(String searchQuery) {

        if (!searchQuery.equals("")) {
            SearchContactsRequest request = new SearchContactsRequest();
            request.setSearchQuery(searchQuery);
            searchContacts(request);
        }
    }

    public void onCall(Context context, String contactPhoneNumber) {

        if (contactPhoneNumber != null && !contactPhoneNumber.equals("0")) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse("tel:" + contactPhoneNumber));
            context.startActivity(callIntent);
        }
        else {
            errorMessageLiveDate.postValue("phone number doesn't exist");
        }
    }

    public void onMessage(Context context, String contactPhoneNumber) {

        if (contactPhoneNumber != null && !contactPhoneNumber.equals("0")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + contactPhoneNumber)); // This ensures only SMS apps respond
            intent.putExtra("sms_body", "hello, it's me.");
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            }
        }
        else {
            errorMessageLiveDate.postValue("phone number doesn't exist");
        }
    }

}
