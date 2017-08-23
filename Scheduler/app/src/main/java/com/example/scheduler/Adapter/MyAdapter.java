package com.example.scheduler.Adapter;

/**
 * Created by warrens on 10.08.17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.example.scheduler.MainActivity;
import com.example.scheduler.R;

import java.io.FileOutputStream;


/**
 * Created by warrens on 09.08.17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private CheckBox[] boxes;
    private MainActivity mainActivity;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CheckBox mCheckBox;

        public ViewHolder(CheckBox v){
            super(v);
            mCheckBox = v;
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, MainActivity m) {
        mDataset = myDataset;
        boxes = new CheckBox[mDataset.length];
        mainActivity = m;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        CheckBox v = (CheckBox) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_check, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder hlder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int index = position;
        final ViewHolder holder = hlder;
        holder.mCheckBox.setText(mDataset[position]);
        boxes[position] = holder.mCheckBox;
        if(position==0){
            if(boxes[position].isChecked()){
                try{
                    FileOutputStream fos = mainActivity.openFileOutput("chosenPlan.txt", Context.MODE_PRIVATE);
                    fos.write(boxes[position].getText().toString().getBytes());
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                boxes[position].toggle();
                try{
                    FileOutputStream fos = mainActivity.openFileOutput("chosenPlan.txt", Context.MODE_PRIVATE);
                    fos.write(boxes[position].getText().toString().getBytes());
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            if(boxes[position].isChecked()){
                boxes[position].toggle();
            }
        }
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if(cb.isChecked()){
                    String associatedText = cb.getText().toString();
                    try{
                        FileOutputStream fos = mainActivity.openFileOutput("chosenPlan.txt", Context.MODE_PRIVATE);
                        fos.write(associatedText.getBytes());
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    for(int i = 0 ; i<boxes.length;i++){
                        if(i!=index){
                            if(boxes[i].isChecked()){
                                boxes[i].toggle();
                            }
                        }
                    }
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

