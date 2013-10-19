package com.listwithmore;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class Appearance extends TabActivity {
	
	public Appearance() {
		
	}
	
	public void AddSimpleTab(TabHost tabHost, String Name , Intent intent) {
		TabSpec newTabSpec = tabHost.newTabSpec(Name);
		newTabSpec.setIndicator(Name);
		newTabSpec.setContent(intent);
		tabHost.addTab(newTabSpec);
	}
	
	public void alertWithOneButton(String Title , String Message,Context context,int icon, String ButtonName) {
		AlertDialog alert = new AlertDialog.Builder(context).create();
		alert.setMessage(Message);
		alert.setTitle(Title);
		alert.setIcon(icon);
		alert.setButton(ButtonName,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						

					}
				});
		alert.show();
	}
	
	public void makeNotification(String Title,String Subject, int Icon, boolean Cancalable, PendingIntent pIntent,Context context){
		NotificationManager notificationManager = (NotificationManager) 
				context.getSystemService(NOTIFICATION_SERVICE); 
		Notification n  = new NotificationCompat.Builder(context)
        .setContentTitle(Title)
        .setContentText(Subject)
        .setSmallIcon(Icon)
        .setContentIntent(pIntent)
        .setAutoCancel(true).build();

		notificationManager.notify(1, n); 
	}
}
