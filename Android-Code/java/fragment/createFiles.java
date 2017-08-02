package com.example.application.fragment;

/**
 * Created by warrens on 31.05.17.
 */
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import com.example.application.MainActivity;
import com.example.schedulelibrary.Action;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by warrens on 31.05.17.
 */

public class createFiles extends AsyncTask<String, Integer, String> {
    private String[][][] data;
    public Context context;
    public MainActivity activity;

    private String[] actionNames = {
            "cooking (Hob)",
            "cooking(Oven)",
            "Dry clothes (Tumble Dryer)",
            "Wash clothes (Washing Machine)",
            "Use Computer",
            "Boil Water (Kettle)",
            "Wash Dishes (dishwasher)",
            "Shower"
    };
    private String[] actionFileNames  ={
            "Hob", //2
            "Oven", //3
            "Tumble_Dryer", //7
            "Washing_Machine", //8
            "Computer", //1
            "Kettle", //5
            "Dishwasher",//4
            "Shower" //6
            };

    public createFiles(String[][][] d, Context c, MainActivity m){
        this.data = d;
        this.context = c;
        this.activity = m;
    }

    protected String doInBackground(String... a){
        storeData(data);
        return null;
    }
    protected void onProgressUpdate(Integer... a){

    }
    protected void onPostExecute(String result){

    }
    public boolean checkCancelled(){
       return activity.checkTasksStop();
    }
    /* store data in the following way:
             *"Plan/Time,00:00,00:01:,00:02,...,23:59
             * Plan 1,[0 or 1],[0 or 1],[0 or 1],...,[0 or 1]
             * Plan 2,[0 or 1],[0 or 1],[0 or 1],...,[0 or 1]
             * Plan 3,[0 or 1],[0 or 1],[0 or 1],...,[0 or 1]
             *                      .
             *                      .
             *                      .
             * Plan X,[0 or 1],[0 or 1],[0 or 1],...,[0 or 1]"
             *
             * A file of this type should be made for every appliance
             * The title should be USER_ID-APPLIANCE_NAME-DATE-SETUP.txt
             * where SETUP is a count of how many other schedules were created that day prior to this one.
             *
             *
             */
    //a[Schedule][Device][Whether on or off at this index converted to a time+1]
    public void storeData(String[][][] a) {
        if (checkCancelled()) {

        } else {
            long startTime = System.currentTimeMillis();
            if(a.length>0){
                if(a[0].length>1){
                    String wattages = "";
                    String[] wattagesArray;
                    try{
                        FileInputStream fis = activity.openFileInput("wattagesFile.txt");
                        int chr;
                        StringBuilder builder = new StringBuilder();
                        while ((chr = fis.read()) != -1) {
                            if(checkCancelled()){
                                break;
                            }else{
                                builder.append((char) chr);
                            }
                        }
                        wattages = builder.toString();
                        //  0,              1,          2,          3,              4,    5,      6,        7,              8
                        //House Number, Computer, Cooker (Hob),Cooker (Oven),Dishwasher,Kettle,Shower,Tumble Dryer, Washing Machine

                        fis.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    wattagesArray = wattages.split(",");
                    Action helper = new Action();
                    String[] device = new String[a[0].length];

                    String title = "Plan/Time,";

                    for(int i = 0; i<1440;i++){
                        if(checkCancelled()){
                            break;
                        }else{
                            if(i!=1439){
                                title += helper.getTimeString(i)+",";
                            }else{
                                title += helper.getTimeString(i)+"\n";
                            }
                        }
                    }
                    for(int i = 0; i<device.length;i++){
                        if(checkCancelled()){
                            break;
                        }else{
                            device[i] = title;
                        }

                    }
                    int index = 0;
                    for(int i = 0; i<a.length;i++){
                        if(checkCancelled()){
                            break;
                        }else{
                            for(int j = 0; j<a[i].length;j++){
                                if(checkCancelled()){
                                    break;
                                }else{
                                    int planNumber = i+1;
                                    device[j]+="Plan "+planNumber+",";
                                    for(int q = 0; q<a[i][j].length;q++){
                                        if(checkCancelled()){
                                            break;
                                        }else{
                                            if(a[i][j][q].equals("1")){
                                                switch(j){
                                                    case 0:
                                                        index = 2;
                                                        break;
                                                    case 1:
                                                        index = 3;
                                                        break;
                                                    case 2:
                                                        index = 7;
                                                        break;
                                                    case 3:
                                                        index = 8;
                                                        break;
                                                    case 4:
                                                        index = 1;
                                                        break;
                                                    case 5:
                                                        index = 5;
                                                        break;
                                                    case 6:
                                                        index = 4;
                                                        break;
                                                    case 7:
                                                        index = 6;
                                                        break;
                                                }
                                                device[j]+=wattagesArray[index];
                                            }else{
                                                device[j]+="0";
                                            }
                                            if(q<a[i][j].length-1){
                                                device[j]+=",";
                                            }else{
                                                device[j]+="\n";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-HH_mm_ss");
                    String date = simpleDateFormat.format(new Date());

                    String android_id;
                    android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    String fileFirstHalfTitle = android_id+"-";
                    String setup = "0";

                    try {
                        FileInputStream fisPSched = context.openFileInput("PastSchedules.txt");
                        int ch;
                        StringBuilder builder = new StringBuilder();
                        while ((ch = fisPSched.read()) != -1) {
                            if(checkCancelled()){
                                break;
                            }else {
                                char s = (char) ch;
                                String st = "" + s;
                                if (!st.equals(null)) {
                                    builder.append((char) ch);
                                }
                            }
                        }
                        fisPSched.close();
                        setup = builder.toString();
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                    if(checkCancelled()){

                    }else{
                        int setupInt = Integer.parseInt(setup);
                        setupInt++;
                        String fileSecondHalfTitle = "-"+date+"-"+setupInt+".txt" ;

                        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                        FileOutputStream fOut=null;
                        for(int i = 0; i<device.length;i++){
                            String submitString = device[i];
                            String fileName = fileFirstHalfTitle+actionFileNames[i]+"-"+fileSecondHalfTitle;
                            System.out.print(fileName+"\n");
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try{
                            fOut = context.openFileOutput("PastSchedules.txt", Context.MODE_PRIVATE);
                            setup = ""+setupInt;
                            fOut.write(setup.getBytes());
                            fOut.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{

            }
            if(isCancelled()){

            }else{
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                String TimingsFile = "timings.txt";
                try{
                    FileOutputStream fos = activity.openFileOutput(TimingsFile,context.MODE_APPEND);
                    String submit = elapsedTime+"\n";
                    fos.write(submit.getBytes());
                    fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                FileInputStream fis;
                int ch;
                StringBuilder builder;
                String print = "";
                try{
                    fis = activity.openFileInput(TimingsFile);
                    builder = new StringBuilder();
                    while ((ch = fis.read()) != -1) {
                        if(isCancelled()){
                            break;
                        }else {
                            builder.append((char) ch);
                        }
                    }
                    fis.close();
                    print = builder.toString();
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.print(print+"\n");
                String countFile = "count.txt";
                try{
                    fis = activity.openFileInput(countFile);
                    builder = new StringBuilder();
                    while ((ch = fis.read()) != -1) {
                        if(isCancelled()){
                            break;
                        }else {
                            builder.append((char) ch);
                        }
                    }
                    fis.close();
                    print = builder.toString();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(isCancelled()){

                }else{
                    int count = Integer.parseInt(print);
                    count++;
                    System.out.print("\n"+count+"\n");
                    try{
                        FileOutputStream fos = activity.openFileOutput(countFile,context.MODE_PRIVATE);
                        String submit = ""+count;
                        fos.write(submit.getBytes());
                        fos.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}