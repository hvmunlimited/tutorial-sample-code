package com.listwithmore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainPage extends Activity implements OnClickListener  {
	SharedPreferences prefs= MyApplicationContext.getAppContext().getSharedPreferences(Wizard.PREFCOUNTS, 0);
	SharedPreferences.Editor prefEditor = prefs.edit();
	ArrayList<String> thumbs= new ArrayList<String>();
	UseElements SiteElements= new UseElements();
	FileOperations fo = new FileOperations();	
	Context c= MainPage.this;


	int id;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {


		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);
		ImageButton btn_animals = (ImageButton) findViewById(R.id.btn_animals);
	}

	@Override
	public void onClick(View v) {
		id = v.getId();
		new loading().execute();
	}
	
	class loading extends AsyncTask {
		ProgressDialog pds=new ProgressDialog(MainPage.this);
		
		@Override
		protected void onPreExecute() {
			pds.setCancelable(true);
			pds.setMessage("Loading ...");
			pds.show();
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... ids) {
//			fo.SaveFileFromLinkToRoot("http://static.adzerk.net/Advertisers/839852b9a1334dddb623a20d7f9310c0.jpg", MyApplicationContext.getAppContext());
//			
//			android.os.Debug.waitForDebugger();
			for (int j = 0; j < getLINKS().size(); j++) {
				String LINK = getLINKS().get(j);
				File file = new File(Environment.getExternalStorageDirectory().toString()+"/listwithmore/xml/"+FileOperations.getNameFromLink(LINK));
				
				
				if (file.exists()) {

				}
				else {
					try {
						fo.SaveFileFromLinkToSD(LINK, "listwithmore/xml");
						thumbs= SiteElements.getElements(LINK, "thumb_url");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					prefEditor.putInt(LINK, 0);
					prefEditor.commit();
					
				}
			}
	
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			Intent intent;
			switch (id) {
			case R.id.btn_downloadItems:
				intent = new Intent(c, Wizard.class);
				break;
			case R.id.btn_Favorites:
				intent = new Intent(c, FaveActivity.class);
				break;

			default:
				intent = new Intent(c, MainActivity.class);
				intent.putExtra("id", id);
				break;
			}
			
			startActivity(intent);

			pds.cancel();
			super.onPostExecute(result);
		}
	}
	
	public ArrayList<String> getLINKS(){
		Context context= MyApplicationContext.getAppContext();
		ArrayList<String> LINKS = new ArrayList<String>();
		LINKS.add(context.getResources().getString(R.string.animals));
		LINKS.add(context.getResources().getString(R.string.automobile));
		LINKS.add(context.getResources().getString(R.string.cartoon));
		LINKS.add(context.getResources().getString(R.string.dionasours));
		LINKS.add(context.getResources().getString(R.string.dragon));
		LINKS.add(context.getResources().getString(R.string.fantasy));
		LINKS.add(context.getResources().getString(R.string.flowers));
		LINKS.add(context.getResources().getString(R.string.people));
		
		return LINKS;
		
	}
	public ArrayList<ArrayList<String>> getLINKSLists() throws IOException{
		UseElements UE= new UseElements();
		
		ArrayList<String> ResLINKS= getLINKS();
		ArrayList<ArrayList<String>> ToTalList= new ArrayList<ArrayList<String>>();
		for (int j=0; j<ResLINKS.size(); j++) {
			String Link = ResLINKS.get(j);
			File Myfile = new File(Environment.getExternalStorageDirectory().toString()+"/listwithmore/xml/"+FileOperations.getNameFromLink(Link));
			ArrayList<String> IDha = new ArrayList<String>();
			
			
			if (Myfile.exists()) {
				IDha= UE.getElementsFromFile(Myfile, "thumb_url");
			}
			ToTalList.add(j, IDha);
		}
		
		return ToTalList;
		
	}

}
