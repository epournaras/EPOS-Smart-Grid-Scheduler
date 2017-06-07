package com.example.application.fragment;

import android.content.Context;
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
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import com.example.application.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by warrens on 13.03.17.
 */

public class Fragment4 extends Fragment {
    private String selectedAnswerOne,selectedAnswerTwo,selectedAnswerThree,selectedAnswerFour,selectedAnswerFive,selectedAnswerSix,selectedAnswerSeven,selectedAnswerEight,
            selectedAnswerNine, selectedAnswerTen, selectedAnswerEleven,selectedAnswerTwelve, selectedAnswerThirteen, selectedAnswerFourteen,selectedAnswerFifteen,selectedAnswerSixteen;
    private String android_id;
    private SeekBar qOneOneSimpleSeekBar,qOneTwoSimpleSeekBar,qOneThreeSimpleSeekBar,qOneFourSimpleSeekBar,qOneFiveSimpleSeekBar,qOneSixSimpleSeekBar,qOneSevenSimpleSeekBar,qOneEightSimpleSeekBar,
            qTwoOneSimpleSeekBar,qTwoTwoSimpleSeekBar,qTwoThreeSimpleSeekBar,qTwoFourSimpleSeekBar,qTwoFiveSimpleSeekBar,qTwoSixSimpleSeekBar,qTwoSevenSimpleSeekBar,qTwoEightSimpleSeekBar;
    private int qOneOneProgressChangedValue,qOneTwoProgressChangedValue,qOneThreeProgressChangedValue,qOneFourProgressChangedValue,qOneFiveProgressChangedValue,qOneSixProgressChangedValue,qOneSevenProgressChangedValue,qOneEightProgressChangedValue,
            qTwoOneProgressChangedValue,qTwoTwoProgressChangedValue,qTwoThreeProgressChangedValue,qTwoFourProgressChangedValue,qTwoFiveProgressChangedValue,qTwoSixProgressChangedValue,qTwoSevenProgressChangedValue,qTwoEightProgressChangedValue;

    public Fragment4() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment4, container, false);
        Context myContext = getActivity();
        String[] QIDs = new String[]{
                "1-1",
                "1-2",
                "1-3",
                "1-4",
                "1-5",
                "1-6",
                "1-7",
                "1-8",
                "2-1",
                "2-2",
                "2-3",
                "2-4",
                "2-5",
                "2-6",
                "2-7",
                "2-8"
        };
        //put a seek bar agree disagree for each appkliance for both questions.

        qOneOneSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q11SeekBar);
        qOneOneSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qOneOneProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qOneTwoSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q12SeekBar);
        qOneTwoSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qOneTwoProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qOneThreeSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q13SeekBar);
        qOneThreeSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qOneThreeProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qOneFourSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q14SeekBar);
        qOneFourSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qOneFourProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qOneFiveSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q15SeekBar);
        qOneFiveSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qOneFiveProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qOneSixSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q16SeekBar);
        qOneSixSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qOneSixProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qOneSevenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q17SeekBar);
        qOneSevenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qOneSevenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qOneEightSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q18SeekBar);
        qOneEightSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qOneEightProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwoOneSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q21SeekBar);
        qTwoOneSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwoOneProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwoTwoSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q22SeekBar);
        qTwoTwoSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwoTwoProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwoThreeSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q23SeekBar);
        qTwoThreeSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwoThreeProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwoFourSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q24SeekBar);
        qTwoFourSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwoFourProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwoFiveSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q25SeekBar);
        qTwoFiveSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwoFiveProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwoSixSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q26SeekBar);
        qTwoSixSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwoSixProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        qTwoSevenSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q27SeekBar);
        qTwoSevenSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwoSevenProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        qTwoEightSimpleSeekBar=(SeekBar)v.findViewById(R.id.Q28SeekBar);
        qTwoEightSimpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                qTwoEightProgressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        DialogFragment newFragment = new passwordFragment();
        FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
        newFragment.show(fragManager, "passwordFragment");

        Button b = (Button) v.findViewById(R.id.Submit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View g) {
                android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                int[] answers = new int[]{
                        qOneOneProgressChangedValue,
                        qOneTwoProgressChangedValue,
                        qOneThreeProgressChangedValue,
                        qOneFourProgressChangedValue,
                        qOneFiveProgressChangedValue,
                        qOneSixProgressChangedValue,
                        qOneSevenProgressChangedValue,
                        qOneEightProgressChangedValue,
                        qTwoOneProgressChangedValue,
                        qTwoTwoProgressChangedValue,
                        qTwoThreeProgressChangedValue,
                        qTwoFourProgressChangedValue,
                        qTwoFiveProgressChangedValue,
                        qTwoSixProgressChangedValue,
                        qTwoSevenProgressChangedValue,
                        qTwoEightProgressChangedValue
                };
                String submitString = "QID,Answer\n";
                for(int i =0; i<QIDs.length;i++){
                    submitString = QIDs[i]+","+answers[i]+"\n";
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
                    fOut.close();

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
