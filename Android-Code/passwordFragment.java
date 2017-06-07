package com.example.application.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.application.MainActivity;
import com.example.application.R;

import java.io.FileInputStream;

/**
 * Created by warrens on 13.03.17.
 */

public class passwordFragment extends android.support.v4.app.DialogFragment {
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(false);
        final Context context = getActivity();
        View layoutView = inflater.inflate(R.layout.fragment_password, container, false);
        Button b = (Button) layoutView.findViewById(R.id.buttonEnter);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textView = (EditText)layoutView.findViewById(R.id.editText);
                String password= textView.getText().toString();
                String passwordCheck = "";
                try{
                    FileInputStream fisPass = context.openFileInput("Password.txt");
                    int ch;
                    StringBuilder builder = new StringBuilder();
                    while ((ch = fisPass.read()) != -1) {
                        builder.append((char) ch);
                    }
                    fisPass.close();
                    passwordCheck = builder.toString();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(password.equals(passwordCheck)){
                    dismiss();
                }else{
                    ((MainActivity)getActivity()).replaceFragment(0);
                    dismiss();
                }
            }
        });
        Button bTwo = (Button) layoutView.findViewById(R.id.buttonCancel);
        bTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(0);
                dismiss();
            }
        });
        return layoutView;
    }
}
