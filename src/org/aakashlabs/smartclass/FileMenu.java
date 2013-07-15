package org.aakashlabs.smartclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aakashlabs.smartclass.FileChooser.FileClient;

import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileMenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/eraserdust.ttf");
       
		setContentView(R.layout.activity_file_menu);
		 TextView tv1 = (TextView) findViewById(R.id.sendfile);
	        TextView tv2 = (TextView) findViewById(R.id.recievefile);
	        tv1.setTypeface(tf);
	        tv2.setTypeface(tf);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
                case android.R.id.home:
                    // app icon in action bar clicked; go home
                    Intent intent = new Intent(this, MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}
	public void MyClick(View view)
	{
		switch(view.getId())
		{
		case R.id.fileopt1:
			if(CommonUtilities.mode==0)
			{
				Intent myIntent4 = new Intent(this, FileChooser.class);
	            startActivity(myIntent4);
			}
			else
				Toast.makeText(this, "Only teacher is allowed to send files",Toast.LENGTH_SHORT ).show();
			
			break;
		case R.id.fileopt2:
			
			Intent myIntent4 = new Intent(this, ReceivedFiles.class);
            startActivity(myIntent4);
			break;
		}
	}
	
	
	
}


