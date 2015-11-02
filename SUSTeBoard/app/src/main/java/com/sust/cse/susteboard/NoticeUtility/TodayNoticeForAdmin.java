package com.sust.cse.susteboard.NoticeUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.sust.cse.susteboard.Adapter.CustomListViewAdapter;
import com.sust.cse.susteboard.Adapter.SetRows;
import com.sust.cse.susteboard.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodayNoticeForAdmin extends AppCompatActivity {

    private String PREFS_NAME = "SUSTeBoard_Settings";
    private SharedPreferences settings;
    private RequestQueue requestQueue;
    private String url;
    private ArrayList<SetRows> noticeList;
    private JsonArrayRequest request;
    private ListView listView;
    private String ID;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;
    private TextView inputSearch;
    private CustomListViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notice_list);

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String value = settings.getString("key", "");
        if(value=="")value="all";
        Log.d("simul", value);

        url = "http://192.168.43.147:8080/SUSTeBoardServer/SusteboardServlet?command=getnotice&&department="+value;

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.admin_swipe_refresh_layout);

        requestQueue = Volley.newRequestQueue(this);
        listView = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);

        progressDialog = new ProgressDialog(TodayNoticeForAdmin.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        new ServerRequestAsync().execute();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ServerRequestAsync().execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_notice_list, menu);
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

    private void parseJsonArrayResponse(JSONArray response) {
        noticeList = new ArrayList<>();

        for (int i = response.length(); i >=0; i--) {
            try {
                JSONObject jsonobject = null;
                jsonobject = response.getJSONObject(i);
                ID = jsonobject.getString("ID");
                SetRows setRows = new SetRows();
                if(formateStringDate(jsonobject.getString("createdAt")).equals(todaysDate())){
                    setRows.setNotice(jsonobject.getString("notice"));
                    setRows.setDetails(jsonobject.getString("details"));
                    setRows.setCreatedAt(jsonobject.getString("createdAt"));
                    setRows.setImage(jsonobject.getString("image"));
                    setRows.setFileLink(jsonobject.getString("pdf"));
                    setRows.setDepartment(jsonobject.getString("department"));
                    noticeList.add(setRows);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new CustomListViewAdapter((Activity) this, noticeList);
        listView.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                TodayNoticeForAdmin.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("Title", noticeList.get(i).getNotice());
                bundle.putString("Image", noticeList.get(i).getImage());
                bundle.putString("Details", noticeList.get(i).getDetails());
                bundle.putString("CreatedDate", noticeList.get(i).getCreatedAt());
                bundle.putString("fileLink", noticeList.get(i).getFileLink());
                Intent in = new Intent(TodayNoticeForAdmin.this, NoticeDetails.class);
                in.putExtras(bundle);
                startActivity(in);
            }
        });
    }

    class ServerRequestAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // we got the response, now our job is to handle it
                    progressDialog.dismiss();
                    //Toast.makeText(TodayNoticeForAdmin.this, "Success", Toast.LENGTH_LONG).show();
                    parseJsonArrayResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    alartDialog("Server not responding");
                    //Toast.makeText(TodayNoticeForAdmin.this, "Not Success", Toast.LENGTH_LONG).show();
                    //something happened, treat the error.
                }
            });
            requestQueue.add(request);

        }

        @Override
        protected Void doInBackground(Void... params) {


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }


    }

    public String formateStringDate(String strDate){
        String dateStr = strDate;
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String formatedDate = format.format(date);
        System.out.println(formatedDate);
        return formatedDate;
    }

    public String todaysDate(){
        Date date = new Date();
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String DateToStr = format.format(curDate);
        System.out.println(DateToStr);
        return DateToStr;
    }

    public void alartDialog(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(TodayNoticeForAdmin.this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
