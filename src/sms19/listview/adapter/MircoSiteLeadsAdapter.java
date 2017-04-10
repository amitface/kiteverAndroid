package sms19.listview.adapter;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kitever.android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sms19.listview.newproject.model.ClientLeadDetails;
import sms19.listview.newproject.model.MicrositeDetails;

/**
 * Created by android on 21/2/17.
 */

public class MircoSiteLeadsAdapter extends RecyclerView.Adapter<MircoSiteLeadsAdapter.MyViewHolder> {

    private ArrayList<ClientLeadDetails> clientLeadDetailses;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, source, genre;
        public TextView tablerow1, tablerow2, tablerow3, tablerow4;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.tvMicroSiteDate);
            source = (TextView) view.findViewById(R.id.tvMicroSiteSource);
            tablerow1 = (TextView) view.findViewById(R.id.tablerowMicroSite1);
            tablerow2 = (TextView) view.findViewById(R.id.tablerowMicroSite2);
            tablerow3 = (TextView) view.findViewById(R.id.tablerowMicroSite3);
            tablerow4 = (TextView) view.findViewById(R.id.tablerowMicroSite4);
        }
    }


    public MircoSiteLeadsAdapter(ArrayList<ClientLeadDetails> clientLeadDetailses) {
        this.clientLeadDetailses = clientLeadDetailses;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.micro_site_layout_leads, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ClientLeadDetails temp = clientLeadDetailses.get(position);
        String pattern1 = "d-MMM";
        String s="";
        if (temp.getDate() != null && !temp.getDate().equals("")) {
            SimpleDateFormat format = new SimpleDateFormat(pattern1);
            try {
                Date date = new Date(temp.getDate());
                format = new SimpleDateFormat(pattern1);
//	      System.out.println("Date"+date+ " new Date"+format.format(date));
                s = format.format(date);
            } catch (Exception c) {

            }
        } else
            s = "";
        holder.date.setText(s);
        holder.source.setText(temp.getSource());
        holder.tablerow1.setText(temp.getFirstName() + " " + temp.getLastName());
        holder.tablerow2.setText(temp.getEmail());
        holder.tablerow3.setText(temp.getMobile());
        holder.tablerow4.setText(temp.getAddress() + ", " + temp.getCity() + ", " + temp.getZip());
    }

    @Override
    public int getItemCount() {
        return clientLeadDetailses.size();
    }
}
