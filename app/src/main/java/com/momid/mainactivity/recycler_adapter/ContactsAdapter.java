package com.momid.mainactivity.recycler_adapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.momid.mainactivity.ColorHelper;
import com.momid.mainactivity.MainActivity;
import com.momid.mainactivity.R;
import com.momid.mainactivity.data_model.Contact;

import java.util.List;

import javax.inject.Inject;

public class ContactsAdapter extends PagedListAdapter<Contact, RecyclerView.ViewHolder> {

    private List<Contact> contacts;
    private OnItemClick onItemClick;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean loadMoreEnabled = false;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    @Inject
    public ColorHelper colorHelper;

    public ContactsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact1, parent, false);
        RecyclerView.ViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Contact contact = getItem(position);
        int contactsCount = getItemCount() - 1;

        if (contact != null) {

            MyViewHolder viewHolder = (MyViewHolder) holder;

            String fullName = contact.getFullName();

            if (fullName == null) {
                fullName = "no name";
            }
            if (position > 0 && getItem(position - 1).getFullName() == null) {
                getItem(position - 1).setFullName("no name");
            }
            if (position < contactsCount - 1 && getItem(position + 1).getFullName() == null) {
                getItem(position).setFullName("no name");
            }

            viewHolder.name.setText(contact.getFullName());
            viewHolder.imageName.setText(fullName.charAt(0) + "");
            viewHolder.image.setBackgroundTintList(ColorStateList.valueOf(viewHolder.itemView.getContext().getColor(ColorHelper.getColor())));

            if (position == 0) {
                viewHolder.nameSeparator.setText(fullName.charAt(0) + "");
                viewHolder.nameSeparatorLayout.setVisibility(View.VISIBLE);
            }
            if (position > 0 && !(fullName.charAt(0) + "").equals(getItem(position - 1).getFullName().charAt(0) + "")) {
                viewHolder.nameSeparator.setText(fullName.charAt(0) + "");
                viewHolder.nameSeparatorLayout.setVisibility(View.VISIBLE);
            }
            if (position < contactsCount - 1 && !(fullName.charAt(0) + "").equals(getItem(position + 1).getFullName().charAt(0) + "")) {
                viewHolder.divider.setVisibility(View.INVISIBLE);
            }

            viewHolder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
                        onItemClick.onCallClick(contact);
                    }
                }
            });
            viewHolder.sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
                        onItemClick.onSmsClick(contact);
                    }
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                onItemClick.onItemClick(contact);
                }
            });
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

        public TextView name, imageName, nameSeparator;
        public ImageView image;
        public ImageButton call, sms;
        private LinearLayout nameSeparatorLayout;
        private View divider;

        public MyViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.contact1_name);
            imageName = view.findViewById(R.id.contact1_image_name);
            image = view.findViewById(R.id.contact1_imageview);
            call = view.findViewById(R.id.contact1_call);
            sms = view.findViewById(R.id.contact1_sms);
            nameSeparator = view.findViewById(R.id.contact1_name_separator_name);
            nameSeparatorLayout = view.findViewById(R.id.contact1_name_separator_layout);
            divider = view.findViewById(R.id.contact1_divider);
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
}
