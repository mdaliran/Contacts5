package com.momid.mainactivity.recycler_adapter;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.momid.mainactivity.ColorHelper;
import com.momid.mainactivity.data_model.Contact;

public class ContactsBindingAdapter {

    @BindingAdapter("full_name")
    public static void setFullName(TextView textView, String fullName) {

        String name = fullName;

        if (name == null) {
            name = "no name";
        }

        textView.setText(name);
    }

    @BindingAdapter("image_name")
    public static void setImageName(TextView textView, String fullName) {

        String name = fullName;

        if (name == null) {
            name = "no name";
        }

        textView.setText(String.valueOf(name.charAt(0)));
    }

    @BindingAdapter("image_background")
    public static void setImageBackground(ImageView imageView, String image) {

        imageView.setBackgroundTintList(ColorStateList.valueOf(imageView.getContext().getColor(ColorHelper.getColor())));
    }

    @BindingAdapter({"full_name", "previousContact", "position"})
    public static void setNameSeparatorText(TextView nameSeparator, String fullName, @Nullable Contact previousContact, int position) {

        String name = fullName;

        if (name == null) {
            name = "no name";
        }

        if (position == 0) {
            nameSeparator.setText(String.valueOf(name.charAt(0)));
        }
        if (position > 0 && !(String.valueOf(name.charAt(0))).equals(previousContact.getFullName().charAt(0) + "")) {
            nameSeparator.setText(String.valueOf(name.charAt(0)));
        }
    }

    @BindingAdapter({"full_name", "previousContact", "position"})
    public static void setNameSeparatorLayout(LinearLayout nameSeparatorLayout, String fullName, @Nullable Contact previousContact, int position) {

        String name = fullName;

        if (name == null) {
            name = "no name";
        }

        if (position == 0) {
            nameSeparatorLayout.setVisibility(View.VISIBLE);
        }
        if (position > 0 && !(String.valueOf(name.charAt(0))).equals(previousContact.getFullName().charAt(0) + "")) {
            nameSeparatorLayout.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter({"full_name", "nextContact", "itemCount", "position"})
    public static void setDividerVisibility(View divider, String fullName, @Nullable Contact nextContact, int itemCount, int position) {

        String name = fullName;

        if (name == null) {
            name = "no name";
        }

        if (position < itemCount - 1 && !(String.valueOf(name.charAt(0)).equals(nextContact.getFullName().charAt(0) + ""))) {
            divider.setVisibility(View.INVISIBLE);
        }
    }
}
