package org.aakashlabs.smartclass;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherListActivity extends Activity {
	
	static ArrayList<String> teachersnmArrayList = new ArrayList<String>();    //Store teachers' names
	static ArrayList<String> teachersadArrayList = new ArrayList<String>();    //Store teachers' addresses
	static Context con;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_list);
		con=this;
		new scan().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher_list, menu);
		return true;
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
        private ProgressDialog Dialog = new ProgressDialog(TeacherListActivity.this);
        @Override
        protected void onPreExecute()
        {
        Dialog.setMessage("Updating teachers list...");
        Dialog.show();
        teachersnmArrayList.clear();
        teachersadArrayList.clear();
    }

    @Override
    protected Void doInBackground(Void... arg0) 
    {	
   	 
    	Log.e("PM", "before makelistview callrbfhbrfhbfbdnfbdnfbdfdfhbsdfnhbsdhb");
    	ConnectivityManager connManager = (ConnectivityManager) TeacherListActivity.this.getSystemService(CONNECTIVITY_SERVICE); 
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 

		
		do
		  {
           		if (mWifi.isConnected()) { 
           			Log.d("teacher", "sending");
					CommonUtilities.message="-teacher/";
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

    
    public void makeListView(){
		Log.d("PM", "before listview initializing");
		final ListView listview = (ListView) findViewById(R.id.teacher_list);
		Log.d("PM", "before Adapter initializing");
		final StableArrayAdapter adapter = new StableArrayAdapter(con,
		        R.layout.scanlist_row, teachersnmArrayList);
		Log.d("PM", "before setAdapter");
		    listview.setAdapter(adapter);
		    if(teachersnmArrayList.isEmpty())
		    {
		    	Toast.makeText(con, "NO TEACHERS CURRENTLY AVAILABLE", Toast.LENGTH_LONG).show();
		    }
		    	
		    Log.d("PM", "before setOnItemClickListener");
		    listview.setOnItemClickListener(new OnItemClickListener() {
		    	  @Override
		    	  public void onItemClick(AdapterView<?> parent, View view,
		    	    int position, long id) {
		    		  CommonUtilities.teachername=teachersnmArrayList.get(position);
		    		  CommonUtilities.teacheraddr=teachersadArrayList.get(position);
		    		  Intent myIntent = new Intent(con, ChatActivity.class);
		    		  myIntent.putExtra("PM user name", teachersnmArrayList.get(position));
		    		  myIntent.putExtra("PM user addr", teachersadArrayList.get(position));
		  		      myIntent.putExtra("is_noti", false);
		    		  
		              startActivity(myIntent);
		              Log.d("PM", "after startActivity");
		    	  }
		    	}); 
	}

}
	}
