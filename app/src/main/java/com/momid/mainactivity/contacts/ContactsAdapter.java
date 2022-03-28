package com.momid.mainactivity.contacts;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.momid.mainactivity.R;
import com.momid.mainactivity.databinding.ItemContactBinding;

public class ContactsAdapter extends PagingDataAdapter<Contact, RecyclerView.ViewHolder> {


    public OnItemClick onItemClick;

    public ContactsAdapter(OnItemClick onItemClick) {
        super(DIFF_CALLBACK);
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_contact, parent, false);
        return new ContactsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Contact contact = getItem(position);

        if (contact != null) {

            String previousContactFirstLetter = position > 0 && getItem(position - 1) != null ? String.valueOf(getItem(position - 1).getFirstLetter()) : "";
            String nextContactFirstLetter = position < getItemCount() - 2 && getItem(position + 1) != null ? String.valueOf(getItem(position + 1).getFirstLetter()) : "";
            ((ContactsViewHolder) holder).bindItem(contact, previousContactFirstLetter, nextContactFirstLetter, getItemCount());
        }
        else {
//            holder.clear
        }
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

    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        private final ItemContactBinding binding;

        public ContactsViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindItem(Contact contact, String previousContactFirstLetter, String nextContactFirstLetter, int itemCount) {

            int position = getAbsoluteAdapterPosition();

            binding.setContact(contact);

            binding.setOnItemClick(onItemClick);
            boolean showNameSeparator = position == 0 || (position > 0 && !(contact.getFirstLetter()).equals(previousContactFirstLetter));

            boolean hideBottomSeparator = position < itemCount - 1 && !(contact.getFirstLetter().equals(nextContactFirstLetter));

            binding.setShowNameSeparator(showNameSeparator);
            binding.setHideBottomSeparator(hideBottomSeparator);
            binding.executePendingBindings();
        }
    }

}
