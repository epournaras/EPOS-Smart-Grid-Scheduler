package com.example.scheduler.fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.scheduler.R;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by warrens on 07.08.17.
 */

public class editApplianceSettings extends android.support.v4.app.DialogFragment {
    public ArrayList textViewIds = new ArrayList();
    public ArrayList editTextIds = new ArrayList();
    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = getActivity();
        this.getDialog().setCanceledOnTouchOutside(true);
        final View v = inflater.inflate(R.layout.fragment_edit_appliance_settings, container, false);
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

        String wattages = "";
        try{
            FileInputStream fis = context.openFileInput("wattagesFile.txt");
            int chr;
            StringBuilder builder = new StringBuilder();
            while ((chr = fis.read()) != -1) {
                builder.append((char) chr);
            }
            wattages = builder.toString();
            //  0,              1,          2,          3,              4,    5,      6,        7,              8
            //House Number, Computer, Cooker (Hob),Cooker (Oven),Dishwasher,Kettle,Shower,Tumble Dryer, Washing Machine

            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.print(wattages+"\n");
        final String[] wattagesArray = wattages.split(",");
        LinearLayout lin = (LinearLayout)v.findViewById(R.id.editTextLayout);
        RelativeLayout tempLayoutView;
        RelativeLayout tempLayoutViewText;

        for(int i =0; i<enableTable.length;i++){
            String currentText;
            int index = 1;
            if(enableTable[i][1].equals("true")){
                switch(enableTable[i][0]){
                    case "Computer":
                        index = 1;
                        break;
                    case "Hob":
                        index = 2;
                        break;
                    case "Oven":
                        index = 3;
                        break;
                    case "DishWasher":
                        index = 4;
                        break;
                    case "Kettle":
                        index = 5;
                        break;
                    case "Shower":
                        index = 6;
                        break;
                    case "TumbleDryer":
                        index = 7;
                        break;
                    case "WashingMachine":
                        index = 8;
                        break;
                }

                String applianceIWattage = wattagesArray[index];
                currentText = enableTable[i][0]+": "+applianceIWattage+" watts";
                EditText etTemp = new EditText(context);
                etTemp.setInputType(InputType.TYPE_CLASS_NUMBER);
                etTemp.setText(applianceIWattage);
                etTemp.setId(v.generateViewId());

                TextView textTemp = new TextView(context);
                textTemp.setText(currentText);
                textTemp.setId(v.generateViewId());

                tempLayoutView = new RelativeLayout(getActivity());
                tempLayoutView.setId(v.generateViewId());
                tempLayoutView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                tempLayoutViewText = new RelativeLayout(getActivity());
                tempLayoutViewText.setId(v.generateViewId());
                tempLayoutViewText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                try{
                    tempLayoutViewText.addView(textTemp);
                    lin.addView(tempLayoutViewText);
                    tempLayoutView.addView(etTemp);
                    lin.addView(tempLayoutView);
                    editTextIds.add(etTemp.getId());
                    textViewIds.add(textTemp.getId());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        final String[][] enableTableFinal = enableTable;
        Button confirm = (Button) v.findViewById(R.id.button2);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                String wattFile = "";
                int ind = 0;
                int index = 0;
                for(int i = 1; i<wattagesArray.length;i++){
                    if(enableTableFinal[i-1][1].equals("true")){
                        TextView textTemp = (TextView)v.findViewById((int)textViewIds.get(ind));
                        EditText temp = (EditText)v.findViewById((int)editTextIds.get(ind));
                        String title = textTemp.getText().toString();
                        String t = temp.getText().toString();
                        ind++;
                        String[] parts = title.split(":");
                        String name = parts[0];
                        switch(name){
                            case "Computer":
                                index = 1;
                                break;
                            case "Hob":
                                index = 2;
                                break;
                            case "Oven":
                                index = 3;
                                break;
                            case "DishWasher":
                                index = 4;
                                break;
                            case "Kettle":
                                index = 5;
                                break;
                            case "Shower":
                                index = 6;
                                break;
                            case "TumbleDryer":
                                index = 7;
                                break;
                            case "WashingMachine":
                                index = 8;
                                break;
                        }
                        wattagesArray[index] = t;
                    }
                }
                for(int i = 0; i<wattagesArray.length;i++) {
                    if(i<wattagesArray.length-1){
                        wattFile += wattagesArray[i] + ",";
                    }else{
                        wattFile+=wattagesArray[i];
                    }
                }
                System.out.print(wattFile+"\n");
                try{
                    FileOutputStream fos = getActivity().openFileOutput("wattagesFile.txt", Context.MODE_PRIVATE);
                    fos.write(wattFile.getBytes());
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                dismiss();
            }
        });
        Button cancel = (Button) v.findViewById(R.id.button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                dismiss();
            }
        });
        return v;
    }
}
