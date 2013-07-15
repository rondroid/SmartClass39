package org.aakashlabs.smartclass;
import java.net.*;
import java.io.*;

public class MultiServerThread extends Thread {
    private Socket socket = null;
    
    public MultiServerThread(Socket socket) {
	super("MultiServerThread");
	this.socket = socket;
	
    }

    public void run() {

	try {
		 BufferedOutputStream outToClient = null;
	    //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	    //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    outToClient = new BufferedOutputStream(socket.getOutputStream());
	    if (outToClient != null) {
            File myFile = new File(FileChooser.path);
            byte[] mybytearray = new byte[(int) myFile.length()];
            
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(myFile);
            } catch (Exception ex) {
            	
            }
            BufferedInputStream bis = new BufferedInputStream(fis);

	    //KnockKnockProtocol kkp = new KnockKnockProtocol();
	    //outputLine = kkp.processInput(null);
	   // out.println(outputLine);

//	    while ((inputLine = in.readLine()) != null) {
		//outputLine = kkp.processInput(inputLine);
		//out.println(outputLine);
	//	if (outputLine.equals("Bye"))
	//	    break;
	  //  }
            try {
                bis.read(mybytearray, 0, mybytearray.length);
                outToClient.write(mybytearray, 0, mybytearray.length);
                outToClient.flush();
                outToClient.close();
                socket.close();
                //welcomeSocket.close();
            
                fis.close();
                // File sent, exit the main method
                
            } catch (Exception ex) {
            	//FileChooser fc=new FileChooser();
            	//fc.toast("Please Restart the Application");
            }
	    //out.close();
	    //in.close();
	    socket.close();

	}
	}catch (IOException e) {
	    e.printStackTrace();
	}
    
}
}