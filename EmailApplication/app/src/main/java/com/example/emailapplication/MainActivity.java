package com.example.emailapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity myActivity = this;
        final Button send = (Button) this.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String fromEmail = "mailsenderforscheduler@gmail.com";
                String fromPassword = "TestMail56";
                String toEmails = "mailsenderforscheduler@gmail.com";
                String adminEmail = "admin@gmail.com";
                String emailSubject = "App Registration Mail";
                String adminSubject = "App Registration Mail";
                String emailBody = "Your message";
                String adminBody = "Your message";
                new SendMailTask(myActivity).execute(fromEmail,
                        fromPassword, toEmails, emailSubject, emailBody);

            }
        });

    }
}
