package sms19.inapp.msg;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import sms19.inapp.single.chatroom.SingleChatRoomFrgament;

public class TimePickerDialogFragment extends DialogFragment implements
TimePickerDialog.OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR);
		int minute = c.get(Calendar.MINUTE);
		 
		TimePickerDialog dialog=new TimePickerDialog(getActivity(), this, hour, minute,DateFormat.is24HourFormat(getActivity()));
		//dialog.setIs24HourVi
		
		return dialog ;
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// do something with the time chosen.
		
		view.setIs24HourView(DateFormat.is24HourFormat(getActivity()));
		
		Message message=new Message();
		message.arg1=hourOfDay;
		message.arg2=minute;
		
		SingleChatRoomFrgament chatRoomFrgament=SingleChatRoomFrgament.newInstance("");
		if(chatRoomFrgament!=null){
		chatRoomFrgament.getUser_sms_creadit().setText("Sms "+new StringBuilder().append(hourOfDay).append(":").append(minute));
		}
		
	}
}
