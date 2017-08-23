package com.example.scheduler.fragment;

/**
 * Created by warrens on 08.08.17.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.app.DialogFragment;
import com.example.scheduler.MainActivity;
import com.example.scheduler.R;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class removeFragment extends DialogFragment {
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
        this.getDialog().setCanceledOnTouchOutside(true);
        View v = inflater.inflate(R.layout.fragment_remove, container, false);
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
            }
            currentActions.close();
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
