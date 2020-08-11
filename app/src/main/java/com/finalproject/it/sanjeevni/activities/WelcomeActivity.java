package com.finalproject.it.sanjeevni.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.activities.bloodBank.BloodBank;
import com.finalproject.it.sanjeevni.activities.bloodBank.BloodDonationRequests;
import com.finalproject.it.sanjeevni.activities.bloodBank.CurrentUserRequests;
import com.finalproject.it.sanjeevni.activities.ui.login.Approve_Requests;
import com.finalproject.it.sanjeevni.activities.ui.login.LoginActivity;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OneSignal;

import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button getStarted;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private LayoutInflater inflater;
    private List<String> list;
    private int identify_operation=0;
    private FloatingActionButton mainbtn,allreq,userreq;
    private TextView allreqText,userreqText;
    private Boolean isOpen;
    private Animation fab_open,fab_close,fab_rotate;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Making notification bar transparent
        /*if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }*/
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new NotificationHandler(this))
                .init();


        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_welcome);
        fstore=FirebaseFirestore.getInstance();
        fstore.collection("User_Type").document("admins").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.getData().containsValue(mAuth.getCurrentUser().getUid())) {
                            identify_operation=1;
                            getStarted.setText("Check Pending Requests");
                        }
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

        viewPager =  findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        getStarted = findViewById(R.id.btn_get_started);
        mainbtn=findViewById(R.id.main_add_fab);
        allreq=findViewById(R.id.fab1);
        userreq=findViewById(R.id.fab2);
        allreqText=findViewById(R.id.all_req);
        userreqText=findViewById(R.id.user_req);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        //changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


        if(mAuth.getCurrentUser()!=null)
        {
            if(identify_operation!=1) {
                identify_operation = 3;
                getStarted.setText("Click To Proceed");
            }
            OneSignal.sendTag("User_Type","regular");
            OneSignal.sendTag("Email",mAuth.getCurrentUser().getEmail());
        }
        else
        {
            identify_operation=2;
        }

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(identify_operation==1 )
                {
                    startActivity(new Intent(getBaseContext(), Approve_Requests.class));
                }
                else if(identify_operation==2)
                {
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                }
                else if(identify_operation==3)
                {
                    Toast toast = Toast.makeText(getBaseContext(),"Please Select One of the Above Options to Proceed",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }

            }
        });


        isOpen=false;

        fab_open= AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.fab_open);
        fab_close= AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.fab_close);
        fab_rotate=AnimationUtils.loadAnimation(WelcomeActivity.this,R.anim.fab_rotate);

        mainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isOpen){

                    mainbtn.startAnimation(fab_rotate);
                    allreq.startAnimation(fab_close);
                    userreq.startAnimation(fab_close);
                    allreqText.startAnimation(fab_close);
                    userreqText.startAnimation(fab_close);

                    isOpen=false;
                }else{

                    mainbtn.startAnimation(fab_rotate);
                    allreq.startAnimation(fab_open);
                    userreq.startAnimation(fab_open);
                    allreqText.startAnimation(fab_open);
                    userreqText.startAnimation(fab_open);

                    isOpen=true;
                }
            }
        });

        allreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), BloodDonationRequests.class));
                finish();
            }
        });

        userreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CurrentUserRequests.class));
                finish();
            }
        });

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;",Html.FROM_HTML_MODE_LEGACY));
            //dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);

    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private boolean checkMail(View view)
    {
        final FirebaseUser fuser= mAuth.getCurrentUser();
        if(!fuser.isEmailVerified()){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Please Verify Email ID First to Proceed, Then Login Again !!")
                    .setCancelable(false)
                    .setPositiveButton("Resend Verification", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast toast = Toast.makeText(getBaseContext(),"Email Sent For Verification, Please Check !",Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast toast = Toast.makeText(getBaseContext(),"Error : "+ e.getMessage(),Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    //Set your icon here
                    .setTitle("Alert !! ")
                    .setIcon(R.drawable.ic_mail_error);
                    builder.create().show();
                    return false;
        }
        return true;
    }


    //Menu Option On Top-Right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mAuth.getCurrentUser()!=null){
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);}
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mAuth.getCurrentUser()!=null){
        int id = item.getItemId();
        if (id == R.id.logout_btn) {
            FirebaseAuth.getInstance().signOut();
            recreate();
        }
        else if(id==R.id.refresh){
            recreate();
        }
        else if(id==R.id.profile_btm){
            startActivity(new Intent(getBaseContext(), ProfileView.class));
        }
        return super.onOptionsItemSelected(item);}
        return false;
    }
    //Menu Option Till here

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            if(mAuth.getCurrentUser()!=null ) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checkMail(view)){
                        if (position == 0) {
                            startActivity(new Intent(WelcomeActivity.this, com.finalproject.it.sanjeevni.activities.searchDoctor.searchMain.class));
                        } else if (position == 1) {
                            startActivity(new Intent(WelcomeActivity.this, BloodBank.class));
                        } else if (position == 2) {
                           // startActivity(new Intent(WelcomeActivity.this,Main2Activity.class));
                        } else {

                        }}
                    }
                });
            }
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}