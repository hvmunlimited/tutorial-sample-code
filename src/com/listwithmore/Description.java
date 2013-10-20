package com.listwithmore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Description extends Activity {
	
	UseElements UE= new UseElements();
	String PathInSD;
	ArrayList<String> Pictures = new ArrayList<String>();
	ImageView iv = new ImageView(MyApplicationContext.getAppContext());
	//its something fake , we will assign new adress in onCreat method
	File DescriptionFile= new File(Environment.getDataDirectory()+"/picdetails.xml");
	File f= new File(Environment.getExternalStorageDirectory().toString()+"/"+"aaa");
	
	String OnlineDir,RealPath;
	ImageButton next;
	ImageButton previous;
	int SelectedItemNum,ListPosition;
	
	int num=0 ,prevnum;
	ArrayList<Bitmap> DescPictures= new ArrayList<Bitmap>();
	MainActivity MA= new MainActivity();

	String thumbPic , DescriptionXML;
	
	int FaveState;

	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {

		
		 
		setContentView(R.layout.description);
		super.onCreate(savedInstanceState);
		PathInSD= getResources().getString(R.string.PathInSD);
		
		next= (ImageButton) findViewById(R.id.ib_next);
		previous = (ImageButton) findViewById(R.id.ib_previous);
		previous.setVisibility(View.INVISIBLE);
		previous.setEnabled(false);
		
		
		SelectedItemNum =getIntent().getExtras().getInt("SelectedItemNum");
		ListPosition =getIntent().getExtras().getInt("ListPosition");
		
		OnlineDir =getIntent().getExtras().getString("OnlineDir");
		RealPath= Environment.getExternalStorageDirectory().toString()+"/"+PathInSD+"/"+OnlineDir+"/"+SelectedItemNum;
		File DescriptionFile= new File(RealPath+"/picdetails.xml");
		
		thumbPic=Environment.getExternalStorageDirectory().toString()+"/"+PathInSD+"/"+OnlineDir+"/"+getIntent().getExtras().getStringArray("thumbs")[ListPosition];
		DescriptionXML = RealPath+"/picdetails.xml";
		
		try {
			Pictures= UE.getElementsFromFile(DescriptionFile,"pic");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final LinearLayout Layout= (LinearLayout) findViewById(R.id.Layout_Description);
		
		
		
		Bitmap bitfac; 
		for (String PictureName : Pictures) {
			new BitmapFactory();
			String hv= RealPath+"/"+PictureName.replaceAll("\\s+","");
			bitfac = BitmapFactory.decodeFile(hv);
			DescPictures.add(bitfac);
		}
		iv.setImageBitmap(DescPictures.get(0));
		Layout.addView(iv);
		num=1;
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (num==1) {
					previous.setVisibility(View.VISIBLE);
					previous.setEnabled(true);
				}


					iv.setImageBitmap(DescPictures.get(num));
					if (num==DescPictures.size()-1) {
						next.setVisibility(View.INVISIBLE);
						next.setEnabled(false);
					}
					prevnum=num;
				num++;
				
			}
		});
		
		previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (prevnum==1) {
					previous.setVisibility(View.INVISIBLE);
					previous.setEnabled(false);
				}
				if (prevnum == num-1 & prevnum== DescPictures.size()-1) {
					next.setVisibility(View.VISIBLE);
					next.setEnabled(true);
				}

					iv.setImageBitmap(DescPictures.get(prevnum-1));
					if (num==DescPictures.size()-1 & prevnum== num) {
						next.setVisibility(View.INVISIBLE);
						next.setEnabled(false);
					}

				prevnum--;
				num--;
			}
		});
		
		
		
	}
	 
	 
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		f= new File(Environment.getExternalStorageDirectory().toString()+"/"+PathInSD+"/favorite.xml");
		getMenuInflater().inflate(R.menu.addtofavorit, menu);
	
		if (FaveExists()) {
			menu.getItem(0).setEnabled(false);
		}
		else{
			menu.getItem(1).setEnabled(false);
		}
		return true;
	}
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		int id = item.getItemId();
		switch (id) {
		case R.id.AddtoFav:
			 try {
					OutputStream OS= new FileOutputStream(f,true);
					byte[] buffer = ("<thumb_url>"+thumbPic+"</thumb_url>\n<xmlfile>"+DescriptionXML+"</xmlfile>\n ").getBytes();
					OS.write(buffer);
					OS.flush();
					OS.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			 item.setEnabled(false);
			 FaveState=0;
			break;
		case R.id.RemoveFromFav:
			try {
				byte[] buffer= FaveRemoved().getBytes();
				OutputStream OS= new FileOutputStream(f,false);
				OS.write(buffer);
				OS.flush();
				OS.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 item.setEnabled(false);
			 FaveState=1;
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	 public boolean FaveExists() {
		 f= new File(Environment.getExternalStorageDirectory().toString()+"/"+PathInSD+"/favorite.xml");
		// according to https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner.html
			try {
				if (new Scanner(f).hasNext()) {
					String FaveFile = new Scanner(f).useDelimiter("\\A").next();
					String[] Exist= FaveFile.split("<thumb_url>"+thumbPic+"</thumb_url>\n<xmlfile>"+DescriptionXML+"</xmlfile>\n");
					String[] Check= FaveFile.split(">"+thumbPic);
					if (Check.length>1) {
						return true;
					}
					else{
						return false;
					}
				}
				else{
					return false;
				}
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				return false;
			}
	}
 	public String FaveRemoved() {
 		String NewText = "";
			// according to https://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner.html
				try {
					if (new Scanner(f).hasNext()) {
						String FaveFile = new Scanner(f).useDelimiter("\\A").next();
						String[] Exist= FaveFile.split("<thumb_url>"+thumbPic+"</thumb_url>\n<xmlfile>"+DescriptionXML+"</xmlfile>\n");
						
						for (int i = 0; i < Exist.length; i++) {
							NewText+=Exist[i];
						}
					}
					
				} catch (FileNotFoundException e1) {
					
				}
			return NewText;
		}
 	@Override
 	public void onBackPressed() {
 		finish();
 		super.onBackPressed();
 	}
 	@Override
 	public void finish() {
 	  // Prepare data intent 
 	  Intent data = new Intent();
 	  data.putExtra("FaveState", FaveState);
 	  // Activity finished ok, return the data
 	  setResult(RESULT_OK, data);
 	  super.finish();
 	} 
}
