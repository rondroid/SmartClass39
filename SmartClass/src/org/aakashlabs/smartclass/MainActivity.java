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
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	static String sIP ;
	static Context con_main;
	static SharedPreferences sharedPrefs;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/eraserregular.ttf");
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv1.setTypeface(tf);tv2.setTypeface(tf);
		con_main = this;
		Intent i=new Intent(this,UDPService.class);
		startService(i);
		sIP = getIpAddress(con_main);
		CommonUtilities.message_show=false;
		try {
			CommonUtilities.broadcastaddr = getBroadcastAddress();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (getIntent().getBooleanExtra("EXIT", false)) {
			 finish();
			}
		if (UDPService.WAKELOCK == null) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            UDPService.WAKELOCK = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Service lock");
            UDPService.WAKELOCK.acquire(); //released is called when activity is stopped
        }
		
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		CommonUtilities.nickname=sharedPrefs.getString("prefUsername", "Anon");
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void MyClick(View view)
	{
		switch(view.getId())
		{
		case R.id.student:
			
             CommonUtilities.mode=1;
			break;
		case R.id.teacher:
			
            CommonUtilities.mode=0;
			break;
		}
		Intent myIntent;
		if(CommonUtilities.nickname.equals("Anon"))
			myIntent = new Intent(this, Preferences.class);
		else
			myIntent = new Intent(this, MenuActivity.class);
			startActivity(myIntent);
	}
	
	static InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) con_main.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}
	
	public static String getIpAddress(Context context) {
		try {

			InetAddress inetAddress=null;
			InetAddress myAddr=null;
			Log.d("check","Here1");
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
			Log.d("check","Here12");
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			Log.d("check","Here13");

			if (mWifi.isConnected()) {
				Log.d("check","Here14");
				for (Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();  networkInterface.hasMoreElements();) {
					Log.d("check","Here2");

					NetworkInterface singleInterface = networkInterface.nextElement();

					for (Enumeration<InetAddress> IpAddresses = singleInterface.getInetAddresses(); IpAddresses.hasMoreElements();) {
						inetAddress = IpAddresses.nextElement();

						Log.d("check","" + inetAddress);
						if(!inetAddress.isLoopbackAddress() && (singleInterface.getDisplayName().contains("wlan0" ) || 
								singleInterface.getDisplayName().contains("eth0"  ))){

							myAddr=inetAddress;
							//myAddr=InetAddress.getByName("127.0.0.1");
						}       
					} 
				}
				sIP=myAddr.toString().substring(1);
				return myAddr.toString().substring(1);
			}


		}catch (Exception ex) {
			ex.printStackTrace();
			Log.e("check", ex.toString());
		}
		return null;
	}
}
