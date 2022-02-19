package com.momid.mainactivity.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.PagedList;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.Contact;
import com.momid.mainactivity.contacts.ContactsRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class SearchContactsViewModel extends ViewModel {

    private MutableLiveData<SearchContactsRequest> searchContactsRequest;
    private LiveData<PagingData<Contact>> searchContactsListLivedata;

    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();

    public MutableLiveData<String> errorMessageLiveDate = new MutableLiveData<>();

    public ContactsRepository contactsRepository;

    @Inject
    public SearchContactsViewModel(ContactsRepository contactsRepository) {

        if (searchContactsListLivedata == null) {

            this.contactsRepository = contactsRepository;

            searchContactsRequest = new MutableLiveData<>();

            searchContactsListLivedata = Transformations.switchMap(searchContactsRequest, new Function<SearchContactsRequest, LiveData<PagingData<Contact>>>() {
                @Override
                public LiveData<PagingData<Contact>> apply(SearchContactsRequest input) {
//                    return new LivePagedListBuilder<>(contactsRepository.searchContacts(searchContactsRequest.getValue()), 25).build();
//                    return null;

                    CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(SearchContactsViewModel.this);
                    Pager<Integer, Contact> pager = new Pager<Integer, Contact>(
                            new PagingConfig(/* pageSize = */ 25), new Function0() {
                        @Override
                        public PagingSource<Integer, Contact> invoke() {
                            return contactsRepository.searchContacts(searchContactsRequest.getValue());
                        }
                    });

                    LiveData<PagingData<Contact>> pagingLiveData = PagingLiveData.getLiveData(pager);
                    PagingLiveData.cachedIn(pagingLiveData, viewModelScope);
                    return pagingLiveData;
                }
            });
        }
    }

    public LiveData<PagingData<Contact>> getSearchContactsListLivedata() {
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
            errorMessageLiveDate.postValue(context.getString(R.string.phone_number_doesnt_exist));
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
