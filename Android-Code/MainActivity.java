package com.example.application;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.application.adapter.SlidingMenuAdapter;
import com.example.application.fragment.Fragment1;
import com.example.application.fragment.Fragment2;
import com.example.application.fragment.Fragment3;
import com.example.application.fragment.Fragment4;
import com.example.application.fragment.changeDateTask;
import com.example.application.fragment.createSchedules;
import com.example.application.model.itemSlideMenu;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.example.schedulelibrary.Action;
import com.example.schedulelibrary.Schedule;

/**
 * Created by warrens on 23.01.17.
 */

public class MainActivity extends ActionBarActivity{

    private List<itemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static String display;
    public String list;
    private String fullTime;
    public String date;
    public String tomorrowsDate;
    private Fragment fragment = null;
    private String batteryLevels = "";
    private int numberOfActions = 8;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryLevels+=level+"\n";
        }
    };
    @SuppressLint({"NewAPI", "NewApi"})
    protected void onCreate(Bundle savedInstanceState) {
        int MY_PERMISSIONS_REQUEST_STORAGE = 200;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        fullTime = " ";
        date = " ";
        tomorrowsDate = " ";
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();
        listSliding.add(new itemSlideMenu(R.mipmap.ic_launcher, "Set Tomorrow's Schedule"));
        listSliding.add(new itemSlideMenu(R.mipmap.ic_launcher, "Tomorrow's Schedule"));
        listSliding.add(new itemSlideMenu(R.mipmap.ic_launcher, "Today's Schedule"));
        listSliding.add(new itemSlideMenu(R.mipmap.ic_launcher,"Output Survey"));
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        settings.edit().putBoolean("putMin", false);
        settings.edit().putBoolean("putMax", false);
        if(settings.getBoolean("my_first_time", true)){
            String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            int version = Build.VERSION.SDK_INT;
            if(version>=23){
                int permsRequestCode = MY_PERMISSIONS_REQUEST_STORAGE;
                requestPermissions(perms, permsRequestCode);
            }

            //the app is being launched for first time, do something
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            fullTime= simpleDateFormat.format(new Date());
            date = fullTime.substring(0,10);
            fullTime = simpleDateFormat.format(new Date(((new Date()).getTime() + 86400000)));
            tomorrowsDate = fullTime.substring(0,10);
            String fileName = "PastSchedules.txt";
            String todaySchedule = "TodaySchedule.txt";
            String tomorrowSchedule = "TomorrowSchedule.txt";
            String listAsItWas = "ActionInputList.txt";
            String boxText = "BoxText.txt";
            String todayDate = "Date.txt";
            String tomorrowDate = "TomorrowsDate.txt";
            String password = "Password.txt";
            String passwordMain = "PasswordMain.txt";

            File tomorrowFile = new File(this.getFilesDir(), tomorrowSchedule);

            // first time task
            try{
                FileOutputStream fosPSched =this.openFileOutput(fileName, MODE_PRIVATE);
                fosPSched.write("0".getBytes());
                fosPSched.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                if(!tomorrowFile.exists()){
                    tomorrowFile.createNewFile();
                }
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


            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }else{
            FileInputStream fisDate;
            int ch;
            StringBuilder builder;
            try{
                fisDate = this.openFileInput("Date.txt");
                builder = new StringBuilder();
                while ((ch = fisDate.read()) != -1) {
                    builder.append((char) ch);
                }
                fisDate.close();
                date = builder.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                fisDate = this.openFileInput("TomorrowsDate.txt");
                builder = new StringBuilder();
                while ((ch = fisDate.read()) != -1) {
                    builder.append((char) ch);
                }
                fisDate.close();
                tomorrowsDate = builder.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            String dateTest = simpleDateFormat.format(new Date());
            dateTest = dateTest.substring(0,10);
            if(!(date.equals(dateTest))){// change so it only examines the day and nothing else
                try {
                    FileInputStream fis = this.openFileInput("TomorrowSchedule.txt");
                    builder = new StringBuilder();
                    while((ch = fis.read()) != -1){
                        builder.append((char)ch);
                    }
                    fis.close();

                    String todaySch = builder.toString();
                    FileOutputStream fos = this.openFileOutput("TodaySchedule.txt", this.MODE_PRIVATE);
                    fos.write(todaySch.getBytes());
                    fos.close();
                }catch(Exception e){
                    String toastString = "Internal error";
                    int durationOfToast = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, toastString, durationOfToast);
                    toast.show();
                    e.printStackTrace();
                }
                date = dateTest;
                fullTime = simpleDateFormat.format(new Date(((new Date()).getTime() + 86400000)));
                tomorrowsDate = fullTime.substring(0,10);
                try{
                    FileOutputStream fosDate = this.openFileOutput("Date.txt", MODE_PRIVATE);
                    fosDate.write(date.getBytes());
                    fosDate.close();

                    fosDate= this.openFileOutput("TomorrowsDate.txt", MODE_PRIVATE);
                    fosDate.write(tomorrowsDate.getBytes());
                    fosDate.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        chngDateTask(this);
        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(listSliding.get(0).getTitle());

        listViewSliding.setItemChecked(0, true);

        drawerLayout.closeDrawer(listViewSliding);

        replaceFragment(0);

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTitle(listSliding.get(position).getTitle());
                listViewSliding.setItemChecked(position, true);
                replaceFragment(position);

                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void replaceFragment(int pos) {

        String title = "";
        switch(pos) {
            case 0:
                fragment = new Fragment1();
                title = "Set "+tomorrowsDate+"'s Schedule";
                break;
            case 1:
                fragment = new Fragment2();
                Bundle b = new Bundle();
                if(display!=null){
                    b.putString("display",display);
                }else{
                    b.putString("display", "Nothing to show yet!");
                }
                fragment.setArguments(b);
                title = tomorrowsDate+"'s Schedule";
                break;
            case 2:
                fragment = new Fragment3();
                Bundle c = new Bundle();
                c.putString("displayToday","No Schedule for Today");
                fragment.setArguments(c);
                title = date+"'s Schedule";
                break;
            case 3:
                fragment = new Fragment4();
                title = "Output Survey";
                break;
            default:
                fragment = new Fragment1();
                title = "Set "+tomorrowsDate+"'s Schedule";
                break;
        }
        if(null!=fragment){
            FragmentManager fragmentManager = getFragmentManager();
            getSupportActionBar().setTitle(title);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void setDate(String da, String tda){
        FileInputStream fisDate;
        StringBuilder builder;
        int ch;
        String dateTest = "";
        try{
            fisDate = this.openFileInput("Date.txt");
            builder = new StringBuilder();
            while ((ch = fisDate.read()) != -1) {
                builder.append((char) ch);
            }
            fisDate.close();
            dateTest = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        if(dateTest.equals(da)){

        }else{
            this.date = da;
            this.tomorrowsDate = tda;
            FileOutputStream fosDate;
            try{
                fosDate =this.openFileOutput("Date.txt",MODE_PRIVATE);
                fosDate.write(da.getBytes());
                fosDate.close();

                fosDate = this.openFileOutput("TomorrowsDate.txt",MODE_PRIVATE);
                fosDate.write(tda.getBytes());
                fosDate.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            try {
                FileInputStream fis = this.openFileInput("TomorrowSchedule.txt");
                builder = new StringBuilder();
                while((ch = fis.read()) != -1){
                    builder.append((char)ch);
                }
                fis.close();

                String todaySch = builder.toString();
                FileOutputStream fos = this.openFileOutput("TodaySchedule.txt", this.MODE_PRIVATE);
                fos.write(todaySch.getBytes());
                fos.close();
            }catch(Exception e){
                e.printStackTrace();
            }
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
        Schedule lists = new Schedule(array);
        Schedule[] pass = new Schedule[]{lists};
        new createSchedules(MainActivity.this,progressChangedValue, this).execute(pass);
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
            actionList.add(new Action(temp[0],temp[1],temp[2],temp[3],temp[4], false));
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

    public String getBatteryLevels(){
        return this.batteryLevels;
    }

    public void setBatteryLevels(String a){
        this.batteryLevels = a;
    }

    public void chngDateTask(MainActivity m){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String s = "";
                new changeDateTask(date, tomorrowsDate, m).execute(s);
                handler.postDelayed(this, 100);
            }
        }, 100);
    }

}
