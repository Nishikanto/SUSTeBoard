package com.sust.cse.susteboard.NoticeUtility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sust.cse.susteboard.R;
import com.sust.cse.susteboard.UserAuthentication.LoginActivity;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
    }

    public void onClick(View view){
        if(view == findViewById(R.id.all_noitce_button)){
            Intent i = new Intent(this, AllNoticeForAdmin.class);
            startActivity(i);
        }
        else if(view == findViewById(R.id.today_noitce_button)){
            Intent i = new Intent(this, TodayNoticeForAdmin.class);
            startActivity(i);
        }
        else if(view == findViewById(R.id.post_notice_button)){
            Intent i = new Intent(this, PostNotice.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_page, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_out) {
            ArrayList<String> selectedItems;
            String PREFS_NAME = "SUSTeBoard_Settings";
            SharedPreferences settings;

            settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            // Writing data to SharedPreferences
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("email", "");
            editor.putString("password", "");
            editor.putString("position", "");
            editor.commit();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            return true;
        } else if(id == R.id.settings){
            Intent i = new Intent(this, settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
