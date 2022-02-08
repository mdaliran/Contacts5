package com.momid.mainactivity.recycler_adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.momid.mainactivity.ColorHelper;
import com.momid.mainactivity.MainActivity;
import com.momid.mainactivity.R;
import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.databinding.ItemContact1Binding;

import java.util.List;

import javax.inject.Inject;

public class ContactsAdapter extends PagedListAdapter<Contact, RecyclerView.ViewHolder> {

    private List<Contact> contacts;
    private OnItemClick onItemClick;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loadMoreEnabled = false;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private Activity activity;
    @Inject
    public ColorHelper colorHelper;

    public ContactsAdapter(Activity activity) {
        super(DIFF_CALLBACK);
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContact1Binding binding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.item_contact1, parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact1, parent, false);
        RecyclerView.ViewHolder viewHolder = new MyViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Contact contact = getItem(position);
        Contact previousContact = position > 0 ? getItem(position - 1) : null;
        Contact nextContact = position < getItemCount() - 2 ? getItem(position + 1) : null;
        int contactsCount = getItemCount() - 1;

        if (contact != null) {

            MyViewHolder viewHolder = (MyViewHolder) holder;

            viewHolder.bindItem(contact, previousContact, nextContact, contactsCount, position);
        }
        else {
//            holder.clear
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoadMore(RecyclerView recyclerView, LinearLayoutManager layoutManager) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!loadMoreEnabled) {
                    return;
                }

                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            onLoadMoreListener.onLoadMore();

                            loading = true;
                        }
                    }
                }
            }
        });
    }

    public void enableLoadMore() {
        loadMoreEnabled = true;
    }

    public void disableLoadMore() {
        loadMoreEnabled = false;
    }

    private static DiffUtil.ItemCallback<Contact> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Contact>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(Contact oldContact, Contact newContact) {
                    return oldContact.getId() == newContact.getId();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Contact oldContact,
                                                  Contact newContact) {
                    return oldContact.equals(newContact);
                }
            };

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ItemContact1Binding binding;

        public MyViewHolder(ItemContact1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindItem(Contact contact, Contact previousContact, Contact nextContact, int itemCount, int position) {
            binding.setItem(contact);
            binding.setPreviousItem(previousContact);
            binding.setNextItem(nextContact);
            binding.setItemCount(itemCount);
            binding.setPosition(position);
            binding.executePendingBindings();
        }
    }

    public static interface OnItemClick {

        public void onItemClick(Contact contact);

        public void onCallClick(Contact contact);

        public void onSmsClick(Contact contact);
    }

    public static interface OnLoadMoreListener {

        public void onLoadMore();
    }

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
