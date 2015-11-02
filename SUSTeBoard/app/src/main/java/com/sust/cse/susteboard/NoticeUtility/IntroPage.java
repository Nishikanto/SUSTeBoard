package com.sust.cse.susteboard.NoticeUtility;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sust.cse.susteboard.R;
import com.sust.cse.susteboard.UserAuthentication.LoginActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class IntroPage extends AppCompatActivity {

    CardView allButtonCardView;
    CardView todayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);

        TextView dateView = (TextView) findViewById(R.id.date_view);
        allButtonCardView = (CardView) findViewById(R.id.all_button);
        todayButton=(CardView) findViewById(R.id.today_button);

        Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
        int currentDay = localCalendar.get(Calendar.DATE);
        int currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        int currentYear = localCalendar.get(Calendar.YEAR);

        dateView.setText("Today's Date: " + currentDay + "/" + currentMonth + "/" + currentYear);

    }

    public void onAllButtonClick(View view){
        Intent intent = new Intent(this, AllNoticeList.class);
        startActivity(intent);
    }

    public void onTodayButtonClick(View view){
        Intent intent = new Intent(this, TodayNoticeList.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro_page, menu);
        return true;
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
        }else if(id == R.id.settings){
            Intent i = new Intent(this, settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
}
