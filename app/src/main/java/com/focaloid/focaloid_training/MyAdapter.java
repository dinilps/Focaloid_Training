package com.focaloid.focaloid_training;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.security.PrivateKey;

/**
 * Created by dinil on 11-08-2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java

    private String name;        //String Resource for header View Name
    private int profileImg;        //int Resource for header view profile picture
    private String ProfileImg;
    private String email;       //String Resource for header view email
    Context contxt;
    static Button btncam;
    private static int RESULT_LOAD_IMAGE = 1;
    private OnItemInteractionListener listener;


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;
        //Context contxt;
        Button camButton;
        View root;

        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);

            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                root=itemView.findViewById(R.id.root);
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{

                camButton= (Button) itemView.findViewById(R.id.buttoncam);
                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }

    }


    MyAdapter(String Titles[], int Icons[], String Name, String Email, String Profile, Context context){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        name = Name;
        email = Email;
        profileImg = R.drawable.profile_pic;
        listener = (OnItemInteractionListener) context;
        contxt=context;
        ProfileImg=Profile;
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout
            ViewHolder vhHeader = new ViewHolder(v,viewType);
            return vhHeader;
        }
        return null;

    }


    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
        if(holder.Holderid ==1) {
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position -1]);
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(position);
                }
            });
        }
        else{

            if(ProfileImg.contains("profile_pic")){
                holder.profile.setImageResource(profileImg);
            }
            else {
                // loading album cover using Glide library
                Glide.with(contxt).load(ProfileImg).into(holder.profile);
            }


            holder.Name.setText(name);
            holder.email.setText(email);
            holder.camButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCamButtonClicked();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mNavTitles.length+1;
    }



    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public interface OnItemInteractionListener{
        void onCamButtonClicked();
        void onItemClicked(int position);
    }

}
