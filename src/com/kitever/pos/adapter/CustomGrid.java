package com.kitever.pos.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.pos.activity.POSAddOrderScreen;

import java.util.ArrayList;


/*
 * created bu Anurag 
 * for display horizontal listview using twoway view
 */
public class CustomGrid extends BaseAdapter {
    private Context mContext;
    int arsize = 0;
    POSAddOrderScreen posorderScreen;
    ArrayList<String> web = new ArrayList<String>();
    boolean isOthher = false;

    public CustomGrid(Context c, ArrayList<String> ar, boolean IsOthher) {
        mContext = c;
        posorderScreen = (POSAddOrderScreen) mContext;
        web = ar;
        isOthher = IsOthher;

    }

    public int getCount() {
        if (web != null)
            return web.size();
        else
            return 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final int id = position;
        Bitmap bm = null;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_single, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.grid_txt);
            holder.delimage = (ImageView) convertView.findViewById(R.id.delimage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String titledata = web.get(id);
        holder.title.setText(titledata);

        holder.delimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posorderScreen.deleteItem(id, isOthher);
                updateResults(id);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        ImageView delimage;
    }

    public void updateResults(int arid) {
        web.remove(arid);
        notifyDataSetChanged();
    }


    public interface Actionable {
        void deleteItem(int id, boolean isOthher);
    }
}

