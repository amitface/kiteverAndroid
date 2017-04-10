package sms19.inapp.msg;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kitever.android.R;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;

import sms19.inapp.msg.constant.ConstantFields;
import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;

public class Settings extends Fragment implements OnClickListener {


    private static Settings addName_1;
    private InAppMessageActivity homeActivity;
    private String mLastTitle = "";

    private TextView mGroupNameEdt;
    private Button mCreateGroup;
    private Button mCancel;

    private int LAST_HIDE_MENU = 0;

    private RadioButton available, busy, invisable, only;
    private RadioButton every_one, none, only_my_contact;
    private RadioButton forever, mTendays, mThirtyDay, unlimeted;
    private RadioButton default_ring, select, vibrate_ring, vibrate_and_ring;
    private RadioButton group_default_ring, group_select_ring, group_vibrate_only, group_ring_and_vibrate;
    private RadioButton yes, no;
    private RadioButton mail_yes, mail_no;
    private TextView time, mail_time;
    private ImageView time_picker, mail_time_picker;


    public static Settings getInstance() {
        return addName_1;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (InAppMessageActivity) getActivity();
        homeActivity.groupActionBarControlIsVisual();
        homeActivity.getLayoutTab_contact_chat().setVisibility(View.GONE);
        mLastTitle = homeActivity.getActionbar_title().getText().toString();
        homeActivity.getActionbar_title().setText("Settings");

        homeActivity.getLayout_name_status().setVisibility(View.GONE);
        homeActivity.getmActionBarImage().setVisibility(View.GONE);
        homeActivity.getActionbar_title().setVisibility(View.VISIBLE);

        LAST_HIDE_MENU = ConstantFields.HIDE_MENU;
        ConstantFields.HIDE_MENU = 3;
        homeActivity.invalidateOptionMenuItem();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addName_1 = this;
        View v = inflater.inflate(R.layout.settings_new, container, false);
        Init(v);
        return v;
    }

    private void Init(View view) {

        available = (RadioButton) view.findViewById(R.id.available);
        busy = (RadioButton) view.findViewById(R.id.busy);
        invisable = (RadioButton) view.findViewById(R.id.invisible);
        only = (RadioButton) view.findViewById(R.id.only);

        every_one = (RadioButton) view.findViewById(R.id.every_one);
        none = (RadioButton) view.findViewById(R.id.none);
        only_my_contact = (RadioButton) view.findViewById(R.id.only_contact);

        yes = (RadioButton) view.findViewById(R.id.message_to_contact_yes);
        no = (RadioButton) view.findViewById(R.id.message_to_contact_no);

        mail_yes = (RadioButton) view.findViewById(R.id.email_to_contact_yes);
        mail_no = (RadioButton) view.findViewById(R.id.email_to_contact_no);

        forever = (RadioButton) view.findViewById(R.id.forever);
        mTendays = (RadioButton) view.findViewById(R.id.sixten_days);
        mThirtyDay = (RadioButton) view.findViewById(R.id.thirty_days);
        unlimeted = (RadioButton) view.findViewById(R.id.unlimeted);

        default_ring = (RadioButton) view.findViewById(R.id.default_ring);
        select = (RadioButton) view.findViewById(R.id.select);
        vibrate_ring = (RadioButton) view.findViewById(R.id.vibrate_ring);
        vibrate_and_ring = (RadioButton) view.findViewById(R.id.vibrate_and_ring);

        group_default_ring = (RadioButton) view.findViewById(R.id.group_default_ring);
        group_select_ring = (RadioButton) view.findViewById(R.id.group_select_ring);
        group_vibrate_only = (RadioButton) view.findViewById(R.id.group_vibrate_only);
        group_ring_and_vibrate = (RadioButton) view.findViewById(R.id.group_ring_and_vibrate);

        forever.setOnClickListener(this);
        mTendays.setOnClickListener(this);
        mThirtyDay.setOnClickListener(this);
        unlimeted.setOnClickListener(this);

        default_ring.setOnClickListener(this);
        select.setOnClickListener(this);
        vibrate_ring.setOnClickListener(this);
        vibrate_and_ring.setOnClickListener(this);

        group_default_ring.setOnClickListener(this);
        group_select_ring.setOnClickListener(this);
        group_vibrate_only.setOnClickListener(this);
        group_ring_and_vibrate.setOnClickListener(this);


        time = (TextView) view.findViewById(R.id.time);
        time_picker = (ImageView) view.findViewById(R.id.time_picker);

        mail_time = (TextView) view.findViewById(R.id.mail_time);
        mail_time_picker = (ImageView) view.findViewById(R.id.mail_time_picker);


        select.setChecked(true);
        select.setTextColor(Color.parseColor("#E77129"));

        forever.setChecked(true);
        forever.setTextColor(Color.parseColor("#E77129"));

        every_one.setChecked(true);
        every_one.setTextColor(Color.parseColor("#E77129"));

        //	yes.setChecked(true);
        //	yes.setTextColor(Color.parseColor("#E77129"));


        boolean isByDefaultSendSmsCheckedYesNo = Utils.getByDefaultSendMsgYesNo(getActivity());
        boolean isByDefaultSendMailCheckedYesNo = Utils.getByDefaultSendMailYesNo(getActivity());


        if (isByDefaultSendSmsCheckedYesNo) {
            yes.setChecked(true);
            yes.setTextColor(Color.parseColor("#E77129"));
            no.setTextColor(Color.parseColor("#0867A3"));
        } else {
            no.setChecked(true);
            no.setTextColor(Color.parseColor("#E77129"));
            yes.setTextColor(Color.parseColor("#0867A3"));
        }

        if (isByDefaultSendMailCheckedYesNo) {
            mail_yes.setChecked(true);
            mail_yes.setTextColor(Color.parseColor("#E77129"));
            mail_no.setTextColor(Color.parseColor("#0867A3"));
        } else {
            mail_no.setChecked(true);
            mail_no.setTextColor(Color.parseColor("#E77129"));
            mail_yes.setTextColor(Color.parseColor("#0867A3"));
        }

        String timeValue = Utils.getExpiryTimeValue(getActivity());
        if (!timeValue.equalsIgnoreCase("")) {
            time.setTag(timeValue);
            String newTime[] = timeValue.split(":");
            time.setText("" + newTime[0] + " : " + newTime[1] + " Min "); //set the value to textview
            //time.setText("User SMS Credit to send message not delevired in App with in "+String.valueOf(newTime[0])+":"+newTime[1]+ " Min "); //set the value to textview
        }


        time_picker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showTimePicker();
            }
        });

        mail_time_picker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               showTimePickerMail();
            }
        });


        yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (yes.isChecked()) {
                    yes.setTextColor(Color.parseColor("#E77129"));
                    no.setTextColor(Color.parseColor("#0867A3"));
                    Utils.saveByDefaultSendMsgYesNo(getActivity(), true);
                }
            }
        });

        no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (no.isChecked()) {
                    yes.setTextColor(Color.parseColor("#0867A3"));
                    no.setTextColor(Color.parseColor("#E77129"));
                    Utils.saveByDefaultSendMsgYesNo(getActivity(), false);
                }
            }
        });

        mail_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mail_yes.isChecked()) {
                    mail_yes.setTextColor(Color.parseColor("#E77129"));
                    mail_no.setTextColor(Color.parseColor("#0867A3"));
                    Utils.saveByDefaultSendMailYesNo(getActivity(), true);
                }
            }
        });

        mail_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mail_no.isChecked()) {
                    mail_yes.setTextColor(Color.parseColor("#0867A3"));
                    mail_no.setTextColor(Color.parseColor("#E77129"));
                    Utils.saveByDefaultSendMailYesNo(getActivity(), false);
                }
            }
        });


        every_one.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (every_one.isChecked()) {
                    every_one.setTextColor(Color.parseColor("#E77129"));
                    none.setTextColor(Color.parseColor("#0867A3"));
                    only_my_contact.setTextColor(Color.parseColor("#0867A3"));
                }
            }
        });

        none.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (none.isChecked()) {
                    every_one.setTextColor(Color.parseColor("#0867A3"));
                    none.setTextColor(Color.parseColor("#E77129"));
                    only_my_contact.setTextColor(Color.parseColor("#0867A3"));
                }
            }
        });

        only_my_contact.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (only_my_contact.isChecked()) {
                    every_one.setTextColor(Color.parseColor("#0867A3"));
                    none.setTextColor(Color.parseColor("#0867A3"));
                    only_my_contact.setTextColor(Color.parseColor("#E77129"));
                }
            }
        });


        String custom_status = Utils.getUserCustomStatus(getActivity());

        if (custom_status.equalsIgnoreCase(GlobalData.AVAILABLE)) {
            available.setChecked(true);
            available.setTextColor(Color.parseColor("#E77129"));

        } else if (custom_status.equalsIgnoreCase(GlobalData.BUSY)) {
            busy.setChecked(true);
            busy.setTextColor(Color.parseColor("#E77129"));
        } else if (custom_status.equalsIgnoreCase(GlobalData.INVISIABLE)) {
            invisable.setChecked(true);
            invisable.setTextColor(Color.parseColor("#E77129"));
        } else if (custom_status.equalsIgnoreCase(GlobalData.ONLY)) {
            only.setChecked(true);
            only.setTextColor(Color.parseColor("#E77129"));
        } else {
            available.setChecked(true);
            available.setTextColor(Color.parseColor("#E77129"));
        }


        available.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (available.isChecked()) {

                    if (GlobalData.connection != null && GlobalData.connection.isConnected() && GlobalData.connection.isAuthenticated()) {
                        String msgtime = Utils.currentTimeStap();
                        Presence presence = new Presence(Presence.Type.available, "available" + GlobalData.status_time_separater + msgtime, 0, Mode.available);
                        presence.setStatus(GlobalData.AVAILABLE + GlobalData.status_time_separater + msgtime);
                        //Presence presence = new Presence(Presence.Type.available);
                        GlobalData.connection.sendPacket(presence);
                        Utils.saveUserCustomStatus(getActivity(), GlobalData.AVAILABLE);
                        available.setTextColor(Color.parseColor("#E77129"));
                        busy.setTextColor(Color.parseColor("#0867A3"));
                        invisable.setTextColor(Color.parseColor("#0867A3"));
                        only.setTextColor(Color.parseColor("#0867A3"));
                    }
                    //Toast.makeText(getActivity(), "available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        busy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (busy.isChecked()) {

                    if (GlobalData.connection != null && GlobalData.connection.isConnected() && GlobalData.connection.isAuthenticated()) {

                        String msgtime = Utils.currentTimeStap();
                        Presence presence = new Presence(Presence.Type.available, "busy" + GlobalData.status_time_separater + msgtime, 0, Mode.available);
                        presence.setStatus(GlobalData.BUSY + GlobalData.status_time_separater + msgtime);

                        GlobalData.connection.sendPacket(presence);
                        Utils.saveUserCustomStatus(getActivity(), GlobalData.BUSY);

                        available.setTextColor(Color.parseColor("#0867A3"));
                        busy.setTextColor(Color.parseColor("#E77129"));
                        invisable.setTextColor(Color.parseColor("#0867A3"));
                        only.setTextColor(Color.parseColor("#0867A3"));
                    }
                    //	Toast.makeText(getActivity(), "busy", Toast.LENGTH_SHORT).show();
                }
            }
        });

        invisable.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (invisable.isChecked()) {
                    if (GlobalData.connection != null && GlobalData.connection.isConnected() && GlobalData.connection.isAuthenticated()) {
                        String msgtime = Utils.currentTimeStap();
                        Presence presence = new Presence(Presence.Type.available, "invisiable" + GlobalData.status_time_separater + msgtime, 0, Mode.available);
                        presence.setStatus(GlobalData.INVISIABLE + GlobalData.status_time_separater + msgtime);
                        GlobalData.connection.sendPacket(presence);
                        Utils.saveUserCustomStatus(getActivity(), GlobalData.INVISIABLE);

                        available.setTextColor(Color.parseColor("#0867A3"));
                        busy.setTextColor(Color.parseColor("#0867A3"));
                        invisable.setTextColor(Color.parseColor("#E77129"));
                        only.setTextColor(Color.parseColor("#0867A3"));
                    }
                    //	Toast.makeText(getActivity(), "invisable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        only.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (only.isChecked()) {
                    if (GlobalData.connection != null && GlobalData.connection.isConnected() && GlobalData.connection.isAuthenticated()) {
                        String msgtime = Utils.currentTimeStap();
                        Presence presence = new Presence(Presence.Type.available, "original" + GlobalData.status_time_separater + msgtime, 0, Mode.available);
                        presence.setStatus(GlobalData.ONLY + GlobalData.status_time_separater + msgtime);
                        GlobalData.connection.sendPacket(presence);
                        Utils.saveUserCustomStatus(getActivity(), GlobalData.ONLY);

                        available.setTextColor(Color.parseColor("#0867A3"));
                        busy.setTextColor(Color.parseColor("#0867A3"));
                        invisable.setTextColor(Color.parseColor("#0867A3"));
                        only.setTextColor(Color.parseColor("#E77129"));
                    }
                    //	Toast.makeText(getActivity(), "only", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (group_default_ring == v) {

            group_default_ring.setTextColor(Color.parseColor("#E77129"));
            group_select_ring.setTextColor(Color.parseColor("#0867A3"));
            group_vibrate_only.setTextColor(Color.parseColor("#0867A3"));
            group_ring_and_vibrate.setTextColor(Color.parseColor("#0867A3"));

            group_default_ring.setChecked(true);
            group_select_ring.setChecked(false);
            group_vibrate_only.setChecked(false);
            group_ring_and_vibrate.setChecked(false);


        }

        if (group_select_ring == v) {

            group_default_ring.setTextColor(Color.parseColor("#0867A3"));
            group_select_ring.setTextColor(Color.parseColor("#E77129"));
            group_vibrate_only.setTextColor(Color.parseColor("#0867A3"));
            group_ring_and_vibrate.setTextColor(Color.parseColor("#0867A3"));

            group_default_ring.setChecked(false);
            group_select_ring.setChecked(true);
            group_vibrate_only.setChecked(false);
            group_ring_and_vibrate.setChecked(false);

        }

        if (group_vibrate_only == v) {

            group_default_ring.setTextColor(Color.parseColor("#0867A3"));
            group_select_ring.setTextColor(Color.parseColor("#0867A3"));
            group_vibrate_only.setTextColor(Color.parseColor("#E77129"));
            group_ring_and_vibrate.setTextColor(Color.parseColor("#0867A3"));

            group_default_ring.setChecked(false);
            group_select_ring.setChecked(false);
            group_vibrate_only.setChecked(true);
            group_ring_and_vibrate.setChecked(false);


        }
        if (group_ring_and_vibrate == v) {

            group_default_ring.setTextColor(Color.parseColor("#0867A3"));
            group_select_ring.setTextColor(Color.parseColor("#0867A3"));
            group_vibrate_only.setTextColor(Color.parseColor("#0867A3"));
            group_ring_and_vibrate.setTextColor(Color.parseColor("#E77129"));

            group_default_ring.setChecked(false);
            group_select_ring.setChecked(false);
            group_vibrate_only.setChecked(false);
            group_ring_and_vibrate.setChecked(true);

        }


        if (default_ring == v) {

            default_ring.setTextColor(Color.parseColor("#E77129"));
            select.setTextColor(Color.parseColor("#0867A3"));
            vibrate_ring.setTextColor(Color.parseColor("#0867A3"));
            vibrate_and_ring.setTextColor(Color.parseColor("#0867A3"));

            default_ring.setChecked(true);
            select.setChecked(false);
            vibrate_ring.setChecked(false);
            vibrate_and_ring.setChecked(false);


        }

        if (select == v) {

            default_ring.setTextColor(Color.parseColor("#0867A3"));
            select.setTextColor(Color.parseColor("#E77129"));
            vibrate_ring.setTextColor(Color.parseColor("#0867A3"));
            vibrate_and_ring.setTextColor(Color.parseColor("#0867A3"));

            default_ring.setChecked(false);
            select.setChecked(true);
            vibrate_ring.setChecked(false);
            vibrate_and_ring.setChecked(false);

        }

        if (vibrate_ring == v) {

            default_ring.setTextColor(Color.parseColor("#0867A3"));
            select.setTextColor(Color.parseColor("#0867A3"));
            vibrate_ring.setTextColor(Color.parseColor("#E77129"));
            vibrate_and_ring.setTextColor(Color.parseColor("#0867A3"));


            default_ring.setChecked(false);
            select.setChecked(false);
            vibrate_ring.setChecked(true);
            vibrate_and_ring.setChecked(false);


        }
        if (vibrate_and_ring == v) {

            default_ring.setTextColor(Color.parseColor("#0867A3"));
            select.setTextColor(Color.parseColor("#0867A3"));
            vibrate_ring.setTextColor(Color.parseColor("#0867A3"));
            vibrate_and_ring.setTextColor(Color.parseColor("#E77129"));

            default_ring.setChecked(false);
            select.setChecked(false);
            vibrate_ring.setChecked(false);
            vibrate_and_ring.setChecked(true);

        }


        if (forever == v) {

            forever.setTextColor(Color.parseColor("#E77129"));
            mTendays.setTextColor(Color.parseColor("#0867A3"));
            mThirtyDay.setTextColor(Color.parseColor("#0867A3"));
            unlimeted.setTextColor(Color.parseColor("#0867A3"));

        }

        if (mTendays == v) {

            forever.setTextColor(Color.parseColor("#0867A3"));
            mTendays.setTextColor(Color.parseColor("#E77129"));
            mThirtyDay.setTextColor(Color.parseColor("#0867A3"));
            unlimeted.setTextColor(Color.parseColor("#0867A3"));

        }

        if (mThirtyDay == v) {

            forever.setTextColor(Color.parseColor("#0867A3"));
            mTendays.setTextColor(Color.parseColor("#0867A3"));
            mThirtyDay.setTextColor(Color.parseColor("#E77129"));
            unlimeted.setTextColor(Color.parseColor("#0867A3"));

        }
        if (unlimeted == v) {

            forever.setTextColor(Color.parseColor("#0867A3"));
            mTendays.setTextColor(Color.parseColor("#0867A3"));
            mThirtyDay.setTextColor(Color.parseColor("#0867A3"));
            unlimeted.setTextColor(Color.parseColor("#E77129"));

        }

    }


    @SuppressLint("NewApi")
    public void showTimePicker() {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("Time Picker");
        d.setContentView(R.layout.numeric_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
        np.setMaxValue(23); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);


        np2.setMaxValue(59); // max value 100
        np2.setMinValue(1);   // min value 0
        np2.setWrapSelectorWheel(false);


        // np.setOnValueChangedListener(this);
        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setTag(String.valueOf(np.getValue()) + " : " + String.valueOf(np2.getValue()));

                if (np2.getValue() < 10) {
                    time.setText("" + String.valueOf(np.getValue()) + " : " + "0" + String.valueOf(np2.getValue()) + " Min "); //set the value to textview
                    Utils.saveExpiryTimeValue(getActivity(), String.valueOf(np.getValue()) + " : " + "0" + String.valueOf(np2.getValue()));
                } else {
                    time.setText("" + String.valueOf(np.getValue()) + " : " + String.valueOf(np2.getValue()) + " Min "); //set the value to textview
                    Utils.saveExpiryTimeValue(getActivity(), String.valueOf(np.getValue()) + " : " + String.valueOf(np2.getValue()));
                }


                d.dismiss();


            }
        });
        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }

    public void showTimePickerMail() {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("Time Picker");
        d.setContentView(R.layout.numeric_picker);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
        np.setMaxValue(23); // max value 100
        np.setMinValue(0);   // min value 0
        np.setWrapSelectorWheel(false);


        np2.setMaxValue(59); // max value 100
        np2.setMinValue(1);   // min value 0
        np2.setWrapSelectorWheel(false);


        // np.setOnValueChangedListener(this);
        b1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setTag(String.valueOf(np.getValue()) + " : " + String.valueOf(np2.getValue()));

                if (np2.getValue() < 10) {
                    mail_time.setText("" + String.valueOf(np.getValue()) + " : " + "0" + String.valueOf(np2.getValue()) + " Min "); //set the value to textview
                    Utils.saveExpiryTimeValueMail(getActivity(), String.valueOf(np.getValue()) + " : " + "0" + String.valueOf(np2.getValue()));
                } else {
                    mail_time.setText("" + String.valueOf(np.getValue()) + " : " + String.valueOf(np2.getValue()) + " Min "); //set the value to textview
                    Utils.saveExpiryTimeValueMail(getActivity(), String.valueOf(np.getValue()) + " : " + String.valueOf(np2.getValue()));
                }
                d.dismiss();
            }
        });
        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        homeActivity.onBothTabPageControlIsGone();
        homeActivity.getActionbar_title().setText(mLastTitle);
        homeActivity.getLayoutTab_contact_chat().setVisibility(View.VISIBLE);
        homeActivity.getLayout_name_status().setVisibility(View.GONE);
        ConstantFields.HIDE_MENU = LAST_HIDE_MENU;
        homeActivity.invalidateOptionMenuItem();
    }
}
