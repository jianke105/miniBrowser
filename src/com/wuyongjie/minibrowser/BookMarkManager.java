package com.wuyongjie.minibrowser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookMarkManager {
	private ArrayList<Map<String, String>> data;
	private static BookMarkManager bookMarkManagerInstance = new BookMarkManager();
	
	//���췽��˽��
	private BookMarkManager(){
		data = new ArrayList<Map<String, String>>();
	}
	
	//��ȡ����
	public static BookMarkManager getBookMarkManager(){		 
		return bookMarkManagerInstance;		
	}
	
	public ArrayList<Map<String, String>> getData(){
		return data;
	}
	
	public boolean addItem(Map<String, String> item){
		if (isMark(item.get("title"))){
			return false;
		}
		return data.add(item);
	}
	
	public boolean addItem(String title, String url){
		if (isMark(title)){
			return false;
		}
		Map<String, String> item = new HashMap<String, String>();
		item.put("title", title);
		item.put("url", url);
		return data.add(item);
	}
	
	public boolean deleteItem(Map<String, String> item){
		for (int i=0; i<data.size(); i++)
		{
			if (data.get(i).equals(item)){
				data.remove(i);
				break;
			}
		}
		return true;
		
	}
	
	public boolean deleteItem(String title){
		for (int i=0; i<data.size(); i++)
		{
			if (data.get(i).get("title").equals(title)){
				data.remove(i);
				break;
			}
		}
		return true;
	}
	
	//�жϵ�ǰҳ���Ƿ����ղ�
	public boolean isMark(String title){
		for (int i=0; i<data.size(); i++)
		{
			if (data.get(i).get("title").equals(title)){
				return true;
			}
		}
		return false;
	}
}
