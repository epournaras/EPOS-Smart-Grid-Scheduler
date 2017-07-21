package com.example.application;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.SystemClock;
import android.preference.PreferenceManager;
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
import com.example.application.fragment.NotificationService;
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
        String frag = getIntent().getStringExtra("fragment");

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        settings.edit().putBoolean("putMin", false).commit();
        settings.edit().putBoolean("putMax", false).commit();
        if(settings.getBoolean("my_first_time", true)){
            String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            int version = Build.VERSION.SDK_INT;
            if(version>=23){
                int permsRequestCode = MY_PERMISSIONS_REQUEST_STORAGE;
                requestPermissions(perms, permsRequestCode);
            }
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
            String houseDataName = "houseData.txt";
            String chosenPlanFile = "chosenPlan.txt";
            String wattageFile = "wattagesFile.txt";
            String timesToNotifyFile = "timesToNotify.txt";
            File tomorrowFile = new File(this.getFilesDir(), tomorrowSchedule);

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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String dateTest = simpleDateFormat.format(new Date());
            dateTest = dateTest.substring(0,10);
            if(!(date.equals(dateTest))){// change so it only examines the day and nothing else
                try {
                    FileInputStream fis = this.openFileInput("chosenPlan.txt");
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
        if(frag!=null){
            if(frag.equals("3")){
                replaceFragment(2);
            }else{
                replaceFragment(0);
            }
        }else{
            replaceFragment(0);
        }

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
        checkForTodaysScheduleTask(this);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    public void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        int minutes = prefs.getInt("interval",1); //user defined how often they want to be notified.

        AlarmManager am =(AlarmManager) getSystemService(ALARM_SERVICE);

        Intent i = new Intent(this, NotificationService.class);
        PendingIntent pi =PendingIntent.getService(this, 0, i, 0);
        am.cancel(pi);

        // by my own convention, minutes <= 0 means notifications are disabled
        if(minutes > 0){
            am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + minutes*60*1000,minutes*60*1000, pi);
        }
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
            String todaySch = "";
            try {
                FileInputStream fis = this.openFileInput("chosenPlan.txt");
                builder = new StringBuilder();
                while((ch = fis.read()) != -1){
                    builder.append((char)ch);
                }
                fis.close();

                todaySch = builder.toString();
                FileOutputStream fos = this.openFileOutput("TodaySchedule.txt", this.MODE_PRIVATE);
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
                        FileOutputStream fos = openFileOutput("timesToNotify.txt", Context.MODE_PRIVATE);
                        String submit = timeStrings[i]+",";
                        fos.write(submit.getBytes());
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    if(i!=timeStrings.length-1){
                        try{
                            FileOutputStream fos = openFileOutput("timesToNotify.txt", Context.MODE_APPEND);
                            String submit = timeStrings[i]+",";
                            System.out.print("\n"+submit+"\n");
                            fos.write(submit.getBytes());
                            fos.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        try{
                            FileOutputStream fos = openFileOutput("timesToNotify.txt", Context.MODE_APPEND);
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
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            settings.edit().putBoolean("defaultBool",true).commit();
            String tomorrowsSchedule = "TomorrowSchedule.txt";
            FileOutputStream fos;
            try {
                setDisplay(" ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fos = openFileOutput("chosenPlan.txt", Context.MODE_PRIVATE);
                fos.write(" ".getBytes());
                fos.close();
            } catch (Exception e) {
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

    public void checkForTodaysScheduleTask(MainActivity m){
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
                String date = ""+timeChars[0]+timeChars[1]+timeChars[2]+timeChars[3]+timeChars[4]+timeChars[5]+timeChars[6]+timeChars[7]+timeChars[8]+timeChars[9];
                if(!dayDate.equals(date)){
                    if(hour >= 21){
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
                }
                handler.postDelayed(this, 100);
            }
        },100);
    }

}
