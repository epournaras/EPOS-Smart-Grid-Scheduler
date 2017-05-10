package com.example.application.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.application.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by warrens on 13.03.17.
 */

public class Fragment4 extends Fragment {
    private String selectedAnswerOne,selectedAnswerTwo,selectedAnswerThree,selectedAnswerFour,selectedAnswerFive,selectedAnswerSix,selectedAnswerSeven,selectedAnswerEight,
            selectedAnswerNine, selectedAnswerTen, selectedAnswerEleven,selectedAnswerTwelve, selectedAnswerThirteen, selectedAnswerFourteen,selectedAnswerFifteen,selectedAnswerSixteen;
    private String android_id;
    public Fragment4() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment4, container, false);
        Context myContext = getActivity();
        String[] QIDs = new String[]{
                "1",
                "2",
                "3",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12",
                "13",
                "14",
                "15",
                "16"
        };
        String [] answers = new String[]{
                "Yes - Definitely",
                "Yes - Moderately",
                "Not really",
                "Not at all"
        };

        ArrayAdapter<CharSequence> adapterActive;
        final Spinner activeDrp = (Spinner)v.findViewById(R.id.spinnerAnswers1);
        adapterActive = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrp.setAdapter(adapterActive);
        selectedAnswerOne = activeDrp.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveOne;
        final Spinner activeDrpOne = (Spinner)v.findViewById(R.id.spinnerAnswers2);
        adapterActiveOne = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpOne.setAdapter(adapterActiveOne);
        selectedAnswerTwo = activeDrpOne.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwo;
        final Spinner activeDrpTwo = (Spinner)v.findViewById(R.id.spinnerAnswers3);
        adapterActiveTwo = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwo.setAdapter(adapterActiveTwo);
        selectedAnswerThree = activeDrpTwo.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveThree;
        final Spinner activeDrpThree = (Spinner)v.findViewById(R.id.spinnerAnswers4);
        adapterActiveThree = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveThree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpThree.setAdapter(adapterActiveThree);
        selectedAnswerFour = activeDrpThree.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveFour;
        final Spinner activeDrpFour = (Spinner)v.findViewById(R.id.spinnerAnswers5);
        adapterActiveFour = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveFour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpFour.setAdapter(adapterActiveFour);
        selectedAnswerFive = activeDrpFour.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveFive;
        final Spinner activeDrpFive = (Spinner)v.findViewById(R.id.spinnerAnswers6);
        adapterActiveFive = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveFive.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpFive.setAdapter(adapterActiveFive);
        selectedAnswerSix = activeDrpFive.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveSix;
        final Spinner activeDrpSix = (Spinner)v.findViewById(R.id.spinnerAnswers7);
        adapterActiveSix = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveSix.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpSix.setAdapter(adapterActiveSix);
        selectedAnswerSeven = activeDrpSix.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveSeven;
        final Spinner activeDrpSeven = (Spinner)v.findViewById(R.id.spinnerAnswers8);
        adapterActiveSeven = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveSeven.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpSeven.setAdapter(adapterActiveSix);
        selectedAnswerEight = activeDrpSeven.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveEight;
        final Spinner activeDrpEight = (Spinner)v.findViewById(R.id.spinnerAnswers9);
        adapterActiveEight = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveEight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpEight.setAdapter(adapterActiveEight);
        selectedAnswerNine = activeDrpEight.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveNine;
        final Spinner activeDrpNine = (Spinner)v.findViewById(R.id.spinnerAnswers10);
        adapterActiveNine = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveNine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpNine.setAdapter(adapterActiveNine);
        selectedAnswerTen = activeDrpNine.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTen;
        final Spinner activeDrpTen = (Spinner)v.findViewById(R.id.spinnerAnswers11);
        adapterActiveTen = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveTen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTen.setAdapter(adapterActiveTen);
        selectedAnswerEleven = activeDrpTen.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveEleven;
        final Spinner activeDrpEleven = (Spinner)v.findViewById(R.id.spinnerAnswers12);
        adapterActiveEleven = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveEleven.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpEleven.setAdapter(adapterActiveEleven);
        selectedAnswerTwelve = activeDrpEleven.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveTwelve;
        final Spinner activeDrpTwelve = (Spinner)v.findViewById(R.id.spinnerAnswers13);
        adapterActiveTwelve = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveTwelve.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpTwelve.setAdapter(adapterActiveTwelve);
        selectedAnswerThirteen = activeDrpTwelve.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveThirteen;
        final Spinner activeDrpThirteen = (Spinner)v.findViewById(R.id.spinnerAnswers14);
        adapterActiveThirteen = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveThirteen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpThirteen.setAdapter(adapterActiveThirteen);
        selectedAnswerFourteen = activeDrpThirteen.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveFourteen;
        final Spinner activeDrpFourteen = (Spinner)v.findViewById(R.id.spinnerAnswers15);
        adapterActiveFourteen = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveFourteen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpFourteen.setAdapter(adapterActiveFourteen);
        selectedAnswerFifteen = activeDrpFourteen.getSelectedItem().toString();

        ArrayAdapter<CharSequence> adapterActiveFifteen;
        final Spinner activeDrpFifteen = (Spinner)v.findViewById(R.id.spinnerAnswers16);
        adapterActiveFifteen = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item, answers);
        adapterActiveFifteen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activeDrpFifteen.setAdapter(adapterActiveFifteen);
        selectedAnswerSixteen = activeDrpFifteen.getSelectedItem().toString();

        DialogFragment newFragment = new passwordFragment();
        FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
        newFragment.show(fragManager, "passwordFragment");

        Button b = (Button) v.findViewById(R.id.Submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String[] Answers  = new String[]{
                        selectedAnswerOne,
                        selectedAnswerTwo,
                        selectedAnswerThree,
                        selectedAnswerFour,
                        selectedAnswerFive,
                        selectedAnswerSix,
                        selectedAnswerSeven,
                        selectedAnswerEight,
                        selectedAnswerNine,
                        selectedAnswerTen,
                        selectedAnswerEleven,
                        selectedAnswerTwelve,
                        selectedAnswerThirteen,
                        selectedAnswerFourteen,
                        selectedAnswerFifteen,
                        selectedAnswerSixteen
                };
                String submitString = "QID,Answer\n";
                for(int i =0; i<QIDs.length;i++){
                    submitString = QIDs[i]+","+Answers[i]+"\n";
                }
                String fileName = android_id+"OUTPUTSURVEY.txt";
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                FileOutputStream fOut=null;
                File file1 = new File(root+ File.separator + fileName);
                if(!file1.exists()) {
                    try {
                        file1.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    fOut = new FileOutputStream(file1);
                    fOut.write(submitString.getBytes());
                    String toastString = "Submitted";
                    Context context = getActivity();
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, toastString, durationOfToast);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }
}
