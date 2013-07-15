package org.aakashlabs.smartclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class DrawView extends View implements OnTouchListener {
	
	static boolean board_start=false;
	
    private Canvas  mCanvas;
    private Path    mPath;
    static float x,y;
    static float dpx,dpy;
    static char identify='s';
    private Paint mPaint;   
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>(); 
    private Map<Path, Integer> colorsMap = new HashMap<Path, Integer>();
    private Map<Path, Integer> widthMap = new HashMap<Path, Integer>();
    private Bitmap im;
    public DrawView(Context context) 
    {
    	
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);      
        this.setOnTouchListener(this);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
        mCanvas = new Canvas();
        mPath = new Path();

        im=BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);


    }               
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            
            for (Path p : paths){
            	
            	 mPaint.setColor(colorsMap.get(p));
            	 mPaint.setStrokeWidth(widthMap.get(p));
            	 
                canvas.drawPath(p, mPaint);
                
            }
            mPaint.setColor(CommonUtilities.selectedcolour);
            mPaint.setStrokeWidth(CommonUtilities.widthvalue);
            canvas.drawPath(mPath, mPaint);
            
            
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            undonePaths.clear();
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
              mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
        	
                mX = x;
                mY = y;
           }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            paths.add(mPath);
            colorsMap.put(mPath,CommonUtilities.selectedcolour);
            widthMap.put(mPath,CommonUtilities.widthvalue);
            mPath = new Path();            

        }

        public void onClickUndo () { 
        	identify='n';
            if (paths.size()>0) 
            { 
               undonePaths.add(paths.remove(paths.size()-1));
               invalidate();
             }
            else
            {
            	//Toast.makeText(con, "All undone", Toast.LENGTH_SHORT).show();
            }
            board_start=true;
            
            new Thread(new BoardClient()).start();
        }

        public void onClickRedo (){
        	identify='r';
           if (undonePaths.size()>0) 
           { 
               paths.add(undonePaths.remove(undonePaths.size()-1)); 
               invalidate();
           } 
           else 
           {

           }
             //toast the user 
           board_start=true;
           
           new Thread(new BoardClient()).start();
        }
        
      

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
         x = event.getX();
         y = event.getY();
         
         Resources resources = ((Activity) getContext()).getResources();
         DisplayMetrics metrics = resources.getDisplayMetrics();
         dpx = x / (metrics.densityDpi / 160f);
         dpy = y / (metrics.densityDpi / 160f);
          
          switch (event.getAction()) {
              case MotionEvent.ACTION_DOWN:
                  touch_start(x, y);
                  identify='d';
                  invalidate();
                  break;
              case MotionEvent.ACTION_MOVE:
                  touch_move(x, y);
                  identify='m';
                  invalidate();
                  break;
              case MotionEvent.ACTION_UP:
                  touch_up();
                  identify='u';
                  invalidate();
                  break;
          }
          
          board_start=true;
          
          new Thread(new BoardClient()).start();
          return true;
    }
   
}