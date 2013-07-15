package org.aakashlabs.smartclass;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.util.Log;

public class Client implements Runnable {
	@Override
	public void run() {
		
		//Log.d("checkchange client", String.valueOf(start)+String.valueOf(box));
		/*while(ChatActivity.start==false)
		{
			//Log.d("checkchange","inside client while");

		}*/
		
		Log.d("Send message client",ChatActivity.message);

		try {
			//InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			//updatetrack("Client: Start connecting\n");
			DatagramSocket socket = new DatagramSocket();
			byte[] buf;


			if(CommonUtilities.recipient=="public") // Used only for public chat
				buf=(CommonUtilities.nickname+"/"+ChatActivity.message).getBytes();
			else // Used in case of private chat, once setting up of Window is done
				buf=("-PM/:"+CommonUtilities.recipient+"#"+CommonUtilities.nickname+"/"+ChatActivity.message).getBytes();;

			DatagramPacket packet=null;
			
			do{
				packet = new DatagramPacket(buf, buf.length, ChatActivity.getBroadcastAddress(), CommonUtilities.SERVERPORT);
				
				socket.send(packet);
				Log.d("subnet scan", "Trying: send"+ new String(buf));
			}while(packet==null);
			Log.d("Send message client","Client: Message sent\n");
			Log.d("Send message client","Client: Succeed!\n");
			//ChatActivity.start=false;
		} catch (Exception e) {
			Log.d("Send message client","Client: Error!\n"+e.getMessage());
			e.printStackTrace();
		}
	}
}

//Message format in public is "nickname/message"
//For board messages, the string starts with this identifier "-board/" 
//For private message, if teacher starts, PingClient starts and sends only "ping"
//In response, devices other than teacher send "ping:nickname/sIP#" sIP is now the IP of that device
//the listview is created and the teacher clicks one of them
//for future messages, each message begins with "PM" and are sent through Client.java