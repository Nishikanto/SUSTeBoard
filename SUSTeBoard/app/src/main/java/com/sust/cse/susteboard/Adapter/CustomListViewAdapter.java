package com.sust.cse.susteboard.Adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sust.cse.susteboard.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by nishi_000 on 17-08-15.
 */
public class CustomListViewAdapter extends BaseAdapter implements Filterable {


    private Activity context;
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    private TextView noticeTitle;
    private TextView createdAt;
    private String noticPostDate;
    private TextView department;
    private ArrayList<SetRows> data;
    private ArrayList<SetRows> originalList;
    private NameFilter filter;

    public CustomListViewAdapter(Activity context, ArrayList<SetRows> noticeList){
        this.context = context;
        this.data = noticeList;

        this.originalList = new ArrayList<SetRows>();
        this.originalList.addAll(data);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = context.getLayoutInflater().inflate(R.layout.custom_listview, parent, false);
        }

        noticeTitle = (TextView) convertView.findViewById(R.id.notice_title);
        mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.networkImageView);

        createdAt = (TextView) convertView.findViewById(R.id.created_at);
        department = (TextView) convertView.findViewById(R.id.department);


        mImageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
        //Image URL - This can point to any image file supported by Android
        final String url = data.get(position).getImage();
        mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        mNetworkImageView.setImageUrl(url, mImageLoader);
        noticeTitle.setText(data.get(position).getNotice());
        noticPostDate = formateStringDate(data.get(position).getCreatedAt());
        createdAt.setText(noticPostDate);
        department.setText("Notice From - " + data.get(position).getDepartment());
        Log.d("simul", noticPostDate);

        return convertView;
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


    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new NameFilter();
        }
        return filter;
    }
    private class NameFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<SetRows> filteredItems = new ArrayList<SetRows>();

                for(int i = 0, l = originalList.size(); i < l; i++)
                {
                    SetRows notice = originalList.get(i);
                    String title = notice.getNotice();
                    String dep = notice.getDepartment();

                    if(title.toString().toLowerCase().contains(constraint) || dep.toString().toLowerCase().contains(constraint))
                        filteredItems.add(notice);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originalList;
                    result.count = originalList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.count > 0) {
                Log.println(Log.INFO, "Results", "FOUND");
                data.clear();
                data.addAll((ArrayList<SetRows>) results.values);
                notifyDataSetChanged();
            } else {
                Log.println(Log.INFO, "Results", "-");
                notifyDataSetInvalidated();
            }
        }
    }

}



