package org.aakashlabs.smartclass;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.Intent;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileChooser extends ListActivity {
	static final int PORT = 13267;
    private File currentDir;
    private File parentDir;
    private FileArrayAdapter adapter;
    byte[] bfile;
    static String path;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File("/sdcard/");
        fill(currentDir);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
    }
    private void fill(File f)
    {
    	File[]dirs = f.listFiles();
		 this.setTitle("Current Dir: "+f.getName());
		 currentDir = f.getAbsoluteFile();
		 //parentDir = new File(f.getAbsoluteFile().getParentFile().getName());
		 List<Option>dir = new ArrayList<Option>();
		 List<Option>fls = new ArrayList<Option>();
		 try{
			 for(File ff: dirs) 
			 {
				if(ff.isDirectory())
					dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
				else
				{
					fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
				}
			 }
		 }catch(Exception e)
		 {
			 
		 }
		 Collections.sort(dir);
		 Collections.sort(fls);
		 dir.addAll(fls);
		 
		 if(!f.getName().equalsIgnoreCase("sdcard"))
		 {
			 dir.add(0,new Option("..","Parent Directory",f.getParent()));
			 parentDir = new File(f.getParent());
		 }
		 adapter = new FileArrayAdapter(FileChooser.this,R.layout.file_view,dir);
		 this.setListAdapter(adapter);
    }
    
    public void onBackPressed() {
		if(!currentDir.getName().equals("sdcard"))
		{	Log.d("test",currentDir.getName() );
			fill(parentDir);	
		}	
		else
		{
			Log.d("test", "Ghusla");
			Intent intent = new Intent(this, FileMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
		}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
                case android.R.id.home:
                    // app icon in action bar clicked; go home
                    Intent intent = new Intent(this, FileMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
				currentDir = new File(o.getPath());
				fill(currentDir);
		}
		else
		{
			
			onFileClick(o);
			
		}
	}
    private void onFileClick(Option o)
    {
    	Toast.makeText(this, "File Sent: "+o.getName(), Toast.LENGTH_SHORT).show();
    	//bfile=converttobytes(o.getPath());
    	//sendFile(o.getPath());
    	File f=new File(o.getPath());
    	if(f.length()<5000000)
    	{
    	//new Filesend().execute(o.getPath());
    		
    		path=o.getPath();
    		new Thread(new FileClient(o.getName())).start();
    		//new KKMultiServer();
    		new Filesend().execute(o.getPath());
        }
         
         
    	else
    		Toast.makeText(this, "Size Exceeds 5 MB", Toast.LENGTH_SHORT).show();
    		
    }
    public void toast(String text)
    {
    	Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    	public class FileClient implements Runnable {
    		String name;
    	FileClient(String name)
    	{
    		this.name=name;
    	}
    		
    		@Override
    		public void run() {
    	try {
			
			DatagramSocket socket = new DatagramSocket();
			String head="-file/"+name+"/";
			Log.d("fileheader", head);
			byte[] buf=head.getBytes();
			//buf = new byte[header.length + bfile.length];
			
			//System.arraycopy(header,0,buf,0,header.length);
			//System.arraycopy(bfile,0,buf,header.length,bfile.length);

			 
			DatagramPacket packet=null;
			
			do{
				packet = new DatagramPacket(buf, buf.length, MainActivity.getBroadcastAddress(), CommonUtilities.SERVERPORT);
				String data=new String(packet.getData());
				Log.d("FILE", data);
				socket.send(packet);
				Log.d("subnet scan", "Trying: send"+ new String(buf));
			}while(packet==null);
			Log.d("Send message client","Client: Message sent\n");
			Log.d("Send message client","Client: Succeed!\n");
		} catch (Exception e) {
			Log.d("Send message client","Client: Error!\n"+e.getMessage());
			e.printStackTrace();
		}
	}
    	}
    	
     
}


class Filesend extends AsyncTask<String, Void, String> {

    

    protected String doInBackground(String... urls) {
    	//while(true)
    	/*{
            ServerSocket welcomeSocket = null;
            Socket connectionSocket = null;
            BufferedOutputStream outToClient = null;

            try {
                welcomeSocket = new ServerSocket(13267);
                connectionSocket = welcomeSocket.accept();
                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
            } catch (Exception ex) {
                // Do exception handling
            }

            if (outToClient != null) {
                File myFile = new File( urls[0] );
                byte[] mybytearray = new byte[(int) myFile.length()];
                
                FileInputStream fis = null;

                try {
                    fis = new FileInputStream(myFile);
                } catch (Exception ex) {
                	FileChooser fc=new FileChooser();
                	fc.toast("Please Restart the Application");
                }
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {
                    bis.read(mybytearray, 0, mybytearray.length);
                    outToClient.write(mybytearray, 0, mybytearray.length);
                    outToClient.flush();
                    outToClient.close();
                    connectionSocket.close();
                    welcomeSocket.close();
                
                    fis.close();
                    // File sent, exit the main method
                    
                } catch (Exception ex) {
                	FileChooser fc=new FileChooser();
                	fc.toast("Please Restart the Application");
                }
            }
        }*/
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
		return null;
    }

    @Override
    protected void onPostExecute(String result) {
    	
    }


}