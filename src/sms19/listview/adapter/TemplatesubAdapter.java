package sms19.listview.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Utils;
import sms19.listview.database.DataBaseDetails;
import sms19.listview.newproject.TemplateList;
import sms19.listview.newproject.model.SMSTemplateDetailsModel;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class TemplatesubAdapter extends BaseAdapter implements NetworkManager {

    private Context con;
    private String items_array[];
    private String templateCategory;
    public static String resultData = "";
    private DataBaseDetails dbObject;
    private ArrayList<TemplateList.templateModel> templateModel;
    private final int KEY_UPDATE_TEMPLATE = 2;
    private final int KEY_DELETE_TEMPLATE = 3;
    private int lastPosition;
    private MoonIcon moonIcon;

    public TemplatesubAdapter(Context context, int resource, ArrayList<TemplateList.templateModel> objects,
                              String temname, DataBaseDetails dbObject) {
//		super(context, resource, objects.);
        this.con = context;
        this.dbObject = dbObject;
        this.templateModel = objects;
        templateCategory = temname;
        moonIcon = new MoonIcon(con);
    }

    @Override
    public int getCount() {
        try {
            return templateModel.size();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return 0;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());// (LayoutInflater)
            // con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    R.layout.template_subpage_custom_adapter, null);
            viewHolder.textableLayout = convertView
                    .findViewById(R.id.textable_layout);
            viewHolder.templateSubpagemsg = (TextView) convertView
                    .findViewById(R.id.templatesubpagemsg);
            if (templateCategory.equalsIgnoreCase("Personal")) {
                // viewHolder.templateSubpagemsg.setText(items_array[position]);
                viewHolder.allBtnLayout = convertView
                        .findViewById(R.id.all_btn_layout);
                viewHolder.editBtn = (TextView) convertView
                        .findViewById(R.id.edit_btn);
                viewHolder.deleteBtn = (TextView) convertView
                        .findViewById(R.id.delete_btn);
                moonIcon.setTextfont(viewHolder.editBtn);
                moonIcon.setTextfont(viewHolder.deleteBtn);

            } else {
                try {
                    viewHolder.templateSubpagemsg
                            .setText(Html.fromHtml(items_array[position]));
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
            setRobotoThinFont(viewHolder.templateSubpagemsg,con);


            viewHolder.templateSubpagemsg.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (templateCategory.equalsIgnoreCase("Personal")) {
            viewHolder.templateSubpagemsg.setText(Html.fromHtml(templateModel.get(position).getTemname()));
            viewHolder.allBtnLayout.setVisibility(View.VISIBLE);
            resultData = templateModel.get(position).getTemname();/*items_array[position];*/
            viewHolder.editBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    final Dialog dialog = new Dialog(con);
                    dialog.setContentView(R.layout.template_editable_layout);
                    dialog.setCancelable(false);
                    dialog.setTitle("Personal");
                    final TextView txt_count = (TextView) dialog
                            .findViewById(R.id.txt_count);
                    Button done = (Button) dialog.findViewById(R.id.done_btn);
                    Button cancel = (Button) dialog
                            .findViewById(R.id.cancel_btn);
                    final EditText edit = (EditText) dialog
                            .findViewById(R.id.edit_txt);
                    edit.setText(viewHolder.templateSubpagemsg.getText());
                    if (edit.getText().toString() != null
                            && edit.getText().toString().length() > 0) {
                        txt_count.setText(edit.getText().toString().length()
                                + "/2000");
                    }
                    edit.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s,
                                                      int start, int count, int after) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // TODO Auto-generated method stub
                            if (edit.getText().toString() != null
                                    && edit.getText().toString().length() > 0) {
                                txt_count.setText(edit.getText().toString()
                                        .length()
                                        + "/2000");
                            } else {
                                txt_count.setText("0" + "/2000");
                            }
                        }
                    });
                    done.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            lastPosition = position;
                            resultData = edit.getText().toString();
                            viewHolder.templateSubpagemsg.setText(resultData);
                            try {
                                udpateSMSTemplate(resultData, templateModel.get(position).getTemid());

                            } catch (Exception e) {
                                // TODO: handle exception
                            }

                            dialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
            viewHolder.deleteBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new AlertDialog.Builder(con)
                            .setCancelable(false)
                            // .setTitle("Login Failed!")
                            .setMessage(
                                    "Do you want to delete "
                                            + templateModel.get(position).getTemname()
                                            + " template")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            lastPosition = position;
                                            deleteSMSTemplate(templateModel.get(position).getTemid());

                                            dialog.dismiss();
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            dialog.dismiss();

                                        }
                                    }).show();
                }
            });
        }else {
            viewHolder.templateSubpagemsg.setText(Html.fromHtml(templateModel.get(position).getTemname()));
        }
        return convertView;
    }

    private void udpateSMSTemplate(String content, String templateId) {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(con)) {
            try {
                Map map = new HashMap<>();
                map.put("Page", "InsertSMSTemplates");
                map.put("Userid", Utils.getUserId(con));
                map.put("TemplateID", templateId);
                map.put("TemplateName", "Personal");
                map.put("TemplateContent", content);

                Log.i("Order", "" + map.toString());

                new RequestManager().sendPostRequest(this,
                        KEY_UPDATE_TEMPLATE, map);
            } catch (Exception e) {
                Toast.makeText(con, con.getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(con, "No Internet connect found", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteSMSTemplate(String templateId) {
        if (sms19.inapp.msg.constant.Utils.isDeviceOnline(con)) {
            try {
                Map map = new HashMap<>();
                map.put("Page", "DeleteSMSTemplates");
                map.put("Userid", Utils.getUserId(con));
                map.put("TemplateID", templateId);
                Log.i("template", "" + map.toString());

                new RequestManager().sendPostRequest(this,
                        KEY_DELETE_TEMPLATE, map);
            } catch (Exception e) {
                Toast.makeText(con, con.getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(con, "No Internet connect found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onReceiveResponse(int requestId, String response) {
        if (response != null) {
            if (requestId == KEY_UPDATE_TEMPLATE) {
                Gson gson = new Gson();
                SMSTemplateDetailsModel smsTemplateDetailsModel = gson.fromJson(response, SMSTemplateDetailsModel.class);
                if (smsTemplateDetailsModel != null && smsTemplateDetailsModel.getStatus().equalsIgnoreCase("true")) {
                    try {
                        templateModel.get(lastPosition).setTemname(smsTemplateDetailsModel.getDetails().get(0).getTemplate());
                        if (dbObject == null)
                            dbObject = new DataBaseDetails(
                                    con);
                        dbObject.Open();
                        dbObject.updateTemp("Personal", smsTemplateDetailsModel.getDetails().get(0).getTemplate(),
                                templateModel.get(lastPosition).getTemid());

                        notifyDataSetChanged();
                    } catch (Exception e) {

                    } finally {
                        dbObject.close();
                    }

                } else
                    Toast.makeText(con, con.getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();

            } else if (requestId == KEY_DELETE_TEMPLATE) {
                DataBaseDetails dbObject = null;
                Gson gson = new Gson();
                SMSTemplateDetailsModel smsTemplateDetailsModel = gson.fromJson(response, SMSTemplateDetailsModel.class);
                if (smsTemplateDetailsModel != null && smsTemplateDetailsModel.getStatus().equalsIgnoreCase("true")) {
                    try {
                        if (dbObject == null)
                            dbObject = new DataBaseDetails(
                                    con);
                        dbObject.Open();
                        dbObject.deleteTemp(templateModel.get(lastPosition).getTemid());
                        templateModel.remove(lastPosition);
                        notifyDataSetChanged();
                    } catch (Exception e) {

                    } finally {
                        if (dbObject != null)
                            dbObject.close();
                    }
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(con, con.getResources().getString(R.string.volleyError), Toast.LENGTH_LONG).show();
    }

    private static class ViewHolder {
        View textableLayout;
        View allBtnLayout;
        TextView templateSubpagemsg, editBtn, deleteBtn;
    }

}
