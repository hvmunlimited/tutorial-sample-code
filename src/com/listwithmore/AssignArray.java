package com.listwithmore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class AssignArray  {
	
	String[] TITLES, DESCRIPTIONS,PRICES;
	String[] PicArray, textArray;
	String ResPath;
	String[] ResPathArray;
	Context c;
	UseElements UE = new UseElements();
	SomeTests ST= new SomeTests();
	String[] PoSITION;
	

	public void SimpleGrid(Context context, String[] array,GridView gv_view) {
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, array);
		gv_view.setAdapter(adapter);
		
	}
	
	public void SimpleList(Context context, String[] array,ListView lv_view) {
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, array);
		lv_view.setAdapter(adapter);
		
	}
	
	public void SimpleSpinner(Context context, String[] array,Spinner sp_view) {
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, array);
		sp_view.setAdapter(adapter);
	}
	// ------------------------------ Picture Grid -------------------------------
	public void PictureGrid(String[] PictureArray,String path,GridView gv_view , Context context) {

		ArrayAdapter<String> adapter= new MyAdapter(context, android.R.layout.simple_list_item_1, 0, PictureArray);
		gv_view.setAdapter(adapter);
		PicArray= PictureArray;
		ResPath= path;
		c= context;
	}
	
	class MyAdapter extends ArrayAdapter {

		@SuppressWarnings("unchecked")
		public MyAdapter(Context context, int resource, int textViewResourceId,
				Object[] objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.gridlayout, parent, false);
			//get resources
			ImageView iv = (ImageView) row.findViewById(R.id.iv_gridlayout);
//			TextView tv = (TextView) row.findViewById(R.id.text_gridlayout);
			//set image
			String[] resImages = PicArray;
			String TeaserPicPath = Environment.getExternalStorageDirectory().toString()+"/"+ResPath+"/"+ resImages[position];
			String PicDirPath = (Environment.getExternalStorageDirectory().toString()+"/"+ResPath+"/"+ position).replaceAll("\\s+","");
			boolean needRepair= ST.NeedRepair(position,TeaserPicPath,PicDirPath);
			
			new BitmapFactory();
			Bitmap bitFac= BitmapFactory.decodeFile(TeaserPicPath);
			if (needRepair) {
				iv.setImageBitmap(bitFac);
				iv.setTag(0);
			}
			else{
				iv.setTag(R.drawable.default_pic);
			}
			
		
			//set text
//			tv.setText(textArray[position]);
			return row;
		}
	};
	// ------------------------------ Picture Grid End ---------------------------------
	
	// ------------------------------ Picture List -------------------------------------
	public void PictureList(String[] PictureArray,String path, String[] TextArray,ListView gv_view, Context context) {
		
		ArrayAdapter<String> adapter= new MyListAdapter(context, android.R.layout.simple_list_item_1, 0, TextArray);
		gv_view.setAdapter(adapter);
		PicArray= PictureArray;
		ResPath= path;
		textArray= TextArray;
		c= context;
	}
	
	class MyListAdapter extends ArrayAdapter {

		@SuppressWarnings("unchecked")
		public MyListAdapter(Context context, int resource, int textViewResourceId,
				Object[] objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
			View row = inflater.inflate(R.layout.listlayout, parent, false);
			//get resources
			ImageView iv = (ImageView) row.findViewById(R.id.iv_listlayout);
			TextView tv = (TextView) row.findViewById(R.id.text_listlayout);
			//set image

			String[] resImages = PicArray;
			new BitmapFactory();
			String PATH = Environment.getExternalStorageDirectory().toString()+"/"+ResPath+"/"+ resImages[position];
			Bitmap bitFac= BitmapFactory.decodeFile(PATH);
			if (bitFac != null) {
				iv.setImageBitmap(bitFac);
				iv.setTag(0);
			}
			else{
				iv.setTag(R.drawable.default_pic);
			}
			
			//set text
			tv.setText(textArray[position]);
			return row;
		}
	};
	
	// ------------------------------ Picture List End ---------------------------------
	
	
	// ------------------------------ Picture Spinner -------------------------------------
		public void PictureSpinner(String[] PictureArray, String[] TextArray,Spinner sp_view, Context context) {
			ArrayAdapter<String> adapter= new MySpinnerAdapter(context, android.R.layout.simple_expandable_list_item_1, 0, TextArray);
			sp_view.setAdapter(adapter);
			PicArray= PictureArray;
			textArray= TextArray;
			c= context;
		}
		
		class MySpinnerAdapter extends ArrayAdapter {

			@SuppressWarnings("unchecked")
			public MySpinnerAdapter(Context context, int resource, int textViewResourceId,
					Object[] objects) {
				super(context, resource, textViewResourceId, objects);
				// TODO Auto-generated constructor stub
			}
			
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
				View row = inflater.inflate(R.layout.spinnerlayout, parent, false);
				//get resources
				ImageView iv = (ImageView) row.findViewById(R.id.iv_spinnerlayout);
				TextView tv = (TextView) row.findViewById(R.id.text_spinnerlayout);
				//set image
				iv.setImageResource(c.getResources().getIdentifier(PicArray[position], "drawable", "com.listwithmore"));
				//set text
				tv.setText(textArray[position]);
				return row;
			}
			
			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
				View row = inflater.inflate(R.layout.spinnerlayout, parent, false);
				//get resources
				ImageView iv = (ImageView) row.findViewById(R.id.iv_spinnerlayout);
				TextView tv = (TextView) row.findViewById(R.id.text_spinnerlayout);
				//set image
				iv.setImageResource(c.getResources().getIdentifier(PicArray[position], "drawable", "com.listwithmore"));
				//set text
				tv.setText(textArray[position]);
				return row;
			}
		};
		
		// ------------------------------ Picture Spinner End ---------------------------------
		
		
		public void PictureGrid(String[] PictureArray,String[] path,String[] POSITION,GridView gv_view , Context context) {

			ArrayAdapter<String> adapter= new MyGridAdapter(context, android.R.layout.simple_list_item_1, 0, PictureArray);
			gv_view.setAdapter(adapter);
			PicArray= PictureArray;
			ResPathArray= path;
			c= context;
			PoSITION= POSITION;
		}
		
		class MyGridAdapter extends ArrayAdapter {

			@SuppressWarnings("unchecked")
			public MyGridAdapter(Context context, int resource, int textViewResourceId,
					Object[] objects) {
				super(context, resource, textViewResourceId, objects);
				// TODO Auto-generated constructor stub
			}
			
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
				View row = inflater.inflate(R.layout.gridlayout, parent, false);
				//get resources
				ImageView iv = (ImageView) row.findViewById(R.id.iv_gridlayout);
//				TextView tv = (TextView) row.findViewById(R.id.text_gridlayout);
				//set image
				String[] resImages = PicArray;
				String TeaserPicPath = Environment.getExternalStorageDirectory().toString()+"/"+ResPathArray[position]+"/"+ resImages[position];
				String PicDirPath = (Environment.getExternalStorageDirectory().toString()+"/"+ResPathArray[position]+"/"+ PoSITION[position]).replaceAll("\\s+","");
				boolean needRepair= ST.NeedRepair(position,TeaserPicPath,PicDirPath);
				
				new BitmapFactory();
				Bitmap bitFac= BitmapFactory.decodeFile(TeaserPicPath);
				if (needRepair) {
					iv.setImageBitmap(bitFac);
					iv.setTag(0);
				}
				else{
					iv.setTag(R.drawable.default_pic);
				}
				
			
				//set text
//				tv.setText(textArray[position]);
				return row;
			}
		};
		// ------------------------------ Picture Grid End ---------------------------------

		// ------------------------------ Picture List for purchases -------------------------------------
		public void PictureList(String[] Titles,String[] Descriptions, String[] Prices,ListView lv_view, Context context) {
			
			ArrayAdapter<String> adapter= new MyListAdapterForPurchase(context, android.R.layout.simple_list_item_1, 0, Titles);
			lv_view.setAdapter(adapter);
			TITLES= Titles;
			DESCRIPTIONS= Descriptions;
			PRICES= Prices;
			c= context;
		}
		
		class MyListAdapterForPurchase extends ArrayAdapter {

			@SuppressWarnings("unchecked")
			public MyListAdapterForPurchase(Context context, int resource, int textViewResourceId,
					Object[] objects) {
				super(context, resource, textViewResourceId, objects);
				// TODO Auto-generated constructor stub
			}
			
			public View getView(int position, View convertView, ViewGroup parent) {
				LayoutInflater inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
				View row = inflater.inflate(R.layout.purchaselayout, parent, false);
				//get resources

				TextView Description = (TextView) row.findViewById(R.id.tv_description);
				TextView Title = (TextView) row.findViewById(R.id.tv_title);
				TextView Price = (TextView) row.findViewById(R.id.tv_price);

				//set text
				Description.setText(DESCRIPTIONS[position]);
				Title.setText(TITLES[position]);
				Price.setText(PRICES[position]);
				return row;
			}
		};
		
		// ------------------------------ Picture List End ---------------------------------
		

}



