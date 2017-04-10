package com.kitever.customviews;

import java.util.Calendar;

import sms19.inapp.msg.constant.Utils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.kitever.android.R;
import com.kitever.utils.DateHelper;

public class DateTimePickerDialog extends AlertDialog {

    private static final String TAG = "DateTimePickerDialog";
    
    private static final String DEFAULT_TITLE = "Pick date & time";
    private static final String SET           = "Schedule";
    private static final String CANCEL        = "Cancel";
    
    private static final String STATE_DATETIME_KEY = "DateTimePickerDialog.datetime";
    
    //private static final String TIME_FORMAT_24 = "24";
    
    private DatePicker datePicker;
    private TimePicker timePicker;
    
    
    
    private int minHour = -1;
    private int minMinute = -1;

    private int maxHour = 25;
    private int maxMinute = 25;

    private int currentHour = 0;
    private int currentMinute = 0;
    

   private Calendar calendar = Calendar.getInstance();
    //private DateFormat dateFormat;
    
    public DateTimePickerDialog(Context context, DateTimeAcceptor datetimeAcceptor) {
        this(context, datetimeAcceptor, null, System.currentTimeMillis(), null);
    }

    public DateTimePickerDialog(Context context, DateTimeAcceptor datetimeAcceptor,
            String title) {
        this(context, datetimeAcceptor, title, System.currentTimeMillis(), null);
    }

    public DateTimePickerDialog(Context context, DateTimeAcceptor datetimeAcceptor,DateTimeDeselector dateTimeDeselector,
            long datetime) {
        this(context, datetimeAcceptor, null, datetime, dateTimeDeselector);
    }
    
    public DateTimePickerDialog(Context context, DateTimeAcceptor datetimeAcceptor,
            String title, long datetime) {
        this(context, datetimeAcceptor, title, datetime, null);
    }

    public DateTimePickerDialog(Context context, final DateTimeAcceptor datetimeAcceptor,
            String title, long datetime, final DateTimeDeselector dateTimeDeselector) {
        super(context);
        
   
        
        final LayoutInflater factory = LayoutInflater.from(context);
        
        final FrameLayout pickersContainer = 
            (FrameLayout) factory.inflate(R.layout.date_time_picker, null);
        
        datePicker = (DatePicker) pickersContainer.findViewById(R.id.date);
        timePicker = (TimePicker) pickersContainer.findViewById(R.id.time);
        
        resetDatetime(datetime);
        
        datePicker.setMinDate(System.currentTimeMillis()-1000);
        calendar.setTimeInMillis(System.currentTimeMillis());
        
        timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				
				final int yearPicked=datePicker.getYear();
				final int yearToday=calendar.get(Calendar.YEAR);
				
				if(yearPicked>yearToday){
					return;
				}
				
				final int monthPicked=datePicker.getMonth();
				final int monthToday=calendar.get(Calendar.MONTH);
				
				if(monthPicked>monthToday){
					return;
				}
				
				final int dayPicked=datePicker.getDayOfMonth();
				final int dayToday=calendar.get(Calendar.DAY_OF_MONTH);
				
				if(dayPicked>dayToday){
					return;
				}else{
				    boolean validTime = true;
				    if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
				        validTime = false;
				    }

				    if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
				        validTime = false;
				    }

				    if (validTime) {
				        currentHour = hourOfDay;
				        currentMinute = minute;
				    }

				    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
			        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
			            timePicker.setHour(currentHour);
					    timePicker.setMinute(currentMinute);
			        } else {
			            timePicker.setCurrentHour(currentHour);
					    timePicker.setCurrentMinute(currentMinute);
			        }
				}
				
		
			    
			    //updateDialogTitle(view, currentHour, currentMinute);
			}
		});
        
        
        setIcon(R.drawable.ic_schedule_black_24dp);
        setTitle(title == null ? DEFAULT_TITLE : title);
        
        setView(pickersContainer);
        
        setButton(BUTTON_POSITIVE, SET, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                log("positiveBtnListener: entered");
                datetimeAcceptor.accept(getDateTime());
            }
        });
        
        setButton(BUTTON_NEGATIVE, CANCEL, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                log("negativeBtnListener: entered");
                if (dateTimeDeselector != null)
                	dateTimeDeselector.deselect();
            }
        });
        
        setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                log("cancelListener: entered");
                if (dateTimeDeselector != null) dateTimeDeselector.deselect();
            }
        });
        
        setCancelable(true);
    }
    
    public void setMin(int hour, int minute) {
        minHour = hour;
        minMinute = minute;
    }

    public void setMax(int hour, int minute) {
        maxHour = hour;
        maxMinute = minute;
    }
    
   /* private void updateDialogTitle(TimePicker timePicker, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        String title = dateFormat.format(calendar.getTime());
        setTitle(title);
    }*/
    
    @Override
    public Bundle onSaveInstanceState() {
        Bundle outState = super.onSaveInstanceState();
        outState.putLong(STATE_DATETIME_KEY, getDateTime());
        return outState;
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resetDatetime(savedInstanceState.getLong(STATE_DATETIME_KEY));
    }
    
    @SuppressWarnings("deprecation")
	public void resetDatetime(long datetime) {
        // honor user settings as to time format
        //final String timeFormat = Settings.System.getString(getContext().getContentResolver(), Settings.System.TIME_12_24);
        //timePicker.setIs24HourView(TIME_FORMAT_24.equals(timeFormat));
        timePicker.setIs24HourView(false);
        DateHelper.initDatePicker(datePicker, datetime);
        DateHelper.initTimePicker(timePicker, datetime);
        
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1){
        	currentHour = timePicker.getHour();
        	currentMinute = timePicker.getMinute();
        } else {
        	currentHour = timePicker.getCurrentHour();
        	currentMinute = timePicker.getCurrentMinute();
        }

       // dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        setMin(currentHour, currentMinute);
    }
    
    private long getDateTime() {
        return DateHelper.getStartOfTheDay(DateHelper.getDate(datePicker)) 
            + DateHelper.getTime(timePicker);
    }
    
    private void log(String msg) {
    	Utils.printLog(TAG+" "+msg);
    }
    
    public interface DateTimeAcceptor {
        void accept(long datetime);
    }
    
    public interface DateTimeDeselector {
        void deselect();
    }
}
