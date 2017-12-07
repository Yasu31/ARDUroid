/**
 * 2014-08-01 K.OHWADA
 */ 

package com.example.yasunori.myapplication;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.support.annotation.InterpolatorRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasunori.myapplication.usbcdc.UsbCdcManager;

import java.util.List;
import java.util.Random;

import com.example.yasunori.myapplication.usbcdc.UsbCdcManager;

/**
 * CommonActivity
 */     
public class CommonActivity extends AppCompatActivity {

	// debug
	protected final static boolean D = true;
	private final static String TAG = "UsbSerial";
	
	// serial
	private final static int BAUDRATE = 9600;

	// recieve
	private final static int MAX_STRING_LENGTH = 256;
	private final static int MAX_STRING_WAIT = 100;	

	// char
	protected final static String SPACE = " ";
	private final static String LF = "\n";
				
	// object
	private UsbCdcManager mUsbCdcManager;

	// UI 
	private TextView mTextViewConnect;
	public float origLX,origLY,origRX,origRY;
	private int count=1;
	boolean obstacle=false;
	boolean roam=false;
	int speed=0;int turn=0;

	/**
	 * initManager
	 */  
	protected void initManager() {                
		mUsbCdcManager = new UsbCdcManager( this );
		mUsbCdcManager.setBaudrate( BAUDRATE );
		mUsbCdcManager.setOnChangedListener( new UsbCdcManager.OnChangedListener() {
			@Override
			public void onAttached( UsbDevice device ) {
				execAttached( device );
			}
			@Override
			public void onDetached( UsbDevice device ) {
				execDetached( device );
			}
		});
		mUsbCdcManager.setOnRecieveListener( new UsbCdcManager.OnRecieveListener() {
			@Override
			public void onRecieve( byte[] bytes ) {
				execRecieve( bytes ); 
			}
		});
	}

	/**
	 * initView
	 */     
	protected void initView() {
		//mTextViewConnect = (TextView) findViewById( R.id.TextView_connect );
	}

	/**
	 * === onResume ===
	 */
	@Override
	public void onResume() {
		super.onResume();
		mUsbCdcManager.open();
		UsbDevice device = mUsbCdcManager.findDevice();
		if ( device != null ) {
			String name = device.getDeviceName();
			showConnected( name );
			String msg = getString( R.string.msg_found ) + SPACE + name;
			toast_show( msg );
		} else {
			showNotConnect();
		}       
	}

	/**
	 * === onDestroy ===
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		mUsbCdcManager.close();
	}

	protected void sendLF( String str) {
		send(str + LF);
	}

	protected void send( String str ) {
		mUsbCdcManager.sendBulkTransfer(str.getBytes());
	}

	protected void execRecieve( byte[] bytes ) {
		// dummy
	}

	protected boolean setBaudrate( int baudrate ) {
		return mUsbCdcManager.setBaudrate( baudrate );
	}

	protected String bytesToString( byte[] bytes ) {
		return mUsbCdcManager.bytesToString(bytes);
	}


	protected List<String> getListString( String str ) {
		// arduino send with CR and LF
		return mUsbCdcManager.getListString(
				str, LF, MAX_STRING_LENGTH, MAX_STRING_WAIT);
	}

	protected void procRecieve( String str ) {
		// dummy
	}

	private void execAttached( UsbDevice device ) {
		String name = device.getDeviceName();
		showConnected( name );
		String msg = getString( R.string.msg_attached ) + SPACE + name;
		toast_show( msg );
	}

	private void execDetached( UsbDevice device ) {
		showNotConnect();
		String msg = getString( R.string.msg_detached ) + SPACE + device.getDeviceName();
		toast_show( msg );  
	}

	/**
	 * showNotConnect
	 */
	private void showNotConnect() {
		//mTextViewConnect.setText(R.string.msg_not_connect );
	}

	/**
	 * showNotConnect
	 */      
	private void showConnected( String name ) {
		String msg = getString( R.string.msg_connected ) + SPACE +  name;
		//mTextViewConnect.setText( msg );
	}

	protected  void toast_show( int res_id ) {
		ToastMaster.showText(this, res_id, Toast.LENGTH_SHORT);
	}


	protected void toast_show( String msg ) {
		ToastMaster.showText( this, msg, Toast.LENGTH_SHORT );
	}

	protected void log_d( String msg ) {
		Log.d(TAG, msg);
	}

	public void initPupils(){
		View pupilL=findViewById(R.id.pupilL);
		View pupilR=findViewById(R.id.pupilR);
		origLX=pupilL.getX();
		origLY=pupilL.getY();
		origRX=pupilR.getX();
		origRY=pupilR.getY();
	}

	public void movePupils(float x,float y){
		View pupilL=findViewById(R.id.pupilL);
		View pupilR=findViewById(R.id.pupilR);
		float LX,LY;
		LX=pupilL.getX();
		LY=pupilL.getY();
		TranslateAnimation ta=new TranslateAnimation(LX-origLX-x,0,LY-origLY-y,0);
		ta.setDuration(1000);
		pupilL.setX(origLX+x);
		pupilL.setY(origLY+y);
		pupilR.setX(origRX+x);
		pupilR.setY(origRY+y);
		pupilL.startAnimation(ta);
		pupilR.startAnimation(ta);

	}

	public void blinkEyes(){
		//AnimationSet as=new AnimationSet(true);
		//as.setInterpolator(new DecelerateInterpolator());
		final View eyelids=findViewById(R.id.eyelids);
		WeightAnimation close=new WeightAnimation(6,0,eyelids);
		close.setDuration(200);
		final Animation open=new WeightAnimation(0,6,eyelids);
		open.setDuration(200);
		open.setStartOffset(close.getDuration());
		close.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation close) {
				eyelids.startAnimation(open);

			}
		});
		eyelids.startAnimation(close);
	}

	public void send() {
		String str;
		if (count % 2 == 0) {
			str = "1a";
		} else {
			str = "2a";
		}
		count++;
		send(str);
		//Toast.makeText(MainActivity.this, "sent?", Toast.LENGTH_SHORT).show();
		movePupils(20, 50);
		blinkEyes();

	}

	public void go(int speed,int turn){ //100 is max speed
		speed=speed*(255-100)/100;
		if(speed>0){
			speed+=100;
		}
		else if(speed<0) {
			speed-=100;
		}
		else{
			speed=0;
		}
		int right,left;
		if(turn>0){
			left=speed;
			right=speed-speed*turn/100;
		}
		else {
			right=speed;
			left=speed-speed*(-turn)/100;
		}
		String str;
		str=String.valueOf(right)+"r"+String.valueOf(left)+"l";
		send(str);
	}

	public void roamAround(){
		final Handler handler=new Handler();
		final Random rand=new Random();
		Runnable r=new Runnable() {
			@Override
			public void run() {
				int speed=rand.nextInt(130)-30;
				int turn=rand.nextInt(200)-100;
				go(speed,turn);

				if(!roam){
					speed=0;turn=0;go(speed,turn);return;
				}
				if(obstacle){
					go(-100,0);
					Handler turnaround =new Handler();
					Runnable l=new Runnable() {
						@Override
						public void run() {
							go(80,100);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							go(0,0);
						}
					};
					turnaround.postDelayed(l,2000);
				}
				handler.postDelayed(this,rand.nextInt(3000)+1000);
			}
		};
		handler.post(r);
	}

                
}
