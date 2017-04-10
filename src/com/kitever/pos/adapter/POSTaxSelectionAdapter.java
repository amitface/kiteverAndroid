package com.kitever.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.pos.model.data.TaxModelData;

import java.util.ArrayList;

public class POSTaxSelectionAdapter extends BaseAdapter {
    ArrayList<TaxModelData> list;
    private boolean[] checkBoxState;
    private ArrayList<Integer> checkedpositions;
    ArrayList<String> taxNameList;

    public POSTaxSelectionAdapter(ArrayList<TaxModelData> list, ArrayList<String> taxNameList) {
        // TODO Auto-generated constructor stub
        this.list = list;
        this.taxNameList = taxNameList;
        checkedpositions = new ArrayList<>();
        checkBoxState = new boolean[list.size()];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.tax_selection_layout,
                    parent, false);
            viewHolder.taxName = (TextView) convertView
                    .findViewById(R.id.tax_name);
            viewHolder.taxBox = (CheckBox) convertView
                    .findViewById(R.id.check_box_tax);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.taxBox.setTag(position);

        TaxModelData object = list.get(position);
        viewHolder.taxName.setText(object.getTaxName() + " - " + object.getTaxPer() + " %");
        final Object tag = viewHolder.taxBox.getTag();

        if (taxNameList!=null && taxNameList.contains(object.getTaxName())) {
            viewHolder.taxBox.setChecked(true);
            checkedpositions.add((Integer) tag);
        } else {
            viewHolder.taxBox.setChecked(false);
            checkedpositions.remove(tag);
        }
        viewHolder.taxBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.taxBox.isChecked()) {

                    checkedpositions.add((Integer) tag);
                } else {
                    checkedpositions.remove(tag);

                }
            }
        });

        return convertView;
    }

    public ArrayList<Integer> getcheckeditemcount() {
        return this.checkedpositions;
    }

    private static class ViewHolder {
        TextView taxName;
        CheckBox taxBox;
    }
}
