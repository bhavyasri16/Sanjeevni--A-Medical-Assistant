package com.finalproject.it.sanjeevni.fragment;

import androidx.appcompat.app.AppCompatActivity;
import com.finalproject.it.sanjeevni.R;

import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;

public class ContactUs extends AppCompatActivity {

    private String Strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About Us");
        Strings="“Best clinical decisions are at the heart of appropriate care, the goal to which our system should aspire.”\n" +
                "—DR. ANNA REID\n" +
                "The main motive behind this app is to provide assistance with health related issues and other facilities," +
                " automate the existing manual system." +
                " We provide a common and secure platform that can assist the user in emergencies and keeping up with the medicine schedule, " +
                "protecting the user’s privacy and ensuring the communication with an authenticated party only.";
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .enableDarkMode(false)
                .setDescription(Strings) // or Typeface
                .setImage(R.mipmap.cropped_logo_transparent)
                .addGroup("Connect with us")
                .addEmail("sanjeevni@gmail.com")
                .addWebsite("https://sanjeevni.com/")
                .addFacebook("www.facebook.com")
                .addTwitter("www.twitter.com")
                .addYoutube("www.youtube.in")
                .addPlayStore("www.google.com")
                .addGitHub("bhavyasri16/Sanjeevni-Medical-Assistant/tree/forshow8")
                .addInstagram("www.instagram.com")
                .create();

        setContentView(aboutPage);
    }
}
