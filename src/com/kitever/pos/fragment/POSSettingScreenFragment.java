package com.kitever.pos.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.app.context.StringWithTag;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;
import com.kitever.pos.fragment.Interfaces.ExecuteService;
import com.kitever.pos.model.data.InvoiceSetting;
import com.kitever.pos.model.data.PosSettingReportModelData;
import com.kitever.pos.model.data.PosSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontButton;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontSWitch;
import static com.kitever.app.context.CustomStyle.setRobotoThinFontradio;
import static com.kitever.utils.Utils.actionBarSettingWithBack;
import static sms19.inapp.msg.constant.Apiurls.KIT19_BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link POSSettingScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class POSSettingScreenFragment extends Fragment implements NetworkManager, ExecuteService, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private final int KEY_FETCH_INVOICE_SETTINGS = 1;
    private final int KEY_INSERT_UPDATE_INVOICE_SETTINGS = 2;
    private final int KEY_FETCH_REPORT_SETTING = 3;
    private final int KEY_SET_REPORT_SETTING = 4;
    private final int KEY_SET_CREDIT_SMS_SETTING = 5;
    private final int KEY_SET_CREDIT_MAIL_SETTING = 6;
    private final int KEY_SET_INVOICE_SMS_SETTING = 7;
    private final int KEY_SET_INVOICE_MAIL_SETTING = 8;
    private final int KEY_SET_PAYMENT_SMS_SETTING = 9;
    private final int KEY_SET_PAYMENT_MAIL_SETTING = 10;
    private final int KEY_FETCH_MAIL_TEMPLATE = 11;
    private final int KEY_FETCH_SMS_TEMPLATE = 12;
    private final int KEY_FETCH_PAYMENTGATEWAY_SETTINGS = 13;
    private final int KEY_SET_PAYMENTGATEWAY_SETTINGS = 14;
    private final int KEY_SET_PAYMENTGATEWAY_ACTIVATION = 15;
    ArrayList<InvoiceSetting> settingList;
    Boolean paymentgatewayStatus = false;
    // TODO: Rename and change types of parameters
    private String UserId;
    private String UserLogin;
    private String PassWord;
    private RelativeLayout invoiceLayout, reportLayout, alertLayout, payment_gateway_layout, editLayout;
    private ScrollView sv;
    private Bitmap bitmapDropdown = null;
    private LinearLayout invoiceContent, alertContent, reportContent, payment_content;
    private int statusInvoice = 0, statusAlert = 0, statusReport = 0, statuspayment = 0;
    private Switch toggle1, toggle2, toggle3, toggle4, toggle5, toggle6;
    private TextView tvInvoicePrefix, tvServiceTax, tvTin, tvPanNo, tvGst, tvInvoiceNoStart, tvReceiptNoStart, tvOrderPrefix;
    private EditText etServiceTax, etTin, etPanNo, etGst, etInvoicePrefix, etInvoiceNoStart, etReceiptNoStart, etOrderPrefix;
    private EditText payment_key, payment_salt;
    private TextView payment_link,payment_key_txt, payment_salt_txt;
    private Button payment_save;
    private Switch payment_activation;
    private Spinner payment_spinner;
    private ImageView invoicePopUp, creditPopUp;
    private Boolean statusHeader = true, statusFooter = true, statusPan = true, statusDaily = true, statusWeekly = true, statusMonthly = true, statusdOrder = true, statusdPayments = true, statusdCredits = true,
            statuswOrder = true, statuswPayments = true, statuswCredits = true, statusmOrder = true, statusmPayments = true, statusmCredits = true;
    private RadioGroup group;
    private RadioButton btnRadio1, btnRadio2;
    private AlertDialog dialog;
    private Button btnSaveSetting;
    private String PaymentGateway = "PayUMoney";
    private EditText headerContent, footerContent, prefixContent, orderPrefixContent, tinNoContent, panNoContent, serviceTaxNoContent;
    private String headerString = "", footerString = "", prefixString = "", orderString = "", tinString = "", panString = "", serviceString = "";
    //Textview for sms and mail icon
    private TextView sms_invoice, mail_invoice, sms_payment, mail_payment, sms_credit, mail_credit;
    private TextView report_order_sms, report_order_mail, report_payment_sms, report_payment_mail, report_credit_sms, report_credit_mail;
    private MoonIcon mIcon;
    private PosSettingReportModelData posReport;
    private String rs1 = "false", rs2 = "false", rs3 = "false", rs4 = "false", rs5 = "false", rs6 = "false", mode = "Mail", type = "Order";
    private ArrayAdapter<StringWithTag> dataAdapter;
    private Spinner temlate_spinner;
    private int template_position = 0;
    private String temaplate_title = "", Template_ID = "0", keyvalue, saltvalue, paymentgateway_link;
    private RadioGroup radioGroup;
    private LinearLayout link_layout;
    private Switch invoice_header_enable, invoice_footer_enable, receipt_header_enable, receipt_footer_enable;
    private ImageView invoice_header_click, invoice_footer_click, receipt_header_click, receipt_footer_click;
    private TextView setting_invoice_title,setting_alert_title,setting_report_title,setting_gateway_title,note_txt;
    private TextView invoice_radio_title,alert_order,alert_payment,alert_credit,report_order,report_payment,report_credit;
    RelativeLayout taxes_layout,other_charges_layout;
    TextView setting_tax_title, other_charges_title;

    String htmlstr = "<p>Hello test html <br mce_bogus=\"1\"></p><p>this is html and image <img src=\"https://lh3.googleusercontent.com/-Zm6DyqZOmik/WE5Lz1hhZKI/AAAAAAAAAO0/m1HYPbHixf8ZQOgY_oT3qHvvaYbQOTsJwCL0B/h983/Logo.jpg\" mce_src=\"https://lh3.googleusercontent.com/-Zm6DyqZOmik/WE5Lz1hhZKI/AAAAAAAAAO0/m1HYPbHixf8ZQOgY_oT3qHvvaYbQOTsJwCL0B/h983/Logo.jpg\" height=\"120\" width=\"100\"></p><p></p>";
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            String str = s.toString();
            if (etInvoicePrefix.getText().hashCode() == s.hashCode()) {
                if (s.length() > 0)
                    tvInvoicePrefix.setText("Invoice Prefix*");
                else if (s.length() == 0)
                    tvInvoicePrefix.setText("");

            } else if (etServiceTax.getText().hashCode() == s.hashCode()) {
                if (s.length() > 0)
                    tvServiceTax.setText("Service Tax");
                else if (s.length() == 0)
                    tvServiceTax.setText("");

            } else if (etTin.getText().hashCode() == s.hashCode()) {
                if (s.length() > 0)
                    tvTin.setText("Tin");
                else if (s.length() == 0)
                    tvTin.setText("");

            } else if (etPanNo.getText().hashCode() == s.hashCode()) {
                if (s.length() > 0)
                    tvPanNo.setText("Pan No");
                else if (s.length() == 0)
                    tvPanNo.setText("");

            } else if (etGst.getText().hashCode() == s.hashCode()) {
                if (s.length() > 0)
                    tvGst.setText("GST (coming soon)");
                else if (s.length() == 0)
                    tvGst.setText("");

            } else if (etInvoiceNoStart.getText().hashCode() == s.hashCode()) {
                if (s.length() > 0)
                    tvInvoiceNoStart.setText("Invoice No. Start*");
                else if (s.length() == 0)
                    tvInvoiceNoStart.setText("");
            } else if (etReceiptNoStart.getText().hashCode() == s.hashCode()) {
                if (s.length() > 0)
                    tvReceiptNoStart.setText("Receipt No. Start*");
                else if (s.length() == 0)
                    tvReceiptNoStart.setText("");
            } else if (etOrderPrefix.getText().hashCode() == s.hashCode()) {
                if (s.length() > 0)
                    tvOrderPrefix.setText("Order Prefix*");
                else if (s.length() == 0)
                    tvOrderPrefix.setText("");
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    public POSSettingScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment POSSettingScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static POSSettingScreenFragment newInstance(String param1, String param2, String param3) {
        POSSettingScreenFragment fragment = new POSSettingScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserId = getArguments().getString(ARG_PARAM1);
            UserLogin = getArguments().getString(ARG_PARAM2);
            PassWord = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pos_setting_layout, container, false);
        setScreen(view);
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                posReport = new PosSettingReportModelData();
                fetchInvoiceSettings();
                fetchReportSettings();
                FetchPaymentSettings();
            }
        }, 500);
        return view;
    }

    private void setScreen(View view) {

        mIcon = new MoonIcon(this);
        bitmapDropdown = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.back_btn);

        RelativeLayout page_main_layout=(RelativeLayout)view.findViewById(R.id.page_main_layout);
        page_main_layout.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));


        invoiceLayout = (RelativeLayout) view.findViewById(R.id.invoice_layout);
        reportLayout = (RelativeLayout) view.findViewById(R.id.report_layout);
        alertLayout = (RelativeLayout) view.findViewById(R.id.alert_layout);
        payment_gateway_layout= (RelativeLayout) view.findViewById(R.id.payment_gateway_layout);

        taxes_layout = (RelativeLayout) view.findViewById(R.id.taxes_layout);
        other_charges_layout = (RelativeLayout) view.findViewById(R.id.other_charges_layout);

        //printLayout = (RelativeLayout) findViewById(R.id.print_layout);
        invoiceContent = (LinearLayout) view.findViewById(R.id.invoice_content);
        reportContent = (LinearLayout) view.findViewById(R.id.report_content);
        alertContent = (LinearLayout) view.findViewById(R.id.alert_content);
        payment_content = (LinearLayout) view.findViewById(R.id.payment_content);

        btnSaveSetting = (Button) view.findViewById(R.id.submit_setting);

        setRobotoThinFontButton(btnSaveSetting,getActivity());
        btnSaveSetting.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        btnSaveSetting.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

        setting_invoice_title = (TextView) view.findViewById(R.id.setting_invoice_title);
        setting_alert_title = (TextView) view.findViewById(R.id.setting_alert_title);
        setting_report_title = (TextView) view.findViewById(R.id.setting_report_title);
        setting_gateway_title = (TextView) view.findViewById(R.id.setting_gateway_title);
        setting_tax_title = (TextView) view.findViewById(R.id.setting_tax_title);
        other_charges_title = (TextView) view.findViewById(R.id.other_charges_title);
        note_txt= (TextView) view.findViewById(R.id.note_txt);

        setRobotoThinFont(setting_invoice_title,getActivity());
        setRobotoThinFont(setting_alert_title,getActivity());
        setRobotoThinFont(setting_report_title,getActivity());
        setRobotoThinFont(setting_gateway_title,getActivity());
        setRobotoThinFont(note_txt,getActivity());
        setRobotoThinFont(setting_tax_title,getActivity());
        setRobotoThinFont(other_charges_title,getActivity());

        setting_invoice_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        setting_alert_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        setting_report_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        setting_gateway_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        note_txt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        setting_tax_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        other_charges_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        sms_invoice = (TextView) view.findViewById(R.id.sms_invoice);
        mail_invoice = (TextView) view.findViewById(R.id.mail_invoice);
        sms_payment = (TextView) view.findViewById(R.id.sms_payment);
        mail_payment = (TextView) view.findViewById(R.id.mail_payment);
        sms_credit = (TextView) view.findViewById(R.id.sms_credit);
        mail_credit = (TextView) view.findViewById(R.id.mail_credit);

        report_order_sms = (TextView) view.findViewById(R.id.report_order_sms);
        report_order_mail = (TextView) view.findViewById(R.id.report_order_mail);
        report_payment_sms = (TextView) view.findViewById(R.id.report_payment_sms);
        report_payment_mail = (TextView) view.findViewById(R.id.report_payment_mail);
        report_credit_sms = (TextView) view.findViewById(R.id.report_credit_sms);
        report_credit_mail = (TextView) view.findViewById(R.id.report_credit_mail);

        payment_spinner = (Spinner) view.findViewById(R.id.payment_spinner);
        payment_key = (EditText) view.findViewById(R.id.payment_key);
        payment_salt = (EditText) view.findViewById(R.id.payment_salt);
        payment_link = (TextView) view.findViewById(R.id.payment_link);
        payment_save = (Button) view.findViewById(R.id.payment_save);
        payment_activation = (Switch) view.findViewById(R.id.payment_activation);
        link_layout = (LinearLayout) view.findViewById(R.id.link_layout);
        link_layout.setVisibility(View.GONE);

        invoice_header_enable = (Switch) view.findViewById(R.id.invoice_header_enable);
        invoice_footer_enable = (Switch) view.findViewById(R.id.invoice_footer_enable);
        receipt_header_enable = (Switch) view.findViewById(R.id.receipt_header_enable);
        receipt_footer_enable = (Switch) view.findViewById(R.id.receipt_footer_enable);

        invoice_header_click = (ImageView) view.findViewById(R.id.invoice_header_click);
        invoice_footer_click = (ImageView) view.findViewById(R.id.invoice_footer_click);
        receipt_header_click = (ImageView) view.findViewById(R.id.receipt_header_click);
        receipt_footer_click = (ImageView) view.findViewById(R.id.receipt_footer_click);

        setRobotoThinFont(payment_key, getActivity());
        setRobotoThinFont(payment_salt, getActivity());
        setRobotoThinFont(payment_link, getActivity());
        setRobotoThinFontSWitch(payment_activation, getActivity());

        mIcon.setTextfont(sms_invoice);
        mIcon.setTextfont(mail_invoice);
        mIcon.setTextfont(sms_payment);
        mIcon.setTextfont(mail_payment);
        mIcon.setTextfont(sms_credit);
        mIcon.setTextfont(mail_credit);

        mIcon.setTextfont(report_order_sms);
        mIcon.setTextfont(report_order_mail);
        mIcon.setTextfont(report_payment_sms);
        mIcon.setTextfont(report_payment_mail);
        mIcon.setTextfont(report_credit_sms);
        mIcon.setTextfont(report_credit_mail);

        sms_invoice.setOnClickListener(this);
        mail_invoice.setOnClickListener(this);
        sms_payment.setOnClickListener(this);
        mail_payment.setOnClickListener(this);
        sms_credit.setOnClickListener(this);
        mail_credit.setOnClickListener(this);

        report_order_sms.setOnClickListener(this);
        report_order_mail.setOnClickListener(this);
        report_payment_sms.setOnClickListener(this);
        report_payment_mail.setOnClickListener(this);
        report_credit_sms.setOnClickListener(this);
        report_credit_mail.setOnClickListener(this);


     /*   headerContent = (EditText) view.findViewById(R.id.invoice_setting_header_content);
        footerContent = (EditText) view.findViewById(R.id.invoice_setting_footer_content);
        prefixContent = (EditText) view.findViewById(R.id.invoice_setting_prefix_content);
        orderPrefixContent = (EditText) view.findViewById(R.id.invoice_setting_order_prefix_content);
        tinNoContent = (EditText) view.findViewById(R.id.invoice_setting_tin_no_content);
        panNoContent = (EditText) view.findViewById(R.id.invoice_setting_pan_no_content);
        serviceTaxNoContent = (EditText) view.findViewById(R.id.invoice_setting_servicetax_no_content);

           setRobotoThinFont(headerContent, getActivity());
        setRobotoThinFont(footerContent, getActivity());
        setRobotoThinFont(prefixContent, getActivity());
        setRobotoThinFont(orderPrefixContent, getActivity());
        setRobotoThinFont(tinNoContent, getActivity());
        setRobotoThinFont(panNoContent, getActivity());
        setRobotoThinFont(serviceTaxNoContent, getActivity());

          headerContent.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        footerContent.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        prefixContent.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        orderPrefixContent.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tinNoContent.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        panNoContent.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        serviceTaxNoContent.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        */

        tvInvoicePrefix = (TextView) view.findViewById(R.id.setting_invoice_prefix_label);
        tvServiceTax = (TextView) view.findViewById(R.id.setting_service_tax_label);
        tvTin = (TextView) view.findViewById(R.id.setting_tin_label);
        tvPanNo = (TextView) view.findViewById(R.id.setting_pan_label);
        tvGst = (TextView) view.findViewById(R.id.setting_gst_label);
        tvInvoiceNoStart = (TextView) view.findViewById(R.id.setting_invoice_number_label);
        tvReceiptNoStart = (TextView) view.findViewById(R.id.setting_receipt_number_label);
        tvOrderPrefix = (TextView) view.findViewById(R.id.setting_order_prefix_label);


        etInvoicePrefix = (EditText) view.findViewById(R.id.setting_invoice_prefix);
        etServiceTax = (EditText) view.findViewById(R.id.setting_service_tax);
        etTin = (EditText) view.findViewById(R.id.setting_tin);
        etPanNo = (EditText) view.findViewById(R.id.setting_pan);
        etGst = (EditText) view.findViewById(R.id.setting_gst);
        etInvoiceNoStart = (EditText) view.findViewById(R.id.setting_invoice_number);
        etReceiptNoStart = (EditText) view.findViewById(R.id.setting_receipt_number);
        etOrderPrefix = (EditText) view.findViewById(R.id.setting_order_prefix);

        invoice_radio_title = (TextView) view.findViewById(R.id.invoice_radio_title);
        alert_order = (TextView) view.findViewById(R.id.alert_order);
        alert_payment = (TextView) view.findViewById(R.id.alert_payment);
        alert_credit = (TextView) view.findViewById(R.id.alert_credit);
        report_order = (TextView) view.findViewById(R.id.report_order);
        report_payment = (TextView) view.findViewById(R.id.report_payment);
        report_credit = (TextView) view.findViewById(R.id.report_credit);
        payment_key_txt = (TextView) view.findViewById(R.id.payment_key_txt);
        payment_salt_txt = (TextView) view.findViewById(R.id.payment_salt_txt);


        setRobotoThinFont(invoice_radio_title, getActivity());
        setRobotoThinFont(alert_order, getActivity());
        setRobotoThinFont(alert_payment, getActivity());
        setRobotoThinFont(alert_credit, getActivity());
        setRobotoThinFont(report_order, getActivity());
        setRobotoThinFont(report_payment, getActivity());
        setRobotoThinFont(report_credit, getActivity());
        setRobotoThinFont(payment_key_txt, getActivity());
        setRobotoThinFont(payment_salt_txt, getActivity());


        invoice_radio_title.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        alert_order.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        alert_payment.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        alert_credit.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        report_order.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        report_payment.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        report_credit.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        payment_key_txt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        payment_salt_txt.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));



        setRobotoThinFont(tvInvoicePrefix, getActivity());
        setRobotoThinFont(tvServiceTax, getActivity());
        setRobotoThinFont(tvTin, getActivity());
        setRobotoThinFont(tvPanNo, getActivity());
        setRobotoThinFont(tvGst, getActivity());
        setRobotoThinFont(tvInvoiceNoStart, getActivity());
        setRobotoThinFont(tvReceiptNoStart, getActivity());
        setRobotoThinFont(tvOrderPrefix, getActivity());
        setRobotoThinFont(etInvoicePrefix, getActivity());
        setRobotoThinFont(etServiceTax, getActivity());
        setRobotoThinFont(etTin, getActivity());
        setRobotoThinFont(etPanNo, getActivity());
        setRobotoThinFont(etGst, getActivity());
        setRobotoThinFont(etInvoiceNoStart, getActivity());
        setRobotoThinFont(etReceiptNoStart, getActivity());
        setRobotoThinFont(etOrderPrefix, getActivity());



        payment_save.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
        payment_save.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        tvInvoicePrefix.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvServiceTax.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvTin.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvPanNo.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvGst.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        tvInvoiceNoStart.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvReceiptNoStart.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        tvOrderPrefix.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etInvoicePrefix.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etServiceTax.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etTin.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

        etPanNo.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etGst.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etInvoiceNoStart.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etReceiptNoStart.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));
        etOrderPrefix.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));



        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        btnRadio1 = (RadioButton) view.findViewById(R.id.radio_yes);
        btnRadio2 = (RadioButton) view.findViewById(R.id.radio_no);

        setRobotoThinFontradio(btnRadio1,getActivity());
        setRobotoThinFontradio(btnRadio2,getActivity());



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_yes:
                        if (settingList != null) settingList.get(0).setOrderPrefix("true");
                        break;
                    case R.id.radio_no:
                        if (settingList != null) settingList.get(0).setOrderPrefix("false");
                        break;
                }

            }
        });

        invoice_header_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htmlpoupup("Invoice Header");
            }
        });

        invoice_footer_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htmlpoupup("Invoice Footer");
            }
        });

        receipt_header_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htmlpoupup("Invoice Header");
            }
        });

        receipt_footer_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                htmlpoupup("Invoice Footer");
            }
        });

        sv = (ScrollView) view.findViewById(R.id.scrollview_setting);
        etInvoicePrefix.addTextChangedListener(textWatcher);
        etServiceTax.addTextChangedListener(textWatcher);
        etTin.addTextChangedListener(textWatcher);
        etPanNo.addTextChangedListener(textWatcher);
        etGst.addTextChangedListener(textWatcher);
        etInvoiceNoStart.addTextChangedListener(textWatcher);
        etReceiptNoStart.addTextChangedListener(textWatcher);
        etOrderPrefix.addTextChangedListener(textWatcher);

        btnSaveSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                PostInvoiceSettings();
                invoiceContent.setVisibility(View.GONE);
            }
        });

        invoiceLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusInvoice == 0) {
                    payment_content.setVisibility(View.GONE);
                    invoiceContent.setVisibility(View.VISIBLE);
                    alertContent.setVisibility(View.GONE);
                    reportContent.setVisibility(View.GONE);
                    statusInvoice = 1;

                } else {
                    invoiceContent.setVisibility(View.GONE);
                    statusInvoice = 0;

                }
            }
        });

        alertLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusAlert == 0) {
                    payment_content.setVisibility(View.GONE);
                    alertContent.setVisibility(View.VISIBLE);
                    invoiceContent.setVisibility(View.GONE);
                    reportContent.setVisibility(View.GONE);
                    statusAlert = 1;

                    sv.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            sv.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } else {
                    alertContent.setVisibility(View.GONE);
                    statusAlert = 0;

                }
            }
        });


        payment_gateway_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statuspayment == 0) {
                    payment_content.setVisibility(View.VISIBLE);
                    alertContent.setVisibility(View.GONE);
                    invoiceContent.setVisibility(View.GONE);
                    reportContent.setVisibility(View.GONE);
                    statuspayment = 1;

                    sv.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            sv.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } else {
                    payment_content.setVisibility(View.GONE);
                    statuspayment = 0;

                }
            }
        });

        taxes_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarSettingWithBack(getActivity(),((AppCompatActivity)getActivity()).getSupportActionBar(),"Taxes");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.layoutPosFragmentHolder, POSTaxMasterScreenFragment.newInstance(UserId, UserLogin, PassWord), "POSTaxMasterScreenFragment").commit();

            }
        });

        other_charges_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBarSettingWithBack(getActivity(),((AppCompatActivity)getActivity()).getSupportActionBar(),"Other Charges");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.layoutPosFragmentHolder, POSOtherChargeScreenFragment.newInstance(UserId, UserLogin, PassWord), "POSOtherChargeScreenFragment").commit();

            }
        });

        payment_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentvalidation()) setPaymentSetting();
            }
        });

        payment_activation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PaymentGatewayActivation();
            }
        });

        reportLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (statusReport == 0) {
                    payment_content.setVisibility(View.GONE);
                    reportContent.setVisibility(View.VISIBLE);
                    alertContent.setVisibility(View.GONE);
                    invoiceContent.setVisibility(View.GONE);
                    statusReport = 1;
                    sv.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            sv.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                } else {
                    reportContent.setVisibility(View.GONE);
                    statusReport = 0;

                }
            }
        });

    }

    // payment_key and salt validation
    private boolean paymentvalidation() {
        keyvalue = payment_key.getText().toString().trim();
        saltvalue = payment_salt.getText().toString().trim();
        if (keyvalue.length() < 1) {
            Toast.makeText(getActivity(), "Enter the Payment Key", Toast.LENGTH_LONG).show();
            return false;
        }
        if (saltvalue.length() < 1) {
            Toast.makeText(getActivity(), "Enter the payment salt", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        onSetingClick(v);
        switch (id) {
            case R.id.sms_invoice:
                showInvoicePopUp(true, true, settingList.get(0).getOrderSMSCustomer(), settingList.get(0).getSmsTemplateId(), settingList.get(0).getTemplate_Title(), settingList.get(0).getOrderSMSTime(), settingList.get(0).getOrdersmstimeDelay(), settingList.get(0).getOrdersmsDaysDelay());
                break;
            case R.id.sms_payment:
                showInvoicePopUp(true, false, settingList.get(0).getPaymentSMSCustomer(), settingList.get(0).getSmsTemplateIdPayment(), settingList.get(0).getPaymentTemplate(), settingList.get(0).getPaymentSMSTime(), settingList.get(0).getPaymentsmstimeDelay(), settingList.get(0).getPaymentsmsDaysDelay());
                break;

            case R.id.sms_credit:
                showCreditPopUp(true, settingList.get(0).getCreditSMSCustomer(), settingList.get(0).getCreditTemplate(), settingList.get(0).getSmsTemplateIdCredit(), settingList.get(0).getDueDateSMSRadio(), settingList.get(0).getAfterDueDateSMSTime(), settingList.get(0).getAfterDueDateSMSPeriod(), settingList.get(0).getAfterDueDateSMSChecked());
                break;

            case R.id.mail_credit:
                showCreditPopUp(false, settingList.get(0).getCreditEmailCustomer(), settingList.get(0).getMail_Template_Credit(), settingList.get(0).getMailTemplateIdCredit(), settingList.get(0).getDueDateMailRadio(), settingList.get(0).getAfterDueDateTime(), settingList.get(0).getAfterDueDatePeriod(), settingList.get(0).getAfterDueDateChecked());
                break;

            case R.id.mail_invoice:
                showInvoicePopUp(false, true, settingList.get(0).getOrderEmailCustomer(), settingList.get(0).getMailTemplateId(), settingList.get(0).getMail_Template(), settingList.get(0).getOrderMailTime(), settingList.get(0).getOrdermailtimeDelay(), settingList.get(0).getOrdermailDaysDelay());
                break;
            case R.id.mail_payment:
                showInvoicePopUp(false, false, settingList.get(0).getPaymentEmailCustomer(), settingList.get(0).getMailTemplateIdPayment(), settingList.get(0).getMail_Payment_Template(), settingList.get(0).getPaymentMailTime(), settingList.get(0).getPaymentmailtimeDelay(), settingList.get(0).getPaymentmailDaysDelay());
                break;


            case R.id.report_order_sms:
                showReportPopUp(true, 1, posReport.getOrderDailySMS(), posReport.getOrderWeeklySMS(), posReport.getOrderMonthlySMS(), posReport.getOrderQuarterlySMS(), posReport.getOrderHalfYearlySMS(), posReport.getOrderYearlySMS());
                break;
            case R.id.report_payment_sms:
                showReportPopUp(true, 2, posReport.getPaymentDailySMS(), posReport.getPaymentWeeklySMS(), posReport.getPaymentMonthlySMS(), posReport.getPaymentQuarterlySMS(), posReport.getPaymentHalfYearlySMS(), posReport.getPaymentYearlySMS());
                break;
            case R.id.report_credit_sms:
                showReportPopUp(true, 3, posReport.getCreditsDailySMS(), posReport.getCreditsWeeklySMS(), posReport.getCreditsMonthlySMS(), posReport.getCreditsQuarterlySMS(), posReport.getCreditsHalfYearlySMS(), posReport.getCreditsYearlySMS());
                break;
            case R.id.report_order_mail:
                showReportPopUp(false, 1, posReport.getOrderDailyMail(), posReport.getOrderWeeklyMail(), posReport.getOrderMonthlyMail(), posReport.getOrderQuarterlyMail(), posReport.getOrderHalfYearlyMail(), posReport.getOrderYearlyMail());
                break;
            case R.id.report_payment_mail:
                showReportPopUp(false, 2, posReport.getPaymentDailyMail(), posReport.getPaymentWeeklyMail(), posReport.getPaymentMonthlyMail(), posReport.getPaymentQuarterlyMail(), posReport.getPaymentHalfYearlyMail(), posReport.getPaymentYearlyMail());
                break;
            case R.id.report_credit_mail:
                showReportPopUp(false, 3, posReport.getCreditsDailyMail(), posReport.getCreditsWeeklyMail(), posReport.getCreditsMonthlyMail(), posReport.getCreditsQuarterlyMail(), posReport.getCreditsHalfYearlyMail(), posReport.getCreditsYearlyMail());
                break;
        }
    }

    protected void showDialog(String string) {
        // TODO Auto-generated method stub
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(string)
                .setTitle("");

        // 3. Get the AlertDialog from create()
        dialog = builder.create();
        dialog.show();
    }

    private void PostInvoiceSettings() {

        if (Utils.isDeviceOnline(getActivity())) {
//           showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("Page", "SetInvoiceSetting");
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            map.put("InvoicePrefix", etInvoicePrefix.getText().toString());
            map.put("OrderPrefix", etOrderPrefix.getText().toString());
            map.put("TinNo", etTin.getText().toString());
            map.put("PanNo", etPanNo.getText().toString());
            map.put("ServicetaxNo", etServiceTax.getText().toString());
            map.put("Invoice_No_Start", etInvoiceNoStart.getText().toString());
            map.put("Receipt_No_Start", etReceiptNoStart.getText().toString());

            if (btnRadio1.isChecked())
                map.put("OrderDefaultTaxApplicable", "true");
            else
                map.put("OrderDefaultTaxApplicable", "false");

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_INSERT_UPDATE_INVOICE_SETTINGS, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    private void FetchPaymentSettings() {
        if (Utils.isDeviceOnline(getActivity())) {
//           showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("Page", "GetPaymentGatewayDetails");
            map.put("userID", UserId);

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_PAYMENTGATEWAY_SETTINGS, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    private void PaymentGatewayActivation() {
        if (Utils.isDeviceOnline(getActivity())) {
//           showLoading();
            String isactive = payment_activation.isChecked() ? "1" : "0";
            Map<String, String> map = new HashMap<>();
            map.put("Page", "ActivateDeactivateStatus");
            map.put("userID", UserId);
            map.put("Status", isactive);
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_SET_PAYMENTGATEWAY_ACTIVATION, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    private void setPaymentSetting() {
        if (Utils.isDeviceOnline(getActivity())) {
//           showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("Page", "SavePaymentGatewayDetails");
            map.put("PaymentGateway", PaymentGateway);
            map.put("Key", keyvalue);
            map.put("Salt", saltvalue);
            map.put("userID", UserId);

            try {
                new RequestManager().sendPostRequest(this,
                        KEY_SET_PAYMENTGATEWAY_SETTINGS, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    private void getPosTemplate(String templatefor, boolean IsSMS) {

        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            String type = "MAIL";
            if (IsSMS) type = "SMS";
            Map<String, String> map = new HashMap<>();
            map.put("Page", "GetMailTemplateDetail");
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            map.put("MailTemplatefor", templatefor);
            map.put("Type", type);
            Log.i("template", "template - " + map.toString());
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_MAIL_TEMPLATE, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    private void alertCheckedStatus() {
        if (settingList.get(0).getCreditSMSCustomer() != null) {
            if (settingList.get(0).getCreditSMSCustomer().equalsIgnoreCase("1"))
                sms_credit.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
            else sms_credit.setTextColor(Color.parseColor("#808080"));
        }
        if (settingList.get(0).getCreditEmailCustomer() != null) {
            if (settingList.get(0).getCreditEmailCustomer().equalsIgnoreCase("1"))
                mail_credit.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
            else mail_credit.setTextColor(Color.parseColor("#808080"));
        }
        if (settingList.get(0).getPaymentSMSCustomer() != null) {
            if (settingList.get(0).getPaymentSMSCustomer().equalsIgnoreCase("1"))
                sms_payment.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
            else sms_payment.setTextColor(Color.parseColor("#808080"));
        }
        if (settingList.get(0).getPaymentEmailCustomer() != null) {
            if (settingList.get(0).getPaymentEmailCustomer().equalsIgnoreCase("1"))
                mail_payment.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
            else mail_payment.setTextColor(Color.parseColor("#808080"));
        }

        if (settingList.get(0).getOrderSMSCustomer() != null) {
            if (settingList.get(0).getOrderSMSCustomer().equalsIgnoreCase("1"))
                sms_invoice.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
            else sms_invoice.setTextColor(Color.parseColor("#808080"));
        }
        if (settingList.get(0).getOrderEmailCustomer() != null) {
            if (settingList.get(0).getOrderEmailCustomer().equalsIgnoreCase("1"))
                mail_invoice.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
            else mail_invoice.setTextColor(Color.parseColor("#808080"));
        }

    }

    private void reportCheckedStatus() {
        report_order_sms.setTextColor(Color.parseColor("#808080"));
        report_payment_sms.setTextColor(Color.parseColor("#808080"));
        report_credit_sms.setTextColor(Color.parseColor("#808080"));
        report_order_mail.setTextColor(Color.parseColor("#808080"));
        report_payment_mail.setTextColor(Color.parseColor("#808080"));
        report_credit_mail.setTextColor(Color.parseColor("#808080"));

        if (posReport.getIsOrderActiveSMS() != null && posReport.getIsOrderActiveSMS().equalsIgnoreCase("true"))
            report_order_sms.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        if (posReport.getIsPaymentActiveSMS() != null && posReport.getIsPaymentActiveSMS().equalsIgnoreCase("true"))
            report_payment_sms.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        if (posReport.getIsCreditActiveSMS() != null && posReport.getIsCreditActiveSMS().equalsIgnoreCase("true"))
            report_credit_sms.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        if (posReport.getIsOrderActive() != null && posReport.getIsOrderActive().equalsIgnoreCase("true"))
            report_order_mail.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        if (posReport.getIsPaymentActive() != null && posReport.getIsPaymentActive().equalsIgnoreCase("true"))
            report_payment_mail.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        if (posReport.getIsCreditActive() != null && posReport.getIsCreditActive().equalsIgnoreCase("true"))
            report_credit_mail.setTextColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
    }

    private void htmlpoupup(String str) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(str);
        dialog.setContentView(R.layout.htmlpopup);
        final TextView htmltv = (TextView) dialog.findViewById(R.id.htmltv);
        htmltv.setText(Html.fromHtml(htmlstr, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                //Toast.makeText(getActivity(), source,Toast.LENGTH_LONG).show();
                HttpGetDrawableTask httpGetDrawableTask = new HttpGetDrawableTask(htmltv, htmlstr);
                httpGetDrawableTask.execute(source);
                return null;
            }
        }, null));
        htmltv.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.show();
    }

    // used for invoice and payment
    private void showInvoicePopUp(final boolean IsSmsType, final boolean isInvoice, String InvoiceEnable, String TemplateId, String TemplateName, String AfterdueIschecked, String afterDueTime, String afterDueperiod) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Alert Setting");
        dialog.setContentView(R.layout.pos_setting_alert_popup);

        RelativeLayout title_layout= (RelativeLayout) dialog.findViewById(R.id.invoice_popup_layout);
        title_layout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));
        TextView invoice_msg_type = (TextView) dialog.findViewById(R.id.invoice_msg_type);
        TextView invoice_done = (TextView) dialog.findViewById(R.id.invoice_done);

        invoice_done.setText("Done");
        mIcon.setTextfont(invoice_msg_type);
        mIcon.setTextfont(invoice_done);

        temaplate_title = TemplateName;

        if (IsSmsType) {
            invoice_msg_type.setText(getString(R.string.sms_icon));
        } else {
            invoice_msg_type.setText(getString(R.string.mail_icon));
        }
        final ImageView preview = (ImageView) dialog.findViewById(R.id.preview);
        final EditText total_delay = (EditText) dialog.findViewById(R.id.total_delay);

        final Spinner delay_type = (Spinner) dialog.findViewById(R.id.delay_type);
        temlate_spinner = (Spinner) dialog.findViewById(R.id.temlate_spinner);
        Template_ID = "0";

        preview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String stype = IsSmsType ? "SMS" : "MAIL";
                if (Integer.parseInt(Template_ID) > 0)
                    templateView(stype);
            }


        });


        if (isInvoice) {
            invoice_msg_type.append(" Order Alert");
            getPosTemplate("POS-Order", IsSmsType);
        } else {
            invoice_msg_type.append(" Payment Alert");
            getPosTemplate("POS-Payment", IsSmsType);
        }

        final Spinner send_type = (Spinner) dialog.findViewById(R.id.send_type);

        int selcted_position = 0;
        if (AfterdueIschecked.equalsIgnoreCase("true") || AfterdueIschecked.equalsIgnoreCase("1"))
            selcted_position = 1;
        else selcted_position = 0;
        send_type.setSelection(selcted_position);


        if (afterDueTime != null) total_delay.setText(afterDueTime);
        String[] DelayArray = getResources().getStringArray(R.array.delayarray);
        delay_type.setSelection(getselection(afterDueperiod, DelayArray));

        final Switch InvoiceSettingSwitch = (Switch) dialog.findViewById(R.id.InvoiceSettingSwitch);

        if (InvoiceEnable.equals("1")) {
            InvoiceSettingSwitch.setChecked(true);
        } else {
            InvoiceSettingSwitch.setChecked(false);
        }

        InvoiceSettingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    temlate_spinner.setEnabled(true);
                    preview.setEnabled(true);
                    send_type.setEnabled(true);
                    if (send_type.getSelectedItemPosition() == 0) {
                        delay_type.setEnabled(false);
                        total_delay.setEnabled(false);
                        total_delay.setText("0");
                    } else {
                        delay_type.setEnabled(true);
                        total_delay.setEnabled(true);


                    }

                } else {
                    temlate_spinner.setEnabled(false);
                    preview.setEnabled(false);
                    send_type.setEnabled(false);
                    delay_type.setEnabled(false);
                    total_delay.setEnabled(false);
                }

            }
        });


        invoice_done.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unused")
            @Override
            public void onClick(View v) {

                int tempid = Integer.parseInt(Template_ID);
                String isInvoiceEnable;
                if (InvoiceSettingSwitch.isChecked())
                    isInvoiceEnable = "1";
                else
                    isInvoiceEnable = "0";

                String AfterDueDatePeriod = delay_type.getSelectedItem().toString();
                String AfterDueDateTime = total_delay.getText().toString();
                int send_type_position = send_type.getSelectedItemPosition();
                if (InvoiceSettingSwitch.isChecked()) {
                    //if (tempid > 0) {
                    if (send_type_position > 0) {
                        int delaytime = Integer.parseInt(AfterDueDateTime.trim());
                        if (delaytime > 0) {
                            if (isInvoice)
                                SetInvoiceSettings(IsSmsType, isInvoiceEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod);
                            else
                                SetPaymentSettings(IsSmsType, isInvoiceEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod);
                            dialog.cancel();
                        } else
                            Toast.makeText(getActivity(), "Enter the time delay", Toast.LENGTH_SHORT).show();
                        total_delay.setFocusable(true);
                    } else {
                        if (isInvoice)
                            SetInvoiceSettings(IsSmsType, isInvoiceEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod);
                        else
                            SetPaymentSettings(IsSmsType, isInvoiceEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod);
                        dialog.cancel();
                    }

                    //} else Toast.makeText(getActivity(), "Select Template", Toast.LENGTH_SHORT).show();
                } else {
                    if (isInvoice)
                        SetInvoiceSettings(IsSmsType, isInvoiceEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod);
                    else
                        SetPaymentSettings(IsSmsType, isInvoiceEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod);
                    dialog.cancel();
                }
            }
        });


        send_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 1) {
                    delay_type.setEnabled(true);
                    total_delay.setEnabled(true);
                } else {
                    delay_type.setEnabled(false);
                    total_delay.setEnabled(false);
                    total_delay.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialog.show();
    }

    private void templateView(String type) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Template");
        WebView wv = new POSSettingScreenFragment.WebViewHelper(getActivity());
        wv.clearHistory();
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        String base_url = KIT19_BASE_URL.replace("NewService.aspx?Page=", "");
        wv.loadUrl(base_url + "mtemplatemail.aspx?ID=" + Template_ID + "&type=" + type);
        Log.i("base_url=>", base_url + "mtemplatemail.aspx?ID=" + Template_ID);
        POSSettingScreenFragment.WebClientClass webViewClient = new POSSettingScreenFragment.WebClientClass();
        wv.setWebViewClient(webViewClient);
        // wv.setWebViewClient(new WebViewClient());

        alert.setView(wv);
        alert.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        alert.show();
    }

    private Integer getselection(String timePeriod, String strArray[]) {
        int selction = 0;
        if (java.util.Arrays.asList(strArray).contains(timePeriod)) ;
        selction = java.util.Arrays.asList(strArray).indexOf(timePeriod);
        return selction;
    }
// showCreditPopUp(true, settingList.get(0).getCreditSMSCustomer(), settingList.get(0).getCreditTemplate(), settingList.get(0).getSmsTemplateIdCredit(), settingList.get(0).getDueDateSMSRadio(), settingList.get(0).getAfterDueDateSMSTime(), settingList.get(0).getAfterDueDateSMSPeriod(), settingList.get(0).getAfterDueDateSMSChecked());
    private void showCreditPopUp(final boolean IsSmsType, final String CreditEnable, String TemplateName, String TemplateId, String DueDateRadio, String afterDueTime, String afterDueperiod, String AfterdueIschecked) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Alert Setting");
        dialog.setContentView(R.layout.pos_setting_credit_popup);
        temaplate_title = TemplateName;

        RelativeLayout title_layout= (RelativeLayout) dialog.findViewById(R.id.credit_title_layout);
        title_layout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        TextView credit_msg_type = (TextView) dialog.findViewById(R.id.credit_msg_type);
        TextView credit_done = (TextView) dialog.findViewById(R.id.credit_done);
        final TextView keep_frequency = (TextView) dialog.findViewById(R.id.keep_frequency);

        credit_done.setText("Done");
        mIcon.setTextfont(credit_msg_type);
        mIcon.setTextfont(credit_done);

        final String[] Frequency_Array={"0","1","7","30"};

        final ImageView preview = (ImageView) dialog.findViewById(R.id.preview);
        final EditText total_delay = (EditText) dialog.findViewById(R.id.total_delay);
        final Spinner frequency_type = (Spinner) dialog.findViewById(R.id.frequency_type);
        final Spinner delay_type = (Spinner) dialog.findViewById(R.id.delay_type);
        temlate_spinner = (Spinner) dialog.findViewById(R.id.temlate_spinner);
        final Spinner send_type = (Spinner) dialog.findViewById(R.id.credit_send_type);
        int selcted_position = 0;
        if (AfterdueIschecked.equals("true") || AfterdueIschecked.equals("1")) selcted_position = 1;
        else selcted_position = 0;
        send_type.setSelection(selcted_position);
        if (afterDueTime != null) total_delay.setText(afterDueTime);
        String[] DelayArray = getResources().getStringArray(R.array.credit_delayarray);
        delay_type.setSelection(getselection(afterDueperiod, DelayArray));
        String[] DArray = getResources().getStringArray(R.array.darray);
        frequency_type.setSelection(getselection(DueDateRadio, Frequency_Array));

        preview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String stype = IsSmsType ? "SMS" : "MAIL";
                if (Integer.parseInt(Template_ID) > 0)
                    templateView(stype);
            }

        });
        //fetch template
        getPosTemplate("POS-Credit", IsSmsType);

        final Switch creditSettingSwitch = (Switch) dialog.findViewById(R.id.creditSettingSwitch);


        if (IsSmsType) {
            credit_msg_type.setText(getString(R.string.sms_icon));

        } else {
            credit_msg_type.setText(getString(R.string.mail_icon));

        }
        credit_msg_type.append(" Credit Alert");
        if (CreditEnable.equals("1")) {
            creditSettingSwitch.setChecked(true);
        } else {
            creditSettingSwitch.setChecked(false);
        }
        creditSettingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    temlate_spinner.setEnabled(true);
                    preview.setEnabled(true);
                    frequency_type.setEnabled(true);
                    send_type.setEnabled(true);
                    keep_frequency.setEnabled(true);
                    if (send_type.getSelectedItemPosition() == 0) {
                        delay_type.setEnabled(false);
                        total_delay.setEnabled(false);
                        total_delay.setText("0");
                    } else {
                        delay_type.setEnabled(true);
                        total_delay.setEnabled(true);


                    }

                } else {
                    temlate_spinner.setEnabled(false);
                    preview.setEnabled(false);
                    frequency_type.setEnabled(false);
                    send_type.setEnabled(false);
                    delay_type.setEnabled(false);
                    total_delay.setEnabled(false);
                    total_delay.setText("0");
                    keep_frequency.setEnabled(false);

                }

            }
        });

        credit_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int tempid = Integer.parseInt(Template_ID);
                String isCreditenableEnable;
                if (creditSettingSwitch.isChecked())
                    isCreditenableEnable = "1";
                else
                    isCreditenableEnable = "0";

                String AfterDueDatePeriod = delay_type.getSelectedItem().toString();
                String AfterDueDateTime = total_delay.getText().toString();
                int send_type_position = send_type.getSelectedItemPosition();
                int freq_pos=frequency_type.getSelectedItemPosition();
                String keep_send_freq = Frequency_Array[freq_pos];

                if (creditSettingSwitch.isChecked()) {
                    // if (tempid > 0) {
                    if (send_type_position > 0) {
                        int delaytime = Integer.parseInt(AfterDueDateTime.trim());
                        if (delaytime > 0) {
                            SetCreditSettings(IsSmsType, isCreditenableEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod, keep_send_freq);
                            dialog.cancel();
                        } else
                            Toast.makeText(getActivity(), "Enter the time of send after ", Toast.LENGTH_SHORT).show();

                    } else {
                        SetCreditSettings(IsSmsType, isCreditenableEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod, keep_send_freq);
                        dialog.cancel();
                    }
                    //} else Toast.makeText(getActivity(), "Select Template ", Toast.LENGTH_SHORT).show();

                } else {
                    SetCreditSettings(IsSmsType, isCreditenableEnable, Template_ID, "" + send_type_position, AfterDueDateTime, AfterDueDatePeriod, keep_send_freq);
                    dialog.cancel();
                }

            }
        });


        //final String[] sendTypeArray= getResources().getStringArray(R.array.Send_type_Array);

        send_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position == 1) {
                    delay_type.setEnabled(true);
                    total_delay.setEnabled(true);
                } else {
                    delay_type.setEnabled(false);
                    total_delay.setEnabled(false);
                    total_delay.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialog.show();
    }

    private void showReportPopUp(final boolean IsSmsType, final int reportType, String s1, String s2, String s3, String s4, String s5, String s6) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.pos_setting_report_popup);
        dialog.setTitle("");
        toggle1 = (Switch) dialog.findViewById(R.id.report_sms1);
        toggle2 = (Switch) dialog.findViewById(R.id.report_sms2);
        toggle3 = (Switch) dialog.findViewById(R.id.report_sms3);
        toggle4 = (Switch) dialog.findViewById(R.id.report_sms4);
        toggle5 = (Switch) dialog.findViewById(R.id.report_sms5);
        toggle6 = (Switch) dialog.findViewById(R.id.report_sms6);

        RelativeLayout title_layout= (RelativeLayout) dialog.findViewById(R.id.report_title_layout);
        title_layout.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

        TextView pos_report_clock = (TextView) dialog.findViewById(R.id.pos_report_clock);
        TextView report_done = (TextView) dialog.findViewById(R.id.report_done);
        pos_report_clock.append(" How Often ?");
        report_done.setText("Done");
        mIcon.setTextfont(pos_report_clock);
        mIcon.setTextfont(report_done);

        if (s1.equals("true")) {
            toggle1.setChecked(true);
        }
        if (s2.equals("true")) {
            toggle2.setChecked(true);
        }
        if (s3.equals("true")) {
            toggle3.setChecked(true);
        }
        if (s4.equals("true")) {
            toggle4.setChecked(true);
        }
        if (s5.equals("true")) {
            toggle5.setChecked(true);
        }
        if (s6.equals("true")) {
            toggle6.setChecked(true);
        }

        report_done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                rs1 = "false";
                rs2 = "false";
                rs3 = "false";
                rs4 = "false";
                rs5 = "false";
                rs6 = "false";
                mode = "Mail";
                type = "Order";

                if (toggle1.isChecked())
                    rs1 = "true";
                if (toggle2.isChecked())
                    rs2 = "true";
                if (toggle3.isChecked())
                    rs3 = "true";
                if (toggle4.isChecked())
                    rs4 = "true";
                if (toggle5.isChecked())
                    rs5 = "true";
                if (toggle6.isChecked())
                    rs6 = "true";
                if (IsSmsType)
                    mode = "SMS";
                if (reportType == 2)
                    type = "Payment";
                else if (reportType == 3)
                    type = "Credit";
                postReportValues(mode, type, rs1, rs2, rs3, rs4, rs5, rs6);
                setReportModelvalue(IsSmsType, reportType, rs1, rs2, rs3, rs4, rs5, rs6);
                dialog.hide();
            }
        });

        dialog.show();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void fetchInvoiceSettings() {
        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "GetInvoiceSetting");
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_INVOICE_SETTINGS, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        // TODO Auto-generated method stub
//        hideLoading();
        System.out.println("Response " + response);
        if (response != null && response.length() > 0) {
            if (requestId == KEY_FETCH_INVOICE_SETTINGS) {
                JSONObject jsonObject;
                JSONArray array = null;
                try {
                    jsonObject = new JSONObject(response);
                    array = jsonObject.getJSONArray("InvoiceSetting");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                PosSettings setting = gson.fromJson(response, PosSettings.class);
                if (array != null && array.length() > 0) {
                    settingList = setting.getInvoiceSetting();
                    setInvoiceValues();

                } else {
//						showMessage("error");
                    settingList = new ArrayList<>();
                    settingList.add(new InvoiceSetting());
                    System.out.println("setting response " + response);
                }
            } else if (requestId == KEY_INSERT_UPDATE_INVOICE_SETTINGS) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("InvoiceSetting");
                    if (array.getJSONObject(0).getString("SettingID").length() > 0) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        SharedPreferences preference = getActivity().getSharedPreferences("profileData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preference.edit();
                        editor.putString("isPosSettingEnabled", "1");
                        editor.commit();
                    } else
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (requestId == KEY_FETCH_REPORT_SETTING) {
                Gson gson = new Gson();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);

                    JSONArray array = jsonObject.getJSONArray("ReportSetting");
                    posReport = null;
                    posReport = gson.fromJson(array.get(0).toString(), PosSettingReportModelData.class);
                    reportCheckedStatus();
                    System.out.println("Report " + posReport.toString());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    posReport = new PosSettingReportModelData();
                    reportCheckedStatus();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    posReport = new PosSettingReportModelData();
                    reportCheckedStatus();
                }
            } else if (requestId == KEY_SET_REPORT_SETTING) {
                fetchReportSettings();
            } else if (requestId == KEY_FETCH_MAIL_TEMPLATE) {
                fetchTemplate(response);
            } else if (requestId == KEY_SET_CREDIT_SMS_SETTING) {
                fetchInvoiceSettings();
            } else if (requestId == KEY_SET_CREDIT_MAIL_SETTING) {
                fetchInvoiceSettings();
            } else if (requestId == KEY_SET_INVOICE_SMS_SETTING) {
                fetchInvoiceSettings();
            } else if (requestId == KEY_SET_INVOICE_MAIL_SETTING) {
                fetchInvoiceSettings();
            } else if (requestId == KEY_SET_PAYMENT_SMS_SETTING) {
                fetchInvoiceSettings();
            } else if (requestId == KEY_SET_PAYMENT_MAIL_SETTING) {
                fetchInvoiceSettings();
            } else if (requestId == KEY_FETCH_PAYMENTGATEWAY_SETTINGS) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    if (jsonobject.has("PaymentGateway")) {
                        JSONArray jarray = jsonobject.getJSONArray("PaymentGateway");
                        if (jarray != null && jarray.length() > 0) {
                            JSONObject object = jarray.getJSONObject(0);
                            if (object.has("PaymentGateway"))
                                PaymentGateway = object.getString("PaymentGateway");
                            if (object.has("Key")) keyvalue = object.getString("Key");
                            if (object.has("Salt")) saltvalue = object.getString("Salt");
                            if (object.has("Status"))
                                paymentgatewayStatus = object.getBoolean("Status");
                            ArrayList<String> payulist = new ArrayList<String>();
                            payulist.add(PaymentGateway);
                            CustomStyle.MySpinnerAdapter payuAdapter = new CustomStyle.MySpinnerAdapter(getActivity(),
                                    android.R.layout.simple_spinner_item, payulist);
                            payuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            payment_spinner.setAdapter(payuAdapter);
                            payment_key.setText(keyvalue);
                            payment_salt.setText(saltvalue);
                            payment_activation.setVisibility(View.VISIBLE);
                            payment_activation.setChecked(paymentgatewayStatus);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (requestId == KEY_SET_PAYMENTGATEWAY_SETTINGS) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    if (jsonobject.has("PaymentGateway")) {
                        JSONArray jarray = jsonobject.getJSONArray("PaymentGateway");
                        if (jarray != null && jarray.length() > 0) {
                            JSONObject object = jarray.getJSONObject(0);
                            String Link = object.getString("Link");
                            link_layout.setVisibility(View.VISIBLE);
                            payment_link.setText(Link);
                            payment_activation.setChecked(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (requestId == KEY_SET_PAYMENTGATEWAY_ACTIVATION) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    if (jsonobject.has("PaymentGatewayStatus")) {
                        JSONArray jarray = jsonobject.getJSONArray("PaymentGatewayStatus");
                        if (jarray != null && jarray.length() > 0) {
                            JSONObject object = jarray.getJSONObject(0);
                            String msg = object.getString("Column1");
                            //Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
//            showMessage("Please try again. last");
        }
    }

    private void fetchTemplate(String response) {
        ArrayList<StringWithTag> TemplateList = new ArrayList<StringWithTag>();
        template_position = 0;
        Log.i("temaplate", "--" + response);
        Log.i("temaplate_title", "--" + temaplate_title);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("MailTemplateDetail")) {
                JSONArray TemplateArray = jsonObject.getJSONArray("MailTemplateDetail");
                // TemplateList.add(new StringWithTag("Select Template", 0));
                for (int i = 0; i < TemplateArray.length(); i++) {
                    JSONObject tlist = TemplateArray.getJSONObject(i);
                    final String TemplateName = tlist.getString("template_title");
                    String TemplateUrl = tlist.getString("template");
                    int TemplateID = tlist.getInt("template_id");
                    TemplateList.add(new StringWithTag(TemplateName, TemplateID));
                    if (TemplateName.equalsIgnoreCase(temaplate_title)) {
                        template_position = i;
                    }
                }

                dataAdapter = new ArrayAdapter<StringWithTag>(getActivity(), android.R.layout.simple_spinner_item, TemplateList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                temlate_spinner.setAdapter(dataAdapter);
                temlate_spinner.setSelection(template_position);
                temlate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        StringWithTag s = (StringWithTag) parent.getItemAtPosition(position);
                        Object tag = s.tag;
                        Template_ID = tag.toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.volleyError).toString(), Toast.LENGTH_SHORT).show();
        }catch(NullPointerException e){e.printStackTrace();}
    }

    private void setReportModelvalue(boolean IsSmsType, int reportType, String daily, String weekly, String monthly, String quaterly, String halfYearly, String yearly) {
//		String mode, type,  daily,  weekly,  monthly,  quaterly,  halfYearly,  yearly;

        if (IsSmsType) {
            if (reportType == 1) {
                posReport.setOrderDailySMS(daily);
                posReport.setOrderWeeklySMS(weekly);
                posReport.setOrderMonthlySMS(monthly);
                posReport.setOrderQuarterlySMS(quaterly);
                posReport.setOrderHalfYearlySMS(halfYearly);
                posReport.setOrderYearlySMS(yearly);
            } else if (reportType == 2) {
                posReport.setPaymentDailySMS(daily);
                posReport.setPaymentWeeklySMS(weekly);
                posReport.setPaymentMonthlySMS(monthly);
                posReport.setPaymentQuarterlySMS(quaterly);
                posReport.setPaymentHalfYearlySMS(halfYearly);
                posReport.setPaymentYearlySMS(yearly);

            } else if (reportType == 3) {
                posReport.setCreditsDailySMS(daily);
                posReport.setCreditsWeeklySMS(weekly);
                posReport.setCreditsMonthlySMS(monthly);
                posReport.setCreditsQuarterlySMS(quaterly);
                posReport.setCreditsHalfYearlySMS(halfYearly);
                posReport.setCreditsYearlySMS(yearly);
            }
        } else {

            if (reportType == 1) {
                posReport.setOrderDailyMail(daily);
                posReport.setOrderWeeklyMail(weekly);
                posReport.setOrderMonthlyMail(monthly);
                posReport.setOrderQuarterlyMail(quaterly);
                posReport.setOrderHalfYearlyMail(halfYearly);
                posReport.setOrderYearlyMail(yearly);

            } else if (reportType == 2) {
                posReport.setPaymentDailyMail(daily);
                posReport.setPaymentWeeklyMail(weekly);
                posReport.setPaymentMonthlyMail(monthly);
                posReport.setPaymentQuarterlyMail(quaterly);
                posReport.setPaymentHalfYearlyMail(halfYearly);
                posReport.setPaymentYearlyMail(yearly);

            } else if (reportType == 3) {
                posReport.setCreditsDailyMail(daily);
                posReport.setCreditsWeeklyMail(weekly);
                posReport.setCreditsMonthlyMail(monthly);
                posReport.setCreditsQuarterlyMail(quaterly);
                posReport.setCreditsHalfYearlyMail(halfYearly);
                posReport.setCreditsYearlyMail(yearly);

            }
        }
    }

    private void fetchReportSettings() {
        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "GetReportSetting");
//			map.put("userID", "589");
//			map.put("UserLogin", "+918512851664");
//			map.put("Password", "103641");

            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_FETCH_REPORT_SETTING, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    @SuppressWarnings({"unused", "unchecked"})
    private void SetCreditSettings(boolean IsSmstype, String isCreditEnable, String TemplateCredit, String AfterDueDateChecked, String AfterDueDateTime, String AfterDueDatePeriod, String keep_sending) {
        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            int settingtype = 0;
            Map map = new HashMap<>();
            if (IsSmstype) {
                map.put("Page", "SetSMSCreditSetting");
                map.put("SmsTemplateIdCredit", TemplateCredit);
                map.put("CreditSMSCustomer", isCreditEnable);
                map.put("AfterDueDateSMSChecked", AfterDueDateChecked);
                map.put("AfterDueDateSMSTime", AfterDueDateTime);
                map.put("AfterDueDateSMSPeriod", AfterDueDatePeriod);
                map.put("DueDateSMSRadio", keep_sending);
                settingtype = KEY_SET_CREDIT_SMS_SETTING;

            } else {
                map.put("Page", "SetMAILCreditSetting");
                map.put("MailTemplateIdCredit", TemplateCredit);
                map.put("CreditEmailCustomer", isCreditEnable);
                map.put("AfterDueDateChecked", AfterDueDateChecked);
                map.put("AfterDueDateTime", AfterDueDateTime);
                map.put("AfterDueDatePeriod", AfterDueDatePeriod);
                map.put("DueDateMailRadio", keep_sending);
                settingtype = KEY_SET_CREDIT_MAIL_SETTING;
            }

            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);

            Log.i("SetCredit:", map.toString());

            try {
                new RequestManager().sendPostRequest(this,
                        settingtype, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    @SuppressWarnings("unchecked")
    private void SetInvoiceSettings(boolean IsSmstype, String isInvoiceEnable, String InvoiceTemplateId, String InvoiceTime, String AfterDueDateTime, String AfterDueDatePeriod) {
        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            int settingtype = 0;
            Map map = new HashMap<>();
            if (IsSmstype) {

                map.put("Page", "SetSMSInvoiceSetting");
                map.put("OrderSMSCustomer", isInvoiceEnable);
                map.put("SmsTemplateIdOrder", InvoiceTemplateId);
                map.put("OrderSMSTime", InvoiceTime);
                map.put("OrdersmstimeDelay", AfterDueDateTime);
                map.put("OrdersmsDaysDelay", AfterDueDatePeriod);
                settingtype = KEY_SET_INVOICE_SMS_SETTING;
            } else {
                map.put("Page", "SetMAILInvoiceSetting");
                map.put("OrderEmailCustomer", isInvoiceEnable);
                map.put("MailTemplateIdOrder", InvoiceTemplateId);
                map.put("OrderMailTime", InvoiceTime);
                map.put("OrdermailtimeDelay", AfterDueDateTime);
                map.put("OrdermailDaysDelay", AfterDueDatePeriod);
                settingtype = KEY_SET_INVOICE_MAIL_SETTING;
            }

            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            Log.i("Invoice", "--" + map.toString());
            try {
                new RequestManager().sendPostRequest(this,
                        settingtype, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    @SuppressWarnings("unchecked")
    private void SetPaymentSettings(boolean IsSmstype, String ispaymentEnable, String PaymentTemplateId, String paymentTime, String AfterDueDateTime, String AfterDueDatePeriod) {
        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            int settingtype = 0;
            Map map = new HashMap<>();
            if (IsSmstype) {

                map.put("Page", "SetSMSPaymentSetting");
                map.put("SmsTemplateIdPayment", PaymentTemplateId);
                map.put("PaymentSMSCustomer", ispaymentEnable);
                map.put("PaymentSMSTime", paymentTime);
                map.put("PaymentsmstimeDelay", AfterDueDateTime);
                map.put("PaymentsmsDaysDelay", AfterDueDatePeriod);
                settingtype = KEY_SET_PAYMENT_SMS_SETTING;
            } else {
                map.put("Page", "SetMAILPaymentSetting");
                map.put("MailTemplateIdPayment", PaymentTemplateId);
                map.put("PaymentEmailCustomer", ispaymentEnable);
                map.put("PaymentMailTime", paymentTime);
                map.put("PaymentmailtimeDelay", AfterDueDateTime);
                map.put("PaymentmailDaysDelay", AfterDueDatePeriod);
                settingtype = KEY_SET_PAYMENT_MAIL_SETTING;
            }

            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);

            Log.i("Payment", "--" + map.toString());

            try {
                new RequestManager().sendPostRequest(this,
                        settingtype, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }
    }

    private void setInvoiceValues() {
        // TODO Auto-generated method stub
        if (settingList != null && settingList.size() > 0 && settingList.get(0) != null) {
            if (settingList.get(0).getInvoicePrefix() != null && settingList.get(0).getInvoicePrefix().length() != 0 ) {
                tvInvoicePrefix.setVisibility(View.VISIBLE);
                etInvoicePrefix.setText(settingList.get(0).getInvoicePrefix());
            }

            if (settingList.get(0).getServicetaxNo() != null && settingList.get(0).getServicetaxNo().length() != 0 ) {
                tvServiceTax.setVisibility(View.VISIBLE);
                etServiceTax.setText(settingList.get(0).getServicetaxNo());
            }

            if (settingList.get(0).getTinNo() != null && settingList.get(0).getTinNo().length() != 0 ) {
                tvTin.setVisibility(View.VISIBLE);
                etTin.setText(settingList.get(0).getTinNo());
            }

            if (settingList.get(0).getPanNo() != null && settingList.get(0).getPanNo().length() != 0 ) {
                tvPanNo.setVisibility(View.VISIBLE);
                etPanNo.setText(settingList.get(0).getPanNo());
            }


            if (settingList.get(0).getInvoice_No_Start() != null && settingList.get(0).getInvoice_No_Start().length() != 0) {
                tvInvoiceNoStart.setVisibility(View.VISIBLE);
                etInvoiceNoStart.setText(settingList.get(0).getInvoice_No_Start());
            }

            if ( settingList.get(0).getReceipt_No_Start() != null && settingList.get(0).getReceipt_No_Start().length() != 0 ) {
                tvReceiptNoStart.setVisibility(View.VISIBLE);
                etReceiptNoStart.setText(settingList.get(0).getReceipt_No_Start());
            }


            if ( settingList.get(0).getOrderPrefix() != null && settingList.get(0).getOrderPrefix().length() != 0 ) {
                tvOrderPrefix.setVisibility(View.VISIBLE);
                etOrderPrefix.setText(settingList.get(0).getOrderPrefix());
            }
            if (settingList.get(0).getOrderDefaultTaxApplicable() != null && settingList.get(0).getOrderDefaultTaxApplicable().length() != 0 ) {
                tvOrderPrefix.setVisibility(View.VISIBLE);
                if (settingList.get(0).getOrderDefaultTaxApplicable() != null && settingList.get(0).getOrderDefaultTaxApplicable().equals("true"))
                    btnRadio1.setChecked(true);
                else
                    btnRadio2.setChecked(true);
            }

            if (settingList.get(0).getIseditable() != null && settingList.get(0).getIseditable().equals("1")) {
                etInvoicePrefix.setEnabled(false);
                etInvoiceNoStart.setEnabled(false);
                etReceiptNoStart.setEnabled(false);
                etOrderPrefix.setEnabled(false);
            }
            if(settingList.get(0).getIsInvoiceHeader().equals("1"))
                    invoice_header_enable.setChecked(true);
            if(settingList.get(0).getIsInvoiceHeader().equals("1"))
                    invoice_footer_enable.setChecked(true);
            if(settingList.get(0).getIsInvoiceHeader().equals("1"))
                    receipt_header_enable.setChecked(true);
            if(settingList.get(0).getIsInvoiceHeader().equals("1"))
                    receipt_footer_enable.setChecked(true);

            alertCheckedStatus();
        }
    }

    private void postReportValues(String mode, String type, String daily, String weekly, String monthly, String quaterly, String halfYearly, String yearly) {
        // TODO Auto-generated method stub
        if (Utils.isDeviceOnline(getActivity())) {
//            showLoading();
            Map map = new HashMap<>();
            map.put("Page", "SetReportSetting");
//			map.put("userID", "589");
//			map.put("UserLogin", "+918512851664");
//			map.put("Password", "103641");
            map.put("Mode", mode);
            map.put("ReportType", type);
            map.put("Daily", daily);
            map.put("Weekly", weekly);
            map.put("Monthly", monthly);
            map.put("Quarterly", quaterly);
            map.put("HalfYearly", halfYearly);
            map.put("Yearly", yearly);
            map.put("IsActive", getReportStatus(daily, weekly, monthly, quaterly, halfYearly, yearly));
            map.put("userID", UserId);
            map.put("UserLogin", UserLogin);
            map.put("Password", PassWord);
            System.out.println("Report values " + map.toString());
            try {
                new RequestManager().sendPostRequest(this,
                        KEY_SET_REPORT_SETTING, map);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
//            showMessage("Internet connection not found");
        }

    }

    private String getReportStatus(String s1, String s2, String s3, String s4, String s5, String s6) {
        if (s1.equals("true"))
            return "true";

        else if (s2.equals("true"))
            return "true";
        else if (s3.equals("true"))
            return "true";
        else if (s4.equals("true"))
            return "true";
        else if (s5.equals("true"))
            return "true";
        else if (s6.equals("true"))
            return "true";
        return "false";
    }

    @Override
    public void executeService() {
        Handler a = new Handler();
        a.postDelayed(new Runnable() {
            @Override
            public void run() {
                posReport = new PosSettingReportModelData();
                fetchInvoiceSettings();
                fetchReportSettings();
                FetchPaymentSettings();
            }
        }, 500);
    }

    private void showMessage(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    private static class WebViewHelper extends WebView {
        public WebViewHelper(Context context) {
            super(context);
        }

        // Note this!
        @Override
        public boolean onCheckIsTextEditor() {
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (!hasFocus())
                        requestFocus();
                    break;
            }
            return super.onTouchEvent(ev);
        }
    }

    private class WebClientClass extends WebViewClient {
        ProgressDialog pd = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (pd == null) {
                pd = new ProgressDialog(getActivity());
            }
            /*pd.setTitle("Please wait");
            pd.setMessage("Page is loading..");
			pd.show();*/

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (pd != null)
                //	pd.dismiss();
                super.onPageFinished(view, url);
        }
    }

    private class HttpGetDrawableTask extends AsyncTask<String, Void, Drawable> {

        TextView taskTextView;
        String taskHtmlString;

        HttpGetDrawableTask(TextView v, String s) {
            taskTextView = v;
            taskHtmlString = s;
        }

        protected Drawable doInBackground(String... params) {
            Drawable drawable = null;
            URL sourceURL;
            try {
                sourceURL = new URL(params[0]);
                URLConnection urlConnection = sourceURL.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(
                        inputStream);
                Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream);

                // convert Bitmap to Drawable
                drawable = new BitmapDrawable(getResources(), bm);

                drawable.setBounds(0, 0, bm.getWidth(), bm.getHeight());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            return drawable;
        }

        protected void onPostExecute(Drawable result) {
            final Drawable taskDrawable = result;
            if (taskDrawable != null) {
                taskTextView.setText(Html.fromHtml(taskHtmlString,
                        new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                return taskDrawable;
                            }
                        }, null));
            }
        }


    }


}
