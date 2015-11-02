package com.sust.cse.susteboard.NoticeUtility;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sust.cse.susteboard.Adapter.CustomVolleyRequestQueue;
import com.sust.cse.susteboard.Adapter.DownloadAdapter;
import com.sust.cse.susteboard.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeDetails extends AppCompatActivity {

    private ImageLoader mImageLoader;
    private Bundle bundle;
    private Button downloadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        bundle = getIntent().getExtras();

        downloadBtn = (Button) findViewById(R.id.download_button);

        if(bundle.getString("fileLink").equals("null")) {
            downloadBtn.setVisibility(View.GONE);
        }

        TextView textView = (TextView) findViewById(R.id.notice_title);
        textView.setText(bundle.getString("Title"));

        TextView timeView = (TextView) findViewById(R.id.notice_time);
        timeView.setText(formateStringDate(bundle.getString("CreatedDate")));

        TextView textView2 = (TextView) findViewById(R.id.notice_details);
        textView2.setText(bundle.getString("Details"));

        String url = bundle.getString("Image");
        mImageLoader = CustomVolleyRequestQueue.getInstance(this).getImageLoader();
        NetworkImageView mNetworkImageView = (NetworkImageView) findViewById(R.id.networkImageView);
        mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        mNetworkImageView.setImageUrl(bundle.getString("Image"), mImageLoader);

    }

    public void onClick(View view) {


        DownloadAdapter downloadAdapter = new DownloadAdapter(getApplicationContext());
        Log.d("simul", bundle.getString("fileLink"));
        downloadAdapter.setUrl(bundle.getString("fileLink"));
        downloadAdapter.startDownload();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notice_details, menu);
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

    public String formateStringDate(String strDate){
        String dateStr = strDate;
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        try {
            date = (Date)formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
        String formatedDate = format.format(date);
        return formatedDate;
    }
}
