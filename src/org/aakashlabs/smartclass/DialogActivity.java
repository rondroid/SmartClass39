package org.aakashlabs.smartclass;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class DialogActivity extends Activity {
static int accept=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setMessage("Want to Receive File?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
		//if(accept==1)
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog, menu);
		return true;
	}
	 DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	accept=1;
		        	
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            accept=2;
		            break;
		        
		        }
		        finish();
		        
		    }
		};


	
}
