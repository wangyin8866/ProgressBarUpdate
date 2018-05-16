package com.wyman.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * @author wyman
 * @date 2018/4/24
 * description :
 */

public class CustomView extends View {
    private int mWidth;
    private int mHeight;
    private int mProgressWidth;
    private int mProgressHeight;
    private int mProgressBackgroundColor;
    private int mProgressProcessBackgroundColorStart;
    private int mProgressProcessBackgroundColorEnd;
    private int mProgressTextColor;
    private int mProgressTextSize;
    private int mProgressDotBackgroundColor;
    private int mProgressDotRadius;
    private String mProgressText;
    private Bitmap mProgressTipImg;
    private Paint backRectPaint;
    private Paint doneRectPaint;
    private Paint textRectPaint;
    private Paint dotRectPaint;
    /**
     * 绘制圆角矩形
     */
    private RectF mRectF1;
    private RectF mRectF2;

    private float investAmount;
    private float totalAmount;
    private float percentage;
    private Rect mTextRound;
    private float mLeft;
    private float mRight;
    private LinearGradient gradient;

    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.CustomView_progressHeight:
                    mProgressHeight = array.getDimensionPixelSize(attr, (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                                    getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomView_progressWidth:
                    mProgressWidth = array.getDimensionPixelOffset(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100,
                            getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomView_progressBackgroundColor:
                    mProgressBackgroundColor = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.CustomView_progressProcessBackgroundColorStart:
                    mProgressProcessBackgroundColorStart = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.CustomView_progressProcessBackgroundColorEnd:
                    mProgressProcessBackgroundColorEnd = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.CustomView_progressTextColor:
                    mProgressTextColor = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.CustomView_progressTextSize:
                    mProgressTextSize = array.getDimensionPixelOffset(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;

                case R.styleable.CustomView_progressTipImg:
                    mProgressTipImg = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomView_progressDotBackgroundColor:
                    mProgressDotBackgroundColor = array.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.CustomView_progressDotRadius:
                    mProgressDotRadius = array.getDimensionPixelOffset(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10
                            , getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomView_investAmount:
                    investAmount = array.getFloat(attr, 0);
                    break;
                case R.styleable.CustomView_totalAmount:
                    totalAmount = array.getFloat(attr, 0);
                    break;
            }
        }
        array.recycle();
        init();

    }

    private void init() {
        mRectF1 = new RectF();
        mRectF2 = new RectF();


        //背景
        backRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backRectPaint.setAntiAlias(true);

        //进度背景
        doneRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        doneRectPaint.setAntiAlias(true);
        //圆点
        dotRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotRectPaint.setAntiAlias(true);

        dotRectPaint.setStyle(Paint.Style.FILL);
        dotRectPaint.setMaskFilter(new BlurMaskFilter(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()), BlurMaskFilter.Blur.SOLID)); //需关闭硬件加速
        //文本

        mTextRound = new Rect();
        textRectPaint = new Paint();
        textRectPaint.setAntiAlias(true);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("mLeft", mLeft + "");


        mLeft = getPaddingLeft();
        mRight = getPaddingRight();
        backRectPaint.setColor(mProgressBackgroundColor);
        dotRectPaint.setColor(mProgressDotBackgroundColor);
        textRectPaint.setColor(mProgressTextColor);
        textRectPaint.setTextSize(mProgressTextSize);
        percentage = investAmount / totalAmount;
        mProgressText = percentage * 100 + "%";
        textRectPaint.getTextBounds(mProgressText, 0, mProgressText.length(), mTextRound);
        canvas.translate(mLeft, 0);
        canvas.save();
        mRectF1.left = 0;
        mRectF1.top = mHeight / 2;
        mRectF1.right = mProgressWidth;
        mRectF1.bottom = mProgressHeight + mHeight / 2;
        gradient = new LinearGradient(0, mHeight / 2, mProgressWidth * percentage, mHeight / 2,
                new int[]{mProgressProcessBackgroundColorStart, mProgressProcessBackgroundColorEnd}, null, Shader.TileMode.CLAMP);
        doneRectPaint.setShader(gradient);
        //绘制进度条背景
        canvas.drawRoundRect(mRectF1, 10, 10, backRectPaint);

        mRectF2.left = 0;
        mRectF2.top = mHeight / 2;
        mRectF2.right = (mProgressWidth * percentage);
        mRectF2.bottom = mProgressHeight + mHeight / 2;


        //绘制进度条进度
        canvas.drawRoundRect(mRectF2, 10, 10, doneRectPaint);
        //绘制圆角
        canvas.drawCircle((mProgressWidth * percentage), mHeight / 2 + mProgressHeight / 2, mProgressDotRadius, dotRectPaint);

        //绘制图片

        canvas.drawBitmap(mProgressTipImg, mProgressWidth * percentage - mProgressTipImg.getWidth() / 2, mHeight / 2 + mProgressHeight / 2 - mProgressTipImg.getHeight() - mProgressDotRadius, textRectPaint);
        //绘制文本

        canvas.drawText(mProgressText, mProgressWidth * percentage - mTextRound.width() / 2, mHeight / 2 + mProgressHeight / 2 - mProgressTipImg.getHeight() - mProgressDotRadius + mTextRound.height() + 8, textRectPaint);
    }

    public void setData(float investAmount, float totalAmount) {
        this.investAmount = investAmount;
        this.totalAmount = totalAmount;
        percentage = investAmount / totalAmount;
        if (percentage == 1) {
            mProgressProcessBackgroundColorStart = Color.parseColor("#ebebeb");
            mProgressProcessBackgroundColorEnd = Color.parseColor("#ebebeb");
            mProgressTipImg = BitmapFactory.decodeResource(getResources(), R.mipmap.pao_gray_progres);
            mProgressDotBackgroundColor = Color.parseColor("#ebebeb");
        }
        postInvalidate();
    }
}
