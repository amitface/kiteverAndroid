package sms19.inapp.msg.asynctask;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import sms19.inapp.msg.constant.GlobalData;
import sms19.inapp.msg.constant.Utils;
import android.util.Log;

public class UploadThread implements Runnable {

	Object object[] = null;
	File file = null;
	/*org.jivesoftware.smack.packet.Message message = null;
	FTPClient clientFtpNew = null;
	String imagetoupload = "";
	String timestamp = "";
	String liveuploaded = "";
	boolean ischecked = false;*/

	public UploadThread(Object[] s) {
		object = s;
		file = (File) object[0];
		/*clientFtpNew = (FTPClient) object[1];
		message = (org.jivesoftware.smack.packet.Message) object[2];
		imagetoupload = (String) object[3];
		timestamp = (String) object[4];
		liveuploaded = (String) object[5];
		ischecked = (Boolean) object[6];*/

	}

	@Override
	public void run() {

		MyTransferListener listener = new MyTransferListener("");
		// File f = new File(imagetoupload);

		try {

			GlobalData.clientFtp.upload(file, listener);

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				GlobalData.clientFtp.disconnect(true);
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				GlobalData.clientFtp.disconnect(true);
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
			try {
				GlobalData.clientFtp.disconnect(true);
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				GlobalData.clientFtp.disconnect(true);
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				GlobalData.clientFtp.disconnect(true);
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			try {
				GlobalData.clientFtp.disconnect(true);
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPIllegalReplyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FTPException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	@Override
	public String toString() {
		return "";
	}
	
	
	
	 class MyTransferListener implements FTPDataTransferListener {
		//org.jivesoftware.smack.packet.Message message;
		String msg = "";
		//String liveuploadedurl = "";
		//boolean ischecked = false;

		public MyTransferListener(String msg) {
			this.msg = msg;
			
		}

		public void started() {
		}

		public void transferred(int length) {

//			Log.w("transferred", "transferred=======" + String.valueOf(length));

		}

		public void completed() {

			try {
				Utils.printLog("Success send  file==");
				
			} catch (Exception e) {
				Utils.printLog("Failed send  file==");
				e.printStackTrace();
			}

		}

		public void aborted() {

		}

		public void failed() {
			

		}

	}
	
	
}
