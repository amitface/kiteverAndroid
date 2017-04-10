package sms19.listview.newproject;

import java.util.HashMap;
import java.util.Map;

import sms19.inapp.msg.constant.Apiurls;
import sms19.listview.database.DataBaseDetails;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kitever.android.R;

public class ChangePassword extends ActionBarActivity{

	EditText EDIT_OLD_PASSWORD;
	EditText EDIT_NEW_PASSWORD;
	EditText EDIT_CONFIRM_PASSWORD;
	Button   BTN_UPDATE;
	
	String STR_OLD_PASSWORD;
	String STR_NEW_PASSWORD;
	String STR_CONFIRM_PASSWORD;
	
//	String STR_DATABASE_PASSORD;
	String STR_DATABASE_USERID;
	String STR_DATABASE_LOGIN;
	
	DataBaseDetails databaseobj;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.changepassword);
		
		// mapping id with xml layout
		EDIT_OLD_PASSWORD     = (EditText) findViewById(R.id.editOldPassword);
		EDIT_NEW_PASSWORD     = (EditText) findViewById(R.id.editNewPassword);
		EDIT_CONFIRM_PASSWORD = (EditText) findViewById(R.id.editConfirmPassword);
		BTN_UPDATE            = (Button)   findViewById(R.id.btnchangepassword);
		
		
		// create database object
		databaseobj  = new DataBaseDetails(this);
		
		// change action bar name and color
		ActionBar bar = getSupportActionBar();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#006966")));
		bar.setTitle(Html.fromHtml("<font color='#ffffff'>Change Password</font>"));
//		bar.setHomeAsUpIndicator(R.drawable.back_new);
		
		   
		
		// onclick listener at update button
		BTN_UPDATE.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			 
		
				STR_OLD_PASSWORD     = EDIT_OLD_PASSWORD.getText().toString();
				STR_NEW_PASSWORD     = EDIT_NEW_PASSWORD.getText().toString();
				STR_CONFIRM_PASSWORD = EDIT_CONFIRM_PASSWORD.getText().toString();
				
		        fetchPasswordFromDatabase();
		        //Toast.makeText(getApplicationContext(), "login "+STR_DATABASE_LOGIN, Toast.LENGTH_LONG).show();
		        // check old password is right or not
		        try {
					if(STR_OLD_PASSWORD.length()>4){
//						if(STR_OLD_PASSWORD.trim().equals(STR_DATABASE_PASSORD.trim())){
							if(STR_NEW_PASSWORD.length()>4){
								if(STR_CONFIRM_PASSWORD.length()>4){
									if(STR_NEW_PASSWORD.trim().equals(STR_CONFIRM_PASSWORD)){
										// call web service for change password
										callChangePasswordService();
									}
									else{
										showDialogAlert("New password and Confirm password does not match");
//										Toast.makeText(getApplicationContext(), "New password does not match", Toast.LENGTH_SHORT).show();
									}
								}
								else{
									showDialogAlert("Please enter valid Confirm password");
//									Toast.makeText(getApplicationContext(), "Please enter Confirm password", Toast.LENGTH_SHORT).show();
								}
							}
							else{
								showDialogAlert("Please enter valid New password");
//								Toast.makeText(getApplicationContext(), "Please enter New password", Toast.LENGTH_SHORT).show();

							}
						}
//						else{
//							showDialogAlert("Old password does not match");
////							Toast.makeText(getApplicationContext(), "Old password does not match", Toast.LENGTH_SHORT).show();
//						}
//					}
					else{
						showDialogAlert("Please enter valid Old password");
//						Toast.makeText(getApplicationContext(), "Please enter Old password", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			}
		});
	}
	
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		 switch (item.getItemId()) {
	        case android.R.id.home:
                      finish();
	            return true;
	        }
	        return super.onOptionsItemSelected(item);

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		finish();
	}
	public void callChangePasswordService(){
		
//          Toast.makeText(getApplicationContext(), "login="+STR_DATABASE_LOGIN+" dbpwd="+STR_DATABASE_PASSORD+" nwpd="+STR_NEW_PASSWORD, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apiurls.KIT19_BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    	//Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    	if(response.toLowerCase().contains("successfully")){
                    		EDIT_OLD_PASSWORD.setText("");
                    		EDIT_NEW_PASSWORD.setText("");
                    		EDIT_CONFIRM_PASSWORD.setText("");
//                    		showDialogAlert("password changed successfully");
                    		new AlertDialog.Builder(ChangePassword.this)
                    		.setCancelable(false)
//                    		.setTitle("Login Failed!")
                    		.setMessage("password changed successfully")
                    		.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
                    		{

                    			@Override
                    			public void onClick(DialogInterface dialog, int which) 
                    			{
                    				
                    				dialog.cancel();
                    				Intent intent=new Intent(ChangePassword.this, LoginPage.class);
                    				intent.putExtra("relogin", true);
                    				startActivity(intent);
                    				finish();

                    			}
                    		}).show();
//                    		Toast.makeText(getApplicationContext(), "password changed nw pass:="+STR_NEW_PASSWORD, Toast.LENGTH_LONG).show();
                    		/*
                    		databaseobj.Open();
            				Cursor c;
            				c=databaseobj.getLoginDetails();
            				if(c.getCount()>0)
            				{
            					databaseobj.updateUserpassword(STR_NEW_PASSWORD);
            				}
            				databaseobj.close();
                    		
                    		*/
                    		
                    		/*
                    		SQLiteDatabase db = databaseobj.getWritableDatabase();
                    		ContentValues cv = new ContentValues();
                    		cv.put("password", STR_NEW_PASSWORD);
                    		db.update("login", cv, "password"+"="+"'"+STR_NEW_PASSWORD+"'", null);
                    		*/
                    			
                    		/*
                    		databaseobj.Open();
							databaseobj.updateUserpassword(STR_NEW_PASSWORD);
							databaseobj.close(); 
							*/
						}
                    	else{
//                    		showDialogAlert("failed");
                    		showDialogAlert("wrong password");
//                    		Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();
                    	}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePassword.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Page", "ChangePassword");
                params.put("UserLogin",STR_DATABASE_LOGIN);
                params.put("OldPwd",STR_OLD_PASSWORD);
                params.put("NewPwd", STR_NEW_PASSWORD);
              
                
                
                return params;
            }
 
        };
 
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
		
		
		/*
		Toast.makeText(getApplicationContext(), "above", Toast.LENGTH_LONG).show();
		new webservice(null, webservice.ChangePassword.geturl(STR_DATABASE_LOGIN,STR_OLD_PASSWORD, STR_NEW_PASSWORD) , webservice.TYPE_POST, webservice.TYPE_CHANGE_PASSWORD, new ServiceHitListener() {
			
			@Override
			public void onSuccess(Object Result, int id) {
				//Toast.makeText(getApplicationContext(), " 5 hello"+ STR_DATABASE_USERID+ " ha "+STR_DATABASE_PASSORD+" no?", Toast.LENGTH_SHORT).show();
				
				Toast.makeText(getApplicationContext(), "onsuccess here", Toast.LENGTH_SHORT).show();
				ChangePasswordModel model = (ChangePasswordModel) Result;
				String temp = "temp";
				
				try {
					//Toast.makeText(getApplicationContext(), temp+" size="+model.getChangepassword().size(), Toast.LENGTH_LONG).show();
					if(model.getChangepassword().size() > 0){
						try {

							Toast.makeText(getApplicationContext(), "checking", Toast.LENGTH_LONG).show();
							temp = temp+" size="+model.getChangepassword().size();
							Toast.makeText(getApplicationContext(), temp+" msg="+model.getChangepassword().get(0).getMsg().length(), Toast.LENGTH_LONG).show();
							
							if(model.getChangepassword().get(0).getMsg().length()>0){
								Toast.makeText(getApplicationContext(), "entered into db with pass="+ STR_NEW_PASSWORD, Toast.LENGTH_SHORT).show();
								//Toast.makeText(getApplicationContext(), " "+model.getChangepassword().get(0).getMsg(), Toast.LENGTH_SHORT).show();
								
				Toast.makeText(getApplicationContext(), "entered into db with pass="+ STR_NEW_PASSWORD, Toast.LENGTH_SHORT).show();
								databaseobj.Open();
								databaseobj.updateUserpassword(STR_NEW_PASSWORD);
								databaseobj.close();
								
							}
							else{
								//Toast.makeText(getApplicationContext(), "1 hello"+ STR_DATABASE_USERID+ " ha "+STR_DATABASE_PASSORD+" no?", Toast.LENGTH_SHORT).show();
								Toast.makeText(getApplicationContext(), " "+model.getChangepassword().get(0).getError(), Toast.LENGTH_SHORT).show();

							}
						} catch (Exception e) {
							//Toast.makeText(getApplicationContext(), "2 hello"+ STR_DATABASE_USERID+ " ha "+STR_DATABASE_PASSORD+" no?", Toast.LENGTH_SHORT).show();
							Toast.makeText(getApplicationContext(), "k "+model.getChangepassword().get(0).getError(), Toast.LENGTH_SHORT).show();
						}
					}
					else{
						//Toast.makeText(getApplicationContext(), "6 hello"+ STR_DATABASE_USERID+ " ha "+STR_DATABASE_PASSORD+" no?", Toast.LENGTH_SHORT).show();
						Toast.makeText(getApplicationContext(), "k "+model.getChangepassword().get(0).getError(), Toast.LENGTH_SHORT).show();
                            ///remove ths else
					}
				} catch (Exception e) {
					//Toast.makeText(getApplicationContext(), "4 hello"+ STR_DATABASE_USERID+ " ha "+STR_DATABASE_PASSORD+" no?", Toast.LENGTH_SHORT).show();
					Toast.makeText(getApplicationContext(), "k "+model.getChangepassword().get(0).getError(), Toast.LENGTH_SHORT).show();
				}
				
				
			}
			
			@Override
			public void onError(String Error, int id) {
				
			}
		});
		*/
	}


	
	public void fetchPasswordFromDatabase() 
	{
		databaseobj.Open();
		  
		   Cursor c;
		   
		   c= databaseobj.getLoginDetails();
		   
		  				   
		   while(c.moveToNext())
		   {
//			   STR_DATABASE_PASSORD = c.getString(5); 
			   STR_DATABASE_USERID  = c.getString(3);
			   STR_DATABASE_LOGIN = c.getString(6);
			  // Toast.makeText(getApplicationContext(), "mobile "+c.getString(1), Toast.LENGTH_LONG).show();
			   
		   }
		 
		   databaseobj.close();
	}
	private void showDialogAlert(String msg){
		new AlertDialog.Builder(ChangePassword.this)
		.setCancelable(false)
//		.setTitle("Login Failed!")
		.setMessage(msg)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
		{

			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();

			}
		}).show();
	}
}
