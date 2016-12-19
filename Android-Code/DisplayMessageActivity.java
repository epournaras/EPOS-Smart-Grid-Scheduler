package com.example.scheduleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayMessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_display_message);

            String display;
            ArrayList<String> displayerName = new ArrayList<String>();
            ArrayList<String> displayerMinTime = new ArrayList<String>();
            ArrayList<String> displayerMaxTime = new ArrayList<String>();

            FileInputStream fisOne = openFileInput("minTime.txt");
            FileInputStream fisTwo = openFileInput("maxTime.txt");
            FileInputStream fisFour = openFileInput("nameOfAction.txt");
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

            TextView textView = (TextView)findViewById(R.id.text);
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
    }
}
