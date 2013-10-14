package com.listwithmore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.preference.Preference;
import android.view.View;
import android.widget.Toast;

public class MyProgress {
	// ---------------- progress bar example variables 
		ProgressDialog progressBar;
		Handler progressBarHandler = new Handler();
		int progressBarStatus = 0;
		int fileSize=0;
		int percent = 0;
		int progress=0;
		int DownloadListLength;
		String path, ONLINEDIR;
		SharedPreferences prefs= MyApplicationContext.getAppContext().getSharedPreferences(Wizard.PREFCOUNTS, 0);
		SharedPreferences.Editor editor = prefs.edit();
		ArrayList<String> LINKSARRAY= new ArrayList<String>();
		MainPage MP= new MainPage();
		FileOperations fo = new FileOperations();
		int previousNum;
		int requestNum,From;
		int multiple;
		File destination = Environment.getExternalStorageDirectory();
		File DetailFile= new File(destination.toString()+"listwithmore/asdasdas.xm");
		
		UseElements UE = new UseElements();
		
		ArrayList<ArrayList<String>> PICURLS= new ArrayList<ArrayList<String>>();
//		ArrayList<ArrayList<String>> PICNAMES= new ArrayList<ArrayList<String>>();

		
		
		ArrayList<String> DownloadItems;
		
		// ------------------------------------------------ progress bar to download every available picture
	
		public int progress(View v, final ArrayList<String> DownloadList, String Path, String OnlineDir){
			previousNum= prefs.getInt(MP.getLINKS().get(Wizard.getSpinnerPosition()), 0);
			requestNum= Wizard.getPicRequestNumbers();
			multiple= previousNum+requestNum;

			
			path = Path;
			DownloadItems = DownloadList;
			DownloadListLength= DownloadList.size();
			ONLINEDIR= OnlineDir;
			
			progressBar = new ProgressDialog(v.getContext());
			progressBar.setCancelable(true);
			progressBar.setMessage("Downloading ... ");
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(requestNum);
			progressBar.show();
			progressBar.setCancelable(true);
			
				 progressBarStatus = 0;
				 fileSize=previousNum;
				 
					
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						

						while (progressBarStatus<requestNum) {
							progressBarStatus = doSomeTask();
							
							LINKSARRAY = MP.getLINKS();
							
							editor.putInt(LINKSARRAY.get(Wizard.getSpinnerPosition()),multiple);
							editor.commit();
							
							
							progressBarHandler.post(new Runnable() {
								
								@Override
								public void run() {
									progressBar.setProgress(progressBarStatus);
									
								}
							});
						};
								

						if (progressBarStatus >= requestNum) {
							
							progressBar.dismiss();
						}
						
						
					}

					
				}).start();
				return multiple;
			}
	
	private int doSomeTask() {
		
		
		ArrayList<ArrayList<String>> Dirs= UE.getDirProperty(MyApplicationContext.getAppContext().getResources().getString(R.string.baseDir)+"index.php?dir=pictures%2F"+ONLINEDIR+"%2F");
		ArrayList<String> DirNames= Dirs.get(0);
		ArrayList<String> picurlss = new ArrayList<String>();
//		ArrayList<String> pictureName = new ArrayList<String>();
		List<String> DirUrls= Dirs.get(1);
		for (int i = 0; i < DirUrls.size(); i++) {
			ArrayList<ArrayList<String>> Pics = UE.getPicProperty(DirUrls.get(i));
			picurlss= Pics.get(1);
			PICURLS.add(i,picurlss);
//			pictureName= Pics.get(0);
//			PICNAMES.add(pictureName);
			
			
		}
		
	
		if (fileSize>=previousNum && fileSize <multiple) {
			String itemToDownload = DownloadItems.get(fileSize).replaceAll("\\s", "");
			FileOperations.SaveFileFromLinkToSD(itemToDownload, path+"/"+ONLINEDIR);
			
			int i = fileSize;
				for (int j = 0; j < PICURLS.get(i).size(); j++) {
					itemToDownload = PICURLS.get(i).get(j).replaceAll("\\s", "");
					FileOperations.SaveFileFromLinkToSD(itemToDownload, path+"/"+ONLINEDIR+"/"+fileSize);
						
					FileOutputStream FOS = null;
					DetailFile= new File(Environment.getExternalStorageDirectory().toString()+"/"+path+"/"+ONLINEDIR+"/"+fileSize+"/picdetails.xml"); 
					
					if (j==0) {
						try {
							FOS = new FileOutputStream(DetailFile,false);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						try {
							FOS = new FileOutputStream(DetailFile,true);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						byte[] StringToAppen =("<pic>\n"+fo.getNameFromLink(itemToDownload)+"\n</pic>\n").getBytes();
						try {
							FOS.write(StringToAppen);
							FOS.flush();
						FOS.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					
				}			
			fileSize++;
			progress++;
			return progress;

		}
		
		return 100;
	}
	
	
	
	
	// ------------- progress bar to download available picture in a range of "FROM" to "TO"
	
			public void progressFromTO(View v, final ArrayList<String> DownloadList, String Path, String OnlineDir,int FROM, int TO){
				
				requestNum= TO-FROM;
				multiple= FROM+requestNum;

				
				path = Path;
				DownloadItems = DownloadList;
				DownloadListLength= DownloadList.size();
				ONLINEDIR= OnlineDir;
				
				progressBar = new ProgressDialog(v.getContext());
				progressBar.setCancelable(true);
				progressBar.setMessage("در حال ترمیم ... ");
				progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressBar.setProgress(0);
				progressBar.setMax(requestNum);
				progressBar.show();
				progressBar.setCancelable(true);
				
					 progressBarStatus = 0;
					 fileSize=FROM;
					 From= FROM;
					 
						
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							

							while (progressBarStatus<DownloadList.size()) {
								progressBarStatus = doSomeTaskFromTo();
								
								LINKSARRAY = MP.getLINKS();
								
								progressBarHandler.post(new Runnable() {
									
									@Override
									public void run() {
										progressBar.setProgress(progressBarStatus);
										
									}
								});
							};
									

							if (progressBarStatus >= DownloadList.size()) {
								
								progressBar.dismiss();
							}
							
						}

						
					}).start();
				}
		
		private int doSomeTaskFromTo() {
			
			
			ArrayList<ArrayList<String>> Dirs= UE.getDirProperty(MyApplicationContext.getAppContext().getResources().getString(R.string.baseDir)+"index.php?dir=pictures%2F"+ONLINEDIR+"%2F");
			ArrayList<String> DirNames= Dirs.get(0);
			ArrayList<String> picurlss = new ArrayList<String>();
//			ArrayList<String> pictureName = new ArrayList<String>();
			List<String> DirUrls= Dirs.get(1);
			for (int i = From; i < multiple; i++) {
				ArrayList<ArrayList<String>> Pics = UE.getPicProperty(DirUrls.get(i));
				picurlss= Pics.get(1);
				PICURLS.add(picurlss);
//				pictureName= Pics.get(0);
//				PICNAMES.add(pictureName);
				
				
			}
			
		
			if (fileSize>=From && fileSize <multiple) {
				String itemToDownload = DownloadItems.get(fileSize);
				FileOperations.SaveFileFromLinkToSD(itemToDownload, path+"/"+ONLINEDIR);
				
				int i = 0;
					for (int j = 0; j < PICURLS.get(i).size(); j++) {
						itemToDownload = PICURLS.get(i).get(j);
						FileOperations.SaveFileFromLinkToSD(itemToDownload, path+"/"+ONLINEDIR+"/"+fileSize);
							
						FileOutputStream FOS = null;
						DetailFile= new File(Environment.getExternalStorageDirectory().toString()+"/"+path+"/"+ONLINEDIR+"/"+fileSize+"/picdetails.xml"); 
						
						if (j==0) {
							try {
								FOS = new FileOutputStream(DetailFile,false);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else{
							try {
								FOS = new FileOutputStream(DetailFile,true);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
							byte[] StringToAppen =("<pic>\n"+fo.getNameFromLink(itemToDownload)+"\n</pic>\n").getBytes();
							try {
								FOS.write(StringToAppen);
								FOS.flush();
							FOS.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						
					}			
				fileSize++;
				progress++;
				return progress;

			}
			
			return 100;
		}


	
}
