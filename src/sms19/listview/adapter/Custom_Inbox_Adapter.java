package sms19.listview.adapter;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import sms19.listview.database.DataBaseDetails;
import sms19.listview.database.ImageLoaderUtility;
import sms19.listview.newproject.FullScreen;
import com.kitever.android.R;
import sms19.listview.newproject.model.InboxReadDummy;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.assist.FailReason;

public class Custom_Inbox_Adapter extends BaseAdapter
{
	private Context gContext;
	private List<InboxReadDummy> gData;
	private int rEsource;
	public String imagePath;

	private String Mobile = "", Password = "", InAppUserid = "", SMstatus = "", IURl = "";
	DataBaseDetails dbObj;

	private String fetchdata;
	private String dd;

	String curDate = "";
    String curTime = "";
	
	ProgressDialog pDialog;
    boolean zoomOut=false;
	private static LayoutInflater inflater = null;
	
	
	String msgStatus  = "";
	String cutmsg     = "";
	String time       = "",setTime="";
	String gURL_IMAGE = "";
	String gdate      = ""; 
	String MediaType  = "";
	String MessageType  = "";
	ProgressDialog p;
	int Sstatus       = 0;
	
	boolean setAudioStatus = true;

	public Custom_Inbox_Adapter(Context cnt, List<InboxReadDummy> data, int resource) 
	{

		this.gData = data;
		this.gContext = cnt;
		this.rEsource = resource;

		inflater = (LayoutInflater) gContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// this.notifyDataSetChanged();

		////Log.w("JSR", "JSR :::::::(data size):" + data.size());

	}

	@Override
	public int getCount()
	{
	return gData.size();
	}

	@Override
	public InboxReadDummy getItem(int position)
	{
	return gData.get(position);

	}

	@Override
	public long getItemId(int position)
	{
	return gData.get(position).hashCode();
	}

	class Holder
	{
		EditText txtReceiveMsg;
		TextView txtReceiveTime;
		
		// for image setTime at send and receive image
		TextView txtReceiveTimeImage;
		TextView txtSendTimeImage;
		
		// for image setTime at send and receive Audio
		TextView txtReceiveTimeAudio;
		TextView txtSendTimeAudio;
				
		// for image setTime at send and receive Video
		TextView txtReceiveTimeVideo;
		TextView txtSendTimeVideo;

		EditText txtSendMsg;
		TextView txtSendTime;
		TextView date;

		// layout text chat
		RelativeLayout relSend;
		RelativeLayout relreceive;

		// layout date chat
		RelativeLayout LinearDATE;

		// layout image chat
		RelativeLayout RelSendImg;
		RelativeLayout RelReceiveImg;

		// imageview for show image chat
		ImageView imageRecChat;
		ImageView imageSendChat;
		
		// imageview for showing audio sending image
		ImageView imageRecAudio;
		ImageView imageSendAudio;
		
		ImageView imageStatusSend;
		
		// layout for video player
		RelativeLayout RelSendvideo;
		RelativeLayout RelReceiveVideo;
		
		// layout for audio player
		RelativeLayout RelSendAudio;
		RelativeLayout RelReceiveAudio;
		
		// videoview for play video/audio
		VideoView videoRecChat;
		VideoView videoSendChat;
		
		// play button show above video view for play video
		ImageButton play_button;
		ImageButton play_buttonRec;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		////Log.w("JSR", "JSR :::::::::(position):" + position);

		dbObj = new DataBaseDetails(gContext);

		final Holder holder;

		if (convertView == null) {

			holder = new Holder();

			convertView = inflater.inflate(rEsource, null);

			holder.txtReceiveMsg   = (EditText) convertView.findViewById(R.id.ReceiveMsg);
			holder.txtReceiveTime  = (TextView) convertView.findViewById(R.id.ReceiveTime);

			holder.txtSendMsg      = (EditText) convertView.findViewById(R.id.sendMsg);
			holder.txtSendTime     = (TextView) convertView.findViewById(R.id.senttime);
			holder.date            = (TextView) convertView.findViewById(R.id.date);

			// layout text chat
			holder.relSend         = (RelativeLayout) convertView.findViewById(R.id.LinearReceive);
			holder.relreceive      = (RelativeLayout) convertView.findViewById(R.id.LinearSend);

			// layout date chat
			holder.LinearDATE      = (RelativeLayout) convertView.findViewById(R.id.LinearDATE);

			// layout text chat image
			holder.RelSendImg      = (RelativeLayout) convertView.findViewById(R.id.RelSendImg);
			holder.RelReceiveImg   = (RelativeLayout) convertView.findViewById(R.id.RelReceiveImg);

			// image urlholder.imageRecChat
			holder.imageRecChat    = (ImageView) convertView.findViewById(R.id.imageRecChat);
			holder.imageSendChat   = (ImageView) convertView.findViewById(R.id.imageSendChat);
			
			holder.imageStatusSend = (ImageView) convertView.findViewById(R.id.imageStatusSend);

			// layout for video player
			holder.RelSendvideo    = (RelativeLayout) convertView.findViewById(R.id.RelSendvideo);
			holder.RelReceiveVideo = (RelativeLayout) convertView.findViewById(R.id.RelReceiveVideo);
			
			// videoview for play video/audio
			holder.videoRecChat    = (VideoView) convertView.findViewById(R.id.videoRecChat);
			holder.videoSendChat   = (VideoView) convertView.findViewById(R.id.videoSendChat);
			
			// map for imagebutton
			holder.play_button     = (ImageButton) convertView.findViewById(R.id.play_button);
			holder.play_buttonRec  = (ImageButton) convertView.findViewById(R.id.play_buttonRec);
			
			// layout for audio player
			holder.RelSendAudio   = (RelativeLayout) convertView.findViewById(R.id.RelAudioSend);
			holder.RelReceiveAudio = (RelativeLayout) convertView.findViewById(R.id.RelAudioReceive);
						
			// image for audio 
			holder.imageRecAudio    = (ImageView) convertView.findViewById(R.id.imageAudioRec);
			holder.imageSendAudio  = (ImageView) convertView.findViewById(R.id.imageAudioSend);
			
			// textview for image showing date and time
			holder.txtReceiveTimeImage  =(TextView) convertView.findViewById(R.id.textDateTimeShowReceive);
			holder.txtSendTimeImage      =(TextView) convertView.findViewById(R.id.textDateTimeShowSend);
			
			// textview for Audio showing date and time
			holder.txtReceiveTimeAudio  =(TextView) convertView.findViewById(R.id.textDateAudioRec);
			holder.txtSendTimeAudio      =(TextView) convertView.findViewById(R.id.textDateAudioSend);
			
			// textview for Video showing date and time
			holder.txtReceiveTimeVideo  =(TextView) convertView.findViewById(R.id.textDateVideoRec);
			holder.txtSendTimeVideo     =(TextView) convertView.findViewById(R.id.textDateVideoSend);
			
			convertView.setTag(holder);

		} 
		else
		{
			holder = (Holder) convertView.getTag();
			
			// do for not blink image at time of thread runing
			holder.imageSendChat.setImageBitmap(null);
			holder.imageRecChat.setImageBitmap(null);
		}

		holder.date.setTag(position);
		final int savePosition = (Integer) holder.date.getTag();
				
		// Animation for text view
		// Animation annimate = AnimationUtils.loadAnimation(gContext,
		// R.anim.textblinking);
		// holder.txtSendTime.startAnimation(annimate);
		
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ HIDE ALL LAYOUT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		holder.LinearDATE.setVisibility(View.GONE);
		holder.imageStatusSend.setVisibility(View.GONE);
		
		//!!!!!!!!!!!!!!!!!!!!IMAGE LAYOUT!!!!!!!!!!!!!!!!!!
		holder.RelSendImg.setVisibility(View.GONE);
		holder.RelReceiveImg.setVisibility(View.GONE);
		
		//!!!!!!!!!!!!!!!!!!!!TEXT LAYOUT!!!!!!!!!!!!!!!!!!
		holder.relreceive.setVisibility(View.GONE);
		holder.relSend.setVisibility(View.GONE);
		
		//!!!!!!!!!!!!!!!!!!!!VIDEO LAYOUT!!!!!!!!!!!!!!!!!!
		holder.RelSendvideo.setVisibility(View.GONE);
		holder.RelReceiveVideo.setVisibility(View.GONE);
		
		//!!!!!!!!!!!!!!!!!!!!AUDIO LAYOUT!!!!!!!!!!!!!!!!!!
	    holder.RelSendAudio.setVisibility(View.GONE);
	    holder.RelReceiveAudio.setVisibility(View.GONE);
		
		//!!!!!!!!!!!!!!!!!!!!HIDE IMAGE BOTTOM DATE TIME TEXTVIEW!!!!!!!!!!!!!!!!!
		holder.txtReceiveTimeImage.setVisibility(View.GONE);
		holder.txtSendTimeImage.setVisibility(View.GONE);
		
		//!!!!!!!!!!!!!!!!!!!!HIDE VIDEO BOTTOM DATE TIME TEXTVIEW!!!!!!!!!!!!!!!!!
		holder.txtReceiveTimeVideo.setVisibility(View.GONE);
		holder.txtSendTimeVideo.setVisibility(View.GONE);
				
		//!!!!!!!!!!!!!!!!!!!!HIDE AUDIO BOTTOM DATE TIME TEXTVIEW!!!!!!!!!!!!!!!!!
		holder.txtReceiveTimeAudio.setVisibility(View.GONE);
		holder.txtSendTimeAudio.setVisibility(View.GONE);

		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ HIDE ALL LAYOUT @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DATA FETCH WITH MODEL @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		try 
		{
			msgStatus  = gData.get(savePosition).getStatus();
			cutmsg     = gData.get(savePosition).getMessage();
			time       = gData.get(savePosition).getTime();
			gURL_IMAGE = gData.get(savePosition).getgIURL();
			gdate      = gData.get(savePosition).getDate();
			MediaType  = gData.get(savePosition).getMediaType();
			MessageType = gData.get(savePosition).getMessageType().trim();
			
			setTime = gdate+"::"+time;
			
			
			Sstatus = gData.get(savePosition).getSStatus();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DATA FETCH WITH MODEL @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
		
		    fetchMobileandUserId();

			////Log.w("imp", "imp :::NOT EQUAL::" + InAppUserid+","+msgStatus+",gURL_IMAGE:"+gURL_IMAGE+",MediaType:"+MediaType);
		
			// R_M(Received Messages) //////////////////////////////////////TEXT, MEDIA RECEIVE FROM SERVER/////////////////////////////RECEIVE
			if (MessageType.equalsIgnoreCase("R_M")) 
			{
				gURL_IMAGE = gData.get(savePosition).getgIURL();
			
			/*********************************************TEXT*************************************************/
			if (gURL_IMAGE == null || gURL_IMAGE.length() < 3) {
						
				/********************** VISIBLE LAYOUT ************************/
				holder.relSend.setVisibility(View.VISIBLE);
		
				/********************** SET DATA ************************/
				holder.txtReceiveMsg.setText(cutmsg);
			
				if(time.length()>5)
				{
				
				holder.txtReceiveTime.setText(time);
				}
				else
				{
			
				holder.txtReceiveTime.setText(setTime);	
				}
			}
			
			/*********************************************TEXT*************************************************/

			/*********************************************MEDIA*************************************************/
			else
			{
						
				//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\IMAGES///////////////////////////////////////RECEIVE
				if(MediaType.trim().equalsIgnoreCase("IMAGE"))
				{
					
					/********************** VISIBLE LAYOUT ************************/
					holder.RelReceiveImg.setVisibility(View.VISIBLE);
					holder.txtReceiveTimeImage.setVisibility(View.VISIBLE);
					
					gURL_IMAGE = gData.get(savePosition).getgIURL();
					
					ImageLoaderUtility imageLoaderUtility = new ImageLoaderUtility(gContext);
					//imageLoaderUtility.setEmptyDrawableID(R.drawable.ic_launcher);
					//imageLoaderUtility.setFailDrawableID(R.drawable.ic_launcher);
				
					String fileName = "";
		
					if (gURL_IMAGE.contains("storage")) 
					{
					fileName = "file://" + gURL_IMAGE;
					} 
					else 
					{
					fileName = gURL_IMAGE;
					}
										
					imageLoaderUtility.getImageLoader().displayImage(fileName, holder.imageRecChat, imageLoaderUtility.getImageOptions(), new com.nostra13.universalimageloader.core.listener.ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {

						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							holder.imageRecChat.setImageBitmap(arg2);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {

						}
					});
					
					
					
					// set date and time into image receive
					if(time.length()>5)
					{
					
					holder.txtReceiveTimeImage.setText(time);
					}
					else
					{
				
					holder.txtReceiveTimeImage.setText(setTime);	
					}
				}
				//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\IMAGES///////////////////////////////////////RECEIVE
				
				//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\VIDEO///////////////////////////////////////RECEIVE
				
				else if(MediaType.trim().equalsIgnoreCase("VIDEO"))
				{
				
						
					   /********************** VISIBLE LAYOUT ************************/
						holder.RelReceiveVideo.setVisibility(View.VISIBLE);
						holder.txtReceiveTimeVideo.setVisibility(View.VISIBLE);
						
						gURL_IMAGE = gData.get(savePosition).getgIURL();
				
						try
						{
						//String video=Environment.getExternalStorageState()+	
						Bitmap thumb = ThumbnailUtils.createVideoThumbnail(gURL_IMAGE,MediaStore.Images.Thumbnails.MINI_KIND);
						
						//BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
						//holder.videoRecChat.seekTo(100);
						//holder.videoRecChat.setBackgroundDrawable(bitmapDrawable);
					    holder.play_buttonRec.setImageBitmap(thumb);
					    holder.videoRecChat.setVisibility(View.INVISIBLE);
					    } 
						catch (Exception e)
						{
						e.printStackTrace();
						}
						
					
						
						
					
						/********************** ON CLICK LISTENER AT PLAY IMAGE ************************/
						holder.play_buttonRec.setOnClickListener(new OnClickListener() 
						{
							
							@Override
							public void onClick(View v) 
							{
														
								gURL_IMAGE = gData.get(savePosition).getgIURL();
								 
								try
								{
								holder.videoRecChat.setVisibility(View.VISIBLE);
									// Start the MediaController
								MediaController mediacontroller = new MediaController(gContext);
								mediacontroller.setAnchorView(holder.videoRecChat);
									// Get the URL from String VideoURL
									Uri video = Uri.parse(gURL_IMAGE);
									holder.videoRecChat.setMediaController(mediacontroller);
									holder.videoRecChat.setVideoURI(video);

								} 
								catch (Exception e) 
								{
//									Log.e("Error", e.getMessage());
									e.printStackTrace();
								}

								
								holder.videoRecChat.requestFocus();
								holder.videoRecChat.setOnPreparedListener(new OnPreparedListener() 
								{
									// Close the progress bar and play the video
									public void onPrepared(MediaPlayer mp) 
									{
										holder.play_buttonRec.setVisibility(View.INVISIBLE);
										holder.videoRecChat.start();
										
									}
								});
								
							}
						});
						/********************** ON CLICK LISTENER AT PLAY IMAGE ************************/
						
						// set date time into video receive from server
						if(time.length()>5)
						{
						
						holder.txtReceiveTimeVideo.setText(time);
						}
						else
						{
					
						holder.txtReceiveTimeVideo.setText(setTime);	
						}
						
					}
				//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\VIDEO//////////////////////////////////////RECEIVE
				
				//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\AUDIO//////////////////////////////////////RECEIVE
				else if(MediaType.trim().equalsIgnoreCase("AUDIO"))
				{

					/********************** VISIBLE LAYOUT ************************/
					holder.RelReceiveAudio.setVisibility(View.VISIBLE);
					holder.txtReceiveTimeAudio.setVisibility(View.VISIBLE);
					
					gURL_IMAGE = gData.get(savePosition).getgIURL();
					
					try {
																	
						// set date and time into image receive
						if(time.length()>5)
						{
						
						holder.txtReceiveTimeAudio.setText(time);
						}
						else
						{
					
						holder.txtReceiveTimeAudio.setText(setTime);	
						}
						
					} catch (Exception e) {}
						
					holder.imageRecAudio.setOnClickListener(new OnClickListener()
					{
						
						@Override
						public void onClick(View v) {
							
							gURL_IMAGE = gData.get(savePosition).getgIURL();
							
							final MediaPlayer player = new MediaPlayer();
							if(setAudioStatus)
							{
								setAudioStatus = false;
								
								 try
								 {
 									  
									 player.setDataSource(gURL_IMAGE);
									 player.prepare();
									 player.start();
									 holder.imageRecAudio.setBackgroundResource(R.drawable.audio_pause);
								        
								     player.setOnCompletionListener(new OnCompletionListener() {
											
											@Override
											public void onCompletion(MediaPlayer mp) {
												player.stop();
												player.release();
												holder.imageRecAudio.setBackgroundResource(R.drawable.aidio_play);
												setAudioStatus = true;
											}
										});
								 									        
								    } catch (Exception e) {
								        e.printStackTrace();
								 }
							}
							else{
								
							}
							
							/*if(setAudioStatus){
								
								 setAudioStatus = false;
								try {
								    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
								    player.setDataSource(gURL_IMAGE);
								    player.prepare();
								    player.start();    
								    
								    holder.imageRecAudio.setBackgroundResource(R.drawable.audio_pause);
								    
								    player.setOnCompletionListener(new OnCompletionListener() {
										
										@Override
										public void onCompletion(MediaPlayer mp) {
											 
											 player.stop();      
											 player.release();
											 holder.imageRecAudio.setBackgroundResource(R.drawable.aidio_play);
										}
									});
								   
								    
								} catch (Exception e) {
								    // TODO: handle exception
								}
							}
							else{
								
								 setAudioStatus = true;
								try {
									 if(player.isPlaying()){
										 player.stop();
										 player.release();
										 holder.imageRecAudio.setBackgroundResource(R.drawable.aidio_play);
									 }
									 
								} catch (Exception e) {
						
								}
							}*/
					
						}
					});
				}
				//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\AUDIO//////////////////////////////////////RECEIVE
			}
			/*********************************************MEDIA*************************************************/
			}
			
           //////////////////////////////////////TEXT, MEDIA RECEIVE FROM SERVER////////////////////////////////RECEIVE
			
           // S_M (Send Message) ////////////////////////////////////////TEXT, MEDIA SEND TO SERVER////////////////////////////////////SEND	
			else if (MessageType.equalsIgnoreCase("S_M"))
			{

				/************************ image chat with send image ***************************/
             	
				gURL_IMAGE = gData.get(savePosition).getgIURL();
				
				// get url and download url from server
				if (gURL_IMAGE == null || gURL_IMAGE.length() < 3) 
				{
					
					/********************** VISIBLE LAYOUT ************************/
					holder.relreceive.setVisibility(View.VISIBLE);
					//holder.imageStatusSend.setVisibility(View.VISIBLE);
					
					/********************** SET DATA ************************/
					holder.txtSendMsg.setText(cutmsg);
								
					if(time.length()>5)
					{
					holder.txtSendTime.setText(time);
					}
					else 
					{
					holder.txtSendTime.setText(setTime);
					}
					
				}

				else {
										
					//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\VIDEO//////////////////////////////////////SEND
					
					if(MediaType.trim().equalsIgnoreCase("VIDEO"))
					{
						
						// visible for video play
						holder.RelSendvideo.setVisibility(View.VISIBLE);
						holder.txtSendTimeVideo.setVisibility(View.VISIBLE);
						
						gURL_IMAGE = gData.get(savePosition).getgIURL();
						
					try
					{
				Bitmap thumb = ThumbnailUtils.createVideoThumbnail(gURL_IMAGE,MediaStore.Images.Thumbnails.MINI_KIND);
							
					BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
					holder.RelSendvideo.setBackgroundDrawable(bitmapDrawable);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						// onclick of videoview
						holder.play_button.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) 
							{
								//String videourl = "http://something.com/blah.mp4";
								// 
								gURL_IMAGE = gData.get(savePosition).getgIURL();
								
								Uri uri = Uri.parse(gURL_IMAGE);
								Intent intent = new Intent(Intent.ACTION_VIEW, uri);
								intent.setDataAndType(uri, "video/mp4");
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								gContext.startActivity(intent);
							}
						});
						
						// set date time to send video
						if(time.length()>5)
						{
						
							holder.txtSendTimeVideo.setText(time);
						}
						else
						{
						holder.txtSendTimeVideo.setText(setTime);	
						}
					}
					//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\VIDEO//////////////////////////////////////SEND
					
					//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\IMAGE//////////////////////////////////////SEND
					else if(MediaType.trim().equalsIgnoreCase("IMAGE"))
					{
							
						// visible for image
						holder.RelSendImg.setVisibility(View.VISIBLE);
						holder.txtSendTimeImage.setVisibility(View.VISIBLE);
						
						gURL_IMAGE = gData.get(savePosition).getgIURL();
						
						// for image of gallery or camera.
						try 
						{
														
							Bitmap bitmap = BitmapFactory.decodeFile(gURL_IMAGE);
							holder.imageSendChat.setImageBitmap(bitmap);
							
							// set date time to send image
							if(time.length()>5)
							{
							
								holder.txtSendTimeImage.setText(time);
							}
							else
							{
							holder.txtSendTimeImage.setText(setTime);	
							}
							
						} catch (Exception e) {}
						
						/*holder.imageSendChat.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
							
								Uri uri = Uri.parse("file://" +gURL_IMAGE);
								
								////Log.w("TT","TT ::::(gUrl_Image)"+gURL_IMAGE+"::uri:(path):"+uri);
								
							    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
								intent.setDataAndType(uri, "image/*");
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								gContext.startActivity(intent);
				     									
							}
						});*/
															
					
					}
					
					//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\IMAGE//////////////////////////////////////SEND
					
					//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\AUDIO//////////////////////////////////////SEND
					else if(MediaType.trim().equalsIgnoreCase("AUDIO")){
						
						// visible for audio
						holder.RelSendAudio.setVisibility(View.VISIBLE);
						holder.txtSendTimeAudio.setVisibility(View.VISIBLE);
						
						gURL_IMAGE = gData.get(savePosition).getgIURL();
						
						try {
														
							//Bitmap bitmap = BitmapFactory.decodeFile(gURL_IMAGE);
							//holder.imageSendAudio.setImageBitmap(bitmap);
												
							// set date time to send Audio
							if(time.length()>5)
							{
							
								holder.txtSendTimeAudio.setText(time);
							}
							else
							{
							holder.txtSendTimeAudio.setText(setTime);	
							}
							
						} catch (Exception e) {}
						
						holder.imageSendAudio.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								
								gURL_IMAGE = gData.get(savePosition).getgIURL();
								
								final MediaPlayer player = new MediaPlayer();
								if(setAudioStatus){
									setAudioStatus = false;
									
									 try {
	 									  
										 player.setDataSource(gURL_IMAGE);
										 player.prepare();
										 player.start();
									     holder.imageSendAudio.setBackgroundResource(R.drawable.audio_pause);
									        
									     player.setOnCompletionListener(new OnCompletionListener() {
												
												@Override
												public void onCompletion(MediaPlayer mp) {
													player.stop();
													player.release();
													holder.imageSendAudio.setBackgroundResource(R.drawable.aidio_play);
													setAudioStatus = true;
												}
											});
									 									        
									    } catch (Exception e) {
									        e.printStackTrace();
									 }
								}
								else{
									
								}
								
								

								/* if(setAudioStatus){
									 setAudioStatus = false;
									 try {
										  									  
										 player.setDataSource(gURL_IMAGE);
										 player.prepare();
										 player.start();
									     holder.imageSendAudio.setBackgroundResource(R.drawable.audio_pause);
									        
									     player.setOnCompletionListener(new OnCompletionListener() {
												
												@Override
												public void onCompletion(MediaPlayer mp) {
													player.stop();
													player.release();
													holder.imageSendAudio.setBackgroundResource(R.drawable.aidio_play);
													
												}
											});
									        
									        
											
																			        
									    } catch (Exception e) {
									        e.printStackTrace();
									 }
								 }
								 else{
									 setAudioStatus = true;
								
									 try {
										if(player.isPlaying()){
											 player.pause();
											 player.stop();
											 player.release();
											 holder.imageSendAudio.setBackgroundResource(R.drawable.aidio_play);
										 }
									} catch (IllegalStateException e) {
				
									}
									 
									
									
								 }
								 
							   */
							}
						});
					}
					//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\AUDIO//////////////////////////////////////SEND
						
					
				}
			}
			
	  //////////////////////////////////////////TEXT, MEDIA SEND TO SERVER///////////////////////////////////////SEND
			holder.imageSendChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					gURL_IMAGE = gData.get(savePosition).getgIURL();
					
					Intent i=new Intent(gContext,FullScreen.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("image",gURL_IMAGE);
					gContext.startActivity(i);
					

					
				}
			});
			/*holder.imageRecChat.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					gURL_IMAGE = gData.get(savePosition).getgIURL();
					Intent i=new Intent(gContext,FullScreen.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("image",gURL_IMAGE);
					gContext.startActivity(i);
				}
			});
*/
		return convertView;

	}

	public void fetchMobileandUserId() {
		dbObj.Open();
		Cursor c;
		c = dbObj.getLoginDetails();

		while (c.moveToNext()) {
			  InAppUserid    = c.getString(3);
	    }
		dbObj.close();
	}

	public void fetchMSendingStatus(String recipientid) {
		dbObj.Open();
		Cursor c;
		c = dbObj.getallmessages(recipientid);

		while (c.moveToNext()) {
			SMstatus = c.getString(9);
			IURl = c.getString(6);

			////Log.w("IURL", "IURL :::: IURL:" + IURl + ",(SendStatus):" + SMstatus);

		}
		dbObj.close();
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> 
	{

		@Override
		protected void onPreExecute() {
			 super.onPreExecute();
	       
		}
		
		@Override
		protected String doInBackground(String... f_url) {
			int count;
	        try {
	        
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	      
//	            int lenghtOfFile = conection.getContentLength();

	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
	            OutputStream output = new FileOutputStream("/sdcard/Video1"+".3gp");
	            byte data[] = new byte[1024];
	            long total = 0;
	           while ((count = input.read(data)) != -1) 
	           {
	                total += count;
	               output.write(data, 0, count);
	            }
	            output.flush();
	            output.close();
	            input.close();
	            
	        } catch (Exception e) {
//	        Log.e("Error+video: ", "Please Try again later");
	        }
	        
	        return null;
		}
			
		@Override
		protected void onPostExecute(String file_url) {

			
		}

	}

}
