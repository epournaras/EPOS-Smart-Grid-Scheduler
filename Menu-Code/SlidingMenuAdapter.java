package com.example.application.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.R;
import com.example.application.model.itemSlideMenu;

import java.util.List;

/**
 * Created by warrens on 23.01.17.
 */

public class SlidingMenuAdapter extends BaseAdapter{

    private Context context;
    private List<itemSlideMenu> lstItem;

    public SlidingMenuAdapter(Context context, List<itemSlideMenu> lstItem) {
        this.context = context;
        this.lstItem = lstItem;
    }

    @Override
    public int getCount() {
        return lstItem.size();
    }

    @Override
    public Object getItem(int position) {
        return lstItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.item_sliding_menu, null);
        ImageView img = (ImageView)v.findViewById(R.id.item_img);
        TextView tv = (TextView)v.findViewById(R.id.item_title);

        itemSlideMenu item = lstItem.get(position);
        img.setImageResource(item.getImgId());
        tv.setText(item.getTitle());
        return v;
    }
}
