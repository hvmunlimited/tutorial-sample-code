package com.listwithmore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class SomeTests  {
	UseElements UE= new UseElements();
	
	public boolean NeedRepair(int Position, String FirstPic,String PicsDirPath) {
		File PicDetails = new File(PicsDirPath+"/picdetails.xml");
		File TeaserPic= new File(FirstPic);
		if (TeaserPic.exists()) {
		}
		else{
			return false;
		}
		if (PicDetails.exists()) {
			ArrayList<String> PicsToBeChecked = new ArrayList<String>();
			try {
				PicsToBeChecked = UE.getElementsFromFile(PicDetails, "pic");

				
				// to remove all the spaces into the name we must do this part
				for (int j=0 ; j<PicsToBeChecked.size();j++) {
					PicsToBeChecked.set(j,PicsToBeChecked.get(j).replaceAll("\\s+",""));

				}
				for (int i = 0; i < PicsToBeChecked.size(); i++) {
					File F= new File(PicsDirPath+"/"+PicsToBeChecked.get(i));
					if (F.exists()) {
						
					}
					else {
						return false;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
		}
		else{
			return false;
		}

		
		return true;
		
	}

}



