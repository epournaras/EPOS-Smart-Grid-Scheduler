package com.example.scheduler;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.example.schedulecreationlibrary.Action;
import com.example.schedulecreationlibrary.Schedule;
import com.example.scheduler.Adapter.MyAdapter;
import com.example.scheduler.BackgroundTasks.createSchedulesTask;
import com.example.scheduler.Divider.DividerItemDecoration;
import com.example.scheduler.Interface.MyDialogCloseListener;
import com.example.scheduler.Notifications.NotificationService;
import com.example.scheduler.fragment.addRemoveAppliance;
import com.example.scheduler.fragment.betterPlanPopUpFragment;
import com.example.scheduler.fragment.createTomorrowsPlans;
import com.example.scheduler.fragment.editApplianceSettings;
import com.example.scheduler.fragment.fragment_create;
import com.example.scheduler.fragment.surveyFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyDialogCloseListener {
    Animation FabOpen, FabClose, FabRClockwise,FabRAntiClockwise;
    boolean isOpen = false;
    public static String display;
    public String list;
    private String fullTime;
    public String date;
    public String tomorrowsDate;
    private Fragment fragment = null;
    private String batteryLevels = "";
    private int numberOfActions = 8;
    public AsyncTask motherTask;
    public boolean tasksStop = false;
    public NavigationView nav;
    public MainActivity me = this;
    private String[] applianceNames = {
            "Hob",
            "Oven",
            "TumbleDryer",
            "WashingMachine",
            "Computer",
            "Kettle",
            "DishWasher",
            "Shower"
    };
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryLevels+=level+"\n";
        }
    };
    @SuppressLint({"NewAPI", "NewApi"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View layoutView = (View) findViewById(android.R.id.content);
        final Context context = this;

        fullTime = " ";
        date = " ";
        tomorrowsDate = " ";
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final LinearLayout textEdit = (LinearLayout) findViewById(R.id.fabEditAppliancesTextLayout);
        final LinearLayout textAddRemove = (LinearLayout) findViewById(R.id.fabAddRemoveAppliancesTextLayout);
        final LinearLayout textCreatePlan = (LinearLayout) findViewById(R.id.fabCreateTomorrowsPlanFabsTextLayout);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRAntiClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_anticlockwise);
        FabRClockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);

        final String PREFS_NAME = "MyPrefsFile";


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int MY_PERMISSIONS_REQUEST_STORAGE = 200;
        settings.edit().putBoolean("putMin", false).commit();
        settings.edit().putBoolean("putMax", false).commit();


        if(settings.getBoolean("my_first_time", true)){
            String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            int version = Build.VERSION.SDK_INT;
            if(version>=23){
                int permsRequestCode = MY_PERMISSIONS_REQUEST_STORAGE;
                requestPermissions(perms, permsRequestCode);
            }

            String appliancesEnabledData = "";
            String houseData = "House,Occupancy,Construction Year,Appliances Owned,Type,Size,Approximate Construction Year\n" +
                    "1,2,1975-1980,35,Detached,4 bed,1970 - 1979\n" +
                    "2,4,-,15,Semi-detached,3 bed,-\n" +
                    "3,2,1988,27,Detached,3 bed,1980 - 1989\n" +
                    "4,2,1850-1899,33,Detached,4 bed,Pre 1900s\n" +
                    "5,4,1878,44,Mid-terrace,4 bed,Pre 1900s\n" +
                    "6,2,2005,49,Detached,4 bed,2000 - 2009\n" +
                    "7,4,1965-1974,25,Detached,3 bed,1960 - 1969\n" +
                    "8,2,1966,35,Detached,2 bed,1960 - 1969\n" +
                    "9,2,1919-1944,24,Detached,3 bed,1920 - 1929\n" +
                    "10,4,1919-1944,31,Detached,3 bed,1920 - 1929\n" +
                    "11,1,1945-1964,25,Detached,3 bed,1950 - 1959\n" +
                    "12,3,1991-1995,26,Detached,3 bed,1990 - 1999\n" +
                    "13,4,post 2002,28,Detached,4 bed,2000 - 2010\n" +
                    "15,1,1965-1974,19,Semi-detached,3 bed,1960 - 1969\n" +
                    "16,6,1981-1990,48,Detached,5 bed,1980 - 1989\n" +
                    "17,3,mid 60s,22,Detached,3 bed,1960 - 1969\n" +
                    "18,2,1965-1974,34,Detached,3 bed,1960 - 1969\n" +
                    "19,4,1945-1964,26,Semi-detached,3 bed,1950 - 1959\n" +
                    "20,2,1965-1974,39,Detached,3 bed,1960 - 1969\n" +
                    "21,4,1981-1990,23,Detached,3 bed,1980 - 1989";
            //the app is being launched for first time, do something
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String lastSendPressDayDateFile = "lastSendPressDayDate.txt";
            display = null;
            fullTime= simpleDateFormat.format(new Date());
            date = fullTime.substring(0,10);
            fullTime = simpleDateFormat.format(new Date(((new Date()).getTime() + 86400000)));
            tomorrowsDate = fullTime.substring(0,10);
            String fileName = "PastSchedules.txt";
            String tomorrowSchedule = "TomorrowSchedule.txt";
            String listAsItWas = "ActionInputList.txt";
            String todayDate = "Date.txt";
            String tomorrowDate = "TomorrowsDate.txt";
            String password = "Password.txt";
            String passwordMain = "PasswordMain.txt";
            String houseDataName = "houseData.txt";
            String chosenPlanFile = "chosenPlan.txt";
            String wattageFile = "wattagesFile.txt";
            String timesToNotifyFile = "timesToNotify.txt";
            String appliancesEnabledDataFile = "appliancesEnabledDataFile.txt";
            String countFile = "count.txt";
            String applianceNamesFile = "applianceNames.txt";
            String suggestedPlanFile = "suggestedPlan.txt";
            String todayScheduleFile = "TodaySchedule.txt";
            String surveyResultsFiles = "surveyResults.txt";
            String surveyProgressFile = "surveyProgress.txt";
            String surveyCompleteFile = "surveyComplete.txt";
            try{
                FileOutputStream fos = this.openFileOutput(applianceNamesFile,MODE_APPEND);
                for(int i = 0; i<applianceNames.length;i++){
                    String submit;
                    if(i<applianceNames.length-1){
                        submit = applianceNames[i]+",";
                        appliancesEnabledData+=applianceNames[i]+","+"false"+"\n";
                    }else{
                        submit = applianceNames[i];
                        appliancesEnabledData+=applianceNames[i]+","+"false";
                    }
                    fos.write(submit.getBytes());
                }
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fos = this.openFileOutput(surveyProgressFile,MODE_PRIVATE);
                fos.write("1".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fos = this.openFileOutput(surveyCompleteFile,MODE_PRIVATE);
                fos.write("false".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fos = this.openFileOutput(surveyResultsFiles,MODE_PRIVATE);
                fos.write(" ".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            try{
                FileOutputStream fos = this.openFileOutput(suggestedPlanFile,MODE_PRIVATE);
                fos.write(" ".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            try{
                FileOutputStream fos = this.openFileOutput("notifyPress.txt",MODE_PRIVATE);
                fos.write("true".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fos = this.openFileOutput(countFile,MODE_PRIVATE);
                fos.write("0".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            String TimingsFile = "timings.txt";
            try{
                FileOutputStream fos = this.openFileOutput(TimingsFile,MODE_PRIVATE);
                fos.write("Make,Sort,Display,Store\n".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fos = this.openFileOutput(appliancesEnabledDataFile,MODE_PRIVATE);
                fos.write(appliancesEnabledData.getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            try{
                FileOutputStream fos = this.openFileOutput(tomorrowSchedule,MODE_PRIVATE);
                fos.write(" ".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            try{
                FileOutputStream fos = this.openFileOutput(todayScheduleFile,MODE_PRIVATE);
                fos.write(" ".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            // first time task
            try{
                FileOutputStream fos = this.openFileOutput(timesToNotifyFile,MODE_PRIVATE);
                fos.write("--:--".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fos = this.openFileOutput(wattageFile,MODE_PRIVATE);
                fos.write("Default,75,1000,3000,1350,1800,9000,2500,700".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fos = this.openFileOutput(chosenPlanFile,MODE_PRIVATE);
                fos.write(" ".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fos = this.openFileOutput(lastSendPressDayDateFile,MODE_PRIVATE);
                fos.write("0".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fosPSched =this.openFileOutput(fileName, MODE_PRIVATE);
                fosPSched.write("0".getBytes());
                fosPSched.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fosPSched =this.openFileOutput(houseDataName, MODE_PRIVATE);
                fosPSched.write(houseData.getBytes());
                fosPSched.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            try{
                FileOutputStream fosPass = this.openFileOutput(password, MODE_PRIVATE);
                fosPass.write("Password1234".getBytes());
                fosPass.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fosPassMain = this.openFileOutput(passwordMain, MODE_PRIVATE);
                fosPassMain.write("Password5678".getBytes());
                fosPassMain.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fosMin = this.openFileOutput("tempMin.txt", MODE_PRIVATE);
                fosMin.write("00:00".getBytes());
                fosMin.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fosMax = this.openFileOutput("tempMax.txt", MODE_PRIVATE);
                fosMax.write("23:59".getBytes());
                fosMax.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fosDate = this.openFileOutput(todayDate, MODE_PRIVATE);
                fosDate.write(date.getBytes());
                fosDate.close();
                fosDate = this.openFileOutput(tomorrowDate, MODE_PRIVATE);
                fosDate.write(tomorrowsDate.getBytes());
                fosDate.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                FileOutputStream fosInitialiseList = this.openFileOutput(listAsItWas, MODE_PRIVATE);
                fosInitialiseList.write("".getBytes());
                fosInitialiseList.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            FragmentManager fragManager = getFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
            surveyFragment newFragment = new surveyFragment();
            newFragment.show(fragmentTransaction,"survey");
            MyDialogCloseListener closeListener = new MyDialogCloseListener() {
                @Override
                public void handleDialogClose(DialogInterface dialog) {
                    String progress = "0";
                    try{
                        FileInputStream fis = me.openFileInput("surveyProgress.txt");
                        StringBuilder builder = new StringBuilder();
                        int ch;
                        while((ch = fis.read()) != -1){
                            builder.append((char)ch);
                        }
                        progress = builder.toString();
                        fis.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    int nextScreen = Integer.parseInt(progress);
                    if(nextScreen!=0){
                        me.callSurvey();
                    }else{
                        try{
                            FileOutputStream fos = me.openFileOutput("surveyComplete.txt",Context.MODE_PRIVATE);
                            fos.write("true".getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            newFragment.DismissListner(closeListener);
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }else{
            String surveyShow = "";
            try{
                FileInputStream fis = openFileInput("surveyComplete.txt");
                int chr;
                StringBuilder builder = new StringBuilder();
                while ((chr = fis.read()) != -1) {
                    builder.append((char) chr);
                }
                surveyShow = builder.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            if(surveyShow.equals("false")){
                callSurvey();
            }
        }

        final FloatingActionButton fabEdit = (FloatingActionButton) findViewById(R.id.fabEditAppliances);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                DialogFragment newFragment = new editApplianceSettings();
                newFragment.show(fragmentTransaction,"editApplianceSettings");
            }
        });

        final FloatingActionButton fabAddRemoveAppliances = (FloatingActionButton) findViewById(R.id.fabAddRemoveAppliances);
        fabAddRemoveAppliances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                DialogFragment newFragment = new addRemoveAppliance();
                newFragment.show(fragmentTransaction, "addRemoveAppliance");
            }
        });

        final FloatingActionButton fabCreateTomorrowsPlan = (FloatingActionButton) findViewById(R.id.fabCreateTomorrowsPlan);
        fabCreateTomorrowsPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragManager = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                DialogFragment newFragment = new fragment_create();
                newFragment.show(fragmentTransaction, "fragment_create");
            }
        });
        fabEdit.setClickable(false);
        fabCreateTomorrowsPlan.setClickable(false);
        fabAddRemoveAppliances.setClickable(false);

        final FloatingActionButton fabRevealFabs = (FloatingActionButton) findViewById(R.id.fabRevealFabs);
        fabRevealFabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    fabCreateTomorrowsPlan.startAnimation(FabClose);
                    fabRevealFabs.startAnimation(FabRAntiClockwise);
                    fabEdit.startAnimation(FabClose);
                    fabAddRemoveAppliances.startAnimation(FabClose);

                    textEdit.startAnimation(FabClose);
                    textAddRemove.startAnimation(FabClose);
                    textCreatePlan.startAnimation(FabClose);
                    fabEdit.setClickable(false);
                    fabCreateTomorrowsPlan.setClickable(false);
                    fabAddRemoveAppliances.setClickable(false);
                    isOpen = false;
                }else{
                    fabCreateTomorrowsPlan.startAnimation(FabOpen);
                    fabRevealFabs.startAnimation(FabRClockwise);
                    fabEdit.startAnimation(FabOpen);
                    fabAddRemoveAppliances.startAnimation(FabOpen);

                    textEdit.startAnimation(FabOpen);
                    textAddRemove.startAnimation(FabOpen);
                    textCreatePlan.startAnimation(FabOpen);
                    fabEdit.setClickable(true);
                    fabCreateTomorrowsPlan.setClickable(true);
                    fabAddRemoveAppliances.setClickable(true);
                    isOpen = true;
                }
            }
        });

        checkForTodaysScheduleTask(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        nav = (NavigationView)findViewById(R.id.nav_view);
        setUpNavigationDrawer();
        ensureDisplay(this, layoutView);

    }
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int minutes = prefs.getInt("interval",1); //user defined how often they want to be notified.

        AlarmManager am =(AlarmManager) getSystemService(ALARM_SERVICE);

        final View layoutView = (View) findViewById(android.R.id.content);
        ensureDisplay(this, layoutView);
        nav = (NavigationView)findViewById(R.id.nav_view);
        setUpNavigationDrawer();
        checkForTodaysScheduleTask(this);
        Intent i = new Intent(this, NotificationService.class);
        PendingIntent pi =PendingIntent.getService(this, 0, i, 0);
        am.cancel(pi);

        // by my own convention, minutes <= 0 means notifications are disabled
        if(minutes > 0){
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + minutes*60*1000,minutes*60*1000, pi);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUpNavigationDrawer(){
        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        if(display==null){
            try {
                FileInputStream fis = this.openFileInput("TomorrowSchedule.txt");
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
                Context context = this;
                Toast toast = Toast.makeText(context, toastString, durationOfToast);
                toast.show();
                e.printStackTrace();
            }
        }
        if(display!=null){
            boolean doNotDisplay = false;
            String[] list = display.split("[\\r\\n]+");
            String[][] plans = new String [list.length][];
            String[] myDataSet = new String[list.length];
            for(int i =0; i<list.length;i++){
                if(list.length== 1&&display.equals(" ")){
                    doNotDisplay = true;
                }else{
                    myDataSet[i] = "";
                    plans[i] = list[i].split(",");
                    for(int j = 0; j<plans[i].length;j++){
                        if(j==plans[i].length-1){
                            myDataSet[i]+=plans[i][j];
                        }else{
                            myDataSet[i]+=plans[i][j]+"\n";
                        }
                    }
                }
            }
            if(!doNotDisplay){
                final RecyclerView.Adapter mAdapter = new MyAdapter(myDataSet, this);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setHasFixedSize(true);
            }else{

            }
        }else{

        }
    }


    public void setDate(MainActivity mainActivity){
        String todayDate = "",tomorrowDate = "";
        FileInputStream fisDate;
        StringBuilder builder;
        int ch;
        try{
            fisDate = mainActivity.openFileInput("Date.txt");
            builder = new StringBuilder();
            while ((ch = fisDate.read()) != -1) {
                builder.append((char) ch);
            }
            fisDate.close();
            todayDate = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            fisDate = mainActivity.openFileInput("TomorrowsDate.txt");
            builder = new StringBuilder();
            while ((ch = fisDate.read()) != -1) {
                builder.append((char) ch);
            }
            fisDate.close();
            tomorrowDate = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateTest = simpleDateFormat.format(new Date());
        dateTest = dateTest.substring(0,10);
        if(todayDate.equals(dateTest)){

        }else{
            System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            System.out.print("SETDATE TASK BEING CALLED\n");
            System.out.print("~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            todayDate = dateTest;
            String fullTime = simpleDateFormat.format(new Date(((new Date()).getTime() + 86400000)));
            tomorrowDate = fullTime.substring(0,10);
            FileOutputStream fosDate;
            try{
                fosDate =mainActivity.openFileOutput("Date.txt",Context.MODE_PRIVATE);
                fosDate.write(todayDate.getBytes());
                fosDate.close();

                fosDate = mainActivity.openFileOutput("TomorrowsDate.txt",Context.MODE_PRIVATE);
                fosDate.write(tomorrowDate.getBytes());
                fosDate.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            String todaySch = "";
            try {
                FileInputStream fis = mainActivity.openFileInput("chosenPlan.txt");
                builder = new StringBuilder();
                while((ch = fis.read()) != -1){
                    builder.append((char)ch);
                }
                fis.close();

                todaySch = builder.toString();
                FileOutputStream fos = mainActivity.openFileOutput("TodaySchedule.txt", Context.MODE_PRIVATE);
                fos.write(todaySch.getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            String[] planParts = todaySch.split("\n");
            char[][] planPartsCharArray = new char[planParts.length][];
            for(int i = 0; i<planParts.length;i++){
                planPartsCharArray[i]=planParts[i].toCharArray();
            }
            char[][] times = new char[planParts.length][11];
            for(int i=0; i<planPartsCharArray.length;i++){
                boolean time = false;
                int index = 0;
                for(int j=0; j<planPartsCharArray[i].length;j++){
                    if((planPartsCharArray[i][j] == '0')||(planPartsCharArray[i][j] == '1')||(planPartsCharArray[i][j] == '2')){
                        time = true;
                    }
                    if(time ==true){
                        if(index<5){
                            times[i][index] = planPartsCharArray[i][j];
                            index++;
                        }
                    }
                }
            }
            String[] timeStrings = new String[times.length];
            for(int i = 0; i<timeStrings.length;i++){
                timeStrings[i] = new String(""+times[i][0]+times[i][1]+times[i][2]+times[i][3]+times[i][4]);
                if(i==0){
                    try{
                        FileOutputStream fos = mainActivity.openFileOutput("timesToNotify.txt", Context.MODE_PRIVATE);
                        String submit = timeStrings[i]+",";
                        fos.write(submit.getBytes());
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    if(i!=timeStrings.length-1){
                        try{
                            FileOutputStream fos = mainActivity.openFileOutput("timesToNotify.txt", Context.MODE_APPEND);
                            String submit = timeStrings[i]+",";
                            fos.write(submit.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        try{
                            FileOutputStream fos = mainActivity.openFileOutput("timesToNotify.txt", Context.MODE_APPEND);
                            String submit = timeStrings[i]+",";
                            fos.write(submit.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                }

            }
            final String PREFS_NAME = "MyPrefsFile";
            SharedPreferences settings = mainActivity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            settings.edit().putBoolean("defaultBool",true).commit();
            String tomorrowsSchedule = "TomorrowSchedule.txt";
            FileOutputStream fos;
            try {
                mainActivity.setDisplay(" ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fos = mainActivity.openFileOutput("chosenPlan.txt", Context.MODE_PRIVATE);
                fos.write(" ".getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            final View layoutView = (View) findViewById(android.R.id.content);
            ensureDisplay(this, layoutView);
            nav = (NavigationView)findViewById(R.id.nav_view);
            display = null;
            setUpNavigationDrawer();
        }
    }
    public void setDisplay(String s){
        try{
            FileOutputStream fos = this.openFileOutput("TomorrowSchedule.txt", this.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        this.display = s;
    }
    public void callBackgroundTasks(Action[] array, int progressChangedValue){
        tasksStop = false;
        Schedule lists = new Schedule(array);
        Schedule[] pass = new Schedule[]{lists};
        motherTask = new createSchedulesTask(MainActivity.this,progressChangedValue, this).execute(pass);
    }

    public void cancelBackgroundTasks(){
        if(motherTask!=null){
            tasksStop = true;
            motherTask.cancel(true);
        }
    }

    public boolean checkTasksStop(){
        return tasksStop;
    }
    public String getDisplay(){
        return this.display;
    }

    public void setList(String a){
        try{
            FileOutputStream fos = this.openFileOutput("ActionInputList.txt", this.MODE_PRIVATE);
            fos.write(a.getBytes());
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        this.list = a;
    }

    public String sendActionList(){
        try {
            FileInputStream fis = this.openFileInput("ActionInputList.txt");
            int ch;
            StringBuilder builder = new StringBuilder();
            while ((ch = fis.read()) != -1) {
                builder.append((char) ch);
            }
            fis.close();
            this.list = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.list;
    }

    public void removeItem(String removeAction){
        ArrayList<Action> actionList = new ArrayList<Action>();
        ArrayList<String> actionStrings = new ArrayList<String>();
        String[] testArray = list.split("[\\r\\n]+");
        for(int i = 0; i<testArray.length;i++){
            String[] temp = testArray[i].split(",");
            boolean parallel;
            switch(temp[0]){
                case "Hob":
                    parallel = false;
                    break;
                case "Oven":
                    parallel = true;
                    break;
                case "TumbleDryer":
                    parallel = true;
                    break;
                case "WashingMachine":
                    parallel = true;
                    break;
                case "Computer":
                    parallel = false;
                    break;
                case "Kettle":
                    parallel = true;
                    break;
                case "DishWasher":
                    parallel = true;
                    break;
                case "Shower":
                    parallel = false;
                    break;
                default:
                    parallel = false;
                    break;
            }
            actionList.add(new Action(temp[0],temp[1],temp[2],temp[3],temp[4], parallel));
            actionStrings.add(temp[0]+"\t"+temp[1]+"-"+temp[2]+"\t"+temp[4]);
        }
        String[] currentActions = new String[actionStrings.size()];
        actionStrings.toArray(currentActions);
        int indexOfRemoval = -1;
        for(int i = 0; i< currentActions.length;i++){
            if(currentActions[i].equals(removeAction)){
                indexOfRemoval = i;
                currentActions[i] = null;
                int durationOfToast = Toast.LENGTH_SHORT;
                String toastString = removeAction+" removed.";
                Toast toast = Toast.makeText(this, toastString, durationOfToast);
                toast.show();
            }
        }

        if(indexOfRemoval>=0&&indexOfRemoval<actionList.size()){
            actionList.remove(indexOfRemoval);
        }

        actionList.removeAll(Collections.singleton(null));
        actionList.trimToSize();
        String listCSV = "";
        for(Action a: actionList){
            listCSV+=a.name+","+a.getTimeString(a.windowStart)+","+a.getTimeString(a.windowEnd)+","+a.getTimeString(a.duration)+","+a.getTimeString(a.optimalTime)+"\n";
        }
        setList(listCSV);
    }

    public void removeItemWithName(String name){
        if(list!=null){
            ArrayList<Action> actionList = new ArrayList<Action>();
            ArrayList<String> actionStrings = new ArrayList<String>();
            String[] testArray = list.split("[\\r\\n]+");
            for(int i = 0; i<testArray.length;i++){
                String[] temp = testArray[i].split(",");
                if(temp.length==5){
                    if(temp[0].equals(name)){

                    }else{
                        boolean parallel;
                        switch(temp[0]){
                            case "Hob":
                                parallel = false;
                                break;
                            case "Oven":
                                parallel = true;
                                break;
                            case "TumbleDryer":
                                parallel = true;
                                break;
                            case "WashingMachine":
                                parallel = true;
                                break;
                            case "Computer":
                                parallel = false;
                                break;
                            case "Kettle":
                                parallel = true;
                                break;
                            case "DishWasher":
                                parallel = true;
                                break;
                            case "Shower":
                                parallel = false;
                                break;
                            default:
                                parallel = false;
                                break;
                        }
                        actionList.add(new Action(temp[0],temp[1],temp[2],temp[3],temp[4], parallel));
                        actionStrings.add(temp[0]+"\t"+temp[1]+"-"+temp[2]+"\t"+temp[4]);
                    }
                }else{
                    break;
                }

            }
            actionList.removeAll(Collections.singleton(null));
            actionList.trimToSize();
            String listCSV = "";
            for(Action a: actionList){
                listCSV+=a.name+","+a.getTimeString(a.windowStart)+","+a.getTimeString(a.windowEnd)+","+a.getTimeString(a.duration)+","+a.getTimeString(a.optimalTime)+"\n";
            }
            setList(listCSV);
        }
    }

    public void checkForTodaysScheduleTask(MainActivity ma){
        final MainActivity m = ma;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run(){

                Intent intent = new Intent(m, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(m, (int) System.currentTimeMillis(), intent, 0);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String time = simpleDateFormat.format(new Date());
                char[] timeChars = time.toCharArray();
                String numeralS =""+timeChars[12];
                String decimalS=""+timeChars[11];
                int numeral = Integer.parseInt(numeralS);
                int decimal = Integer.parseInt(decimalS);
                int hour = (decimal*10)+numeral;

                String dayDate="";
                try{
                    FileInputStream fis = m.openFileInput("lastSendPressDayDate.txt");
                    StringBuilder builder = new StringBuilder();
                    int chr;
                    while ((chr = fis.read()) != -1) {
                        builder.append((char) chr);
                    }
                    fis.close();
                    dayDate = builder.toString();
                }catch(Exception e){
                    e.printStackTrace();
                }
                String notifyPress = "";
                try{
                    FileInputStream fis = m.openFileInput("notifyPress.txt");
                    StringBuilder builder = new StringBuilder();
                    int chr;
                    while ((chr = fis.read()) != -1) {
                        builder.append((char) chr);
                    }
                    fis.close();
                    notifyPress = builder.toString();
                }catch(Exception e){
                    e.printStackTrace();
                }
                String date = time.substring(0,10);
                m.setTitle(date+"'s Schedule");
                setDate(m);
                if(!dayDate.equals(date)){
                    if(hour >= 21){
                        if(notifyPress.equals("true")){
                            Notification noti = new Notification.Builder(m)
                                    .setContentTitle("No new plans created for tomorrow!")
                                    .setContentText("Open Scheduler now to create tomorrow's plans")
                                    .setContentIntent(pIntent)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .build();
                            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            // hide the notification after its selected
                            noti.flags |= Notification.FLAG_AUTO_CANCEL;

                            notificationManager.notify(0, noti);
                            if(dayDate.equals(date)){
                                notificationManager.cancelAll();
                            }
                        }
                        try{
                            FileOutputStream fos = openFileOutput("notifyPress.txt",MODE_PRIVATE);
                            fos.write("false".getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        try{
                            FileOutputStream fos = openFileOutput("notifyPress.txt",MODE_PRIVATE);
                            fos.write("true".getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                handler.postDelayed(this, 100);
            }
        },100);
    }

    public void receiveBetterPlan(String a){
        String plans = "";
        try {
            FileInputStream fis = this.openFileInput("TomorrowSchedule.txt");
            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fis.read()) != -1){
                builder.append((char)ch);
            }
            fis.close();
            plans = builder.toString();
        }catch(Exception e){
            String toastString = "Couldnt get Tomorrow's schedule.";
            int durationOfToast = Toast.LENGTH_SHORT;
            Context context = this;
            Toast toast = Toast.makeText(context, toastString, durationOfToast);
            toast.show();
            e.printStackTrace();
        }
        String[] list = plans.split("[\\r\\n]+");
        for(int i  = 0; i<list.length;i++){
            if(list[i].equals(a)){
                try{
                    FileOutputStream fos = this.openFileOutput("suggestedPlan.txt",this.MODE_PRIVATE);
                    fos.write(a.getBytes());
                    fos.close();
                    FragmentManager fragManager = getFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
                    DialogFragment newFragment = new betterPlanPopUpFragment();
                    newFragment.show(fragmentTransaction, "betterPlanPopUp");
                }catch(Exception e){
                    e.printStackTrace();
                }

            }else{

            }
        }


    }
    public NavigationView getNav(){
        return nav;
    }

    public void ensureDisplay(MainActivity ma, View v){
            final MainActivity m = ma;
            final View layoutView = v;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    String todayDisplay = " ";
                    try {
                        FileInputStream fis = openFileInput("TodaySchedule.txt");
                        StringBuilder builder = new StringBuilder();
                        int ch;
                        while((ch = fis.read()) != -1){
                            builder.append((char)ch);
                        }
                        fis.close();
                        todayDisplay = builder.toString();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    TextView textView = (TextView)layoutView.findViewById(R.id.text);
                    textView.setText(todayDisplay);
                }
            },100);
    }

    public void callSurvey(){
        FragmentManager fragManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragManager.beginTransaction();
        surveyFragment newFragment = new surveyFragment();
        newFragment.show(fragmentTransaction,"survey");
        MyDialogCloseListener closeListener = new MyDialogCloseListener() {
            @Override
            public void handleDialogClose(DialogInterface dialog) {
                String progress = "0";
                try{
                    FileInputStream fis = me.openFileInput("surveyProgress.txt");
                    StringBuilder builder = new StringBuilder();
                    int ch;
                    while((ch = fis.read()) != -1){
                        builder.append((char)ch);
                    }
                    progress = builder.toString();
                    fis.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                int nextScreen = Integer.parseInt(progress);
                System.out.print(nextScreen+"\n");
                if(nextScreen!=0){
                    me.callSurvey();
                }else{
                    try{
                        FileOutputStream fos = me.openFileOutput("surveyComplete.txt",Context.MODE_PRIVATE);
                        fos.write("true".getBytes());
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        newFragment.DismissListner(closeListener);
    }
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        String progress = "0";
        try{
            FileInputStream fis = me.openFileInput("surveyProgress.txt");
            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fis.read()) != -1){
                builder.append((char)ch);
            }
            progress = builder.toString();
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        int nextScreen = Integer.parseInt(progress);
        System.out.print(nextScreen+"\n");
        if(nextScreen!=0){
            me.callSurvey();
        }else{
            try{
                FileOutputStream fos = me.openFileOutput("surveyComplete.txt",Context.MODE_PRIVATE);
                fos.write("true".getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
