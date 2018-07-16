package rekkeitrainning.com.lesson5.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rekkeitrainning.com.lesson5.R;
import rekkeitrainning.com.lesson5.model.Contact;


/**
 * Created by hoang on 7/16/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>{
    Context mContext;
    ArrayList<Contact> mListContact;
    ItemClickListener mClickListener;

    public ContactsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmListContact(ArrayList<Contact> mListContact) {
        this.mListContact = mListContact;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list,parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact mContact = mListContact.get(position);
        holder.tv_name.setText(mContact.getNameContact());
        holder.tv_phone.setText(mContact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return mListContact!=null?mListContact.size():0;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_name;
        TextView tv_phone;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tvNameContact);
            tv_phone = itemView.findViewById(R.id.tvPhone);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener!= null){
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
    public void onItemClickListener(ItemClickListener mClickListener){
        this.mClickListener = mClickListener;
    }
    public interface ItemClickListener{
        public void onItemClick(View view, int position);
    }
}
