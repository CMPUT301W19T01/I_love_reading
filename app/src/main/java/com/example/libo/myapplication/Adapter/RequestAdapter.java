package com.example.libo.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.libo.myapplication.Model.Book;
import com.example.libo.myapplication.Model.Request;
import com.example.libo.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Request adapter.
 */
public class RequestAdapter extends ArrayAdapter{
    private int resourceId;
    private ArrayList<Request> requests;

    /**
     * Instantiates a new Request adapter.
     *
     * @param context  the context of current adapter
     * @param resource the resource id
     * @param requests the requests list
     */
    public RequestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Request> requests) {
        super(context, resource, requests);
        resourceId = resource;
        this.requests = requests;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Request currentRequest = requests.get(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView bookName =  view.findViewById(R.id.request_cell_bookname);
        TextView date =  view.findViewById(R.id.request_cell_date);
        TextView status =  view.findViewById(R.id.request_cell_status);

        bookName.setText(currentRequest.getBookName());
        date.setText(currentRequest.getDate().toString());
        String currentstatus;
        if(currentRequest.isAccepted()){
            currentstatus = "Accepted";

        }else{
            currentstatus = "Not Accepted";
        }

        status.setText(currentstatus);
        return view;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    public Filter getFilter(){
        return bookFilter;
    }

    private Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Request> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(requests);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Request item: requests) {
                    if ((item.getBookName()+' '+item.getBookId()+' '+item.getReceiver()
                    +' '+item.getRequestId()).toLowerCase().contains(filterPattern)) {
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
            requests.clear();
            requests.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
