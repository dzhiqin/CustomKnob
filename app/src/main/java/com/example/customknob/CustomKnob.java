package com.example.customknob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/5/15.
 */

public class CustomKnob extends View {
    private int width;
    private int height;
    private Paint paint;
    private float moveX;
    private float moveY;
    private float moveZ;
    private boolean isGear;
    private int gear;
    private float downX;
    private float downY;
    private float downZ;
    private double downDegree;
    private double beginDegree;
    private double moveDegree;
    private double endDegree;
    private double deltaDegree;
    private float rotateDegree;
    private int maxValue;
    private int progress;
    private int imgKnobId;
    private String minStr="Min";
    private String maxStr="Max";
    private OnRotateListener listener;
    public CustomKnob(Context context){
        super(context);
    }
    public CustomKnob(Context context, AttributeSet attrs){
        super(context,attrs);
        paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        //初始化参数
        rotateDegree=0;
        gear=1;
        maxValue=100;
        imgKnobId=R.mipmap.knob;
    }
    public void setGears(int gear){
        this.gear=gear;
        isGear=true;
    }
    public void setMax(int maxValue){
        this.maxValue=maxValue;
    }
    public void setImgKnobId(int imgKnobId){
        this.imgKnobId=imgKnobId;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width=w;
        this.height=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(width/2,height/2);
        int radius=Math.min(width/2,height/2);
        //设置画笔
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(0,0,(float)0.7*radius,paint);//画内圈
        canvas.drawCircle(0,0,(float)0.8*radius,paint);//画外圈
        paint.setStrokeWidth((float)0.1*radius);//设置画笔的宽度为0.1半径
        paint.setColor(Color.GRAY);//设置画笔为灰色
        canvas.drawCircle(0,0,(float)0.75*radius,paint);//画灰色圆环


        paint.setStrokeWidth(15);
        paint.setColor(Color.RED);//设置画笔为红色
        RectF rectF=new RectF((float)-0.75*radius,(float)-0.75*radius,(float)0.75*radius,(float)0.75*radius);
        //起始角度270，画一个跨度为rotateDegree的圆弧，paint已设置为STROKE 描边
        canvas.drawArc(rectF,270,rotateDegree,false,paint);
        paint.setColor(Color.BLACK);//color
        canvas.drawLine(0,(float)-0.7*radius,0,(float)-0.8*radius,paint);//画起始线

        //旋转 旋钮
        canvas.rotate(rotateDegree);
        paint.setStrokeWidth(20);
        canvas.drawPoint(0,(float)-0.75*radius,paint);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),imgKnobId);
        Rect src=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        RectF dst=new RectF((float)-0.7*radius,(float)-0.7*radius,(float)0.7*radius,(float)0.7*radius);
        canvas.drawBitmap(bitmap,src,dst,new Paint());

    }
    //设置旋转角度
    public void setRotateDegree(float rotateDegree){
        this.rotateDegree=rotateDegree;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=event.getX()-width/2;
                downY=event.getY()-height/2;
                downZ=(float)Math.sqrt(downX*downX+downY*downY);
                downDegree=Math.toDegrees(Math.asin(downY/downZ));
                if(downX<0){
                    downDegree=180-downDegree;
                }
                if((downX>0)&&(downY<0)){
                    downDegree=360+downDegree;
                }
                //Log.v("test","downDegree="+downDegree);
                beginDegree=rotateDegree;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX=event.getX()-width/2;//坐标原点移动到中心位置，经过计算知道，此时如果要获得移动原点后的坐标系中的坐标，需要减去width/2和height/2
                moveY=event.getY()-height/2;
                moveZ=(float)Math.sqrt(moveX*moveX+moveY*moveY);
                double asin=Math.asin(moveY/moveZ);
                moveDegree=Math.toDegrees(asin);
                if((moveX<0)){
                    moveDegree=180-moveDegree;
                }
                if((moveX>0)&&(moveY<0)){
                    moveDegree=360+moveDegree;
                }
                //手移动的角度
                deltaDegree=(float)(moveDegree-downDegree);
                if(deltaDegree<0)deltaDegree+=360;//如果deltaDegree是负，就转为整数，这样做的目的是为了使rotateDegree不出现负数
               // Log.v("test","deltaDegree="+deltaDegree);
                //图片最终旋转的角度
                rotateDegree=(float)(beginDegree+deltaDegree);
                rotateDegree=rotateDegree%360;
                if(rotateDegree<=0) {rotateDegree+=360;}
                //Log.v("test","rotateDegree="+rotateDegree);
                if((rotateDegree>1)&&(rotateDegree<=180))
                {
                    invalidate();
                }else{
                    //180~260这个范围作为超出180（180默认为最大进度）的缓冲范围
                    if((rotateDegree>180)&&(rotateDegree<260)){
                        rotateDegree=180;
                    }else{
                        rotateDegree=0;
                    }
                }
                //Log.v("test","rotateDegree="+rotateDegree);
                progress=(int)(rotateDegree*maxValue/180);
                if(listener!=null){
                    listener.changingProgress(progress);
                }

                break;
            case MotionEvent.ACTION_UP:
                invalidate();//手抬起的时候更新一次界面
                progress=(int)(rotateDegree*maxValue/180);//旋转过的角度转化为进度值
                if(listener!=null){
                    listener.rotatedProgress(progress);
                }

                break;
        }
        return true;
    }
    public void setOnRotateListener(OnRotateListener onRotateListener){
        this.listener=onRotateListener;
    }
    public interface OnRotateListener{
        void changingProgress(int progress);
        void rotatedProgress(int progress);
    }
}
