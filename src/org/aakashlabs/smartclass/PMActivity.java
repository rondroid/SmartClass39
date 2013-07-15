package org.aakashlabs.smartclass;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PMActivity extends Activity {
	
	static ArrayList<String> namesArrayList = new ArrayList<String>();    //Stores nicknames
	static ArrayList<String> addrArrayList = new ArrayList<String>();    //Store corresponding IPs
	static Context con;
	
	static ArrayList<String> permnamesArrayList = new ArrayList<String>();    //Stores nicknames
	static ArrayList<String> permaddrArrayList = new ArrayList<String>();    //Store corresponding IPs
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pm);
		ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
		con=this;
		new scan().execute();
		
	}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {
		

	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }
	}

	private class scan extends AsyncTask<Void, Void, Void> 
    {
        private ProgressDialog Dialog = new ProgressDialog(PMActivity.this);
        @Override
        protected void onPreExecute()
        {
        Dialog.setMessage("Updating students list...");
        Dialog.show();
        namesArrayList.clear();
        addrArrayList.clear();
    }

    @Override
    protected Void doInBackground(Void... arg0) 
    {	
   	 
    	Log.e("PM", "before makelistview callrbfhbrfhbfbdnfbdnfbdfdfhbsdfnhbsdhb");
    	ConnectivityManager connManager = (ConnectivityManager) PMActivity.this.getSystemService(CONNECTIVITY_SERVICE); 
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 

		
		do
		  {
           		if (mWifi.isConnected()) { 
					CommonUtilities.message="-ping/";
	                new Thread(new PingClient()).start(); 
	                } 
				 try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			  //start=false;
		  }while(!CommonUtilities.message_received);

        return null;
    }

    @Override
    protected void onPostExecute(Void params)
        {

// after completed finished the progressbar
        Dialog.dismiss();
        Log.d("PM", "before makelistview call");
        makeListView();
    }

	
    }
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pm, menu);
		return true;
	}
	
	static InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
}

	public void makeListView(){
		Log.d("PM", "before listview initializing");
		final ListView listview = (ListView) findViewById(R.id.list);
		Log.d("PM", "before Adapter initializing");
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
		        R.layout.scanlist_row, namesArrayList);
		Log.d("PM", "before setAdapter");
		    listview.setAdapter(adapter);
		    if(namesArrayList.isEmpty())
		    {
		    	Toast.makeText(con, "NO STUDENTS CURRENTLY AVAILABLE", Toast.LENGTH_LONG).show();
		    }
		    Log.d("PM", "before setOnItemClickListener");
		    listview.setOnItemClickListener(new OnItemClickListener() {
		    	  @Override
		    	  public void onItemClick(AdapterView<?> parent, View view,
		    	    int position, long id) {
		    		  Log.d("PM", "before intent");
		    		  Intent myIntent = new Intent(con, ChatActivity.class);
		    		  Log.d("PM", "before startActivity");
		    		  myIntent.putExtra("PM user name", namesArrayList.get(position));
		    		  myIntent.putExtra("PM user addr", addrArrayList.get(position));
		  		      myIntent.putExtra("is_noti", false);
		    		  
		              startActivity(myIntent);
		              Log.d("PM", "after startActivity");
		    	  }
		    	}); 
	}

}
