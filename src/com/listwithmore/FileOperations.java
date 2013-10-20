package com.listwithmore;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import android.content.Context;
import android.os.Environment;

public class FileOperations {

	public static String getNameFromLink(String link) {
		String[] LinkParts= link.split("/");
		String PicName= LinkParts[LinkParts.length-1];
		return PicName;
	}
	public static String getRowNameFromLink(String link) {
		String name= getNameFromLink(link);
		String[] nameParts = name.split("\\.");
		String RowName= nameParts[0];
		return RowName;
		
	}
	
	public static void SaveFileFromLinkToSD(String link, String Directory) {
		String Name = getNameFromLink(link);
		File NewFile = new File( Environment.getExternalStorageDirectory().toString()+"/"+Directory+"/"+Name);
		if (NewFile.exists()) {
			
		}
		else {
			try {
				FileUtils.copyURLToFile(new URL(link),NewFile);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void SaveFileFromLinkToRoot(String link, Context context) {
		String Name = getNameFromLink(link);
		File NewFile = new File("/d/hvm.jpg");
		if (NewFile.exists()) {
			
		}
		else {
			try {
				FileUtils.copyURLToFile(new URL(link),NewFile);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public static void SaveFileFromLinkToSD(String link, String Directory, boolean CheckExist) {
		String Name = getNameFromLink(link);
		File NewFile = new File( Environment.getExternalStorageDirectory().toString()+"/"+Directory+"/"+Name);
		if (CheckExist) {
			if (NewFile.exists()) {
			
			}
			else {
				try {
					FileUtils.copyURLToFile(new URL(link),NewFile);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			try {
				FileUtils.copyURLToFile(new URL(link),NewFile);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}


