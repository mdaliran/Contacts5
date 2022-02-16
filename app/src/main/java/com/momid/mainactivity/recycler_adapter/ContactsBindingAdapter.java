package com.momid.mainactivity.recycler_adapter;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.momid.mainactivity.ColorHelper;
import com.momid.mainactivity.ContactsGetter;
import com.momid.mainactivity.data_model.Contact;

public class ContactsBindingAdapter {

    @BindingAdapter("imageBackground")
    public static void setImageBackground(ImageView imageView, String imageUri) {

        imageView.setClipToOutline(true);

//        if (TextUtils.isEmpty(imageUri)) {
//            Glide.with(imageView).load(new ColorDrawable(imageView.getContext().getColor(ColorHelper.getColor()))).into(imageView);
            Glide.with(imageView).load(ContactsGetter.StringToBitMap(imageUri)).apply(RequestOptions.placeholderOf(new ColorDrawable(imageView.getContext().getColor(ColorHelper.getColor())))).into(imageView);
//        }
//        else {
//            Glide.with(imageView.getContext()).load(ContactsGetter.StringToBitMap(imageUri)).into(imageView);
//        }
    }
}

