package org.aakashlabs.smartclass;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends Activity {
	
	
	static Context con_menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		Log.d("UDP", "here000");
		ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/eraserdust.ttf");
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        TextView tv3 = (TextView) findViewById(R.id.textView3);
        TextView tv4 = (TextView) findViewById(R.id.textView4);
        TextView tv5 = (TextView) findViewById(R.id.exittext);
        TextView tv6 = (TextView) findViewById(R.id.privButton);
        TextView tv7 = (TextView) findViewById(R.id.filebutton);
        tv1.setTypeface(tf);tv2.setTypeface(tf);tv3.setTypeface(tf);tv4.setTypeface(tf);tv5.setTypeface(tf);tv6.setTypeface(tf);
        tv7.setTypeface(tf);
		con_menu=this;
		SharedPreferences sharedPrefs;
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		CommonUtilities.nickname=sharedPrefs.getString("prefUsername", "Anon");

		Log.d("UDPNick", CommonUtilities.nickname);
		
		
		if(CommonUtilities.mode==0)
		{
			TextView tv = (TextView) findViewById(R.id.privButton);
			tv.setText("Private message");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	public void MyClick(View view)
	{
		switch(view.getId())
		{
		case R.id.tableRow1:
			 Intent myIntent = new Intent(this, ChatActivity.class);
             startActivity(myIntent);
             CommonUtilities.recipient="public";
			break;
		case R.id.tableRow2:
			if(CommonUtilities.mode==0)
			{
				Intent myIntent1 = new Intent(this, BoardActivity.class);
	            startActivity(myIntent1);
			}
			else
			{
				Intent myIntent3 = new Intent(this, BA2.class);
	            startActivity(myIntent3);
			}
			
			break;
			
		case R.id.tableRow3:
			Intent myIntent2 = new Intent(this, Preferences.class);
            startActivity(myIntent2);
			break;
			
		case R.id.help:
			Intent myIntent6 = new Intent(this, Help.class);
            startActivity(myIntent6);
			break;
			
		
		case R.id.privaterow:
			if(CommonUtilities.mode==0)
			{
				Intent myIntent3 = new Intent(this, PMActivity.class);
	            startActivity(myIntent3);
			}
			else
			{
				Intent myIntent3 = new Intent(this, TeacherListActivity.class);
				startActivity(myIntent3);
			}
			break;
		case R.id.file:
		{
			
				Intent myIntent5 = new Intent(this, FileMenu.class);
	            startActivity(myIntent5);
			break;
		}
		case R.id.exit:
		{
				Intent myIntent4 = new Intent(this, UDPService.class);
				stopService(myIntent4);
				myIntent4 = new Intent(this, MainActivity.class);
				myIntent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				myIntent4.putExtra("EXIT", true);
				startActivity(myIntent4);
			break;
		}
		}
	}
	
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
                case android.R.id.home:
                    // app icon in action bar clicked; go home
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

	
	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		CommonUtilities.nickname=MainActivity.sharedPrefs.getString("prefUsername", "Anon");
	}

}
