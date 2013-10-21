package com.listwithmore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.android.vending.billing.IInAppBillingService;

public class MainPage extends Activity  {
	SharedPreferences prefs= MyApplicationContext.getAppContext().getSharedPreferences(Wizard.PREFCOUNTS, 0);
	SharedPreferences.Editor prefEditor = prefs.edit();
	ArrayList<String> thumbs= new ArrayList<String>();
	UseElements SiteElements= new UseElements();
	FileOperations fo = new FileOperations();	
	Context c= MainPage.this;
	IInAppBillingService mService;
	ArrayList skuList = new ArrayList();
	Bundle querySkus = new Bundle();
	Bundle skuDetails = new Bundle();
	AssignArray AA= new AssignArray();
	Context C= MainPage.this;
	UseElements UE= new UseElements();

	int id;
	
//	ServiceConnection mServiceConn = new ServiceConnection() {
//		
//		@Override
//		public void onServiceDisconnected(ComponentName arg0) {
//			mService = null;
//			
//		}
//		
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			mService = IInAppBillingService.Stub.asInterface(service);
//			
//		}
//	};
//	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		bindService(new Intent("ir.cafebazaar.pardakht.InAppBillingService.BIND"), mServiceConn, Context.BIND_AUTO_CREATE);
//		prefEditor.putBoolean("isFirst", false);
//		prefEditor.commit();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);
		ImageButton btn_animals = (ImageButton) findViewById(R.id.btn_animals);
	}

	
	public void onClick(View v) {
		id = v.getId();
		new loading().execute();
	}
	
	public void onDownloadClick(View v) {
		id = v.getId();
		new Wizardloading().execute();
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
					prefEditor.putInt(getResources().getString(R.string.imagesVersionCheck), 0);
					prefEditor.putInt(getResources().getString(R.string.appVersionCheck), 0);
					prefEditor.commit();
					
				}
			}
	
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			Intent intent;
			switch (id) {
//			case R.id.btn_downloadItems:
//				intent = new Intent(c, Wizard.class);
//				break;
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
	

	class Wizardloading extends AsyncTask {
		ProgressDialog pds=new ProgressDialog(MainPage.this);
		
		@Override
		protected void onPreExecute() {
			pds.setCancelable(true);
			pds.setMessage("Loading ...");
			pds.show();
			// operations for fetching bazaar items
			skuList.add("One");
			skuList.add("Five");
//			skuList.add("zzzz");
//			skuList.add("zzzz");
//			skuList.add("zzzz");
//			skuList.add("zzzz");
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			//end
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
					prefEditor.putInt(getResources().getString(R.string.imagesVersionCheck), 0);
					prefEditor.putInt(getResources().getString(R.string.appVersionCheck), 0);
					prefEditor.commit();
					
				}
			}
			//bazaar items
//			try {
//				skuDetails = mService.getSkuDetails(3, 
//				getPackageName(), "inapp", querySkus);
//			} catch (RemoteException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			//end
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			Intent intent;
			switch (id) {
			case R.id.btn_downloadItems:
				intent = new Intent(c, Wizard.class);
				break;
//			case R.id.btn_Favorites:
//				intent = new Intent(c, FaveActivity.class);
//				break;
//
			default:
				intent = new Intent(c, MainActivity.class);
//				intent.putExtra("id", id);
				break;
			}
			//bazaar stuff
			
//			if (skuDetails == null) {
//				
//			}
//			else{
//				int response = skuDetails.getInt("RESPONSE_CODE");
//				intent.putExtra("response", response);
//				   
//				if (response == 0) {
//					ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
//					ArrayList<String> titles = new ArrayList<String>();
//					ArrayList<String> descriptions = new ArrayList<String>();
//					ArrayList<String> prices = new ArrayList<String>();
//					ArrayList<String> skus = new ArrayList<String>();
//					
//				   for (String thisResponse : responseList) {
//				      JSONObject object;
//					try {
//						object = new JSONObject(thisResponse);
//						String sku = object.getString("productId");
//						String title = object.getString("title");
//				        String price = object.getString("price");
//				        String description= object.getString("description");
//				        skus.add(sku);
//				        titles.add(title);
//				        prices.add(price);
//				        descriptions.add(description);
//				        
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				   }
//				   intent.putExtra("titles", titles);
//				   intent.putExtra("prices", prices);
//				   intent.putExtra("descriptions", descriptions);
//				   intent.putExtra("skus", skus);
//				    
//				}
//			}
			   
			   //end
			
			startActivity(intent);
			pds.dismiss();
			
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

	public void onDestroy() {
	    super.onDestroy();
//	    if (mServiceConn != null) {
//	        unbindService(mServiceConn);
//	    }   
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id=item.getItemId();
		switch (id) {
		case R.id.action_help:
			Intent Helpintent= new Intent(MainPage.this, Introduction.class);
			prefEditor.putBoolean("isFirst", true);
			prefEditor.commit();
			startActivity(Helpintent);
			break;
		case R.id.action_about_us:
			Intent Aboutintent= new Intent(MainPage.this, About_us_Activity.class);
			startActivity(Aboutintent);
			break;
		case R.id.action_littl_facts:
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
	
