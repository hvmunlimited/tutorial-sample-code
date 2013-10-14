package com.listwithmore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UseElements {
	
	
	
	
	
	
	public ArrayList<String> getElements(String URL, String attributeToCatch) throws IOException{
		Document doc = Jsoup.connect(URL).get();
		Elements ids= doc.getElementsByTag(attributeToCatch);
		ArrayList<String> rows= new ArrayList<String>();
		for (Element element : ids) {
			rows.add(element.toString().split("<"+attributeToCatch+">"+"\n")[1].split("\n"+"</"+attributeToCatch+">")[0]);
		}
		return rows;
	}
	
	public ArrayList<String> getElementsFromFile(File file, String attributeToCatch) throws IOException{
		Document doc = Jsoup.parse(file, "UTF-8");
		Elements ids= doc.getElementsByTag(attributeToCatch);
		ArrayList<String> rows= new ArrayList<String>();
		for (Element element : ids) {
			rows.add(element.toString().split("<"+attributeToCatch+">"+"\n")[1].split("\n"+"</"+attributeToCatch+">")[0]);
		}
		return rows;
	}
	
	public static String[] ArrayListToStringArray(ArrayList< String> arrayList) {
		String[] array = new String[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			array[i]= arrayList.get(i);
		}
		return array;
		
	}
	
	// 0--> PicNames 1-->PicUrls  2--> PicSizes
		public ArrayList<ArrayList<String>> getPicProperty(String URL){
			Document doc;
			ArrayList<String> PicNames= new ArrayList<String>();
			ArrayList<String> PicUrls= new ArrayList<String>();
			ArrayList<String> PicSizes= new ArrayList<String>();
			ArrayList<ArrayList<String>> PicProperties= new ArrayList<ArrayList<String>>();
			String baseUrl= URL.split("index.php")[0];
			
			try {
				doc = Jsoup.connect(URL).get();
				Elements Pictures = doc.getElementById("listing").select("img[src$=jpg.gif]");
				for (Element picName : Pictures) {
					String Name= picName.attr("alt");
					PicNames.add(Name);
				}
				
				Elements Links = doc.select("div > a");
				for (Element a : Links) {
					String src= a.select(" > img").attr("src");
					if (src.endsWith("jpg.gif")) {
						PicUrls.add(baseUrl+a.attr("href"));
						PicSizes.add(a.select("em").toString().split("<em>")[1].split("</em>")[0].split("KB")[0]);
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PicProperties.add(PicNames);
			PicProperties.add(PicUrls);
			PicProperties.add(PicSizes);
			
			return PicProperties;
			
		}
		
		// 0--> DirNames 1-->DirUrls  
			public ArrayList<ArrayList<String>> getDirProperty(String URL){
				Document doc;
				ArrayList<String> DirNames= new ArrayList<String>();
				ArrayList<String> DirUrls= new ArrayList<String>();

				ArrayList<ArrayList<String>> PicProperties= new ArrayList<ArrayList<String>>();
				String baseUrl= URL.split("/nagashixml/index.php")[0];
				
				try {
					doc = Jsoup.connect(URL).get();
					Elements Pictures = doc.getElementById("listing").select("img[src$=folder.png]");
					for (Element picName : Pictures) {
						String Name= picName.attr("alt");
						DirNames.add(Name);
					}
					
					Elements Links = doc.select("div > a");
					for (Element a : Links) {
						String src= a.select(" > img").attr("src");
						if (src.endsWith("folder.png")) {
							DirUrls.add(baseUrl+a.attr("href"));
						}
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PicProperties.add(DirNames);
				PicProperties.add(DirUrls);

				
				return PicProperties;
				
			}
		
}
