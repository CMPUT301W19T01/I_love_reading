package com.example.libo.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.R;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private ArrayList<Comment> comments;
    private Context myContext;
    private static class ViewHolder {
        TextView username;
        TextView time;
        TextView content;
        ImageView usericon;
        RatingBar ratingbar;
    }

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, R.layout.comment_adapter, comments);
        this.comments = comments;
        this.myContext=context;
    }






    public View getView(int position, View convertView, ViewGroup parent) { // design a custom view

        Comment comment = getItem(position); // get current item
        ViewHolder viewHolder;

        if (convertView == null) { // follow convention
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comment_adapter, parent, false);
            viewHolder.username = (TextView) convertView.findViewById(R.id.TextViewUserName); // initialize all the textview variables
            viewHolder.time = (TextView) convertView.findViewById(R.id.TextViewCommentTime);
            viewHolder.content = (TextView) convertView.findViewById(R.id.TextViewCommentContent);
            viewHolder.ratingbar = (RatingBar) convertView.findViewById(R.id.CommentRatingBar);
            viewHolder.usericon = (ImageView) convertView.findViewById(R.id.ImageViewUserIcon);

            convertView.setTag(viewHolder);
            convertView.setBackgroundColor(Color.parseColor("#00141414"));

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(comment.getUsername()); // show the text in the text view
        viewHolder.time.setText(comment.getTime());
        viewHolder.content.setText(comment.getContent());
        //viewHolder.usericon.setImageBitmap(comment.get);
        viewHolder.ratingbar.setRating((float) comment.getRating());

        return convertView;

    }


    }
