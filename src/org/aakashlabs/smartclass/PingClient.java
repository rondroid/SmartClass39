package org.aakashlabs.smartclass;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.util.Log;

public class PingClient implements Runnable {
	@Override
	public void run() {
		
		//Log.d("checkchange client", String.valueOf(start)+String.valueOf(box));
		//while(ChatActivity.start==false)
		//{
			//Log.d("checkchange","inside client while");

		//}
		
		//Log.d("Send message client",ChatActivity.message);

		try {
			//InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			//updatetrack("Client: Start connecting\n");
			DatagramSocket socket = new DatagramSocket();
			byte[] buf;


			buf=(CommonUtilities.message).getBytes();

			DatagramPacket packet=null;
			
			do{
				packet = new DatagramPacket(buf, buf.length, CommonUtilities.broadcastaddr, CommonUtilities.SERVERPORT);
				
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
