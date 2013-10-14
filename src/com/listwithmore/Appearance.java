package com.listwithmore;

import android.R.drawable;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	

}
