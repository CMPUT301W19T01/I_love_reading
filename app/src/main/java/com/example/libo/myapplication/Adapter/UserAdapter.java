package com.example.libo.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Users;
import com.example.libo.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter <Users> {
    private ArrayList<Users> users;
    private Context myContext;

    public UserAdapter(Context context, ArrayList<Users> users){
        super(context, R.layout.user_adapter, users);
        this.users = users;
        this.myContext = context;
    }

    private static class ViewHolder {
        TextView contactInfo;
        ImageView Userphoto;
        TextView username;
        TextView uid;

    }
    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        final Users user = getItem(position);
        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.user_adapter, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.username = (TextView) convertView.findViewById(R.id.username);
            viewHolder.uid = (TextView) convertView.findViewById(R.id.id);
            viewHolder.contactInfo = (TextView) convertView.findViewById(R.id.contact);
            viewHolder.Userphoto = (ImageView) convertView.findViewById(R.id.userphoto);


            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.username.setText("name: " + user.getUsername());
        if (user.getEmail() != null){
            viewHolder.contactInfo.setText("Email: " + user.getEmail());
        }

        if (user.getUid() != null){
            viewHolder.uid.setText("UID: "+user.getUid());
        }
        Picasso.with(getContext()).load(user.getPhoto()).into(viewHolder.Userphoto);

        return convertView;
    }

    public Filter getFilter(){
        return bookFilter;
    }

    private Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Users> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(users);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Users item: users) {
                    if ((item.getUsername()+' '+item.getUid()+' '+item.getEmail()).toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users.clear();
            users.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}


