package com.listwithmore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	UseElements SiteElements= new UseElements();
	GridView gridtview;
	String LINK;
	static ArrayList<String> IDs= new ArrayList<String>();
	ArrayList<String> thumbs= new ArrayList<String>();
	AssignArray AA= new AssignArray();
	MyProgress progress = new MyProgress();
	Button btn_loadmore;
	ArrayList<String> PicNames = new ArrayList<String>();
	RelativeLayout layout;
	File file = new File(Environment.getExternalStorageDirectory().toString()+"/listwithmore/xml/hv.xml");
	int id,ItemPosition;
	String OnlineDir;
	int ExistedItems;
	View ItemView;
	SharedPreferences prefs= MyApplicationContext.getAppContext().getSharedPreferences(Wizard.PREFCOUNTS, Context.MODE_PRIVATE);
	UseElements UE= new UseElements();
	SomeTests ST= new SomeTests();
	Appearance app = new Appearance();
	Context context;
	ImageView iv_no_item;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		final NetWork network = new NetWork(MainActivity.this);
		layout= (RelativeLayout) findViewById(R.id.layout_MainRelativeLayout);
		gridtview = (GridView) findViewById(R.id.gv_itemsGridView);
		gridtview.setLongClickable(true);
		iv_no_item = (ImageView) findViewById(R.id.iv_no_item);
		iv_no_item.setVisibility(View.INVISIBLE);

		super.onCreate(savedInstanceState);
		id = getIntent().getExtras().getInt("id");
		switch (id) {
		case R.id.btn_animals:
			LINK= getResources().getString(R.string.animals);
			OnlineDir=getResources().getString(R.string.animalsOnlineDir);
			break;
		case R.id.btn_automobile:
			LINK= getResources().getString(R.string.automobile);
			OnlineDir=getResources().getString(R.string.automobileOnlineDir);
			break;
		case R.id.btn_cartoon:
			LINK= getResources().getString(R.string.cartoon);
			OnlineDir=getResources().getString(R.string.cartoonOnlineDir);
			break;
		case R.id.btn_dionasours:
			LINK= getResources().getString(R.string.dionasours);
			OnlineDir=getResources().getString(R.string.dionasoursOnlineDir);
			break;
		case R.id.btn_dragon:
			LINK= getResources().getString(R.string.dragon);
			OnlineDir=getResources().getString(R.string.dragonOnlineDir);
			break;
		case R.id.btn_fantasy:
			LINK= getResources().getString(R.string.fantasy);
			OnlineDir=getResources().getString(R.string.fantasyOnlineDir);
			break;
		case R.id.btn_flower:
			LINK= getResources().getString(R.string.flowers);
			OnlineDir=getResources().getString(R.string.flowersOnlineDir);
			break;
		case R.id.btn_people:
			LINK= getResources().getString(R.string.people);
			OnlineDir=getResources().getString(R.string.peopleOnlineDir);
			break;
		}
		file = new File(Environment.getExternalStorageDirectory().toString()+"/listwithmore/xml/"+FileOperations.getNameFromLink(LINK));

		
		if (file.exists()) {
			ExistedItems= prefs.getInt(LINK, 0);
			//set visiblity to show there is no item in a category
			if (ExistedItems==0) {
				gridtview.setVisibility(View.INVISIBLE);
				iv_no_item.setVisibility(View.VISIBLE);
			}
//------------------------------ start for main layout -----------------


		// extract data from xml file
			try {
				IDs= SiteElements.getElementsFromFile(file, "id");
				thumbs= SiteElements.getElementsFromFile(file, "thumb_url");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		

		
		PicNames= getPicNames();
		int picNums=0;
		for (String PicName : PicNames) {
			File newFile = new File(Environment.getExternalStorageDirectory().toString()+"/"+getResources().getString(R.string.PathInSD)+"/"+OnlineDir+"/"+PicName);
			if (newFile.exists()) {
				picNums++;
			}
			
		}
		

		if (picNums== PicNames.size()) {
			AA.PictureGrid(UseElements.ArrayListToStringArray(PicNames), getResources().getString(R.string.PathInSD)+"/"+OnlineDir,  gridtview, MainActivity.this);
			
		}
		else {	
			app.alertWithOneButton("اشکال در فایل های تصویری ...", "فایل های تصویر شما به خوبی دانلود نشده است. برای ترمیم هر تصویر بر روی تصویر آن که نیاز به ترمیم دارد کلیک نمایید،.", MainActivity.this, android.R.drawable.ic_menu_info_details, "OK");
			AA.PictureGrid(UseElements.ArrayListToStringArray(PicNames), getResources().getString(R.string.PathInSD)+"/"+OnlineDir,  gridtview, MainActivity.this);
		}
		gridtview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				String PicsDirPath= Environment.getExternalStorageDirectory().toString()+"/"+getResources().getString(R.string.PathInSD)+"/"+OnlineDir+"/"+position;
				String FirstPic= Environment.getExternalStorageDirectory().toString()+"/"+getResources().getString(R.string.PathInSD)+"/"+OnlineDir+"/"+PicNames.get(position);

				if (ST.NeedRepair(position, FirstPic, PicsDirPath)) {
					Intent In= new Intent(MainActivity.this, Description.class);
					In.putExtra("SelectedItemNum", position);
					In.putExtra("OnlineDir", OnlineDir);
					In.putExtra("ListPosition", position);
					In.putExtra("thumbs", UseElements.ArrayListToStringArray(PicNames));
					startActivity(In);
				}
				else{
				// do in order to repair not existed image
					if (network.isOnline()) {
						v.setLongClickable(true);
						registerForContextMenu(v);
						v.showContextMenu();
					}
					else{
						Toast.makeText(MyApplicationContext.getAppContext(),"لطفا اتصال خود به اینترنت را برقرار نموده و دوباره تلاش نمایید" , Toast.LENGTH_LONG).show();
					}
					
					ItemPosition=position;	
					ItemView=v;
				}
				
			}
			
		});
		

	}
			

		else {
			// if with no connection user go to our page
			gridtview.setVisibility(View.INVISIBLE);
			iv_no_item.setVisibility(View.VISIBLE);

		}
		
		
// ---------------------- main layout End -------------------

		
		
	}


	public ArrayList<String> getPicNames() {
		for (int i = 0; i < ExistedItems; i++) {
			String Name= FileOperations.getNameFromLink(thumbs.get(i));
			PicNames.add(i,Name);
		}
		return PicNames;
	}


	public class UseElementsTask extends AsyncTask {
		UseElements SiteElements = new UseElements();
		
		protected Void doInBackground(Object... arg0) {
			FileOperations fo = new FileOperations();
			fo.SaveFileFromLinkToSD(LINK, "listwithmore/xml");
			return null;
		}

	}
	

	
	public int getExistedItems (){
		return ExistedItems;
	}
	

	
	@Override
	 
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("منو ترمیم");
		menu.setHeaderIcon(android.R.drawable.ic_menu_recent_history);
		getMenuInflater().inflate(R.menu.repairmenu, menu);


	}
	
	

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ArrayList<String> repairItems= new ArrayList<String>();
		for (int i = 0; i < PicNames.size(); i++) {
			repairItems.add(getResources().getString(R.string.baseDir)+"pictures/"+OnlineDir+"/"+PicNames.get(i));
		}
		
		switch (item.getItemId()) {
		case R.id.RepairItem:
			progress.progressFromTO(ItemView, repairItems, getResources().getString(R.string.PathInSD), OnlineDir,ItemPosition,ItemPosition+1);
			break;
		case R.id.RepairAllItems:
			progress.progressFromTO(ItemView, repairItems, getResources().getString(R.string.PathInSD), OnlineDir,0,PicNames.size());
			break;


		default:
			break;
		}

		return super.onContextItemSelected(item);
	}
	
	
	
	
}


