package org.aakashlabs.smartclass;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.widget.TextView;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		 Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/eraserdust.ttf");
	        TextView tv1 = (TextView) findViewById(R.id.textView1);
	        TextView tv2 = (TextView) findViewById(R.id.textView2);
	        TextView tv3 = (TextView) findViewById(R.id.textView3);
	        TextView tv4 = (TextView) findViewById(R.id.textView4);
	        TextView tv5 = (TextView) findViewById(R.id.textView5);
	        TextView tv6 = (TextView) findViewById(R.id.textView6);
	        TextView tv7 = (TextView) findViewById(R.id.textView7);
	        TextView tv8 = (TextView) findViewById(R.id.textView8);
	        TextView tv9 = (TextView) findViewById(R.id.textView9);
	        TextView tv10 = (TextView) findViewById(R.id.textView10);
	        TextView tv11 = (TextView) findViewById(R.id.textView11);
	        TextView tv12 = (TextView) findViewById(R.id.textView12);
	        TextView tv13 = (TextView) findViewById(R.id.textView13);
	        TextView tv14= (TextView) findViewById(R.id.textView14);
	        TextView tv15= (TextView) findViewById(R.id.textView15);
	        TextView tv16= (TextView) findViewById(R.id.textView16);
	        TextView tv17= (TextView) findViewById(R.id.textView17);
	        TextView tv18= (TextView) findViewById(R.id.textView18);
	        TextView tv19= (TextView) findViewById(R.id.textView19);
	        TextView tv20= (TextView) findViewById(R.id.textView20);
	        TextView tv21= (TextView) findViewById(R.id.textView21);
	        TextView tv22= (TextView) findViewById(R.id.textView22);
	        TextView tv23= (TextView) findViewById(R.id.textView23);
	        TextView tv24= (TextView) findViewById(R.id.textView24);
	        TextView tv25= (TextView) findViewById(R.id.textView25);
	        
	        tv1.setTypeface(tf);tv2.setTypeface(tf);tv3.setTypeface(tf);tv4.setTypeface(tf);tv5.setTypeface(tf);tv6.setTypeface(tf);
	        tv7.setTypeface(tf);
	        tv11.setTypeface(tf);tv12.setTypeface(tf);tv13.setTypeface(tf);tv14.setTypeface(tf);tv15.setTypeface(tf);tv16.setTypeface(tf);
	        tv17.setTypeface(tf);
	        tv8.setTypeface(tf);tv9.setTypeface(tf);tv10.setTypeface(tf);tv18.setTypeface(tf);tv19.setTypeface(tf);tv20.setTypeface(tf);
	        tv21.setTypeface(tf);tv22.setTypeface(tf);tv23.setTypeface(tf);tv24.setTypeface(tf);tv25.setTypeface(tf);
	        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

}
