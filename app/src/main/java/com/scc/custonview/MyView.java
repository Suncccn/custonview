package com.scc.custonview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class MyView extends View {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";
    private Bitmap background;
    private Bitmap icon;
    private float iconX;
    private float minDistance = 0;
    private float maxDistance = 0;


    public MyView(Context context) {
//        super(context);
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, -1);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int iconId = attrs.getAttributeResourceValue(NAMESPACE, "icon", R.drawable.slide_icon2);    //使用命名空间获取自定义属性
//        int backgoundId = attrs.getAttributeResourceValue(NAMESPACE, "background", R.drawable.slide_background2);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);
//        int iconId = typedArray.getResourceId(R.styleable.MyView_icon, R.drawable.slide_icon2);
        int backgoundId = typedArray.getResourceId(R.styleable.MyView_background,R.drawable.slide_background2);
        boolean state = typedArray.getBoolean(R.styleable.MyView_state,false);
        setBackgroundIcon(iconId, backgoundId);
        setState(state);
    }

    public void setState(boolean state){
        if (state){
            iconX = maxDistance;
        }else{
            iconX = minDistance;
        }
        invalidate();
    }

    public void setBackgroundIcon(int iconId, int backgroundId) {
        background = BitmapFactory.decodeResource(getResources(), backgroundId);
        icon = BitmapFactory.decodeResource(getResources(), iconId);
        maxDistance = background.getWidth() - icon.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(background.getWidth(), background.getHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, null);
        if (iconX < minDistance) {
            iconX = minDistance;
        } else if (iconX > maxDistance) {
            iconX = maxDistance;
        }
        canvas.drawBitmap(icon, iconX, 0, null);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                iconX = event.getX() - icon.getWidth() / 2;
                break;
            case MotionEvent.ACTION_MOVE:
                iconX = event.getX() - icon.getWidth() / 2;
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() < background.getWidth() / 2) {
                    iconX = minDistance;
                } else {
                    iconX = maxDistance;
                }
                if (listener != null) {
                    listener.myViewOn(iconX > 0);
                }
                break;
            default:
                break;
        }

        invalidate();   //调用 onDraw()
        return true;
    }

    private OnMyViewListener listener;

    public void setOnMyViewListener(OnMyViewListener listener) {
        this.listener = listener;
    }

    public interface OnMyViewListener {
        public void myViewOn(boolean isOpen);
    }
}
