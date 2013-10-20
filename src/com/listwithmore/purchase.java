package com.listwithmore;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

public class purchase extends Activity {
	
	IInAppBillingService mService;
	ArrayList skuList = new ArrayList();
	Bundle querySkus = new Bundle();
	Bundle skuDetails = new Bundle();
	AssignArray AA= new AssignArray();
	Context C= purchase.this;
	ListView lv_purchases;
	int packages_num;
	Context mContext= purchase.this; 
	
	ServiceConnection mServiceConn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mService = null;
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IInAppBillingService.Stub.asInterface(service);
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase);
//		new getPurchableItems().execute();
		Intent bazaarList= new Intent("ir.cafebazaar.pardakht.InAppBillingService.BIND");
		mContext.bindService(bazaarList, mServiceConn, Context.BIND_AUTO_CREATE);
//		
		
		lv_purchases= (ListView) findViewById(R.id.lv_purchases);
		UseElements UE= new UseElements();
		
		TextView tv_available_package_name= (TextView) findViewById(R.id.tv_available_package_name);
		
		
//		int response = getIntent().getExtras().getInt("response");
		int SpinnerPosition = getIntent().getExtras().getInt("SpinnerPosition");
		int available_count = getIntent().getExtras().getInt("available_count");
		
		
		packages_num= ((int)Math.round(available_count/Wizard.FREE_NUMBERS));
		ArrayList<String> Purchabale_items= new ArrayList<String>();
		
		
		ArrayList<String> CatNames = getIntent().getExtras().getStringArrayList("CatNames");
		
		tv_available_package_name.setText("بسته های قابل دانلود برای "+CatNames.get(SpinnerPosition));
		
		
		
		
		
		
//		if (response==0) {
//			ArrayList<String> skus = getIntent().getExtras().getStringArrayList("skus");
//			ArrayList<String> titles = getIntent().getExtras().getStringArrayList("titles");
//			ArrayList<String> prices = getIntent().getExtras().getStringArrayList("prices");
//			ArrayList<String> descriptions = getIntent().getExtras().getStringArrayList("descriptions");
//			Collections.reverse(skus);
//			Collections.reverse(titles);
//			Collections.reverse(prices);
//			Collections.reverse(descriptions);
//			ArrayList<String> sss= new ArrayList<String>(titles.subList(0, packages_num));
//			if (packages_num > skus.size()) {
//				packages_num= skus.size();
//			}
//			AA.PictureList(UE.ArrayListToStringArray(new ArrayList<String>(titles.subList(0, packages_num))), UE.ArrayListToStringArray(new ArrayList<String>(descriptions.subList(0, packages_num))), UE.ArrayListToStringArray(new ArrayList<String>(prices.subList(0, packages_num))), lv_purchases, C);
//		    skuList= skus;
//		}
//		else{
//			Appearance APP = new Appearance();
//			APP.alertWithOneButton("asdasd", "sadasdasd", MyApplicationContext.getAppContext(), R.drawable.cars, "ok");
//		}
		
		new getPurchableItems().execute();
		  
		lv_purchases.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int position, long paramLong) {
				String sku = (String) skuList.get(position);
				try {
					Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
							   sku, "inapp", "");
					PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
					startIntentSenderForResult(pendingIntent.getIntentSender(),
							   1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
							   Integer.valueOf(0));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SendIntentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		
	}
	
	
	
	
	public class getPurchableItems extends AsyncTask{
		
//		ProgressDialog pds=new ProgressDialog(C);
		
		@Override
		protected void onPreExecute() {
			
//			pds.setCancelable(true);
//			pds.setMessage("Loading ...");
//			pds.show();
			
			skuList.add("One");
			skuList.add("Five");
//			skuList.add("zzzz");
//			skuList.add("zzzz");
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			
			super.onPreExecute();
		}
		
		@Override
		protected Object doInBackground(Object... paramArrayOfParams) {
			android.os.Debug.waitForDebugger();
			Intent bazaarList= new Intent("ir.cafebazaar.pardakht.InAppBillingService.BIND");
//			mContext.bindService(bazaarList, mServiceConn, Context.BIND_AUTO_CREATE);
//			mContext.startService(bazaarList);
			
			 if (mServiceConn != null) {
			        try {
						skuDetails = mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }   
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			int response = skuDetails.getInt("RESPONSE_CODE");
			if (response == 0) {
				ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
				ArrayList<String> titles = new ArrayList<String>();
				ArrayList<String> descriptions = new ArrayList<String>();
				ArrayList<String> prices = new ArrayList<String>();
				ArrayList<String> skus = new ArrayList<String>();
				
			   for (String thisResponse : responseList) {
			      JSONObject object;
				try {
					object = new JSONObject(thisResponse);
					String sku = object.getString("productId");
					String title = object.getString("title");
			        String price = object.getString("price");
			        String description= object.getString("description");
			        skus.add(sku);
			        titles.add(title);
			        prices.add(price);
			        descriptions.add(description);
			        
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      
			     
			   }
			   UseElements UE= new UseElements();
			   
//			   AA.PictureList(UE.ArrayListToStringArray(titles), UE.ArrayListToStringArray(descriptions), UE.ArrayListToStringArray(prices), lv_purchases, C);
			   Collections.reverse(skus);
				Collections.reverse(titles);
				Collections.reverse(prices);
				Collections.reverse(descriptions);
				ArrayList<String> sss= new ArrayList<String>(titles.subList(0, packages_num));
				if (packages_num > skus.size()) {
					packages_num= skus.size();
				}
				AA.PictureList(UE.ArrayListToStringArray(new ArrayList<String>(titles.subList(0, packages_num))), UE.ArrayListToStringArray(new ArrayList<String>(descriptions.subList(0, packages_num))), UE.ArrayListToStringArray(new ArrayList<String>(prices.subList(0, packages_num))), lv_purchases, C);
			    skuList= skus;
			}
			else{
				Appearance APP = new Appearance();
				APP.alertWithOneButton("asdasd", "sadasdasd", MyApplicationContext.getAppContext(), R.drawable.cars, "ok");
			}		
//			pds.cancel();
			super.onPostExecute(result);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	   if (requestCode == 1001) {           
	      int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
	      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	      String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
	        
	      if (resultCode == RESULT_OK) {
	         try {
	            JSONObject jo = new JSONObject(purchaseData);
	            String sku = jo.getString("productId");
	            ArrayList<String> SKULIST= skuList;
	            int k=1;
	            int number_to_download=0;
	            for (String SKU : SKULIST) {
					if (sku.equals(SKU)) {
						number_to_download= k*Wizard.FREE_NUMBERS;
					}
					k++;
				}
	            
	            Intent DownloadIntent = new Intent();
	            DownloadIntent.putExtra("number_to_download", number_to_download);
	            setResult(RESULT_OK, DownloadIntent);
	            finish();

	          }
	          catch (JSONException e) {
	             e.printStackTrace();
	          }
	      }
	      else{
	    	  Appearance APP = new Appearance();
//	    	  APP.alertWithOneButton("اشکال در خرید", "خرید شما با مشکل مواجه شده است، لطفا دوباره تلاش نمایید", MyApplicationContext.getAppContext(), android.R.drawable.ic_dialog_alert, "ok");
	    	  Toast.makeText(MyApplicationContext.getAppContext(), "خرید شما با مشکل مواجه شده است، لطفا بعدا دوباره تلاش نمایید", Toast.LENGTH_LONG).show();
	     
	    	 
	    	  
	      }
	   }
	}
	
	
	public void onDestroy() {
	   
	    if (mServiceConn != null) {
	    	if (mContext != null) mContext.unbindService(mServiceConn);
            mServiceConn = null;
            mService = null;
            
//	        Intent bazaarList= new Intent("ir.cafebazaar.pardakht.InAppBillingService.BIND");
//	        stopService(bazaarList);
//	        mContext.stopService(bazaarList);
//	        mContext.unbindService(mServiceConn);
//	        mServiceConn=null;
//	        mService= null;
	    }  
	    super.onDestroy();
	}
}
