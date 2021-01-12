package com.example.cs496_week2_client.ui.contacts;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs496_week2_client.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Fragment fragment;
    private ArrayList<Contact> mData;

    // Constructor
    ContactAdapter(ArrayList<Contact> list, Fragment fm) {
        mData = list ;
        fragment = fm;
    }

    @Override
    public int getItemCount() {
        if (mData.isEmpty())
            return 0;
        return mData.size();
    }

    public void setData(ArrayList<Contact> data) {
        mData = data;
        // TODO notifyDataset change나 DiffUtils 로 최적화
    }

    // ViewHolder: store item view
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView, numView, statusView;
        ImageButton callButton;

        ViewHolder(View itemView) {
            // Init Views and Button
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameView = itemView.findViewById(R.id.nameTextView);
            numView = itemView.findViewById(R.id.numTextView);
            callButton = (ImageButton) itemView.findViewById(R.id.callButton);
            statusView = itemView.findViewById(R.id.statusView);

            // Click event for itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Contact ct = mData.get(pos);
                        Intent intent = new Intent(v.getContext(), ContactInfoActivity.class);
                        intent.putExtra("Name", ct.fullName);
                        intent.putExtra("Number", ct.phone);
                        if (ct.image != null) intent.putExtra("Image", ct.image.toString());
                        else intent.putExtra("Image", "");
                        fragment.startActivityForResult(intent, 10002);
                    }
                }
            });

            // Click event for callButton
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL,
                            Uri.parse("tel:" + numView.getText()));
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,
                parent, false);
        ContactAdapter.ViewHolder vh = new ContactAdapter.ViewHolder(v) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ViewHolder holder, int position) {
        Contact element = mData.get(position) ;
        if (element.image != null)
            holder.imageView.setImageURI(Uri.parse(element.image));
        holder.nameView.setText(element.fullName);
        holder.numView.setText(element.phone);
    }

    public int findText (String text) {
        for (int i = 0; i < getItemCount(); i++) {
            Contact element = mData.get(i);
            if (element.fullName.toLowerCase().contains(text)
                    || element.phone.toString().contains(text)) {
                return i;
            }
        }
        return 0;
    }

}