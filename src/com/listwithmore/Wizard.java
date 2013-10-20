package com.listwithmore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Wizard extends Activity implements OnSeekBarChangeListener {
	MyProgress progress = new MyProgress();
	String LINK="http://api.androidhive.info/music/music.xml";
	File file = new File(Environment.getExternalStorageDirectory().toString()+"/listwithmore/xml/"+FileOperations.getNameFromLink(LINK));
	ArrayList<String> PicNames = new ArrayList<String>();
	ArrayList<String> IDs= new ArrayList<String>();
	ArrayList<String> thumbs= new ArrayList<String>();
	UseElements SiteElements= new UseElements();
	Button WizardBtn;
	AssignArray AA = new AssignArray();
	MainPage MP= new MainPage();
	ArrayList<String> LINKSARRAY= new ArrayList<String>();
	Appearance appearance= new Appearance();
	final static String PREFCOUNTS= "prefcounts";
	SharedPreferences prefs= MyApplicationContext.getAppContext().getSharedPreferences(PREFCOUNTS, 0);
	SharedPreferences.Editor prefEditor = prefs.edit();
	static int SpinnerPosition;
	FileOperations fo = new FileOperations();
	SeekBar sb;
	boolean firstTime= true;
	static TextView seekbarStatus;
	String OnlineDir;
	String Path;
	int existedItems;
	static int FREE_NUMBERS=5;
	View V;
	ArrayList<String> CatNames= new ArrayList<String>();
	
	
	

	int id;
	
	
	
	

	public void Wizard(){}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wizard);
		
		Path = getResources().getString(R.string.PathInSD);
		
		//making text list of the names example "نقاشی حیوانات"
		ArrayList< String> Names = new ArrayList<String>();
		Names.add(getResources().getString(R.string.name_animals));
		Names.add(getResources().getString(R.string.name_automobile));
		Names.add(getResources().getString(R.string.name_cartoon));
		Names.add(getResources().getString(R.string.name_dionasours));
		Names.add(getResources().getString(R.string.name_dragon));
		Names.add(getResources().getString(R.string.name_fantasy));
		Names.add(getResources().getString(R.string.name_flower));
		Names.add(getResources().getString(R.string.name_people));
		
		CatNames= Names;
		
		// making list of the pictures name in drawable folder example "car"
		ArrayList< String> PicNames = new ArrayList<String>();
		PicNames.add(getResources().getString(R.string.pic_animals));
		PicNames.add(getResources().getString(R.string.pic_automobile));
		PicNames.add(getResources().getString(R.string.pic_cartoon));
		PicNames.add(getResources().getString(R.string.pic_dionasours));
		PicNames.add(getResources().getString(R.string.pic_dragon));
		PicNames.add(getResources().getString(R.string.pic_fantasy));
		PicNames.add(getResources().getString(R.string.pic_flower));
		PicNames.add(getResources().getString(R.string.pic_people));
		
		// making a list of the Directories
		final ArrayList< String> Dirs = new ArrayList<String>();
		Dirs.add(getResources().getString(R.string.animalsOnlineDir));
		Dirs.add(getResources().getString(R.string.automobileOnlineDir));
		Dirs.add(getResources().getString(R.string.cartoonOnlineDir));
		Dirs.add(getResources().getString(R.string.dionasoursOnlineDir));
		Dirs.add(getResources().getString(R.string.dragonOnlineDir));
		Dirs.add(getResources().getString(R.string.fantasyOnlineDir));
		Dirs.add(getResources().getString(R.string.flowersOnlineDir));
		Dirs.add(getResources().getString(R.string.peopleOnlineDir));
		
		Spinner sp_witch_cat= (Spinner) findViewById(R.id.sp_WitchCatagory);
		
		String[] textArray = UseElements.ArrayListToStringArray(Names);
		String[] PictureArray = UseElements.ArrayListToStringArray(PicNames);
		
		AA.PictureSpinner(PictureArray, textArray, sp_witch_cat, MyApplicationContext.getAppContext());
		
		
		sp_witch_cat.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View v,
					int position, long id) {
				OnlineDir=Dirs.get(position);
				SpinnerPosition= position;
				LINKSARRAY= MP.getLINKS();
				LINK= LINKSARRAY.get(position);
				file = new File(Environment.getExternalStorageDirectory().toString()+"/listwithmore/xml/"+FileOperations.getNameFromLink(LINKSARRAY.get(position)));

				if (position==0 && firstTime) {
					firstTime=false;
				}
				else{
					new getDataTask().execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		sb= (SeekBar) findViewById(R.id.sb_howMany);
		seekbarStatus = (TextView) findViewById(R.id.tv_seekbarChanger);
		seekbarStatus.setText("4");
		sb.setProgress(5);
		sb.setOnSeekBarChangeListener(this);
		
		
		final NetWork network = new NetWork(Wizard.this);
		WizardBtn = (Button) findViewById(R.id.btn_Favorites);
		LINKSARRAY= MP.getLINKS();
		file = new File(Environment.getExternalStorageDirectory().toString()+"/listwithmore/xml/"+FileOperations.getNameFromLink(LINKSARRAY.get(SpinnerPosition)));
		
		
		if (network.isOnline()) {
			
			
			if (file.exists()) {
				try {
					thumbs = SiteElements.getElementsFromFile(file, "thumb_url");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LINKSARRAY= MP.getLINKS();
				sb.setMax(getAvailableCount(SpinnerPosition));
			}
			else {
				WizardBtn.setEnabled(false);
//				new UseElementsTask().execute();
			}			
		}
		else {
			if (file.exists()) {
				try {
					thumbs = SiteElements.getElementsFromFile(file, "thumb_url");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LINKSARRAY= MP.getLINKS();
				sb.setMax(getAvailableCount(SpinnerPosition));
			}
			WizardBtn.setEnabled(false);
		}
		
		WizardBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				V=v;
				if (thumbs.size()-getAvailableCount(SpinnerPosition)+Integer.parseInt(seekbarStatus.getText().toString())>FREE_NUMBERS) {
				int hhhh= thumbs.size()-getAvailableCount(SpinnerPosition);
					if (thumbs.size()-getAvailableCount(SpinnerPosition)==0) {
						seekbarStatus.setText(Integer.toString(FREE_NUMBERS));
						int hv= progress.progress(v, thumbs, Path ,OnlineDir);		
						sb.setMax(thumbs.size()-hv);
						prefEditor.putInt(LINKSARRAY.get(Wizard.getSpinnerPosition()),hv);
						prefEditor.commit();
					}
					else{
						Intent purchaseIntent = new Intent(Wizard.this, Purchases_test.class);
						purchaseIntent.putExtra("SpinnerPosition", SpinnerPosition);
						purchaseIntent.putExtra("CatNames", CatNames);
//						purchaseIntent.putExtra("response", getIntent().getExtras().getInt("response"));
						purchaseIntent.putExtra("available_count", getAvailableCount(SpinnerPosition));
						
//						if (getIntent().getExtras().getInt("response")==0) {
//							purchaseIntent.putExtra("skus", getIntent().getExtras().getStringArrayList("skus"));
//							purchaseIntent.putExtra("titles", getIntent().getExtras().getStringArrayList("titles"));
//							purchaseIntent.putExtra("prices", getIntent().getExtras().getStringArrayList("prices"));
//							purchaseIntent.putExtra("descriptions", getIntent().getExtras().getStringArrayList("descriptions"));
//						}
						
//						startActivity(purchaseIntent);
						startActivityForResult(purchaseIntent, 1369);
						
					}
				}
				else{
					int hv= progress.progress(v, thumbs, Path ,OnlineDir);		
					sb.setMax(thumbs.size()-hv);
					prefEditor.putInt(LINKSARRAY.get(Wizard.getSpinnerPosition()),hv);
					prefEditor.commit();
				}
				
			}
			
		});
	}
	


	
	public class getDataTask extends AsyncTask{
		int existedItems;
		ProgressDialog pd= new ProgressDialog(Wizard.this);
		@Override
		protected void onPreExecute() {
			
			pd.setCancelable(true);
			pd.setMessage("در حال بارگذاری");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
//			android.os.Debug.waitForDebugger();
			
//			SharedPreferences prefs= MyApplicationContext.getAppContext().getSharedPreferences(PREFCOUNTS, 0);
			
			if (file.exists()) {
				try {
					thumbs = SiteElements.getElementsFromFile(file, "thumb_url");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				existedItems=prefs.getInt(LINK, 0);
			}
			return null;
		}
		@Override
		protected void onPostExecute(Object result) {
//			android.os.Debug.waitForDebugger();
			pd.cancel();
			sb.setMax(thumbs.size()-existedItems);
			super.onPostExecute(result);
		}
		
	}
	// seek bar implemented methods !
	
	@Override
	public void onProgressChanged(SeekBar paramSeekBar, int progress,
			boolean paramBoolean) {
		int stepSize = 5;
		TextView tv = (TextView)findViewById(R.id.tv_seekbarChanger);
		progress = ((int)Math.round(progress/stepSize))*stepSize;
		tv.setText(Integer.toString(progress));
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar paramSeekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar paramSeekBar) {
		float size = Float.parseFloat(seekbarStatus.getText().toString())*100;
		String shownSize;
		if (size < 1000) {
			shownSize= (int) size +" Kb";
		}
		else{
			shownSize= (size/1000)+" Mb";
		}
		Toast.makeText(MyApplicationContext.getAppContext(), shownSize, Toast.LENGTH_SHORT).show();
		
	}
	// end of seek bar
	
	public int getAvailableCount(int number) {
		existedItems=prefs.getInt(LINKSARRAY.get(number), 0);
		
		return thumbs.size()-existedItems;
		
	}
	public static int getSpinnerPosition (){
		return SpinnerPosition;
		
	}
	public static int getPicRequestNumbers(){

		return Integer.parseInt(seekbarStatus.getText().toString());
	}
	
	public void SetSpinner(){
		sb.setMax(getAvailableCount(SpinnerPosition));
		Log.d("asdasd", "sadasdas");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode== RESULT_OK && requestCode== 1369) {
			Toast.makeText(MyApplicationContext.getAppContext(), "sadasd", Toast.LENGTH_SHORT).show();
			
			seekbarStatus.setText(Integer.toString(data.getExtras().getInt("number_to_download")));
			int hv= progress.progress(V, thumbs, Path ,OnlineDir);		
			sb.setMax(thumbs.size()-hv);
			prefEditor.putInt(LINKSARRAY.get(Wizard.getSpinnerPosition()),hv);
			prefEditor.commit();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
	@Override
	public void onBackPressed() {
		SpinnerPosition=0;
		super.onBackPressed();
	}

	

	@Override
	protected void onResume() {
		final NetWork network = new NetWork(Wizard.this);
file = new File(Environment.getExternalStorageDirectory().toString()+"/listwithmore/xml/"+FileOperations.getNameFromLink(LINKSARRAY.get(SpinnerPosition)));
		
		
		if (network.isOnline()) {
			
			
			if (file.exists()) {
				try {
					thumbs = SiteElements.getElementsFromFile(file, "thumb_url");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LINKSARRAY= MP.getLINKS();
				sb.setMax(getAvailableCount(SpinnerPosition));
			}
			else {
				WizardBtn.setEnabled(false);
//				new UseElementsTask().execute();
			}			
		}
		else {
			if (file.exists()) {
				try {
					thumbs = SiteElements.getElementsFromFile(file, "thumb_url");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LINKSARRAY= MP.getLINKS();
				sb.setMax(getAvailableCount(SpinnerPosition));
			}
			WizardBtn.setEnabled(false);
		}
		super.onPause();
	}
	
	
}
