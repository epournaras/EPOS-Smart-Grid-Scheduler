package com.example.application.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.application.MainActivity;
import com.example.application.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by warrens on 24.07.17.
 */

public class AddRemoveApplianceFragment extends android.support.v4.app.DialogFragment {
    private CheckBox[] applianceCheckBoxs;
    private String[] applianceEnableBooleans;
    private ArrayList checkBoxIdArrayList = new ArrayList();
    private ArrayList relativeLayoutIdArrayList = new ArrayList();
    private ArrayList<RelativeLayout> relativeLayoutArrayList = new ArrayList<>();
    private String[] applianceNames;
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = getActivity();
        View v = inflater.inflate(R.layout.add_remove_menu, container, false);
        applianceCheckBoxs = new CheckBox[applianceNames.length];
        applianceEnableBooleans = new String[applianceNames.length];
        String appliancesEnabledDataFile = "appliancesEnabledDataFile.txt";
        String appliancesEnabled="";
        try{
            FileInputStream fis = getActivity().openFileInput(appliancesEnabledDataFile);
            StringBuilder builder = new StringBuilder();
            int chr;
            while ((chr = fis.read()) != -1) {
                builder.append((char) chr);
            }
            fis.close();
            appliancesEnabled = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        String[] appliancesEnabledArray = appliancesEnabled.split("\n");
        String[][] enableTable = new String[appliancesEnabledArray.length][2];
        for(int i = 0 ; i<enableTable.length;i++){
            enableTable[i] = appliancesEnabledArray[i].split(",");
        }

        String applianceNamesFile = "applianceNames.txt";
        String appliancesNames="";
        try{
            FileInputStream fis = getActivity().openFileInput(applianceNamesFile);
            StringBuilder builder = new StringBuilder();
            int chr;
            while ((chr = fis.read()) != -1) {
                builder.append((char) chr);
            }
            fis.close();
            appliancesNames = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        applianceNames = appliancesNames.split(",");

        RelativeLayout tempLayoutView;

        LinearLayout lin = (LinearLayout)v.findViewById(R.id.checkBoxLayout);

        for(int i = 0; i<applianceCheckBoxs.length;i++){
            final int index = i;
            final CheckBox temp;
            tempLayoutView = new RelativeLayout(getActivity());
            tempLayoutView.setId(v.generateViewId());
            tempLayoutView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            temp = new CheckBox(getActivity());
            temp.setId(v.generateViewId());
            temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            temp.setText(applianceNames[i]);
            if(applianceNames[i].equals(enableTable[i][0])){
                if(enableTable[i][1].equals("true")){
                    if(!temp.isChecked()){
                        applianceEnableBooleans[i] = "true";
                        temp.toggle();
                    }
                }else{
                    if(temp.isChecked()){
                        applianceEnableBooleans[i] = "false";
                        temp.toggle();
                    }
                }
            }
            try{
                lin.addView(tempLayoutView);
                tempLayoutView.addView(temp);
            }catch(Exception e){
                e.printStackTrace();
            }
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vi) {
                    if(temp.isChecked()){
                        applianceEnableBooleans[index] = "true";
                    }else{
                        applianceEnableBooleans[index] = "false";
                    }
                }
            });
            applianceCheckBoxs[i] = temp;
            checkBoxIdArrayList.add(temp.getId());
            relativeLayoutIdArrayList.add(tempLayoutView.getId());
            relativeLayoutArrayList.add(tempLayoutView);
        }
        Button cancel = (Button) v.findViewById(R.id.button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                dismiss();
            }
        });

        Button confirm = (Button) v.findViewById(R.id.button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                String submitString = "";
                for(int i = 0; i <applianceEnableBooleans.length;i++){
                    if(enableTable.length == applianceEnableBooleans.length){
                        enableTable[i][1] = applianceEnableBooleans[i];
                    }
                    for(int j = 0; j<enableTable[i].length;j++){
                        if(j==enableTable[i].length-1){
                            submitString+=enableTable[i][j];
                        }else{
                            submitString+=enableTable[i][j]+",";
                        }
                    }
                    submitString+="\n";
                }
                try{
                    FileOutputStream fos = getActivity().openFileOutput(appliancesEnabledDataFile,Context.MODE_PRIVATE);
                    fos.write(submitString.getBytes());
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                dismiss();
            }
        });

        return v;
    }
}
