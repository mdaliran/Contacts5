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
}
