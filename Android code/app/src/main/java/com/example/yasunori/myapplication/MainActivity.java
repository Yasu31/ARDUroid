package com.example.yasunori.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;


import java.util.List;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends CommonActivity implements SensorEventListener{
    boolean blink=true;
    boolean naturallyMoveEye=true;
    Random random=new Random();
    FloatingActionButton fab;
    private SensorManager sensorManager;
    android.os.Handler naturalEye;
    Runnable naturaleye;
    android.os.Handler naturalBlink;
    Runnable naturalblink;
    int chin=130;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        setContentView(R.layout.activity_main);
        initManager();
        initView();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_menu_manage);
        EditText input=(EditText)findViewById(R.id.inputText);
        input.setOnKeyListener(listen);

        int uiOptions=View.SYSTEM_UI_FLAG_FULLSCREEN;       //code to hide status bar
        fab.setSystemUiVisibility(uiOptions);
        android.os.Handler hand=new android.os.Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                initPupils();
            }
        }, 100);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList=sensorManager.getSensorList(Sensor.TYPE_PROXIMITY);
        if(sensorList.size()>0){
            Sensor sensor=sensorList.get(0);
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
        }



        naturalBlink=new android.os.Handler();
        naturalblink=new Runnable() {
            @Override
            public void run() {
                if(blink){
                    blinkEyes();
                    naturalBlink.postDelayed(this, random.nextInt(5000)+5000);
                }

            }
        };
        naturalBlink.postDelayed(naturalblink, random.nextInt(5000)+5000);
        naturalEye=new android.os.Handler();
        naturaleye=new Runnable() {
            @Override
            public void run() {
                if(naturallyMoveEye){
                    movePupils(random.nextInt(200)-100,random.nextInt(140) - 70);
                    naturalEye.postDelayed(this,random.nextInt(3000)+1000);
                }

            }
        };
        naturalEye.postDelayed(naturaleye,random.nextInt(3000)+1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showPopup(View view){
        final PopupMenu options=new PopupMenu(this,view);
        options.inflate(R.menu.options);
        options.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item){
                switch (item.getItemId()){
                    case R.id.janken:
                        send("130z");
                        Intent intent=new Intent(MainActivity.this,Janken.class);
                        startActivity(intent);
                        return true;
                    case R.id.natural:
                        naturallyMoveEye=!naturallyMoveEye;blink=!blink;if(naturallyMoveEye){naturalEye.post(naturaleye);
                        Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();}else{
                        Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    }if(blink){naturalBlink.post(naturalblink);}return true;
                    case R.id.send:
                        send();return true;
                    case R.id.roam:
                        roam=!roam;if(roam){roamAround();
                        Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT).show();}else{
                        Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    }return true;
                    default:return false;
                }
            }
        });
        options.show();
    }


    private View.OnKeyListener listen=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            switch (keyCode){
                case 19:if(speed+33<100){speed=speed+33;}turn=0;break;
                case 20:if(speed-33>-100){speed-=33;}turn=0;break;
                case 21:if(turn-66>-200){turn-=66;};break;
                case 22:if(turn+66<200){turn+=66;}break;
                case 62:speed=0;turn=0;break;
                case 51:if(chin+20<160){chin=chin+20;}send(String.valueOf(chin)+"z");break;
                case 47:if(chin-20>60){chin=chin-20;}send(String.valueOf(chin)+"z");break;
                default:break;
            }
            go(speed,turn);
            return true;
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if(event.values[0]==0&&roam){
                obstacle=true;
                speed=0;turn=0;go(speed,turn);
            }
            else{
                obstacle=false;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }



}
