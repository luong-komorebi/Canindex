package com.example.admin.dogrecognizer.ListView;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.dogrecognizer.R;
import com.squareup.picasso.Picasso;
import com.vstechlab.easyfonts.EasyFonts;

import java.io.File;
import java.util.ArrayList;

import at.blogc.android.views.ExpandableTextView;


public class CustomListView extends ArrayAdapter<MyImage>{

    private Activity context;
    private ArrayList<MyImage> images;


    public CustomListView(Activity context, ArrayList<MyImage> images){
        super(context, R.layout.listview_layout,images);
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listview_layout,null,false);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }

        MyImage image = getItem(position);
        Picasso.with(context).load(image.getUri()).centerInside().resize(1000,700).onlyScaleDown().into(viewHolder.im1);
        viewHolder.tv1.setText(image.getTitle());
        viewHolder.tv2.setText(image.getDescription());


        return r;
    }

    class ViewHolder{
        TextView tv1;
        ExpandableTextView  tv2;
        ImageView im1;
        Button bt1;

        ViewHolder(View v){
            tv1 = (TextView) v.findViewById(R.id.dname);
            tv2 = (ExpandableTextView) v.findViewById(R.id.dattribute);
            im1 = (ImageView) v.findViewById(R.id.imageview);
            bt1 = (Button) v.findViewById(R.id.read_more_button);
            tv2.setInterpolator(new OvershootInterpolator());

            // Set fonts for the textviews
            tv1.setTypeface(EasyFonts.captureIt(v.getContext()));
            tv2.setTypeface(EasyFonts.caviarDreams(v.getContext()));

            bt1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    if (tv2.isExpanded())
                    {
                        tv2.collapse();
                        bt1.setText(R.string.expand);
                    }
                    else
                    {
                        tv2.expand();
                        bt1.setText(R.string.collapse);
                    }
                }
            });
        }

    }

}
