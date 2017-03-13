package com.example.application.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.application.MainActivity;
import com.example.application.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Created by warrens on 13.03.17.
 */

public class surveyFragment extends android.support.v4.app.DialogFragment {
    private String selectedAnswerOne;
    private String selectedAnswerTwo;
    private String selectedAnswerThree;
    private String selectedAnswerFour;
    private String selectedAnswerFive;
    private String[] questionOneAnswers;
    private String[] questionTwoAnswers;
    private String[] questionThreeAnswers;
    private String[] questionFourAnswers;
    private String[] questionFiveAnswers;

    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(false);
        final Context context = getActivity();
        View v = inflater.inflate(R.layout.fragment_survey, container, false);

        questionOneAnswers = new String[5];
        questionTwoAnswers = new String[5];
        questionThreeAnswers = new String[5];
        questionFourAnswers = new String[5];
        questionFiveAnswers = new String[5];

        for(int i = 0; i < questionOneAnswers.length; i++){
            questionOneAnswers[i] = "Answer "+i;
            questionTwoAnswers[i] = "Answer "+i;
            questionThreeAnswers[i] = "Answer "+i;
            questionFourAnswers[i] = "Answer "+i;
            questionFiveAnswers[i] = "Answer "+i;
        }

        ArrayAdapter<CharSequence> adapterActive;
        final Spinner activeDrp = (Spinner)v.findViewById(R.id.spinnerAnswers);
        adapterActive = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionOneAnswers);
        adapterActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrp.setAdapter(adapterActive);
        selectedAnswerOne = activeDrp.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveOne;
        final Spinner activeDrpOne = (Spinner)v.findViewById(R.id.spinnerAnswers1);
        adapterActiveOne = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionTwoAnswers);
        adapterActiveOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpOne.setAdapter(adapterActiveOne);
        selectedAnswerTwo = activeDrpOne.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwo;
        final Spinner activeDrpTwo = (Spinner)v.findViewById(R.id.spinnerAnswers2);
        adapterActiveTwo = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionThreeAnswers);
        adapterActiveTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwo.setAdapter(adapterActiveTwo);
        selectedAnswerThree = activeDrpTwo.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveThree;
        final Spinner activeDrpThree = (Spinner)v.findViewById(R.id.spinnerAnswers3);
        adapterActiveThree = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionFourAnswers);
        adapterActiveThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpThree.setAdapter(adapterActiveThree);
        selectedAnswerFour = activeDrpThree.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveFour;
        final Spinner activeDrpFour = (Spinner)v.findViewById(R.id.spinnerAnswers4);
        adapterActiveFour = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, questionFiveAnswers);
        adapterActiveFour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpFour.setAdapter(adapterActiveFour);
        selectedAnswerFive = activeDrpFour.getSelectedItem().toString();

        Button b = (Button) v.findViewById(R.id.Submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                String submitString = selectedAnswerOne+"\t"+selectedAnswerTwo+"\t"+selectedAnswerThree+"\t"+selectedAnswerFour+"\t"+selectedAnswerFive+"\n";
                String fileName = "demographics.txt";
                FileOutputStream fos;
                try {
                    fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
                    fos.write(submitString.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String toastString = "Submitted";
                Context context = getActivity();
                int durationOfToast = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, toastString, durationOfToast);
                toast.show();
                dismiss();
            }
        });
        return v;
    }
}
