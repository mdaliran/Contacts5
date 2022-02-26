package com.momid.mainactivity.contacts_reader;

import static com.momid.mainactivity.util.BitmapUtil.BitMapToString;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;

import com.momid.mainactivity.contacts.Contact;
import com.momid.mainactivity.contacts.ContactsRepository;
import com.momid.mainactivity.PermissionHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ContactsReaderImpl implements ContactsReader {

    private final Context context;
    private final ContactsRepository repository;
    private final PermissionHelper permissionHelper;

    @Inject
    public ContactsReaderImpl(@ApplicationContext Context context, ContactsRepository repository, PermissionHelper permissionHelper) {
        this.context = context;
        this.repository = repository;
        this.permissionHelper = permissionHelper;
    }

    @Override
    public void startToRead(ContactsReaderListener contactsReaderListener) {

        new Thread(() -> {

            int contactsCount = repository.getContactsCount1();

            if (contactsCount > 0) {
                runOnUiThread(contactsReaderListener::alreadyStored);
            }
            else {
                if (permissionHelper.hasContactsPermission()) {

                    runOnUiThread(contactsReaderListener::readStart);

                    repository.insertContactsToDatabase(startToGetContactsOnDevice());

                    runOnUiThread(contactsReaderListener::readEnd);
                }
                else {
                    runOnUiThread(contactsReaderListener::permissionNeeded);
                }
            }
        }).start();
    }

    @SuppressLint("Range")
    @Override
    public List<Contact> startToGetContactsOnDevice() {
        List<Contact> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
//                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                String photoString = "";

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (name == null) {
                    name = "no name";
                }
                String lastnumber = "0";

                Bitmap photo = null;
                if (inputStream != null) {
                    photo = BitmapFactory.decodeStream(inputStream);
                    photoString = BitMapToString(photo);
                }
                else {
                    photoString = "";
                }

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]
                            {id}, null);
                    while (pCur.moveToNext()) {
                        String number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        if (number.equals(lastnumber)) {

                        } else {
                            lastnumber = number;
                        }
                    }
                    pCur.close();
                }

//                            int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
//                            if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
//                                    || type == ContactsContract.CommonDataKinds.Phone.TYPE_HOME
//                                    || type == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
                Contact info = new Contact();
                info.setContactId(id);
                info.setFullName(name);
                info.setPhoneNumber(lastnumber);
                info.setImageUri(photoString);
//                        info.photo = photo;
//                        info.photoURI= pURI;
                list.add(info);
//                            }
            }
        }

        return list;
    }

    private void runOnUiThread(Runnable runnable) {

        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
