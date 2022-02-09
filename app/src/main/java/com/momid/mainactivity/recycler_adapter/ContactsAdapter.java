package com.momid.mainactivity.recycler_adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.momid.mainactivity.ColorHelper;
import com.momid.mainactivity.R;
import com.momid.mainactivity.data_model.Contact;
import com.momid.mainactivity.databinding.ItemContactBinding;

import java.util.List;

import javax.inject.Inject;

public class ContactsAdapter extends PagedListAdapter<Contact, RecyclerView.ViewHolder> {

    private List<Contact> contacts;
    private OnItemClick onItemClick;
    private boolean loadMoreEnabled = false;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    @Inject
    public ColorHelper colorHelper;

    public ContactsAdapter(OnItemClick onItemClick) {
        super(DIFF_CALLBACK);
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_contact, parent, false);
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

        public ItemContactBinding binding;

        public MyViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindItem(Contact contact, Contact previousContact, Contact nextContact, int itemCount, int position) {

            binding.setContact(contact);
            binding.setPreviousContact(previousContact);
            binding.setNextContact(nextContact);
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
}
