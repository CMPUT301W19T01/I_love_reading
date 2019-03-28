package com.example.libo.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import com.example.libo.myapplication.Model.Comment;
import com.example.libo.myapplication.R;
import com.squareup.picasso.Picasso;

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
        SparkButton favorButton;
        TextView favorNum;
    }

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, R.layout.comment_adapter, comments);
        this.comments = comments;
        this.myContext=context;
    }


    public View getView(int position, View convertView, ViewGroup parent) { // design a custom view

        final Comment comment = getItem(position); // get current item
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
            viewHolder.favorButton = (SparkButton) convertView.findViewById(R.id.spark_button);
            viewHolder.favorNum = (TextView) convertView.findViewById(R.id.TextViewFavorNumber);
            convertView.setTag(viewHolder);
            convertView.setBackgroundColor(Color.parseColor("#00141414"));

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(comment.getUsername()); // show the text in the text view
        viewHolder.time.setText(comment.getTime());
        viewHolder.content.setText(comment.getContent());
        //Uri photo = Uri.parse(comment.getUser_photo());
        //Picasso.with(this.myContext).load(photo).into(viewHolder.usericon);
        viewHolder.ratingbar.setRating((float) comment.getRating());
        viewHolder.favorNum.setText(comment.getFavor_number().toString());
        viewHolder.favorButton.setEventListener(new SparkEventListener(){
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    comment.setFavor_number(comment.getFavor_number() +1);
                    notifyDataSetChanged();
                } else {
                    if (comment.getFavor_number() > 0) {
                        comment.setFavor_number(comment.getFavor_number() - 1);
                        notifyDataSetChanged();
                    }
                }
            }
            public void onEventAnimationEnd(ImageView button,boolean buttonState){

            };
            public void onEventAnimationStart(ImageView button,boolean buttonState){

            };
        });
        return convertView;

    }


    }
