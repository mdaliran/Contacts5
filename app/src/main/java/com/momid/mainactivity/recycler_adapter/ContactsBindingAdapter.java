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

    @BindingAdapter("fullName")
    public static void setFullName(TextView textView, String fullName) {

        textView.setText(fullName);
    }

    @BindingAdapter("imageName")
    public static void setImageName(TextView textView, Contact contact) {

        if (contact.getImageUri().equals("")) {
            textView.setText(String.valueOf(contact.getFullName().charAt(0)));
        }
    }

    @BindingAdapter("imageBackground")
    public static void setImageBackground(ImageView imageView, Contact contact) {

        imageView.setClipToOutline(true);

        if (!contact.getImageUri().equals("")) {
            Glide.with(imageView.getContext()).load(ContactsGetter.StringToBitMap(contact.getImageUri())).into(imageView);
        }
        else {
            imageView.setBackgroundTintList(ColorStateList.valueOf(imageView.getContext().getColor(ColorHelper.getColor())));
        }
    }

    @BindingAdapter({"fullNameFirstLetter", "previousContactFirstLetter", "position"})
    public static void setNameSeparatorText(TextView nameSeparator, String fullNameFirstLetter, @Nullable String previousContactFirstLetter, int position) {

        if (position == 0) {
            nameSeparator.setText(String.valueOf(fullNameFirstLetter));
        }
        if (position > 0 && !(String.valueOf(fullNameFirstLetter)).equals(previousContactFirstLetter)) {
            nameSeparator.setText(String.valueOf(fullNameFirstLetter));
        }
    }

    @BindingAdapter({"fullNameFirstLetter", "previousContactFirstLetter", "position"})
    public static void setNameSeparatorLayout(LinearLayout nameSeparatorLayout, String fullNameFirstLetter, @Nullable String previousContactFirstLetter, int position) {

        if (position == 0) {
            nameSeparatorLayout.setVisibility(View.VISIBLE);
        }
        if (position > 0 && !(String.valueOf(fullNameFirstLetter)).equals(previousContactFirstLetter)) {
            nameSeparatorLayout.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter({"fullNameFirstLetter", "nextContactFirstLetter", "itemCount", "position"})
    public static void setDividerVisibility(View divider, String fullNameFirstLetter, @Nullable String nextContactFirstLetter, int itemCount, int position) {

        if (position < itemCount - 1 && !(String.valueOf(fullNameFirstLetter).equals(nextContactFirstLetter))) {
            divider.setVisibility(View.INVISIBLE);
        }
    }
}
