package org.aakashlabs.smartclass;
import java.net.*;
import java.io.*;

public class MultiServer {
	MultiServer()
	{
		ServerSocket serverSocket = null;
        boolean listening = true;

        try {
        	System.out.print("Reached till here");
            serverSocket = new ServerSocket(13267);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 13267.");
            System.exit(-1);
        }

        while (listening)
			try {
				new MultiServerThread(serverSocket.accept()).start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
    public static void main(String[] args) throws IOException {
        }
}