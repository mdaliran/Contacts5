package com.momid.mainactivity.contacts.view;

import static com.momid.mainactivity.util.BitmapUtil.StringToBitMap;

import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.momid.mainactivity.util.ColorUtil;

public class ContactsBindingAdapter {

    @BindingAdapter("imageBackground")
    public static void setImageBackground(ImageView imageView, String imageUri) {

        imageView.setClipToOutline(true);

//        if (TextUtils.isEmpty(imageUri)) {
//            Glide.with(imageView).load(new ColorDrawable(imageView.getContext().getColor(ColorHelper.getColor()))).into(imageView);
        if (imageUri.startsWith("http")) {
            Glide.with(imageView).load(imageUri).apply(RequestOptions.placeholderOf(new ColorDrawable(imageView.getContext().getColor(ColorUtil.getColor())))).into(imageView);
        }
        else {
            Glide.with(imageView).load(StringToBitMap(imageUri)).apply(RequestOptions.placeholderOf(new ColorDrawable(imageView.getContext().getColor(ColorUtil.getColor())))).into(imageView);
        }
//        }
//        else {
//            Glide.with(imageView.getContext()).load(ContactsGetter.StringToBitMap(imageUri)).into(imageView);
//        }
    }
}

