package com.example.application.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


import com.example.application.R;

public class Fragment2 extends Fragment {

    public Fragment2() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment2, container, false);
        try{
            String display;
            ArrayList<String> displayerName = new ArrayList<String>();
            ArrayList<String> displayerMinTime = new ArrayList<String>();
            ArrayList<String> displayerMaxTime = new ArrayList<String>();

            FileInputStream fisOne = getActivity().openFileInput("minTime.txt");
            FileInputStream fisTwo = getActivity().openFileInput("maxTime.txt");
            FileInputStream fisFour = getActivity().openFileInput("nameOfAction.txt");
            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fisFour.read()) != -1){
                if((char) ch==','){
                    displayerName.add(builder.toString());
                    builder = new StringBuilder();
                }
                else{
                    builder.append((char)ch);
                }
            };
            builder = new StringBuilder();
            while((ch = fisOne.read()) != -1){
                if((char) ch == ','){
                    displayerMinTime.add(builder.toString());
                    builder = new StringBuilder();
                }
                else{
                    builder.append((char)ch);
                }
            };
            builder = new StringBuilder();
            while((ch = fisTwo.read()) != -1){
                if((char) ch==','){
                    displayerMaxTime.add(builder.toString());
                    builder = new StringBuilder();
                }
                else{
                    builder.append((char)ch);
                }
            };
            display = "";
            displayerName.trimToSize();
            displayerMinTime.trimToSize();
            displayerMaxTime.trimToSize();
            int size = displayerName.size();
            int sizeMin = displayerMinTime.size();
            int sizeMax = displayerMaxTime.size();
            for(int i = 0; i<size&&i<sizeMin&&i<sizeMax; i ++ ){
                display += "Action: " +displayerName.get(i)+" "+displayerMinTime.get(i)+"-"+displayerMaxTime.get(i)+"\n";
            }

            TextView textView = (TextView)getView().findViewById(R.id.text);
            textView.setText(display);
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        catch(NullPointerException ex){
            ex.printStackTrace();
        }
        return v;  // this replaces 'setContentView'
    }
}
