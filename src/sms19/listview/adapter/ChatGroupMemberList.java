package sms19.listview.adapter;

import java.util.ArrayList;

import sms19.inapp.msg.model.ChatMemberListModel;
import sms19.inapp.msg.model.Recentmodel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kitever.android.R;

public class ChatGroupMemberList extends BaseAdapter {

	ArrayList<ChatMemberListModel> arrayList = new ArrayList<ChatMemberListModel>();
	ArrayList<Recentmodel> gmemberlist;

	public ChatGroupMemberList(ArrayList<Recentmodel> gmemberlist) {
		for (int k = 0; k < gmemberlist.size()-1; k++) {
			arrayList.add(new ChatMemberListModel(gmemberlist.get(k)
					.getDisplayname(), gmemberlist.get(k).getUsernumber()));
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.select_admin_list, parent,
					false);
			holder.name = (TextView) convertView.findViewById(R.id.name_id);
			holder.phone = (TextView) convertView
					.findViewById(R.id.phone_num_id);
			ChatMemberListModel chatMemberListModel = arrayList.get(position);
			holder.name.setText(chatMemberListModel.getName());
			holder.phone.setText(chatMemberListModel.getPhone());
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	private static class ViewHolder {
		private TextView name;
		private TextView phone;
	}
}
