package com.example.yasunori.myapplication;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by yasunori on 1/10/16.
 */
public class WeightAnimation extends Animation {
    private  float mStartWeight;
    private  float mDeltaWeight;
    private View mContent;
    public WeightAnimation(float startWeight,float endWeight,View content) {
        mStartWeight = startWeight;
        mDeltaWeight = endWeight - startWeight;
        mContent=content;
    }

    @Override
    protected void applyTransformation(float interpolatedTime,Transformation t){
        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams) mContent.getLayoutParams();
        lp.weight=(mStartWeight+(mDeltaWeight*interpolatedTime));
        mContent.setLayoutParams(lp);
    }
    @Override
    public boolean willChangeBounds(){
        return true;
    }
}
