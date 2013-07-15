package org.aakashlabs.smartclass;



import java.io.IOException;
import java.net.InetAddress;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class BoardActivity extends Activity{
	 DrawView drawView ;
	 static Context context;
	 int[] arr={2,4,6,8,10,12,14};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonUtilities.selectedcolour=Color.BLACK;
		CommonUtilities.start_board=0;
		context=this;
		drawView = new DrawView(this);
		drawView.setBackgroundColor(Color.WHITE);
        setContentView(R.layout.activity_board);
        RelativeLayout rl=(RelativeLayout) findViewById(R.id.rl1);
        rl.addView(drawView);
        new Thread(new BoardClient()).start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.board, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		
	    switch (item.getItemId()) {
	    case R.id.undo:
	    	drawView.onClickUndo();
	    	
	    	break;
	    case R.id.redo:
	    	drawView.onClickRedo();
	    	
	    	break;
	    case R.id.width:
	    	
	    	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View layout = inflater.inflate(R.layout.seekbar, (ViewGroup) findViewById(R.id.seekll));
	        AlertDialog.Builder builder = new AlertDialog.Builder(this)
	        .setView(layout);
	        AlertDialog alertDialog = builder.create();
	        alertDialog.show();
	        SeekBar sb = (SeekBar)layout.findViewById(R.id.seek_bar);
	        sb.setProgress(CommonUtilities.widthvalue);
	        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
	            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
	            	
	            }

				@Override
				public void onStartTrackingTouch(SeekBar arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					Log.d("value",""+seekBar.getProgress());
					CommonUtilities.widthvalue=seekBar.getProgress();
										
					
				}
	        });
			Toast.makeText(this, "hmm", Toast.LENGTH_SHORT).show();
			break;
		
		case R.id.red:
			 CommonUtilities.selectedcolour= Color.RED;
			break;
		case R.id.black:
			 CommonUtilities.selectedcolour= Color.BLACK;
			break;
		case R.id.blue:
			 CommonUtilities.selectedcolour= Color.BLUE;
			break;
		case R.id.green:
			CommonUtilities.selectedcolour= Color.GREEN;
			break;
		case R.id.eraser:
			CommonUtilities.selectedcolour= Color.WHITE;
			break;
			
		case R.id.clear:
			finish();
			
			startActivity(getIntent());
			DrawView.identify='c';
			DrawView.board_start=true;
			new Thread(new BoardClient()).start();
		       
			break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	    return true;
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
