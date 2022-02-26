package com.momid.mainactivity.search;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.Contact;
import com.momid.mainactivity.contacts.ContactsRepository;
import com.momid.mainactivity.util.CallUtil;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class SearchContactsViewModel extends ViewModel {

    private MutableLiveData<SearchContactsRequest> searchContactsRequest = new MutableLiveData<>();
    private LiveData<PagingData<Contact>> searchContactsListLivedata;

    private PagingSource pagingSource;

    public MutableLiveData<Boolean> searchMode = new MutableLiveData<>();

    public MutableLiveData<String> errorMessageLiveDate = new MutableLiveData<>();

    public ContactsRepository contactsRepository;
    @Inject
    public CallUtil callUtil;

    @Inject
    public SearchContactsViewModel(ContactsRepository contactsRepository) {

            this.contactsRepository = contactsRepository;

            searchContactsListLivedata = Transformations.switchMap(searchContactsRequest, input -> getSearchPagingData());
    }

    public LiveData<PagingData<Contact>> getSearchContactsListLivedata() {
        return searchContactsListLivedata;
    }

    private LiveData<PagingData<Contact>> getSearchPagingData() {

        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(SearchContactsViewModel.this);
        Pager<Integer, Contact> pager = new Pager<Integer, Contact>(
                new PagingConfig(/* pageSize = */ 25), () -> {
                    pagingSource = contactsRepository.searchContacts(searchContactsRequest.getValue());
                    return pagingSource;
                });

        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    public void searchContacts(SearchContactsRequest request) {

        this.searchContactsRequest.setValue(request);
    }

    public void refresh() {

        pagingSource.invalidate();
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
            callUtil.call(contactPhoneNumber);
        }
        else {
            errorMessageLiveDate.postValue(context.getString(R.string.phone_number_doesnt_exist));
        }
    }

    public void onMessage(Context context, String contactPhoneNumber) {

        if (contactPhoneNumber != null && !contactPhoneNumber.equals("0")) {
            callUtil.message("hi", contactPhoneNumber);
        }
        else {
            errorMessageLiveDate.postValue(context.getString(R.string.phone_number_doesnt_exist));
        }
    }

}
