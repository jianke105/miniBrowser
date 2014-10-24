package com.wuyongjie.minibrowser;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SkinList extends Activity {

	private View back_skin;
	private ListView list_skin;
	private ArrayList<String> data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skin);
		
		back_skin = findViewById(R.id.back_skin);
		list_skin = (ListView)findViewById(R.id.list_skin);
		
		back_skin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
		SkinManager skinManager = SkinManager.getSkinManager();
		skinManager.addItem("miniBrowser");
		data = skinManager.getData();
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, data);
		list_skin.setAdapter(adapter);
		
		list_skin.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				Toast.makeText(SkinList.this, "ƒ˙—°‘Ò¡À∆§∑Ù£∫" + data.get(position), Toast.LENGTH_SHORT).show();  
				Intent intent = new	Intent(SkinList.this, MainActivity.class);
				intent.putExtra("class", "skinList");
				intent.putExtra("pack", "com.wuyongjie.skin"+data.get(position));
				startActivity(intent);
				
			}  
			
		});
	
	}
	
}
