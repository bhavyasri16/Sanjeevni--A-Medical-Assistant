package com.finalproject.it.sanjeevni.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    public FirebaseAuth getmAuth() {
        return FirebaseAuth.getInstance();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if(getmAuth().getCurrentUser()!=null){
            getMenuInflater().inflate(R.menu.mymenu, menu);
            return super.onCreateOptionsMenu(menu);}
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(getmAuth().getCurrentUser()!=null){
            int id = item.getItemId();
            if (id == R.id.logout_btn) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),WelcomeActivity.class));
                finish();
            }
            else if(id==R.id.refresh){
                recreate();
            }
            else if(id==R.id.profile_btm){
                startActivity(new Intent(getApplicationContext(), ProfileView.class));
                finish();
            }
            return super.onOptionsItemSelected(item);}
        return false;
    }

}
