package com.wuyongjie.minibrowser;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class BookMark extends Activity {

	private View back_bookmark;
	private ListView list_bookmark;
	private ArrayList<Map<String, String>> data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark);
		
		back_bookmark = findViewById(R.id.back_bookmark);
		list_bookmark = (ListView)findViewById(R.id.list_bookmark);
		
		back_bookmark.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
				
			}
			
		});
		
		//item.put("title", "CVTE");
		//item.put("url", "www.cvte.cn");
		//BookMarkManager bm = BookMarkManager.getBookMarkManager();
		//bm.addItem("CVTE", "www.cvte.cn");
		//bm.addItem("百度一下", "www.baidu.com");
		//bm.addItem("新浪", "www.sina.com");
		
		data = BookMarkManager.getBookMarkManager().getData();
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, 
				R.layout.list_item, new String[]{"title", "url"}, 
				new int[]{R.id.title, R.id.url});
		
		list_bookmark.setAdapter(adapter);
		
		list_bookmark.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				Toast.makeText(BookMark.this, "您选择了标题：" + data.get(position).get("title") + 
						"\nUrl："+data.get(position).get("url"), Toast.LENGTH_SHORT).show();  
				Intent intent = new	Intent(BookMark.this, MainActivity.class);
				intent.putExtra("class", "bookMark");
				intent.putExtra("url", data.get(position).get("url"));
				startActivity(intent);
			}  
			
		});
	}

}
