package com.kitever.pos.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import sms19.inapp.msg.constant.Utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.kitever.android.R;
import com.kitever.app.context.CustomStyle;
import com.kitever.app.context.MoonIcon;
import com.kitever.network.NetworkManager;
import com.kitever.pos.adapter.PosCartAdapter;
import com.kitever.pos.adapter.PosCartAdapter.UserCart;
import com.kitever.pos.model.data.FetchSelectedProductModelData;

import static com.kitever.app.context.CustomStyle.setRobotoThinFont;

public class PosCart extends Activity implements OnClickListener,UserCart {
	
	private ProgressDialog progressDialog = null;
	private PosCartAdapter cartAdapter;
	private ListView itemListView;
	Spinner selectType ;
	EditText searchBox ;
	TextView txt_select,update,addItems,go_cart,check_done;
	HashMap<String, FetchSelectedProductModelData> hmap=new HashMap<String, FetchSelectedProductModelData>();
	ArrayList<String> productList=new ArrayList<String>();
	String type="";
	MoonIcon micon;
	String ContactName="";
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pos_cart);
		try
		{
			Intent intent = getIntent();
			if(intent.getExtras()!=null)
			{
			type=intent.getStringExtra("Type");		
			productList = intent.getStringArrayListExtra("productList");
			hmap=(HashMap<String, FetchSelectedProductModelData>)intent.getSerializableExtra("hmap");
			ContactName=intent.getStringExtra("ContactName");
			Log.i("poscart","ContactName-"+ContactName);
			Log.i("poscart","type-"+type);
			//cartData(hmap,productList);
			}
		} catch(Exception e){e.printStackTrace();}
		
		
		setScreen();
		
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		startAcitivityWithEffect(new Intent(this,POSAddOrderScreen.class));
		NavUtils.navigateUpFromSameTask(this);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			onBackPressed();
			startAcitivityWithEffect(new Intent(this,POSAddOrderScreen.class));
			NavUtils.navigateUpFromSameTask(this);
			break;
				
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startAcitivityWithEffect(Intent slideactivity)
	{
		Bundle bndlanimation = 
				ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
		startActivity(slideactivity, bndlanimation);
	}
	private void setScreen() {
		micon=new MoonIcon(this);		
		itemListView = (ListView) findViewById(R.id.item_list_view);		
		go_cart=(TextView)findViewById(R.id.go_cart);
		txt_select=(TextView)findViewById(R.id.txt_select);
		check_done =(TextView)findViewById(R.id.check_done);

		micon.setTextfont(go_cart);
		micon.setTextfont(check_done);
		
		update=(TextView)findViewById(R.id.update);
		addItems=(TextView)findViewById(R.id.addItems);

		LinearLayout select_cart= (LinearLayout) findViewById(R.id.select_cart);

		update.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		update.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

		addItems.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		addItems.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

		select_cart.setBackgroundColor(Color.parseColor(CustomStyle.HEADER_BACKGROUND));

		setRobotoThinFont(txt_select,this);
		setRobotoThinFont(update,this);
		setRobotoThinFont(addItems,this);

		itemListView.setBackgroundColor(Color.parseColor(CustomStyle.LISTVIEW_BACKGROUND));

		go_cart.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		txt_select.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));
		check_done.setTextColor(Color.parseColor(CustomStyle.HEADER_FONT_COLOR));

		if(hmap!=null && hmap.size()>0)
		{
		cartAdapter=new PosCartAdapter(this, productList, hmap);
		itemListView.setAdapter(cartAdapter);
		}
		addItems.setOnClickListener(this);
		update.setOnClickListener(this);
		check_done.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id)
		{
		case R.id.check_done:
		case R.id.update:
			Intent intentItem = new Intent(this, POSAddOrderScreen.class);
			intentItem.putExtra("productList",productList);
			intentItem.putExtra("hmap",hmap);
			intentItem.putExtra("ContactName", ContactName);
			intentItem.putExtra("Type", type);
		    startActivity(intentItem);		
		    finish();
			break;
		case R.id.addItems:			
			Intent intent = new Intent(this, POSItemChooseScreen.class);
			intent.putExtra("productList",productList);
			intent.putExtra("hmap",hmap);
			intent.putExtra("ContactName", ContactName);
			intent.putExtra("Type", type);
		    startActivity(intent);
		    finish();
			break;
		}
	}

	@Override
	public void cartData(HashMap<String, FetchSelectedProductModelData> hmap,
			ArrayList<String> productList) {
		this.hmap=hmap;
		this.productList=productList;
	
		double tprice=0.00d;
			int totalq=0;		
			for(int i=0;i<hmap.size();i++)
			{
				String key=productList.get(i);
				String rate=hmap.get(key).getPerUnitPrice();
				String qty=hmap.get(key).getQuantity();
				int q=Integer.parseInt(qty);
				double item_rate=Double.parseDouble(rate);
				tprice=tprice+q*item_rate;
				totalq=totalq+q;			
			}
			
			if(hmap.size()>0)
			{
				
				String display_str="Items :"+ totalq +" Price : "+ Utils.doubleToString(tprice);
				txt_select.setText(display_str);
			}
			else
			{
				type="";
				txt_select.setText("No Items");
				
			}
	}

	
}
