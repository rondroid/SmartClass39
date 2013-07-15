package org.aakashlabs.smartclass;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {
	//static boolean start=false;
	static boolean sender=true;
	
	static String message;
	static Context context;
	boolean mIsBound;
	static ListView listview;
	static SharedPreferences sharedPrefs;
	static Adapter adp;
	private static final int MSG_SHOW_TOAST = 1;
	
	String name, addr;
	TextView tv;
	boolean is_noti;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//String SERVERIP=getIpAddress(this);
		//String MACADDR=getMACaddr(this);
		//Log.d("SIP", sIP);
		
		ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		//sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		//CommonUtilities.nickname=""+sharedPrefs;
		setContentView(R.layout.activity_chat);
		//CheckIfServiceIsRunning();
		context=this;
			
		//new Thread(new Client()).start();
		
		// Code to get intent info for PM
		Log.d("ChatActivity", "before getExtra");
		Log.d("ChatActivity", "intent is not null");
		name = getIntent().getStringExtra("PM user name");
		addr = getIntent().getStringExtra("PM user addr");
		is_noti = getIntent().getBooleanExtra("is_noti",false);
		if(is_noti)
		{
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.cancel(0);
		}
		tv=(TextView) findViewById(R.id.textView1);
		tv.setText("Chat with "+name);
		//Log.d("Addr", addr);
		if(addr==null)
			CommonUtilities.recipient="public";
		else if(!addr.equals(MainActivity.sIP))
			CommonUtilities.recipient=addr;
		
		if(!(name==null))
		{
			CommonUtilities.name=name;
			tv.setText("Discussion with "+name);
		}
		else
			tv.setText("Discussion with entire class");
		
		listview = (ListView) findViewById(R.id.listView1);
		adp=new Adapter(this, buildData("starting"));
		listview.setAdapter(adp);
			
		EditText t=(EditText) findViewById(R.id.editText1);
		t.setTextColor(Color.WHITE);
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.clear:
		    	DataSource ds=new DataSource(this);
		    	ds.open();
		    	ds.cleardata(CommonUtilities.recipient);
		    	adp=new Adapter(this, buildData("starting"));
				listview.setAdapter(adp);
		    	adp.notifyDataSetChanged();
		    	ds.close();
		    	break;
		    
                case R.id.archive:
                    // app icon in action bar clicked; go home
                    CommonUtilities.message_show=true;
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
	        return true;
        }

	
	static public void initiateList(String sender,String message,String date)
	{ 
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	    ArrayList<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	    Log.d("dasd",message);
	    
	    long milis=Long.parseLong(date);
	    
		Date date1 = new Date(milis);
		 SimpleDateFormat ft = new SimpleDateFormat ("d MMM,y 'at' H:m:s");
	    Log.d("date", ""+date1);
	    System.out.print(date1);
		    list.add(putData( sender,message));
		    list2.add(putData(ft.format(date1), "08:51"));
		   // Log.d("asd",""+adp.getPosition(putData("02,March", "08:51")));   
    	final returndata rd1=new returndata(list,list2);
    	
    	UIHandler.post(new Runnable() {
	        @Override
	        public void run() {
	        	
    	           adp.add(rd1);
    	           
    	           adp.notifyDataSetChanged();
    	           Log.d("listcheck"," " +(listview.getCount()-2)+" " +listview.getLastVisiblePosition());
    	           if((listview.getCount()-2)==listview.getLastVisiblePosition())
    	           {
    	        	   listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    	        	   listview.setStackFromBottom(true);
    	           }
    	           else
    	           {
    	        	   listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
    	        	   listview.setStackFromBottom(false);
    	           }
    	           
    	           
	        }
    	});
    	
	}
		
	
	public void MyClick(View v)
	  {
		  switch(v.getId())
		  {
		  case R.id.send_chat:
			  ArrayList<String> listItems = new ArrayList<String>();
			  EditText t=(EditText) findViewById(R.id.editText1);
			  message=t.getText().toString();
			  
			  //buildData(message);
			ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE); 
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 

            
            do
			  {
	             /* if (mWifi.isConnected()) { 
	              start=true;     //Starts client thread to send through wifi 
	              } 
	
	              try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
	             // Log.d("checkchange set to true", String.valueOf(start)+String.valueOf(message));
					if (mWifi.isConnected()) { 
		                new Thread(new Client()).start(); 
		                } 
					
				  //start=false;
			  }while(!CommonUtilities.message_received);
            	
            	t.setText("");
            	
            
			  break;
			  
		  }
	  }
	
	
	
	public static Handler UIHandler;

	static 
	{
	    UIHandler = new Handler(Looper.getMainLooper());
	}
	static public void updateList(String message)
	{
		
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	    ArrayList<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	    Log.d("dasd",message);
	    final String uname;
	    uname=message.substring(0,message.indexOf("/"));
	    final String messdata= message.substring(message.indexOf("/")+1);
	    Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("d MMM,y 'at' H:m:s");
	    
		    list.add(putData( uname,messdata));
		    list2.add(putData(ft.format(dNow), "08:51"));
		   // Log.d("asd",""+adp.getPosition(putData("02,March", "08:51")));   
    	final returndata rd1=new returndata(list,list2);
    	
    	UIHandler.post(new Runnable() {
	        @Override
	        public void run() {
	        	
 	           if(adp!=null)
 	           {
    	           adp.add(rd1);
    	           adp.notifyDataSetChanged();
    	           Log.d("listcheck"," " +(listview.getCount()-2)+" " +listview.getLastVisiblePosition());
    	           if((listview.getCount()-2)==listview.getLastVisiblePosition())
    	           {
    	        	   listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    	        	   listview.setStackFromBottom(true);
    	           }
    	           else
    	           {
    	        	   listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
    	        	   listview.setStackFromBottom(false);
    	           }
    	           
 	           }   
 	           
	        }
    	});	
    	
	}
	

		public static String getMACaddr(Context context){
			WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			String address = info.getMacAddress();
			return address;
		}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		
			
		return true;
	}

	

	public ArrayList<returndata> buildData(String message) {
		  Log.d("message", message);
	    ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	    ArrayList<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	    ArrayList<returndata> rd=new ArrayList<returndata>();
	    	DataSource ds;
	    	ds = new DataSource(this);
	    	ds.open();
	    	Log.d("recipient at build data", CommonUtilities.recipient);
	    	ds.view(CommonUtilities.recipient);
	    	Log.d("recipient at build data", CommonUtilities.recipient);
	    	
		    returndata rd1=new returndata(list,list2);
		    
		    rd.add(rd1);
		    
		    Log.d("bulid", "complete");
		   ds.close();
	    return rd;
	    
	  }

	 static private HashMap<String, String> putData(String name, String content) {
	    HashMap<String, String> item = new HashMap<String, String>();
	    item.put("name", name);
	    item.put("content", content);
	    
	    return item;
	  }

	   static InetAddress getBroadcastAddress() throws IOException {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			DhcpInfo dhcp = wifi.getDhcpInfo();
			// handle null somehow

			int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
			byte[] quads = new byte[4];
			for (int k = 0; k < 4; k++)
				quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
			return InetAddress.getByAddress(quads);
		}
	  
	
	  }

class Adapter extends ArrayAdapter<returndata> {
	  private final Activity context;
	   private final ArrayList<returndata> names;
	   //private final ArrayList<Map<String, String>> list2;
	   
	   
	  static class ViewHolder {
	    public TextView text,text1,text2;
	    
	  }

	  public Adapter(Activity context, ArrayList<returndata> rd) {
		 
	    super(context, R.layout.chat_align,rd);
	    this.context = context;
	    this.names = rd;
	    Log.d("Constructor ", "complete");
	    //this.list2 = rd.getlist2();
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.chat_align, null);
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.name);
	      viewHolder.text1 = (TextView) rowView.findViewById(R.id.content);
	      viewHolder.text2 = (TextView) rowView.findViewById(R.id.timestamp);
	      rowView.setTag(viewHolder);
	    }

	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String s = names.get(position).getlistname();
	    
	    String s1 = names.get(position).getlistcontent();
	    
	    String s2 = names.get(position).getlist2name();
	    
	    Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/eraserdust.ttf");
	    holder.text.setTypeface(tf); holder.text1.setTypeface(tf); holder.text2.setTypeface(tf);
	    holder.text.setText(s);
	    holder.text1.setText(s1);
	    holder.text2.setText(s2);
	    
	    holder.text.setText(s);
	    holder.text1.setText(s1);
	    holder.text2.setText(s2);
	   

	    return rowView;
	  }
	} 
final class returndata
{
	private final ArrayList<Map<String, String>> list;
	private final ArrayList<Map<String, String>> list2;
	
	public returndata(ArrayList<Map<String, String>> list, ArrayList<Map<String, String>> list2) {
      this.list = list;
      this.list2 = list2;
  }

  public String getlistname() {
	  if(!list.isEmpty())
	  {
		  Log.d("asdjl",list.get(0).get("name"));
	      return list.get(0).get("name");
	  }
	  return "Welcome Class";
  }
  public String getlistcontent() {
	  if(!list.isEmpty())
	  {
		  return list.get(0).get("content");
	  }
	  return "";
  }
  public String getlist2name() {
	  if(!list.isEmpty())
	  {
		  return list2.get(0).get("name");
	  }
	  return "";
  }
  public String getlist2content() {
	  if(!list.isEmpty())
	  {
		  return list2.get(0).get("content");
	  }
	  return "";
  }
/*
  public ArrayList<Map<String, String>> getlist2() {
      return list2;
  }*/
}


