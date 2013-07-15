package org.aakashlabs.smartclass;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.PendingIntent;
import android.content.Context; 
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class UDPService extends Service {
	String senderIP;
	ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
    int mValue = 0; // Holds last value set by a client.
    static final int MSG_REGISTER_CLIENT = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_SET_INT_VALUE = 3;
    static final int MSG_SET_STRING_VALUE = 4;
    static int accept=0;//For file dialog
    private static boolean isRunning = false;
    //final Messenger mMessenger = new Messenger(new IncomingHandler()); // Target we publish for clients to send messages to IncomingHandler.
    private int counter = 0, incrementby = 1;
	static String UDP_BROADCAST = "UDPBroadcast";
	//public static PowerManager.WakeLock WAKELOCK = null;
	public InetAddress senderIPA;
	public static Map<String, InetAddress> devices;
	private Boolean shouldRestartSocketListen=true;
	DatagramSocket socket;
	Thread UDPBroadcastThread;
	String actualMessage;
	public static PowerManager.WakeLock WAKELOCK = null;
	Context con_serv;
	
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    //TODO do something useful
		  Log.d("UDP","Service started");
		  startListenForUDPBroadcast();
		  isRunning = true;
		  con_serv = this;
	    return Service.START_NOT_STICKY;
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
	  //TODO for communication return IBinder implementation
	    return null;
	  }

	  
	  void startListenForUDPBroadcast() {
			UDPBroadcastThread = new Thread(new Runnable() {
				public void run() {
					try {
						Log.d("UDP", "Inside try");
						InetAddress broadcastIP = InetAddress.getByName("127.0.0.1"); //172.16.238.42 //192.168.1.255
						Integer port = CommonUtilities.SERVERPORT;
						while (true) {
							Log.d("UDP", "Inside while");
							listenAndWaitAndThrowIntent(broadcastIP, port);
						}
						//if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
					} catch (Exception e) {
						Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
						//Toast.makeText(con_serv, "Listening service has stopped, please restart the app",Toast.LENGTH_SHORT ).show();

						e.printStackTrace();
					}
				}
			});
			UDPBroadcastThread.start();
		}
	  
		public void alertUser(String name)
		{
			Log.d("Wanna receive File", name);
			   Intent intent = new Intent(this.getApplicationContext(), DialogActivity.class); 
			    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(intent);
			    
			    while(DialogActivity.accept==0)
			    {
			    	
			    }
			    if(DialogActivity.accept==1)
			    {
			    	acceptFile(name);
			    }
		}
			
		
		  public void acceptFile(String name)
		  {
		    	
		    	int filesize=6022386; // filesize temporary hardcoded
		    	
			    long start = System.currentTimeMillis();
			    int bytesRead;
			    int current = 0;
		    	
			    System.out.println("Connecting...");
			    try
			    {
			    	
			    	Socket sock = new Socket(senderIP,13267);
			     
			    	Log.d("Storing File", "ACCEpting");
			     File folder = new File(Environment.getExternalStorageDirectory() + "/SmartClass");
			     boolean success = true;
			     if (!folder.exists()) {
			         success = folder.mkdir();
			     }
			     
			     if (success) {
			    	 byte [] mybytearray  = new byte [filesize];
			    	 
					    InputStream is = sock.getInputStream();
					    System.out.print(is.available());
					    FileOutputStream fos = new FileOutputStream("/sdcard/SmartClass/"+name);
					    
					    BufferedOutputStream bos = new BufferedOutputStream(fos);
					    
					    bytesRead = is.read(mybytearray,0,mybytearray.length);
					    System.out.println(bytesRead);
					    current = bytesRead;
					    Log.d("Received bytes",""+current);
					    
					   
					    do {
					    	System.out.println("sdf "+current);
					       bytesRead =is.read(mybytearray, current, (mybytearray.length-current));
					       System.out.println("current " +current);
					       System.out.println("bytesread"+current);
					       if(bytesRead >= 0) 
					    	   current += bytesRead;
					       
					    } while(bytesRead > -1);

					    bos.write(mybytearray, 0 , current);
					    bos.flush();
					    long end = System.currentTimeMillis();
					    System.out.println(end-start);
					    bos.close();
					    sock.close();
					    fos.close();
			    	 
			     
			    // receive file
			    }
			   
			    }
			    catch(Exception e)
			    {
			    	
			    	e.printStackTrace();
			    }
			  
		  }
	  
	  private void listenAndWaitAndThrowIntent(InetAddress broadcastIP, Integer port) throws Exception {
			byte[] recvBuf = new byte[100];
			if (socket == null || socket.isClosed()) {
				Log.d("UDP", "Inside if");
				//socket.setReuseAddress(true);
				socket = new DatagramSocket(port);
				socket.setReuseAddress(true);
				Log.d("UDP", "Socket port and address set");
				socket.setBroadcast(true);
			}
			
			DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
			Log.e("UDP", "Waiting for UDP broadcast"+"message received"+String.valueOf(CommonUtilities.message_received));
			
			WifiManager mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiManager.MulticastLock multicastLock = mWifiManager.createMulticastLock("net.inside.broadcast");
			multicastLock.setReferenceCounted(true);
			multicastLock.acquire();
	        socket.receive(packet);
	        CommonUtilities.message_received=true;
			Log.e("UDP", "message received"+String.valueOf(CommonUtilities.message_received));
			

	       
			senderIPA=packet.getAddress();
			 senderIP = senderIPA.getHostAddress();
			//socket.close();
			boolean systemMessage=false;
			final String message = new String(packet.getData());
			
			
			Log.d("UDP", "Got UDB broadcast from " + senderIP + ", message: " + message);
			//sendMessageToUI(1);
			
			if((message.startsWith("-ping/"))&&(CommonUtilities.mode==1)&&!message.contains(":"))
			{
				CommonUtilities.message="-ping/:"+CommonUtilities.nickname+";"+MainActivity.sIP+"#";
				Log.d("sssssssssssssssssssssssssssssss", CommonUtilities.nickname);
				ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE); 
		        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 

				
				do
				  {
		            if (mWifi.isConnected()) { 
		            //start=true;     //Starts client thread to send through wifi 
		            } 

		            
		            //Log.d("checkchange set to true", String.valueOf(start)+String.valueOf(message));
						if (mWifi.isConnected()) { 
			                new Thread(new PingClient()).start(); 
			                } 
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					  //start=false;
				  }while(!CommonUtilities.message_received);

			}
			
			else if(message.startsWith("-ping/:")&&(CommonUtilities.mode==0)) // This is a response from the student in order to be registered for PM
				{
					PMActivity.namesArrayList.add(message.substring(message.indexOf(":")+1,message.indexOf(";")));  //Adds name
					Log.d("UDP2", "Got UDB broadcast from " + senderIP + ", message: " + message);
					Log.d("Address", message.substring(message.indexOf(";")+1,message.indexOf("#")));
					PMActivity.addrArrayList.add(message.substring(message.indexOf(";")+1,message.indexOf("#")));  //Adds name
					Log.d("Address", message.substring(message.indexOf(";")+1,message.indexOf("#")));
				}
			
			else if(message.startsWith("-teacher/")&&(CommonUtilities.mode==0)&&!message.contains(":")) // This is a request from each student to which available teacher responds
			{
				CommonUtilities.message="-teacher/:"+CommonUtilities.nickname+";"+MainActivity.sIP+"#";
				Log.d("sssssssssssssssssssssssssssssss", CommonUtilities.nickname);
				ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE); 
		        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 

				
				do
				  {
		            if (mWifi.isConnected()) { 
		            //start=true;     //Starts client thread to send through wifi 
		            } 

		            
		            //Log.d("checkchange set to true", String.valueOf(start)+String.valueOf(message));
						if (mWifi.isConnected()) { 
			                new Thread(new PingClient()).start(); 
			                } 
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					  //start=false;
				  }while(!CommonUtilities.message_received);
			}
			
			else if(message.startsWith("-teacher/:")&&(CommonUtilities.mode==1)) // This is a response from the teacher in order to be listed on student device
			{
				TeacherListActivity.teachersnmArrayList.add(message.substring(message.indexOf(":")+1,message.indexOf(";")));  //Adds name 
				TeacherListActivity.teachersadArrayList.add(message.substring(message.indexOf(";")+1,message.indexOf("#")));  //Adds address
				Log.d("Address", message.substring(message.indexOf(";")+1,message.indexOf("#")));
			}

			else if(message.startsWith("-PM/"))
			{
				//mode 0 coz all private messages are received by teacher
				//message format is "-PM/:" + address of person to be sent + "#" + "name of sender" + "/" + message
				
				// first case is teacher to student, therefore :to# should be deviceIP
				//second case is student to teacher, therefore senderIP should be deviceIP
					if((CommonUtilities.mode==0)||(message.substring(message.indexOf(":")+1,message.indexOf("#")).equals(MainActivity.sIP))||(senderIP.equals(MainActivity.sIP))){
					Log.d("UDP", "Calling ChatActivity function private");
					final String uname;
				    uname=message.substring(message.indexOf("#")+1,message.indexOf("/",5));
				    final String messdata= message.substring(message.indexOf("/",5)+1);
				    Date dNow = new Date( );
				    final String date=""+dNow.getTime();
				    final String recipient;
     		    	recipient=message.substring(message.indexOf(":")+1,message.indexOf("#"));
     		    	actualMessage= messdata;
				    
					DataSource ds1 = new DataSource(this);
					if(messdata.equals(ds1.getLastMessage(recipient))&&((Long.parseLong(date)-Long.parseLong(ds1.getLastDate(recipient)))<=1000))
					{
						
					}
					else
					{
	 	     		  ds1.open();
			          ds1.add(uname,messdata,date,recipient);
			          ds1.close();
			          if((CommonUtilities.recipient.equals(senderIP))||(senderIP.equals(MainActivity.sIP)))
			        	  ChatActivity.updateList(message.substring(message.indexOf("#")+1));
					
					if(message.substring(message.indexOf(":")+1,message.indexOf("#")).equals(MainActivity.sIP))
					{
						Log.d("params of notif", message.substring(message.indexOf("#")+1, message.indexOf("/", 5))+" "+senderIP);
						createNotification(message.substring(message.indexOf("#")+1, message.indexOf("/", 5)),senderIP);
						
					}
					
					}
				}
					
					
			}
			
			else if ((!message.startsWith("-board/"))&&(!message.startsWith("-file/"))&&!message.startsWith("-ping/")&&!message.startsWith("-PM/")&&!message.startsWith("-teacher/"))
			{
				Log.d("UDP", "Calling ChatActivity function");
				final String uname;
			    uname=message.substring(0,message.indexOf("/"));
			    final String messdata= message.substring(message.indexOf("/")+1);
			    Date dNow = new Date( );
			    final String date=""+dNow.getTime();
			    final String recipient="public";
			    
				DataSource ds1 = new DataSource(this);
				if(messdata.equals(ds1.getLastMessage(recipient))&&((Long.parseLong(date)-Long.parseLong(ds1.getLastDate(recipient)))<=1000))
				{
					
				}
				else
				{
 	     		  ds1.open();
		          ds1.add(uname,messdata,date,recipient);
		          ds1.close();
		          if(CommonUtilities.recipient.equals("public"))
		        	  ChatActivity.updateList(message);

				}
			}
			else if((!MainActivity.sIP.equals(senderIP))&&(message.contains("board")))
			{
				Log.d("UDP", "Calling BoardReceiverActivity function");
				char c=message.charAt(7);
				char color=message.charAt(8);
				int width=Integer.parseInt(message.substring(9, message.indexOf("?")));
				BA2.draw(c,color,width,message.substring(message.indexOf("?")+1, message.indexOf(" ")),message.substring(message.indexOf(" ")+1));
			}
			else if(message.startsWith("-file/")&&(!MainActivity.sIP.equals(senderIP)))
			{
				Log.d("FILE", "receiving file");
				alertUser(message.substring(6, message.indexOf("/",7)));
				 DialogActivity.accept=0;//Again initialise to 0 for future use;
				
			}
			
	        multicastLock.release();
	  }
	  DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	accept=1;
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            accept=0;
		            break;
		        }
		    }
		};

		
	  @Override
	    public void onDestroy() {
	        super.onDestroy();
	        
	        Log.i("UDPService", "Service Stopped.");
	        isRunning = false;
	    }
	  	
		void stopListen() {
			WAKELOCK.release();
			shouldRestartSocketListen = false;
			socket.close();
		}
		
		public void createNotification(String name, String addr) {
		    // Prepare intent which is triggered if the
		    // notification is selected
		    Intent intent = new Intent(this, ChatActivity.class);
		    intent.putExtra("PM user name",name );
		    intent.putExtra("PM user addr",addr );
		    intent.putExtra("is_noti", true);
		    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		    
		    String notifrom="Teacher";
		    if(CommonUtilities.mode==0)
		    	notifrom=name;

		    // Build notification
		    // Actions are just fake
		    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
	        .setContentTitle("Message from "+notifrom)
	        .setContentText(actualMessage).setSmallIcon(R.drawable.ic_launcher)
	        .setContentIntent(pIntent);
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    // Hide the notification after its selected

		    notificationManager.notify(0, mBuilder.build());

		  }
} 