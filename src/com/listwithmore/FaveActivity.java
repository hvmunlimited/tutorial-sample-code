package com.listwithmore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class FaveActivity extends Activity {
	GridView gridview;
	File f = new File("asdas");
	String PathInSD;
	UseElements UE= new UseElements();
	ArrayList<String> thumbs = new ArrayList<String>();
	ArrayList<String> XMLS = new ArrayList<String>();
	ArrayList<String> names = new ArrayList<String>();
	AssignArray AA= new AssignArray();
	ArrayList<String> OnlineDirs=new ArrayList<String>();
	ArrayList<String> Pathes=new ArrayList<String>();
	ArrayList<String> Positions=new ArrayList<String>();
	int LISTPOSITION;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.faveactivity);
		super.onCreate(savedInstanceState);
		final NetWork network = new NetWork(FaveActivity.this);
		gridview = (GridView) findViewById(R.id.gv_FaveitemsGridView);
		ImageView iv_no_fave = (ImageView) findViewById(R.id.iv_no_fave);

		
		PathInSD= getResources().getString(R.string.PathInSD);
		
		
		f= new File(Environment.getExternalStorageDirectory().toString()+"/"+PathInSD+"/favorite.xml");
		if (hasFave()) {
			iv_no_fave.setVisibility(View.INVISIBLE);
			try {
				thumbs = UE.getElementsFromFile(f, "thumb_url");
				XMLS = UE.getElementsFromFile(f, "xmlfile");
				for (int i = 0; i < thumbs.size(); i++) {
					names.add(thumbs.get(i).split("/")[thumbs.get(i).split("/").length-1]);
					OnlineDirs.add(thumbs.get(i).split("/")[thumbs.get(i).split("/").length-2]);
					Pathes.add(getResources().getString(R.string.PathInSD)+"/"+OnlineDirs.get(i));
					Positions.add(XMLS.get(i).split("/")[XMLS.get(i).split("/").length-2]);
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			AA.PictureGrid(UseElements.ArrayListToStringArray(names), UseElements.ArrayListToStringArray(Pathes),UseElements.ArrayListToStringArray(Positions) ,gridview, MyApplicationContext.getAppContext());
			gridview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> paramAdapterView,
						View paramView, int Listposition, long paramLong) {
					LISTPOSITION=Listposition;
					Intent intent = new Intent(FaveActivity.this, Description.class);
					intent.putExtra("SelectedItemNum", Integer.parseInt(Positions.get(Listposition)));
					intent.putExtra("OnlineDir", OnlineDirs.get(Listposition));
					intent.putExtra("ListPosition", Listposition);
					intent.putExtra("thumbs", UseElements.ArrayListToStringArray(names));
					startActivityForResult(intent, 0);
					
				}
			});
			
			
		}
		else {
			iv_no_fave.setVisibility(View.VISIBLE);
		}
				
			
	}
	
	public boolean hasFave() {
		try {
			if (new Scanner(f).hasNext()) {
				return true;
			}
			else{
				return false;
			}
		} catch (FileNotFoundException e) {
			return false;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//make refresh if some of items have been removed from a favorite list or some of items have been repaired!
		if (resultCode== RESULT_OK & data.getExtras().getInt("FaveState")==1) {
			names.remove(LISTPOSITION);
			Pathes.remove(LISTPOSITION);
			Positions.remove(LISTPOSITION);
		}

			AA.PictureGrid(UseElements.ArrayListToStringArray(names), UseElements.ArrayListToStringArray(Pathes),UseElements.ArrayListToStringArray(Positions) ,gridview, MyApplicationContext.getAppContext());

		super.onActivityResult(requestCode, resultCode, data);
	}

}


