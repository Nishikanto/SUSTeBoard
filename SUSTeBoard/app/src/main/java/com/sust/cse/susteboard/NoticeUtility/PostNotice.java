package com.sust.cse.susteboard.NoticeUtility;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sust.cse.susteboard.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class PostNotice extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PICKFILE_RESULT_CODE = 2;
    private TextView fileName;
    private String picturePath;
    private String pictureName;
    private String fileRealName;
    private String FilePath;
    private String notice;
    private String details;
    private TextView noticeText;
    private TextView detailsText;
    private ImageLoader imageLoader;
    private RequestParams params;
    private Spinner departmentChooser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_notice);
        fileName = (TextView) findViewById(R.id.fileName);
        noticeText = (TextView) findViewById(R.id.noticeTitleTxt);
        detailsText = (TextView) findViewById(R.id.noticdeDetailsTxt);
        departmentChooser = (Spinner) findViewById(R.id.department_chooser);
        imageLoader = ImageLoader.getInstance();
    }

    public void onImageButtonClick(View view){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void onFileButtonClick(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent,PICKFILE_RESULT_CODE);
    }

    public void onPostButtonClick(View view){

        notice = noticeText.getText().toString();
        details = detailsText.getText().toString();

        if(null != notice && null != details && null != picturePath){
            final ProgressDialog progressDialog = new ProgressDialog(PostNotice.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Posting your notice...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            Date date = new Date();
            final String postDate = date.toString();

            final String url = "http://192.168.43.147:8080/SUSTeBoardServer/UploadServlet";
            File file = new File(FilePath);
            File imageFile = new File(picturePath);


            final RequestParams params_string = new RequestParams();
            try {
                params_string.put("command", "upload");
                params_string.put("image", picturePath);
                params_string.put("file", fileName);
                params_string.put("notice", notice);
                params_string.put("details", details);
                params_string.put("postDate", postDate);
                params_string.put("department", String.valueOf(departmentChooser.getSelectedItem()));
            } catch(Exception e) {}


            RequestParams params = new RequestParams();
            try {
                params.put("File", file);
                params.put("Image", imageFile);
            } catch(FileNotFoundException e) {}


            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    sendString(progressDialog, params_string);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    progressDialog.dismiss();
                    alartDialog("Server not responding");
                }
                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });




        }
    }

    private void sendString(final ProgressDialog progressDialog, RequestParams params) {

        String url = "http://192.168.43.147:8080/SUSTeBoardServer/SusteboardServlet";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                progressDialog.dismiss();
                alartDialog("Notice Posted");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                progressDialog.dismiss();
                alartDialog("Server not responding");
            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    private String reverse(String str){
        str = new StringBuffer(str).reverse().toString().split(File.separator)[0];
        return new StringBuffer(str).reverse().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Log.d("simul", picturePath);

            pictureName = reverse(picturePath);
            Log.d("simul", pictureName);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.noticeImage);

            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
            imageLoader.displayImage("file://" + picturePath, imageView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        } else if(requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK && null != data){
            FilePath = data.getData().getPath();

            fileRealName = reverse(FilePath);

            Log.d("simul", FilePath);
            Log.d("simul", fileRealName);

            fileName.setText(fileRealName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_notice, menu);
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

    public void alartDialog(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PostNotice.this);
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
