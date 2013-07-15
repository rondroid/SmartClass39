package org.aakashlabs.smartclass;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import android.graphics.Color;
import android.util.Log;

public class BoardClient implements Runnable {
	@Override
	public void run() {
		
		//Log.d("checkchange client", String.valueOf(start)+String.valueOf(box));
		while(DrawView.board_start==false)
		{
			//Log.d("checkchange","inside client while");

		}
		
		//Log.d("Send message client",ChatActivity.message);

		try {
			//InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			//updatetrack("Client: Start connecting\n");
			DatagramSocket socket = new DatagramSocket();
			byte[] buf;
			char color='b';
			switch(CommonUtilities.selectedcolour)
			{
			case Color.BLACK:
				color='b';
				break;
			case Color.RED:
				color='r';
				break;
			case Color.BLUE:
				color='u';
				break;
			case Color.GREEN:
				color='g';
				break;
			case Color.WHITE:
				color='w';
				break;
			}
			int width=CommonUtilities.widthvalue;
			buf=("-board/"+DrawView.identify+color+width+"?"+DrawView.dpx+" "+DrawView.dpy).getBytes();

			DatagramPacket packet=null;
			
			do{
				packet = new DatagramPacket(buf, buf.length, BoardActivity.getBroadcastAddress(), CommonUtilities.SERVERPORT);
				
				socket.send(packet);
				Log.d("subnet scan", "Trying: send"+ new String(buf));
			}while(packet==null);
			Log.d("Send message client","Client: Message sent\n");
			Log.d("Send message client","Client: Succeed!\n");
			DrawView.board_start=false;
		} catch (Exception e) {
			Log.d("Send message client","Client: Error!\n"+e.getMessage());
			e.printStackTrace();
		}
	}
}
