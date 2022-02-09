package com.momid.mainactivity.recycler_adapter;

import android.content.res.ColorStateList;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.momid.mainactivity.ColorHelper;
import com.momid.mainactivity.ContactsGetter;
import com.momid.mainactivity.data_model.Contact;

public class ContactsBindingAdapter {

    @BindingAdapter("full_name")
    public static void setFullName(TextView textView, String fullName) {

        textView.setText(fullName);
    }

    @BindingAdapter("image_name")
    public static void setImageName(TextView textView, Contact contact) {

        if (contact.getImageUri().equals("null")) {
            textView.setText(String.valueOf(contact.getFullName().charAt(0)));
        }
    }

    @BindingAdapter("image_background")
    public static void setImageBackground(ImageView imageView, Contact contact) {

        imageView.setClipToOutline(true);

        if (!contact.getImageUri().equals("null")) {
            Glide.with(imageView.getContext()).load(ContactsGetter.StringToBitMap(contact.getImageUri())).into(imageView);
        }
        else {
            imageView.setBackgroundTintList(ColorStateList.valueOf(imageView.getContext().getColor(ColorHelper.getColor())));
        }
    }

    @BindingAdapter({"full_name", "previousContact", "position"})
    public static void setNameSeparatorText(TextView nameSeparator, String fullName, @Nullable Contact previousContact, int position) {

        if (position == 0) {
            nameSeparator.setText(String.valueOf(fullName.charAt(0)));
        }
        if (position > 0 && !(String.valueOf(fullName.charAt(0))).equals(previousContact.getFullName().charAt(0) + "")) {
            nameSeparator.setText(String.valueOf(fullName.charAt(0)));
        }
    }

    @BindingAdapter({"full_name", "previousContact", "position"})
    public static void setNameSeparatorLayout(LinearLayout nameSeparatorLayout, String fullName, @Nullable Contact previousContact, int position) {

        if (position == 0) {
            nameSeparatorLayout.setVisibility(View.VISIBLE);
        }
        if (position > 0 && !(String.valueOf(fullName.charAt(0))).equals(previousContact.getFullName().charAt(0) + "")) {
            nameSeparatorLayout.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter({"full_name", "nextContact", "itemCount", "position"})
    public static void setDividerVisibility(View divider, String fullName, @Nullable Contact nextContact, int itemCount, int position) {

        if (position < itemCount - 1 && !(String.valueOf(fullName.charAt(0)).equals(nextContact.getFullName().charAt(0) + ""))) {
            divider.setVisibility(View.INVISIBLE);
        }
    }
}
