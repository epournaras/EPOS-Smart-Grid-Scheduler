package com.example.application.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.application.R;

import java.io.FileInputStream;

/**
 * Created by warrens on 13.03.17.
 */

public class Fragment4 extends Fragment {

    public Fragment4() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment4, container, false);
        Context myContext = getActivity();
        DialogFragment newFragment = new passwordFragment();
        FragmentManager fragManager = ((FragmentActivity)myContext).getSupportFragmentManager();
        newFragment.show(fragManager, "passwordFragment");
        return v;
    }
}
