package com.sust.cse.susteboard.NoticeUtility;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sust.cse.susteboard.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class settings extends AppCompatActivity {

    private ArrayList<String> selectedItems;
    private String PREFS_NAME = "SUSTeBoard_Settings";
    private SharedPreferences settings;
    private ListView departmentList;
    private String[] items={"CSE","PME","IPE","PHY","EEE","BBA", "ECO", "ENG"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        selectedItems=new ArrayList<String>();
    }

    public void onStart(){
        super.onStart();
        departmentList=(ListView) findViewById(R.id.checkable_list);

        departmentList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.checkable_list_layout,R.id.txt_title,items);
        departmentList.setAdapter(arrayAdapter);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String value = settings.getString("key", "");
        Log.d("simul", value);

        List<String> savedItem = Arrays.asList(value.split(","));

        for(int i=0;i<savedItem.size();i++)
        {
            int position = getPostion(savedItem.get(i));
            if(position!=1000){
                departmentList.setItemChecked(position, true);
            }
        }
    }


    public void saveSelectedItems(View view){

        for(int i = 0; i<departmentList.getCount();i++){
            if(departmentList.isItemChecked(i)){
                selectedItems.add(items[i]);
            }
        }

        String departments="";
        for(String item:selectedItems){
            if(departments=="")
                departments=item;
            else
                departments+=","+item;
        }

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("key", departments);
        editor.commit();

        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getPostion(String item) {
        int position;
        switch (item) {
            case "CSE":
                position = 0;
                break;
            case "PME":
                position = 1;
                break;
            case "IPE":
                position = 2;
                break;
            case "PHY":
                position = 3;
                break;
            case "EEE":
                position = 4;
                break;
            case "BBA":
                position = 5;
                break;
            case "ECO":
                position = 6;
                break;
            case "ENG":
                position = 7;
                break;
            default:
                position = 10000;
                break;
        }
        return position;
    }

}
