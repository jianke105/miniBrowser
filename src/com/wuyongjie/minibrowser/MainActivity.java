package com.wuyongjie.minibrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Context friendContext;
	private WebView wv;
	private View btn_back;
	private View btn_forward;
	private View btn_refresh;
	private View btn_stop;
	private View btn_home;
	private View btn_bookmark;
	private View btn_search;
	private EditText url_edt;
	private String url_home = "http://www.cvte.cn";
	private BookMarkManager bookMarkManager = BookMarkManager.getBookMarkManager();
	private SkinManager skinManager = SkinManager.getSkinManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		init();		
		
		Intent intent = getIntent();		
		String color = intent.getStringExtra("color"); 
		
		if (color != null)
		{
			Toast.makeText(MainActivity.this, "增加了新皮肤：" + color, Toast.LENGTH_SHORT).show();	
			skinManager.addItem(color);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();	
		String classname = intent.getStringExtra("class");
		String pack = intent.getStringExtra("pack");
		String url = intent.getStringExtra("url");
		
		//Toast.makeText(MainActivity.this, pack, Toast.LENGTH_SHORT).show();
		if(classname != null && classname.equals("skinList") && pack != null){
		
			//尝试获取可以共享资源的apk的context
			try {
				friendContext =  createPackageContext(pack, Context.CONTEXT_IGNORE_SECURITY);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
	        if(friendContext == null)
	        {
	        	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("快去安装新皮肤");
				builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	dialog.dismiss();
	            }});    
	            builder.create().show();
	        }
	        else{
				Resources resources = friendContext.getResources();
				btn_back.setBackground(resources.getDrawable(R.drawable.btn_back));
				btn_forward.setBackground(resources.getDrawable(R.drawable.btn_forward));
				btn_refresh.setBackground(resources.getDrawable(R.drawable.btn_refresh));
				btn_stop.setBackground(resources.getDrawable(R.drawable.btn_stop));
				btn_home.setBackground(resources.getDrawable(R.drawable.btn_home));
				btn_search.setBackground(resources.getDrawable(R.drawable.btn_search));		
				
				Toast.makeText(MainActivity.this, "换皮肤啦", Toast.LENGTH_SHORT).show();
	        }

		}
		else if (classname != null && classname.equals("bookMark") && url != null){
			connectToUrl(url);		
		}
	}

	private void init() {
		
		btn_back = findViewById(R.id.btn_back);
		btn_forward = findViewById(R.id.btn_forward);
		btn_refresh = findViewById(R.id.btn_refresh);
		btn_stop = findViewById(R.id.btn_stop);
		btn_home = findViewById(R.id.btn_home);
		btn_bookmark = findViewById(R.id.btn_bookmark);
		btn_search = findViewById(R.id.btn_search);
		url_edt = (EditText) findViewById(R.id.url_string);
		wv = (WebView)findViewById(R.id.webView);
		
		
		wv.setBackgroundColor(Color.rgb(0x7C, 0xDD, 0x7C));
		
		WebSettings ws = wv.getSettings();
		ws.setBuiltInZoomControls(true);
		ws.setJavaScriptEnabled(true);  
        ws.setJavaScriptCanOpenWindowsAutomatically(true);  
        ws.setSaveFormData(false);   
        ws.setAppCacheEnabled(true);  
		
		wv.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				view.loadUrl(url);
				url_edt.setText(url);
				return true;
			}
		});
		
		wv.setWebChromeClient(new WebChromeClient(){
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				//完整加载一个新页面之后
				if(newProgress == 100) 
				{
					//修改地址栏，使得地址栏正确显示当前地址
					url_edt.setText(wv.getOriginalUrl());
					
					//检测当前页面是否已收藏，是则更改收藏图标
					if ( bookMarkManager.isMark(wv.getTitle()) ) {
						btn_bookmark.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.bookmark_selected));
					}
					else{
						btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark);
					}
				}
				super.onProgressChanged(view, newProgress);
			}

			public void onRequestFocus(WebView view){
				super.onRequestFocus(view);
				view.requestFocus();
			}
		});
		
		wv.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent arg1) {
				switch(view.getId()){
					case R.id.webView:
						wv.requestFocus();
						break;
				}		
				return false;
			}		
		});
		
		//设置按钮监听事件
		setListener();
		
		url_edt.setText(url_home);
		connectToUrl(url_edt.getText().toString());
	}
	
	private void setListener(){
		btn_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (wv.canGoBack()){					
						wv.goBack();		
				}else{
					Toast.makeText(MainActivity.this, "这是第一页", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_forward.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (wv.canGoForward()){				
					wv.goForward();				
				}else{
					Toast.makeText(MainActivity.this, "这是最后一页", Toast.LENGTH_SHORT).show();
				}
			}	
		});
		
		btn_refresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "正在刷新页面", Toast.LENGTH_SHORT).show();
				wv.reload();
			}
			
		});
		
		btn_stop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "页面停止加载", Toast.LENGTH_SHORT).show();
				wv.stopLoading();
			}
		});
		
		btn_home.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {				
				connectToUrl(url_home);					
			}
		});
		
		btn_bookmark.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				bookMarkManager.addItem(wv.getTitle(), wv.getOriginalUrl());
				btn_bookmark.setBackground(MainActivity.this.getResources().getDrawable(R.drawable.bookmark_selected));
				
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("页面已经收藏");
				builder.setPositiveButton("确认收藏", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	dialog.dismiss();
                }});
                builder.setNegativeButton("取消收藏", new DialogInterface.OnClickListener() {
                	public void onClick(DialogInterface dialog, int id) {
                		bookMarkManager.deleteItem(wv.getTitle());
                		btn_bookmark.setBackgroundResource(R.drawable.btn_bookmark);
                	}
                });
                builder.create().show();				
			}
		});
		
		btn_search.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				connectToUrl(url_edt.getText().toString());
			}
		});
	}
	
	private void connectToUrl(String string) {
		
		String url = "";
		if(string.contains("http://")){
			url = string;
		}else{
			url = "http://" + string;
		}
		
		wv.loadUrl(url);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			if (wv.canGoBack())
			{
				wv.goBack();
				return true;
			}
			else
			{
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch(id){
	
			case R.id.action_settings: {
				return true;
			}
			
			case R.id.skin_changing: {
				startActivity(new Intent(this, SkinList.class));
				return true;			
			}
			
			case R.id.bookmark: {
				Intent intent = new Intent(MainActivity.this, BookMark.class);
				startActivity(intent);
				return true;
			}
			
			case R.id.hidehome: {
				btn_home.setVisibility(View.GONE);
			}
			
			case R.id.bigword: {
				url_edt.setTextSize(45f);
				
				/*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("请选择字体大小");
				builder.setView(View.inflate(MainActivity.this, R.id.seekbar, null));
				builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                	dialog.dismiss();
	            }});    
	            builder.create().show();*/
			}
		}
		
		return super.onOptionsItemSelected(item);
		
	}
}
