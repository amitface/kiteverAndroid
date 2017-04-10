package sms19.listview.newproject.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;

import sms19.listview.newproject.TemplateList;
import sms19.listview.newproject.Templaten_new;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

/**
 * Created by android on 27/2/17.
 */

public class TemplateSmsFragmentAdapter extends ArrayAdapter<String>{
    private Activity context;
    private int resource;
    String templateneme[];
    private String Temgoto;
    private String InboxRead;
    private String InboxNum;
    private String Inboxrec;

    // private LayoutInflater inflater;

    public TemplateSmsFragmentAdapter(Activity context, int resource,
                         String[] objects, String status, String inboxname,
                         String inboxNumber, String recid) {
        super(context, resource, objects);

        this.templateneme = objects;
        this.resource = resource;
        this.context = context;
        this.Temgoto = status;
        this.InboxRead = inboxname;
        this.InboxNum = inboxNumber;
        this.Inboxrec = recid;
        // inflater = (LayoutInflater)
        // context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    class Holder {
        TextView templatetitle;
        ImageView templateicon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Log.w("TEM", "TEM :::::::::(position)" + position);

        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.template_icon_custom, null);

            holder.templatetitle = (TextView) convertView
                    .findViewById(R.id.templatenbame);
            holder.templateicon = (ImageView) convertView
                    .findViewById(R.id.templateicon);
            convertView.setTag(holder);

            setRobotoThinFont(holder.templatetitle,context);
            holder.templatetitle.setTextColor(Color.parseColor(CustomStyle.THEME_FONT_COLOR));


        }

        else {
            holder = (Holder) convertView.getTag();
        }

        holder.templatetitle.setText(templateneme[position]);
        final String data = templateneme[position];

        holder.templateicon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, TemplateList.class);
                i.putExtra("Template", data);
                i.putExtra("goto", Temgoto);
                i.putExtra("gimp", InboxRead);
                i.putExtra("gimp1", InboxNum);
                i.putExtra("gimp2", Inboxrec);
                context.startActivityForResult(i, 202);

            }
        });

        String value = templateneme[position].trim();
        // static final String[] GRID_DATA
        // ={"Send Sms","Contacts","Template","Sms Reports","Buy Credit","Upcoming Events"};

        //Log.w("TEM", "TEM :::::::::(value)" + value);
        if (value.equalsIgnoreCase("Personal")) {
            holder.templateicon.setImageResource(R.drawable.personal_template);
        } else if (value.equalsIgnoreCase("Akshaya Tritiya")) {
            holder.templateicon.setImageResource(R.drawable.akshaytritiya);
        } else if (value.equalsIgnoreCase("Angry SMS")) {
            holder.templateicon.setImageResource(R.drawable.angrysms);
        } else if (value.equalsIgnoreCase("Anniversary SMS")) {
            holder.templateicon.setImageResource(R.drawable.aniversarysms);
        } else if (value.equalsIgnoreCase("Attitude SMS")) {
            holder.templateicon.setImageResource(R.drawable.attitude);
        } else if (value.equalsIgnoreCase("Bad Luck SMS")) {
            holder.templateicon.setImageResource(R.drawable.badluck);
        } else if (value.equalsIgnoreCase("Baisakhi SMS")) {
            holder.templateicon.setImageResource(R.drawable.baishkhi);
        } else if (value.equalsIgnoreCase("Basant Panchami SMS")) {
            holder.templateicon.setImageResource(R.drawable.basantpanchmi);
        } else if (value.equalsIgnoreCase("Bhai Dooj SMS")) {
            holder.templateicon.setImageResource(R.drawable.bhaiduj);
        } else if (value.equalsIgnoreCase("Birthday")) {
            holder.templateicon.setImageResource(R.drawable.birthday);
        } else if (value.equalsIgnoreCase("Boys Girls SMS")) {
            holder.templateicon.setImageResource(R.drawable.boys_girls);
        } else if (value.equalsIgnoreCase("Brother's Day")) {
            holder.templateicon.setImageResource(R.drawable.brothers_day);
        } else if (value.equalsIgnoreCase("Buddha Purnima SMS")) {
            holder.templateicon.setImageResource(R.drawable.budhpurnima);
        } else if (value.equalsIgnoreCase("Chhath Puja SMS")) {
            holder.templateicon.setImageResource(R.drawable.chhat_puja);
        } else if (value.equalsIgnoreCase("Christmas SMS")) {
            holder.templateicon.setImageResource(R.drawable.cristmas);
        } else if (value.equalsIgnoreCase("Cool Sms")) {
            holder.templateicon.setImageResource(R.drawable.cool_sms);
        } else if (value.equalsIgnoreCase("Cute SMS")) {
            holder.templateicon.setImageResource(R.drawable.cutesms);
        } else if (value.equalsIgnoreCase("Daughters Day SMS")) {
            holder.templateicon.setImageResource(R.drawable.daughtersd);
        } else if (value.equalsIgnoreCase("Dhanteras SMS")) {
            holder.templateicon.setImageResource(R.drawable.dhanteras);
        } else if (value.equalsIgnoreCase("Diwali SMS")) {
            holder.templateicon.setImageResource(R.drawable.dipawali);
        } else if (value.equalsIgnoreCase("Dua SMS")) {
            holder.templateicon.setImageResource(R.drawable.dua);
        } else if (value.equalsIgnoreCase("Durga Puja SMS")) {
            holder.templateicon.setImageResource(R.drawable.durga_puja);
        } else if (value.equalsIgnoreCase("Dussehra SMS")) {
            holder.templateicon.setImageResource(R.drawable.dashuhara);
        } else if (value.equalsIgnoreCase("Easter SMS")) {
            holder.templateicon.setImageResource(R.drawable.easter);
        } else if (value.equalsIgnoreCase("Eid SMS")) {
            holder.templateicon.setImageResource(R.drawable.eidsms);
        } else if (value.equalsIgnoreCase("Emotional SMS")) {
            holder.templateicon.setImageResource(R.drawable.emotional);
        } else if (value.equalsIgnoreCase("Exams SMS")) {
            holder.templateicon.setImageResource(R.drawable.exam);
        } else if (value.equalsIgnoreCase("Fathers Day")) {
            holder.templateicon.setImageResource(R.drawable.fathers);
        } else if (value.equalsIgnoreCase("Forget SMS")) {
            holder.templateicon.setImageResource(R.drawable.forgot);
        } else if (value.equalsIgnoreCase("Friendship day")) {
            holder.templateicon.setImageResource(R.drawable.friendship);
        } else if (value.equalsIgnoreCase("Funny SMS")) {
            holder.templateicon.setImageResource(R.drawable.funny);
        } else if (value.equalsIgnoreCase("Ganesh Chaturthi SMS")) {
            holder.templateicon.setImageResource(R.drawable.ganesh);
        } else if (value.equalsIgnoreCase("Get Well Soon")) {
            holder.templateicon.setImageResource(R.drawable.getwellsoon);
        } else if (value.equalsIgnoreCase("Good Afternoon SMS")) {
            holder.templateicon.setImageResource(R.drawable.goodafter_noon);
        } else if (value.equalsIgnoreCase("Good Evening SMS")) {
            holder.templateicon.setImageResource(R.drawable.goodevening);
        } else if (value.equalsIgnoreCase("Good Friday SMS")) {
            holder.templateicon.setImageResource(R.drawable.goodfriday);
        } else if (value.equalsIgnoreCase("Good Luck")) {
            holder.templateicon.setImageResource(R.drawable.goodluck);
        } else if (value.equalsIgnoreCase("Good Morning")) {
            holder.templateicon.setImageResource(R.drawable.goodmorning);
        } else if (value.equalsIgnoreCase("Good Night")) {
            holder.templateicon.setImageResource(R.drawable.goodnight);
        } else if (value.equalsIgnoreCase("Govardhan Puja SMS")) {
            holder.templateicon.setImageResource(R.drawable.goverdhan);
        } else if (value.equalsIgnoreCase("Greetings SMS")) {
            holder.templateicon.setImageResource(R.drawable.greetings);
        } else if (value.equalsIgnoreCase("Gudi Padwa SMS")) {
            holder.templateicon.setImageResource(R.drawable.gudipadwa);
        } else if (value.equalsIgnoreCase("Gurpurab SMS")) {
            holder.templateicon.setImageResource(R.drawable.guruparva);
        } else if (value.equalsIgnoreCase("Guru Nanak Jayanti SMS")) {
            holder.templateicon.setImageResource(R.drawable.gurunanakjaynti);
        } else if (value.equalsIgnoreCase("Guru Purnima")) {
            holder.templateicon.setImageResource(R.drawable.gurupurnima);
        } else if (value.equalsIgnoreCase("Guru Purnima SMS")) {
            holder.templateicon.setImageResource(R.drawable.gurupurnima);
        } else if (value.equalsIgnoreCase("Hanukkah SMS")) {
            holder.templateicon.setImageResource(R.drawable.hanukka);
        } else if (value.equalsIgnoreCase("Hola Mohalla SMS")) {
            holder.templateicon.setImageResource(R.drawable.holamohala);
        } else if (value.equalsIgnoreCase("Holi SMS")) {
            holder.templateicon.setImageResource(R.drawable.holii);
        } else if (value.equalsIgnoreCase("Independence Day")) {
            holder.templateicon.setImageResource(R.drawable.indipendence);
        } else if (value.equalsIgnoreCase("Janmashtami SMS")) {
            holder.templateicon.setImageResource(R.drawable.janmastmi);
        } else if (value.equalsIgnoreCase("Kali Puja SMS")) {
            holder.templateicon.setImageResource(R.drawable.kalipuja);
        } else if (value.equalsIgnoreCase("Kanjoos SMS")) {
            holder.templateicon.setImageResource(R.drawable.kanjoos);
        } else if (value.equalsIgnoreCase("Karwa Chauth SMS")) {
            holder.templateicon.setImageResource(R.drawable.karwachaouth);
        } else if (value.equalsIgnoreCase("Lohri SMS")) {
            holder.templateicon.setImageResource(R.drawable.lohri);
        } else if (value.equalsIgnoreCase("Love SMS")) {
            holder.templateicon.setImageResource(R.drawable.love);
        } else if (value.equalsIgnoreCase("Mahashivratri SMS")) {
            holder.templateicon.setImageResource(R.drawable.shivratri);
        } else if (value.equalsIgnoreCase("Mahavir Jayanti SMS")) {
            holder.templateicon.setImageResource(R.drawable.mahavirjaynti);
        } else if (value.equalsIgnoreCase("Makar Sankranti SMS")) {
            holder.templateicon.setImageResource(R.drawable.makarsakrati);
        } else if (value.equalsIgnoreCase("Missing SMS")) {
            holder.templateicon.setImageResource(R.drawable.missing);
        } else if (value.equalsIgnoreCase("Mothers Day")) {
            holder.templateicon.setImageResource(R.drawable.mothersday);
        } else if (value.equalsIgnoreCase("Motivational Quotes SMS")) {
            holder.templateicon.setImageResource(R.drawable.motivational);
        } else if (value.equalsIgnoreCase("Nag Panchmi")) {
            holder.templateicon.setImageResource(R.drawable.nagpanchmi);
        } else if (value.equalsIgnoreCase("Navratri SMS")) {
            holder.templateicon.setImageResource(R.drawable.navratri);
        } else if (value.equalsIgnoreCase("New Year SMS")) {
            holder.templateicon.setImageResource(R.drawable.happynewyear);
        } else if (value.equalsIgnoreCase("Onam SMS")) {
            holder.templateicon.setImageResource(R.drawable.onam);
        } else if (value.equalsIgnoreCase("Poetry Sms")) {
            holder.templateicon.setImageResource(R.drawable.poetry);
        } else if (value.equalsIgnoreCase("Pongal SMS")) {
            holder.templateicon.setImageResource(R.drawable.pongal);
        } else if (value.equalsIgnoreCase("Rakhi SMS")) {
            holder.templateicon.setImageResource(R.drawable.rakhi);
        } else if (value.equalsIgnoreCase("Ram Navami SMS")) {
            holder.templateicon.setImageResource(R.drawable.ramnavami);
        } else if (value.equalsIgnoreCase("Ramdan")) {
            holder.templateicon.setImageResource(R.drawable.ramjan);
        } else if (value.equalsIgnoreCase("Republic Day")) {
            holder.templateicon.setImageResource(R.drawable.republicday);
        } else if (value.equalsIgnoreCase("Romantic SMS")) {
            holder.templateicon.setImageResource(R.drawable.romantic);
        } else if (value.equalsIgnoreCase("Saraswati Puja SMS")) {
            holder.templateicon.setImageResource(R.drawable.sarswatipuja);
        } else if (value.equalsIgnoreCase("Sister Day SMS")) {
            holder.templateicon.setImageResource(R.drawable.sisteday);
        } else if (value.equalsIgnoreCase("Smart SMS")) {
            holder.templateicon.setImageResource(R.drawable.smart);
        } else if (value.equalsIgnoreCase("Smile SMS")) {
            holder.templateicon.setImageResource(R.drawable.smaile);
        } else if (value.equalsIgnoreCase("Sorry Sms")) {
            holder.templateicon.setImageResource(R.drawable.sorry);
        } else if (value.equalsIgnoreCase("Success SMS")) {
            holder.templateicon.setImageResource(R.drawable.success);
        } else if (value.equalsIgnoreCase("Thank You SMS")) {
            holder.templateicon.setImageResource(R.drawable.thankyou);
        } else if (value.equalsIgnoreCase("Thanksgiving Day")) {
            holder.templateicon.setImageResource(R.drawable.thanksgiving);
        } else if (value.equalsIgnoreCase("Vishwakarma Puja SMS")) {
            holder.templateicon.setImageResource(R.drawable.vishvakarma);
        } else if (value.equalsIgnoreCase("Weather SMS")) {
            holder.templateicon.setImageResource(R.drawable.weather);
        } else if (value.equalsIgnoreCase("Wisdom SMS")) {
            holder.templateicon.setImageResource(R.drawable.wisdom);
        } else if (value.equalsIgnoreCase("Wishes SMS")) {
            holder.templateicon.setImageResource(R.drawable.wishes);
        } else if (value.equalsIgnoreCase("Womens Day")) {
            holder.templateicon.setImageResource(R.drawable.womens);
        }
        // else
        // {
        // // holder.templateicon.setImageResource(R.drawable.ic_launcher);
        // }

        return convertView;
    }
}