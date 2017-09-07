package com.example.scheduler.fragment;

/**
 * Created by warrens on 07.09.17.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.scheduler.R;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by warrens on 06.09.17.
 */

public class tabsFragment extends DialogFragment {

    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    public int pagesCount;

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View view = inflater.inflate(R.layout.fragment_dialog, null);
        view.setMinimumWidth((int)(displayRectangle.width() * 0.9f));

        // tab slider
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        String[] separate;
        String submit = " ";
        try{
            FileInputStream fis = getActivity().openFileInput("TomorrowSchedule.txt");
            int ch;
            StringBuilder builder = new StringBuilder();
            while((ch=fis.read())!=-1){
                builder.append((char)ch);
            }
            submit = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        separate = submit.split("\n");
        pagesCount = separate.length;
        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager)view.findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(pagesCount-1);
        viewPager.setAdapter(sectionsPagerAdapter);
        Button conf = (Button) view.findViewById(R.id.confirmChoice);
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fis;
                FileOutputStream fos;
                try{
                    fis = getActivity().openFileInput("tempChosenPlan.txt");
                    int ch;
                    StringBuilder builder = new StringBuilder();
                    while((ch=fis.read())!=-1){
                        builder.append((char)ch);
                    }
                    String print = builder.toString();
                    System.out.print(print+"\n");
                    fos = getActivity().openFileOutput("chosenPlan.txt",Context.MODE_PRIVATE);
                    fos.write(builder.toString().getBytes());
                    fis.close();
                    fos.close();
                    dismiss();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public ArrayList<Fragment> pages = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String submit = " ";
            String[] separate;
            try{
                FileInputStream fis = getActivity().openFileInput("TomorrowSchedule.txt");
                int ch;
                StringBuilder builder = new StringBuilder();
                while((ch=fis.read())!=-1){
                    builder.append((char)ch);
                }
                submit = builder.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            separate = submit.split("\n");
            System.out.print(separate+" "+position+"\n");
            submit = separate[position];
            fragment_page page = fragment_page.newInstance(submit);
            pages.add(position,page);
            return pages.get(position);
        }

        @Override
        public int getCount() {
            return pagesCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int title = position+1;
            return "Option "+title;
        }
    }
}

