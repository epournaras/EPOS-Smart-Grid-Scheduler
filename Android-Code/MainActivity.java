package com.example.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
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
import com.example.application.model.itemSlideMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.schedulelibrary.Action;
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
    public ArrayList<Action> list;
    public String fragOneText;
    private String today;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSliding = new ArrayList<>();
        listSliding.add(new itemSlideMenu(R.mipmap.ic_launcher, "Set Tomorrow's Schedule"));
        listSliding.add(new itemSlideMenu(R.mipmap.ic_launcher, "Tomorrow's Schedule"));
        listSliding.add(new itemSlideMenu(R.mipmap.ic_launcher, "Today's Schedule"));
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.contains("my_first_time")) {
            if(settings.getBoolean("my_first_time", true)){
                //the app is being launched for first time, do something
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss");
                today = simpleDateFormat.format(new Date());
                String fileName = "PastSchedules.txt";
                String todaySchedule = "TodaySchedule.txt";
                String tomorrowSchedule = "TomorrowSchedule.txt";
                File file = new File(this.getFilesDir(), fileName);
                File todayFile = new File(this.getFilesDir(), todaySchedule);
                File tomorrowFile = new File(this.getFilesDir(), tomorrowSchedule);
                // first time task

                // record the fact that the app has been started at least once
                settings.edit().putBoolean("my_first_time", false).commit();
            }
        }else{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss");
            String dateTest = simpleDateFormat.format(new Date());
            if(!(today.equals(dateTest))){
                try {
                    FileInputStream fis = openFileInput("TomorrowSchedule.txt");
                    StringBuilder builder = new StringBuilder();
                    int ch;
                    while((ch = fis.read()) != -1){
                        builder.append((char)ch);
                    }
                    String todaySch = builder.toString();
                    FileOutputStream fos = new FileOutputStream("TodaySchedule.txt", false);
                    fos.write(todaySch.getBytes());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
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

    private void replaceFragment(int pos) {
        Fragment fragment = null;
        String title = "";
        switch(pos) {
            case 0:
                fragment = new Fragment1();
                title = "Set Tomorrow's Schedule";

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
                title = "Tomorrow's Schedule";
                break;
            case 2:
                fragment = new Fragment3();
                Bundle c = new Bundle();
                c.putString("displayToday","No Schedule for Today");
                fragment.setArguments(c);
                title = "Today's Schedule";
                break;
            default:
                fragment = new Fragment1();
                title = "Set Tomorrow's Schedule";
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

    public void setDisplay(String s){
        this.display = s;
        String toastString = "Display set";
        int durationOfToast = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, toastString, durationOfToast);
        toast.show();
    }

    public String getDisplay(){
        return this.display;
    }

    public void setList(ArrayList<Action> a){
        this.list = a;
    }

    public void setFragOneText(String a){
        this.fragOneText = a;
    }

    public String sendFragOneText(){
        return this.fragOneText;
    }

    public ArrayList<Action> sendActionList(){
        return this.list;
    }

}

