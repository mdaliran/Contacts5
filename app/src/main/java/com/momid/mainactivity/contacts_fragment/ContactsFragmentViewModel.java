package com.momid.mainactivity.contacts_fragment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.PagingSource;

import com.momid.mainactivity.R;
import com.momid.mainactivity.contacts.AllContactsRequest;
import com.momid.mainactivity.contacts.Contact;
import com.momid.mainactivity.contacts.ContactsRepository;
import com.momid.mainactivity.util.CallUtil;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class ContactsFragmentViewModel extends ViewModel {

    private MutableLiveData<AllContactsRequest> allContactsRequest = new MutableLiveData<>();
    private LiveData<PagingData<Contact>> contactsListLivedata;

    public MutableLiveData<String> errorMessageLiveDate = new MutableLiveData<>();

    private PagingSource<Integer, Contact> pagingSource;

    public ContactsRepository repository;
    @Inject
    public CallUtil callUtil;

    @Inject
    public ContactsFragmentViewModel(ContactsRepository repository) {

        this.repository = repository;

        getAllContacts();
    }

    public LiveData<PagingData<Contact>> getContactsListLivedata() {
        return contactsListLivedata;
    }

    public void getAllContacts() {

        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Contact> pager = new Pager<>(
                new PagingConfig(/* pageSize = */ 25), () -> {
            pagingSource = repository.getAllContacts(allContactsRequest.getValue());
            return pagingSource;
        });

        contactsListLivedata = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    public void refresh() {

        pagingSource.invalidate();
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
