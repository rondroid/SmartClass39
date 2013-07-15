package org.aakashlabs.smartclass;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.RelativeLayout;



public class BA2 extends Activity {
	static Activity act;
	
	static DrawViewReceive drawView ;
	 static Context context;
	 static Resources r;
	 int[] arr={2,4,6,8,10,12,14};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act=this;
		r = getResources();
		context=this;
		 drawView = new DrawViewReceive(this);
		 drawView.setBackgroundColor(Color.WHITE);
       setContentView(R.layout.activity_board);
       RelativeLayout rl=(RelativeLayout) findViewById(R.id.rl1);
       rl.addView(drawView);
       //drawView.touch_start(0,0);
       

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ba2, menu);
		return true;
	}
	
	public static void clearAll()
	{
		act.finish();
		context.startActivity(new Intent(context, BA2.class));
		//startActivity(BA2.this);
	}
	public static Handler UIHandler;

	static 
	{
	    UIHandler = new Handler(Looper.getMainLooper());
	}
	
	public static void draw(final char c,final char color,int width,String x,String y)
	{
		if(context!=null)
		{
		Log.d("Draw", x);
		Log.d("Draw", y);
		final float xp=Float.parseFloat(x);
		final float yp=Float.parseFloat(y);
		CommonUtilities.widthvalue=width;
		Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    final float px = xp * (metrics.densityDpi / 160f);
	    final float py = yp * (metrics.densityDpi / 160f);


		UIHandler.post(new Runnable() {
	        @Override
	        public void run() {
	    		Log.d("Draw", "inside handler");
	    
		switch(color)
		{
		case 'b':
			CommonUtilities.selectedcolour=Color.BLACK;
			break;
		case 'r':
			CommonUtilities.selectedcolour=Color.RED;
			break;
		case 'g':
			CommonUtilities.selectedcolour=Color.GREEN;
			break;
		case 'u':
			CommonUtilities.selectedcolour=Color.BLUE;
			break;
		case 'w':
			CommonUtilities.selectedcolour=Color.WHITE;
			break;
		}
	    		
	    switch(c)
	    {
	    case 'd':
	    	drawView.touch_start(px, py);
	    	break;
	    case 'u':
	    	drawView.touch_up();
	    	break;
	    case 'm':
	    	drawView.touch_move(px, py);
	    	break;
	    case 'n':
	    	drawView.onClickUndo();
	    	break;
	    case 'r': 
	    	drawView.onClickRedo();
	    	break;
	    case 'c':
	    	clearAll();
	    	break;
	    }
		
	        }
	});

}
}
}