package com.wuyongjie.minibrowser;

import java.util.ArrayList;
import java.util.Map;

public class SkinManager {
	ArrayList<String> data;
	private static SkinManager skinManager = new SkinManager();
	
	private SkinManager(){
		data = new ArrayList<String>();
	}
	
	public static SkinManager getSkinManager(){		 
		return skinManager;		
	}
	
	public ArrayList<String> getData(){
		return data;
	}
	
	public boolean addItem(String item){
		if (isContain(item)){
			return false;
		}
		return data.add(item);
	}
	
	public boolean deleteItem(String item){
		return data.remove(item);
	}
	
	public boolean isContain(String item){
		return data.contains(item);
	}
}
