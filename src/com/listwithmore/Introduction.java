package com.listwithmore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class Introduction extends Activity{
	private static final int SWIPE_MIN_DISTANCE = 70;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;        
    private AnimationListener mAnimationListener;
    private Context mContext;
    int i=1;
	SharedPreferences prefs= MyApplicationContext.getAppContext().getSharedPreferences(Wizard.PREFCOUNTS, 0);
	SharedPreferences.Editor prefEditor = prefs.edit();
    
	
    
    @SuppressWarnings("deprecation")
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirst= prefs.getBoolean("isFirst", true);
        if (!isFirst) {
        	Intent intent= new Intent(Introduction.this, MainPage.class);
			startActivity(intent);
			finish();
		}
        else{
        	setContentView(R.layout.viewflipper);
            mContext = this;
            mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);
            mViewFlipper.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(final View view, final MotionEvent event) {
                            detector.onTouchEvent(event);
                            return true;
                    }
            });
            
            
 
            //animation listener
            mAnimationListener = new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                            //animation started event
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                            //TODO animation stopped event
                    }
            };
        }
            
    }


    class SwipeGestureDetector extends SimpleOnGestureListener {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    try {
                            // right to left swipe
                    	
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            	 if (i>=9) {
									Intent intent= new Intent(Introduction.this, MainPage.class);
									startActivity(intent);
									finish();
									prefEditor.putBoolean("isFirst", false);
//									prefEditor.putBoolean("isFirst", true);
									
									prefEditor.commit();
								}
                            	
                            	mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                                // controlling animation
                                mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                                mViewFlipper.showNext();                              
                                i++;
                                return true;
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
                                // controlling animation
                                mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                                mViewFlipper.showPrevious();
                                i--;
                                return true;
                            }
                            
                    } catch (Exception e) {
                            e.printStackTrace();
                    }

                    return false;
            }
    }
    
    @Override
    public void onBackPressed() {
    	prefEditor.putBoolean("isFirst", false);
    	prefEditor.commit();
    	super.onBackPressed();
    }
}
