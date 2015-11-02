package com.sust.cse.susteboard.Adapter;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();

    private final Response.Listener<String> mListener;
    private HashMap<String, String> mParams;


    public MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener) {
        super(Method.POST, url, errorListener);
        mListener = listener;

        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        entity.addPart("profile_picture", new FileBody(new File("/storage/emulated/0/Pictures/VSCOCam/2015-07-31 11.55.14 1.jpg")));
        try {
            entity.addPart("user_id", new StringBody("15"));
            entity.addPart("name", new StringBody("Bogs"));
            entity.addPart("gender", new StringBody("Male"));
            entity.addPart("date_birth", new StringBody("1999-12-5"));
            entity.addPart("breed", new StringBody("monkey"));
        } catch (UnsupportedEncodingException e) {
            VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success("Uploaded", getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);

    }
}