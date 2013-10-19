package com.listwithmore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.os.Environment;

public class NetworkChangeReceiver extends BroadcastReceiver {
	FileOperations FO = new FileOperations();
	Context C;
	Appearance APP= new Appearance();
	SharedPreferences prefs= MyApplicationContext.getAppContext().getSharedPreferences(Wizard.PREFCOUNTS, 0);
	SharedPreferences.Editor prefEditor = prefs.edit();
	MainPage MP= new MainPage();
	String imagesVersion;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		C=context;
		NetWork network= new NetWork(context);
		
		
		if (network.isOnline()) {
			
		new downloader().execute();
			
		}
		else{
			
		}
		
		
	}
	
	public class downloader extends AsyncTask{

		@Override
		protected Object doInBackground(Object... paramArrayOfParams) {
//			android.os.Debug.waitForDebugger();
			String path= C.getResources().getString(R.string.PathInSD);
			FO.SaveFileFromLinkToSD(C.getResources().getString(R.string.imagesVersionCheck),path,false);
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			try {
				int olderVersion= prefs.getInt(C.getResources().getString(R.string.imagesVersionCheck), 0);
				String path= C.getResources().getString(R.string.PathInSD);
				imagesVersion = new Scanner( new File(Environment.getExternalStorageDirectory().toString()+"/"+path+"/imagesversion.xml") ).useDelimiter("\\A").next();
				if (Integer.parseInt(imagesVersion)> olderVersion ) {
					new DownloadXMLS().execute();
				}
				
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
		
	}
	
	public class DownloadXMLS extends AsyncTask{

		@Override
		protected Object doInBackground(Object... arg0) {
			ArrayList<String> Links= MP.getLINKS();
			for (String link : Links) {
				FO.SaveFileFromLinkToSD(C.getResources().getString(R.string.imagesVersionCheck),"listwithmore/xml/",false);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
			Intent i = new Intent(C, Wizard.class);
			PendingIntent pIntent = PendingIntent.getActivity(C, 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
			prefEditor.putInt(C.getResources().getString(R.string.imagesVersionCheck),Integer.parseInt(imagesVersion));
			APP.makeNotification(C.getResources().getString(R.string.imagesUpdateTitle), C.getResources().getString(R.string.imagesUpdateContent), R.drawable.ic_launcher, true, pIntent, C);
			super.onPostExecute(result);
		}
		
	}

}
