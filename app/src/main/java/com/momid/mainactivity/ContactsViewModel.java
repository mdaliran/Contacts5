package com.momid.mainactivity;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.repository.ContactsRepository;
import com.momid.mainactivity.repository.DataCallback;
import com.momid.mainactivity.request_model.AllContactsRequest;
import com.momid.mainactivity.request_model.SearchContactsRequest;
import com.momid.mainactivity.responseModel.AllContactsResponse;
import com.momid.mainactivity.responseModel.SearchContactsResponse;

public class ContactsViewModel extends AndroidViewModel {

    private AllContactsRequest allContactsRequest;
    private SearchContactsRequest searchContactsRequest;
    private MutableLiveData<AllContactsResponse> allContactsLivedata;
    private MutableLiveData<SearchContactsResponse> searchContactsLivedata;
    private ContactsRepository repository;
    public ContactsActivityState state = new ContactsActivityState();

    public ContactsViewModel(Application application) {
        super(application);
    }

    public void init() {
        if (allContactsLivedata == null) {
            allContactsRequest = new AllContactsRequest();
            searchContactsRequest = new SearchContactsRequest();
            repository = new ContactsRepository(getApplication());
            allContactsLivedata = new MutableLiveData<>();
            searchContactsLivedata = new MutableLiveData<>();
            allContactsLivedata.setValue(new AllContactsResponse());
            searchContactsLivedata.setValue(new SearchContactsResponse());

            repository.areContactsStoredInDatabase(new DataCallback<Boolean>() {
                @Override
                public void onFinish(Boolean areStored) {
                    if (areStored) {
                        getAllContacts();
                    }
                    else {
                        if (hasContactPermission()) {
                            state.loading.postValue(true);
                            repository.insertContactsToDatabase(ContactsGetter.getAllContactsOnThisDevice(getApplication()), new DataCallback<Boolean>() {
                                @Override
                                public void onFinish(Boolean aBoolean) {
                                    getAllContacts();
                                }

                                @Override
                                public void onFail(String error) {

                                }
                            });
                        }
                        else {
                            state.contactsPermissionNeeded.postValue(true);
                        }
                    }
                }

                @Override
                public void onFail(String error) {

                }
            });
        }
    }

    public LiveData<AllContactsResponse> getAllContactsLivedata() {
        return allContactsLivedata;
    }

    public LiveData<SearchContactsResponse> getSearchContactsLivedata() {
        return searchContactsLivedata;
    }

    public void getAllContacts() {

        state.loading.postValue(true);
        repository.getAllContacts(allContactsRequest, new DataCallback<AllContactsResponse>() {
            @Override
            public void onFinish(AllContactsResponse allContactsResponse) {
                allContactsLivedata.postValue(allContactsResponse);
                state.loading.postValue(false);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    public void searchContacts(SearchContactsRequest searchContactsRequest) {

        this.searchContactsRequest = searchContactsRequest;
        repository.searchContacts(searchContactsRequest, new DataCallback<SearchContactsResponse>() {
            @Override
            public void onFinish(SearchContactsResponse searchContactsResponse) {
                searchContactsLivedata.postValue(searchContactsResponse);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    public boolean allContactsNextPage() {

        if (allContactsRequest.getPage() < allContactsLivedata.getValue().getTotalPages()) {
            allContactsRequest.setPage(allContactsRequest.getPage() + 1);
            repository.getAllContacts(allContactsRequest, new DataCallback<AllContactsResponse>() {
                @Override
                public void onFinish(AllContactsResponse allContactsResponse) {
                    allContactsLivedata.getValue().getContacts().addAll(allContactsResponse.getContacts());
                    allContactsLivedata.postValue(allContactsLivedata.getValue());
                }

                @Override
                public void onFail(String error) {

                }
            });
            return true;
        }

        return false;
    }

    public boolean searchContactsNextPage() {

        if (searchContactsRequest.getPage() < searchContactsLivedata.getValue().getTotalPages()) {
            searchContactsRequest.setPage(searchContactsRequest.getPage() + 1);
            repository.searchContacts(searchContactsRequest, new DataCallback<SearchContactsResponse>() {
                @Override
                public void onFinish(SearchContactsResponse searchContactsResponse) {
                    searchContactsLivedata.getValue().getContacts().addAll(searchContactsResponse.getContacts());
                    searchContactsLivedata.postValue(searchContactsLivedata.getValue());
                }

                @Override
                public void onFail(String error) {

                }
            });
            return true;
        }

        return false;
    }

    public void onSearchViewTextChange(String searchQuery) {

        if (!searchQuery.equals("")) {
            state.searchMode.setValue(true);
            SearchContactsRequest request = new SearchContactsRequest();
            request.setSearchQuery(searchQuery);
            searchContacts(request);
        }
    }

    public void onCall(Contact contact) {

        if (contact.getPhoneNumber() != null && !contact.getPhoneNumber().equals("0")) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
            getApplication().startActivity(callIntent);
        }
        else {
            Toast.makeText(getApplication(), "شماره موجود نیست", Toast.LENGTH_LONG).show();
        }
    }

    public void onMessage(Contact contact) {

        if (contact.getPhoneNumber() != null && !contact.getPhoneNumber().equals("0")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + contact.getPhoneNumber())); // This ensures only SMS apps respond
            intent.putExtra("sms_body", "hello, it's me.");
            if (intent.resolveActivity(getApplication().getPackageManager()) != null) {
                getApplication().startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplication(), "شماره موجود نیست", Toast.LENGTH_LONG).show();
        }
    }

    public boolean hasContactPermission() {

        return ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public void onPermissionGrant() {

        if (hasContactPermission()) {
            state.loading.postValue(true);
            repository.insertContactsToDatabase(ContactsGetter.getAllContactsOnThisDevice(getApplication()), new DataCallback<Boolean>() {
                @Override
                public void onFinish(Boolean aBoolean) {
                    getAllContacts();
                }

                @Override
                public void onFail(String error) {

                }
            });
        }
    }
}
