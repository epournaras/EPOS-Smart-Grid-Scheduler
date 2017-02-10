package com.example.application.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.application.MainActivity;
import com.example.application.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by warrens on 30.01.17.
 */

public class removeFragment extends android.support.v4.app.DialogFragment {
    private String selectedAction;
    private String[] currentActionList;

    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = getActivity();
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        ArrayList<String> list = new ArrayList<String>();
        try{
            FileInputStream currentActions = context.openFileInput("currentActions.txt");
            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = currentActions.read()) != -1){
                if((char)ch==','){
                    String add = builder.toString();
                    list.add(add);
                    builder = new StringBuilder();
                }else{
                    builder.append((char)ch);
                }
            };
        }catch(IOException e){

        }
        list.trimToSize();
        currentActionList = new String[list.size()];
        list.toArray(currentActionList);
        ArrayAdapter<CharSequence> adapterActiv;
        final Spinner activDrp = (Spinner)v.findViewById(R.id.spinner);
        adapterActiv = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, currentActionList);
        adapterActiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activDrp.setAdapter(adapterActiv);
        selectedAction = activDrp.getSelectedItem().toString();

        Button b = (Button) v.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                selectedAction = activDrp.getSelectedItem().toString();
                ((MainActivity)getActivity()).removeItem(selectedAction);
                dismiss();
            }
        });
        return v;
    }
}
