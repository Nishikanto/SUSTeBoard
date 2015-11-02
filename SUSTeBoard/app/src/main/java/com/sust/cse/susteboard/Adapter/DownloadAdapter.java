package com.sust.cse.susteboard.Adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * Created by nishi_000 on 21-08-15.
 */
public class DownloadAdapter {
    
    private String url;
    private Context context;

    public DownloadAdapter(Context applicationContext) {
        context = applicationContext;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public void startDownload() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(url.split("=")[1]);
        request.setDescription("File is being Download....");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        Log.d("simul", url.split("=")[1]);
        String nameOfFile = url.split("=")[1];
        Log.d("simul", "application/"+ nameOfFile.split("\\.")[1]);
        request.setMimeType("application/"+ nameOfFile.split("\\.")[1]);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }


}
