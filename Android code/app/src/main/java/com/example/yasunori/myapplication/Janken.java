package com.example.yasunori.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Janken extends CommonActivity {
    int myHand=0;//0rock1paper2scissor
    int oppsHand=0;
    Random random=new Random();
    ImageView D;
    TextView E;
    boolean win=false;
    View A,B,C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.janken);
        D=(ImageView)findViewById(R.id.myHand);
        E=(TextView) findViewById(R.id.state);
        initManager();
        initView();
        A=findViewById(R.id.paper);
        B=findViewById(R.id.scissors);
        C=findViewById(R.id.rock);
        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppsHand=1;result();
            }
        });
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppsHand = 2;
                result();
            }
        });
        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppsHand = 0;
                result();
            }
        });
        android.os.Handler handler=new android.os.Handler();

    }
    private void result(){
        myHand=random.nextInt(3);
        switch (myHand){
            case 0:D.setImageResource(R.drawable.rock);break;
            case 1:D.setImageResource(R.drawable.paper);break;
            case 2:D.setImageResource(R.drawable.scissors);break;
            default:return;
        }
        if(myHand==oppsHand){
            E.setText("あいこ！もう一回");return;
        }
        switch (myHand){
            case 0:if(oppsHand==1){win=false;}else{win=true;}break;
            case 1:if(oppsHand==0){win=true;}else{win=false;}break;
            case 2:if(oppsHand==0){win=false;}else{win=true;}break;
        }
        if(win){

            E.setText("あなたの負け！  あっち向いて...");
            switch (oppsHand){
                case 0:C.setBackgroundColor(Color.RED);break;
                case 1:A.setBackgroundColor(Color.RED);break;
                case 2:B.setBackgroundColor(Color.RED);break;
                default:break;
            }
            D.setBackgroundColor(Color.GREEN);
            thisAcchi();

        }
        if(!win){
            E.setText("あなたの勝ち！  「あっち向いて...」");
            switch (oppsHand){
                case 0:C.setBackgroundColor(Color.GREEN);break;
                case 1:A.setBackgroundColor(Color.GREEN);break;
                case 2:B.setBackgroundColor(Color.GREEN);break;
                default:break;
            }
            D.setBackgroundColor(Color.RED);
            thisAcchi();
        }
    }

    private void thisAcchi(){

        Runnable r=new Runnable() {
            @Override
            public void run() {
                E.setText("ほい！！");
                int direction=random.nextInt(4);
                turn(direction);
                android.os.Handler handler=new android.os.Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        send("130z");C.setBackgroundColor(Color.TRANSPARENT);B.setBackgroundColor(Color.TRANSPARENT);A.setBackgroundColor(Color.TRANSPARENT);D.setBackgroundColor(Color.TRANSPARENT);E.setText("最初はグー、じゃんけん...");
                    }
                },3000);
            }
        };
        android.os.Handler handler=new android.os.Handler();
        handler.postDelayed(r,2000);
    }
    private void turn(int direction){
        if(direction<2){
            if(direction==0){
                send("160z");
            }
            else {
                send("60z");
            }
        }
        else{
            Runnable r=new Runnable() {
                @Override
                public void run() {
                    send("0r0l");
                }
            };
            android.os.Handler handler=new android.os.Handler();
            handler.postDelayed(r,600);
            if(direction==2){
                send("255r-255l");
            }
            else {
                send("-255r255l");
            }
        }
    }
}
