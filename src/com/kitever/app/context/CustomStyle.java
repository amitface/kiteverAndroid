package com.kitever.app.context;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

public class CustomStyle {

   public static Typeface robotothin;

    public static String HEADER_BACKGROUND="#006966";
    public static String HEADER_FONT_COLOR="#FFFFFF";
    public static String THEME_FONT_COLOR="#006966";
    public static String FLOATING_ACTION_BACKGROUND="#006966";

    public static String SIGNUP_BACKGROUND="#006966";
    public static String SIGNUP_FONT_COLOR="#757575";

    public static String TAB_BACKGROUND="#006966";
    public static String TAB_FONT_COLOR="#FFFFFF";
    public static String TAB_INDICATOR="#E46C22";

    public static String FOOTER_BACKGROUND="#616161";
    public static String FOOTER_ICON_COLOR_UNSELECTED="#B0BEC5";
    public static String FOOTER_ICON_COLOR_SELECTED="#F1F1F1";


    public static String PAGE_BACKGROUND="#FFFFFF";
    public static String LISTVIEW_FONT_COLOR="#757575";
    public static String LISTVIEW_BACKGROUND="#F5F5F5";
    public static String LISTVIEW_BACKGROUND_ENABLED="#FFFFFF";
    public static String LISTVIEW_BACKGROUND_DISABLED="#DDDDDD";



    public static void setRobotoThinFont(TextView tv, Context ctx) {

       if(robotothin!=null) tv.setTypeface(robotothin);
    }

    public static void setRobotoThinFontSWitch(Switch tv, Context ctx) {
        if(robotothin!=null) tv.setTypeface(robotothin);
    }

    public static void setRobotoThinFontToggleButton(ToggleButton tv, Context ctx) {
        if(robotothin!=null) tv.setTypeface(robotothin);
    }

    public static void setRobotoThinFontButton(Button tv, Context ctx) {
        if(robotothin!=null) tv.setTypeface(robotothin);
    }

    public static void setRobotoThinFontcheckbox(CheckBox tv, Context ctx) {
        if(robotothin!=null) tv.setTypeface(robotothin);
    }

    public static void setRobotoThinFontradio(RadioButton tv, Context ctx) {
        if(robotothin!=null) tv.setTypeface(robotothin);
    }



    public static void defualtStyle(Context context)
    {
        robotothin = Typeface.createFromAsset(context.getAssets(),"fonts/roboto.regular.ttf");
        HEADER_BACKGROUND="#006966";
        HEADER_FONT_COLOR="#FFFFFF";
        THEME_FONT_COLOR="#383933";

        TAB_BACKGROUND="#006966";
        TAB_FONT_COLOR="#FFFFFF";
        TAB_INDICATOR="#E46C22";

        FOOTER_BACKGROUND="#616161";
        FOOTER_ICON_COLOR_UNSELECTED="#B0BEC5";
        FOOTER_ICON_COLOR_SELECTED="#F1F1F1";

        FLOATING_ACTION_BACKGROUND="#E46C22";


        PAGE_BACKGROUND="#FFFFFF";
        LISTVIEW_FONT_COLOR="#757575";
        LISTVIEW_BACKGROUND="#F5F5F5";
        LISTVIEW_BACKGROUND_ENABLED="#FFFFFF";
        LISTVIEW_BACKGROUND_DISABLED="#DDDDDD";
    }
    public static void Style1(Context context)
    {
       robotothin = Typeface.createFromAsset(context.getAssets(),"fonts/roboto.regular.ttf");
       HEADER_BACKGROUND="#0E2328";
       HEADER_FONT_COLOR="#FFFFFF";
       THEME_FONT_COLOR="#383933";

       TAB_BACKGROUND="#0E2328";
       TAB_FONT_COLOR="#FFFFFF";
       TAB_INDICATOR="#FFFFFF";

        FOOTER_BACKGROUND="#0E2328";
        FOOTER_ICON_COLOR_UNSELECTED="#B0BEC5";
        FOOTER_ICON_COLOR_SELECTED="#F1F1F1";

        FLOATING_ACTION_BACKGROUND="#0E2328";

       PAGE_BACKGROUND="#FFFFFF";
       LISTVIEW_FONT_COLOR="#757575";
        LISTVIEW_BACKGROUND="#F5F5F5";
        LISTVIEW_BACKGROUND_ENABLED="#FFFFFF";
        LISTVIEW_BACKGROUND_DISABLED="#DDDDDD";
    }
    public static void Style2(Context context)
    {
        robotothin = Typeface.createFromAsset(context.getAssets(),"Dosis-Bold.otf");
        HEADER_BACKGROUND="#308CCF";
        HEADER_FONT_COLOR="#FFFFFF";
        THEME_FONT_COLOR="#383933";

        TAB_BACKGROUND="#308CCF";
        TAB_FONT_COLOR="#FFFFFF";
        TAB_INDICATOR="#FFFFFF";

        FOOTER_BACKGROUND="#308CCF";
        FOOTER_ICON_COLOR_UNSELECTED="#B0BEC5";
        FOOTER_ICON_COLOR_SELECTED="#F1F1F1";

        FLOATING_ACTION_BACKGROUND="#308CCF";

        PAGE_BACKGROUND="#FFFFFF";
        LISTVIEW_FONT_COLOR="#757575";
        LISTVIEW_BACKGROUND="#F5F5F5";
        LISTVIEW_BACKGROUND_ENABLED="#FFFFFF";
        LISTVIEW_BACKGROUND_DISABLED="#DDDDDD";
    }
    public static void Style3(Context context)
    {
        robotothin = Typeface.createFromAsset(context.getAssets(),"MyriadPro-Regular.otf");
        HEADER_BACKGROUND="#E61A5F";
        HEADER_FONT_COLOR="#FFFFFF";
        THEME_FONT_COLOR="#383933";


        TAB_BACKGROUND="#E61A5F";
        TAB_FONT_COLOR="#FFFFFF";
        TAB_INDICATOR="#FFFFFF";

        FOOTER_BACKGROUND="#E61A5F";
        FOOTER_ICON_COLOR_UNSELECTED="#B0BEC5";
        FOOTER_ICON_COLOR_SELECTED="#F1F1F1";

        FLOATING_ACTION_BACKGROUND="#F15A24";
        PAGE_BACKGROUND="#FFFFFF";
        LISTVIEW_FONT_COLOR="#757575";
        LISTVIEW_BACKGROUND="#F5F5F5";
        LISTVIEW_BACKGROUND_ENABLED="#FFFFFF";
        LISTVIEW_BACKGROUND_DISABLED="#DDDDDD";
    }

    /*
  For custom spinner font

   */
    public static class MySpinnerAdapter extends ArrayAdapter<String> {

        /*  Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/roboto.regular.ttf");
        */
        public MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTextColor(Color.parseColor(THEME_FONT_COLOR));
            if(robotothin!=null)  view.setTypeface(robotothin);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTextColor(Color.parseColor(THEME_FONT_COLOR));
            if(robotothin!=null)  view.setTypeface(robotothin);
            return view;
        }
    }
}
