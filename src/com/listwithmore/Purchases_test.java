package com.listwithmore;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.listwithmore.util.IabHelper;
import com.listwithmore.util.IabHelper.OnIabSetupFinishedListener;
import com.listwithmore.util.IabResult;
import com.listwithmore.util.Purchase;



public class Purchases_test extends Activity {

	Button btn_click,btn_purchase;
	private static final String TAG = "";
	IabHelper mHelper ;
	String ITEM_SKU;
	int packages_num;
	AssignArray AA= new AssignArray();
	ArrayList<String> skuList = new ArrayList<String>();
	ArrayList<String> titles = new ArrayList<String>();
	ArrayList<String> descriptions = new ArrayList<String>();
	ArrayList<String> prices = new ArrayList<String>();
	Context C= Purchases_test.this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// dont forget to initialize skulist,titles,descriptions and prices//--------------------
		skuList.add("One");
		skuList.add("Five");
		
		//----
		titles.add("بسته پنج تایی");
		titles.add("بسته ده تایی");
		//----
		descriptions.add("پس از دریافت این بسته، 5 عدد آموزش جدید برای دانلود برای شما باز خواهد شد");
		descriptions.add("پس از دریافت این بسته، 10 عدد آموزش جدید برای دانلود برای شما باز خواهد شد");
		//----
		prices.add("100 تومان");
		prices.add("180 تومان");		
		// end of initializing skulist arraylist // ------------------------------------
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purcahse_test);

		ListView lv_purchases= (ListView) findViewById(R.id.lv_purchases);

		TextView tv_available_package_name= (TextView) findViewById(R.id.tv_available_package_name);
		
		
		int SpinnerPosition = getIntent().getExtras().getInt("SpinnerPosition");
		int available_count = getIntent().getExtras().getInt("available_count");
		packages_num= ((int)Math.round(available_count/Wizard.FREE_NUMBERS));
		ArrayList<String> CatNames = getIntent().getExtras().getStringArrayList("CatNames");
		
		
		tv_available_package_name.setText("بسته های قابل دانلود برای "+CatNames.get(SpinnerPosition));
		
		// starting setup to use bazaar purchase 
		String base64EncodedPublicKey ="MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDc2bJCByVVNIcjvz42FhKohQyOrzD3wn44rdMSKubZbSiYXnrBaLnvWSIvvrjuK3ba1+3NZkfepgm79f8vLcpoDwqo+ZmnfLo+phxHonenHqD/2YlO0ba3H6jIvSexP6zuF9/y5tetbFYnv7E1zGVUfENqARdMzphQRxX9WWcLCBdkebJ4xJSwvRfeY6kYHlqa8xEdz7UznAefMxFHF1SwjBG/wlnBGPB1HmXjbl0CAwEAAQ==";
		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.startSetup(new OnIabSetupFinishedListener() {
			
			@Override
			public void onIabSetupFinished(IabResult result) {
				 if (!result.isSuccess()) {
      	           Log.d(TAG, "In-app Billing setup failed: " + 
					result);
      	      } else {             
      	      	    Log.d(TAG, "In-app Billing is set up OK");
		      }
				
			}
		});
		//end of setup
		
		UseElements UE= new UseElements();
		// enabling maybe we dont have any more item 
		if (packages_num > skuList.size()) {
			packages_num= skuList.size();
		}
		// end
		AA.PictureList(UE.ArrayListToStringArray(new ArrayList<String>(titles.subList(0, packages_num))), UE.ArrayListToStringArray(new ArrayList<String>(descriptions.subList(0, packages_num))), UE.ArrayListToStringArray(new ArrayList<String>(prices.subList(0, packages_num))), lv_purchases, C);
		lv_purchases.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View v, int position, long id) {
				ITEM_SKU=skuList.get(position);
				mHelper.launchPurchaseFlow(Purchases_test.this, ITEM_SKU, 10001, mPurchaseFinishedListener, "");
				
			}
		});
	}
	


	
	// handling the result of bazaar purchase flow
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase info) {
			if (result.isFailure()) {
				// Handle error
				Toast.makeText(MyApplicationContext.getAppContext(), "خرید شما با مشکل مواجه شده است، لطفا بعدا دوباره تلاش نمایید", Toast.LENGTH_LONG).show();
			    return;
			}
			if (info.getSku().equals(ITEM_SKU)) {
				
				
				String sku = info.getSku();
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
				
//				ArrayList<String> titles = new ArrayList<String>();
//				ArrayList<String> descriptions = new ArrayList<String>();
//				ArrayList<String> prices = new ArrayList<String>();
//				ArrayList<String> skus = new ArrayList<String>();
//				
//			   for (String thisResponse : titles) {
//			      JSONObject object;
//				try {
//					object = new JSONObject(thisResponse);
//					String sku = object.getString("productId");
//					String title = object.getString("title");
//			        String price = object.getString("price");
//			        String description= object.getString("description");
//			        skus.add(sku);
//			        titles.add(title);
//			        prices.add(price);
//			        descriptions.add(description);
//			        
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			      
//			     
//			   }
//			   UseElements UE= new UseElements();
//			   
//			    Collections.reverse(skus);
//				Collections.reverse(titles);
//				Collections.reverse(prices);
//				Collections.reverse(descriptions);
//				ArrayList<String> sss= new ArrayList<String>(titles.subList(0, packages_num));
//				if (packages_num > skus.size()) {
//					packages_num= skus.size();
//				}
////				AA.PictureList(UE.ArrayListToStringArray(new ArrayList<String>(titles.subList(0, packages_num))), UE.ArrayListToStringArray(new ArrayList<String>(descriptions.subList(0, packages_num))), UE.ArrayListToStringArray(new ArrayList<String>(prices.subList(0, packages_num))), lv_purchases, C);
////			    skuList= skus;
				
			}
			
		}

	};
	// end of handling
	
	// closing the service you have been bined
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
	}
	// end of close service
	
	
	
	
	
}
