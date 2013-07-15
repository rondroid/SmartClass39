package org.aakashlabs.smartclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.Toast;

public class ReceivedFiles extends ListActivity {
	private FileArrayAdapter adapter; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		File currentDir;
		currentDir = new File("/sdcard/SmartClass/");
        fill(currentDir);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	                case android.R.id.home:
	                    // app icon in action bar clicked; go home
	                    Intent intent = new Intent(this, FileMenu.class);
	                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    startActivity(intent);
	                    return true;
	                default:
	                    return super.onOptionsItemSelected(item);
	            }
	        }
	private void fill(File f)
    {
    	File[]dirs = f.listFiles();
		 this.setTitle("Current Dir: "+f.getName());
		 List<Option>dir = new ArrayList<Option>();
		 
		 List<Option>fls = new ArrayList<Option>();
		 try{
			 for(File ff: dirs) 
			 {
				//if(ff.isDirectory())
					
				//else
				//{
					fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
				//}
			 }
		 }catch(Exception e)
		 {
			 
		 }
		 //Collections.sort(dir);
		 Collections.sort(fls);
		 dir.addAll(fls); 
		// if(!f.getName().equalsIgnoreCase("sdcard"))
			// dir.add(0,new Option("..","Parent Directory",f.getParent()));
		 adapter = new FileArrayAdapter(ReceivedFiles.this,R.layout.file_view,dir);
		 this.setListAdapter(adapter);
    }
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		//if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
			//	currentDir = new File(o.getPath());
				//fill(currentDir);
		//}
		//else
		//{
			onFileClick(o);
		//}
	}
    public static String getMimeType(String url)
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
    private void onFileClick(Option o)
    {
    	Toast.makeText(this, "File Selected: "+o.getName(), Toast.LENGTH_SHORT).show();
    	
    	Intent intent = new Intent();
    	intent.setAction(android.content.Intent.ACTION_VIEW);
    	File file = new File(o.getPath());
    	intent.setDataAndType(Uri.fromFile(file), getMimeType(o.getPath()));
    	startActivity(intent);
    	
    	//bfile=converttobytes(o.getPath());
    	//sendFile(o.getPath());
    	//new Filesend().execute(o.getPath());
    	
    	//new Thread(new FileClient(o.getName())).start();
    	
    }
	
}
