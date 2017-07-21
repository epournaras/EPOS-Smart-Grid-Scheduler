package com.example.application.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.example.application.R;

import static android.content.Context.MODE_PRIVATE;

public class Fragment2 extends Fragment {
    public ArrayList cbidal = new ArrayList();
    public Fragment2() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View v = inflater.inflate(R.layout.fragment2, container, false);
        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.LinearLayout01);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ArrayList<CheckBox> cbal = new ArrayList<CheckBox>();
        ArrayList<TextView> tval = new ArrayList<TextView>();
        ArrayList<RelativeLayout> rlal = new ArrayList<RelativeLayout>();
        ArrayList idal = new ArrayList<>();

        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        try{
            Bundle args = getArguments();
            String display = args.getString("display");
            if(display.equals("Nothing to show yet!")){
                try {
                    FileInputStream fis = getActivity().openFileInput("TomorrowSchedule.txt");
                    StringBuilder builder = new StringBuilder();
                    int ch;
                    while((ch = fis.read()) != -1){
                        builder.append((char)ch);
                    }
                    fis.close();
                    display = builder.toString();
                }catch(Exception e){
                    String toastString = "Couldnt get Tomorrow's schedule.";
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Context context = getActivity();
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                    e.printStackTrace();
                }
            }else{
                String tomorrowsSchedule = "TomorrowSchedule.txt";
                FileOutputStream fos;
                try {
                    fos = getActivity().openFileOutput(tomorrowsSchedule, MODE_PRIVATE);
                    fos.write(display.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String[] list = display.split("[\\r\\n]+");
            String[][] plans = new String [list.length][];


            RelativeLayout templv;
            CheckBox temp;
            TextView temp2;
            if(display.equals(" ")){

            }else{
                for(int i = 0; i<list.length;i++){
                    String text = "";
                    templv = new RelativeLayout(getActivity());
                    templv.setId(v.generateViewId());
                    templv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    temp = new CheckBox(getActivity());
                    temp.setId(v.generateViewId());
                    temp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    temp.setPadding(0,0,30,0);
                    cbidal.add(temp.getId());


                    temp2 = new TextView(getActivity());
                    temp2.setId(v.generateViewId());
                    temp2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    plans[i] = list[i].split(",");
                    for(int j = 0; j<plans[i].length;j++){
                        text+=plans[i][j]+"\n";
                    }

                    temp2.setText(text);
                    temp2.setPadding(120,9,30,8);
                    idal.add(temp2.getId());
                    try{
                        linearLayout.addView(templv);
                        if(i==0){
                            templv.setBackgroundColor(getResources().getColor(R.color.yellow));
                        }
                        templv.addView(temp);
                        templv.addView(temp2);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    rlal.add(templv);
                    cbal.add(temp);
                    tval.add(temp2);

                }
                if(settings.getBoolean("defaultBool", true)){
                    CheckBox checkB = (CheckBox)v.findViewById((int)cbidal.get(0));
                    checkB.toggle();
                    try{
                        FileOutputStream fos = getActivity().openFileOutput("chosenPlan.txt", MODE_PRIVATE);
                        TextView tempTextV = (TextView) v.findViewById((int)idal.get(0));
                        String defaultPass = tempTextV.getText().toString();
                        fos.write(defaultPass.getBytes());
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    settings.edit().putBoolean("defaultBool", false).commit();
                }else{
                    for(int i = 0; i<tval.size();i++){
                        TextView temperaryTextView = (TextView)v.findViewById((int)idal.get(i));
                        String temperaryString = temperaryTextView.getText().toString();
                        String comparator = "";
                        try{
                            FileInputStream fis = getActivity().openFileInput("chosenPlan.txt");
                            int chr;
                            StringBuilder builder = new StringBuilder();
                            while ((chr = fis.read()) != -1) {
                                builder.append((char) chr);
                            }
                            fis.close();
                            comparator = builder.toString();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        if(comparator.equals(temperaryString)){
                            CheckBox c = (CheckBox)v.findViewById((int)cbidal.get(i));
                            c.toggle();
                            Task(i,v);
                        }
                    }
                }

                CheckBox[] cba = new CheckBox[cbal.size()];
                cbal.toArray(cba);
                for(int i = 0; i<cba.length; i++){
                    final TextView tempTV = (TextView) v.findViewById((int)idal.get(i));
                    final String pass = tempTV.getText().toString();
                    final int index = i;
                    cba[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View vi) {
                            Task(index,v);
                            try{
                                FileOutputStream fos = getActivity().openFileOutput("chosenPlan.txt", MODE_PRIVATE);
                                fos.write(pass.getBytes());
                                fos.close();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }catch(NullPointerException ex){
            TextView textView = (TextView)v.findViewById(R.id.text);
            textView.setText("Nothing to show yet!");
            ex.printStackTrace();
        }

        return v;  // this replaces 'setContentView'
    }
    public void Task(int i, View v) {
        for(int j = 0; j< cbidal.size();j++){
            if(j!=i){
                CheckBox c = (CheckBox)v.findViewById((int)cbidal.get(j));
                if(c.isChecked()){
                    c.toggle();
                }
            }
        }
    }

}
