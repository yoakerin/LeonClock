package com.yoake.clock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LeonClockView extends View {

    //颜色相关参数
    //表盘外框颜色
    private int outlineColor;
    //时刻线颜色（每15秒）
    private int mainTickColor = Color.BLACK;
    //时刻线颜色（每5秒）
    private int secondTickColor = Color.BLACK;
    //时刻线颜色（每1秒）
    private int thirdTickColor = Color.BLACK;
    //时针颜色
    private int hourHandColor = Color.BLACK;
    //分针颜色
    private int minuteHandColor = Color.BLACK;
    //秒针针颜色
    private int secondHandColor = Color.RED;
    //正中心颜色
    private int centerColor;
    //0369字符颜色
    private int timeTextColor = Color.BLACK;
    private SimpleDateFormat sdf;
    //warp情况下
    private int defaultSize = 500;
    //表盘宽度
    private int finalSize;
    //表盘半径
    private float halfSize;
    private Paint paint;
    private String zero = "〇";
    private String three = "Ⅲ";
    private String six = "Ⅵ";
    private String nine = "Ⅸ";
    //用于测量〇字符 的宽高
    private Rect zeroRect;
    //用于测量Ⅲ字符 的宽高
    private Rect threeRect;
    //用于测量Ⅵ字符 的宽高
    private Rect sixRect;
    //用于测量Ⅸ字符 的宽高
    private Rect nineRect;
    //时针占表盘半径的百分比
    private float hourHandPercent = 0.6f;
    //时针长度
    private float hourHandSize;
    //分针占表盘半径的百分比
    private float minuteHandPercent = 0.7f;
    //分针长度
    private float minuteHandSize;
    //秒针占表盘半径的百分比
    private float secondHandPercent = 0.9f;
    //秒针长度
    private float secondHandSize;

    //时刻长度占表盘半径的百分比（每15秒）
    private float mainTickPercent = 0.1f;
    //时刻长度（每15秒）
    private float mainTickLength;
    //时刻长度占表盘半径的百分比（每5秒）
    private float secondTickPercent = 0.1f;
    //时刻长度（每5秒）
    private float secondTickLength;
    //时刻长度占表盘半径的百分比（每1秒）
    private float thirdTickPercent = 0.06f;
    //时刻长度（每1秒）
    private float thirdTickLength;
    //表盘线占半径的百分比
    private float outlinePercent = 0.02f;
    //表盘线的粗细
    private float outlineWidth;
    //主刻度粗细
    private float tickWidth;
    //秒针三角形的路劲
    private Path trianglePath;
    //表盘背景
    private Drawable clockBackground;
    private Bitmap backgroundBitmap;
    //用于表盘背景原始的Bitmap
    private Rect srcBgRect;
    //用于表盘背景Bitmap调整大小
    private RectF dstBgRectF;

    public LeonClockView(Context context) {
        this(context, null);
    }

    public LeonClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LeonClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LeonClock, defStyleAttr, 0);
        int size = typedArray.getIndexCount();
        for (int i = 0; i < size; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.LeonClock_hour_hand_percent) {
                hourHandPercent = typedArray.getDimension(attr, hourHandPercent);
            } else if (attr == R.styleable.LeonClock_minute_hand_percent) {
                minuteHandPercent = typedArray.getDimension(attr, minuteHandPercent);
            } else if (attr == R.styleable.LeonClock_second_hand_percent) {
                secondHandPercent = typedArray.getDimension(attr, secondHandPercent);
            } else if (attr == R.styleable.LeonClock_clock_background) {
                clockBackground = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.LeonClock_clock_outline_color) {
                outlineColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.LeonClock_main_tick_color) {
                mainTickColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.LeonClock_second_tick_color) {
                secondTickColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.LeonClock_third_tick_color) {
                thirdTickColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.LeonClock_hour_hand_color) {
                hourHandColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.LeonClock_minute_hand_color) {
                minuteHandColor = typedArray.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.LeonClock_second_hand_color) {
                secondHandColor = typedArray.getColor(attr, Color.RED);
            } else if (attr == R.styleable.LeonClock_time_zero_text) {
                zero = typedArray.getString(attr);
            } else if (attr == R.styleable.LeonClock_time_three_text) {
                three = typedArray.getString(attr);
            } else if (attr == R.styleable.LeonClock_time_six_text) {
                six = typedArray.getString(attr);
            } else if (attr == R.styleable.LeonClock_time_nine_text) {
                nine = typedArray.getString(attr);
            } else if (attr == R.styleable.LeonClock_time_text_color) {
                timeTextColor = typedArray.getColor(attr, Color.BLACK);
            }
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        if (clockBackground == null) {
            clockBackground = new ColorDrawable(Color.WHITE);
        }
        sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        centerColor = Color.parseColor("#DC143C");
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        zeroRect = new Rect();
        threeRect = new Rect();
        sixRect = new Rect();
        nineRect = new Rect();
        trianglePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        finalSize = Math.min(measureSize(defaultSize, widthMeasureSpec), measureSize(defaultSize, heightMeasureSpec));
        halfSize = finalSize / 2f;
        hourHandSize = halfSize * hourHandPercent;
        minuteHandSize = halfSize * minuteHandPercent;
        secondHandSize = halfSize * secondHandPercent;
        mainTickLength = halfSize * mainTickPercent;
        secondTickLength = halfSize * secondTickPercent;
        thirdTickLength = halfSize * thirdTickPercent;
        outlineWidth = halfSize * outlinePercent;
        if (outlineWidth < 4) {
            outlineWidth = 4;
        }
        tickWidth = outlineWidth;
        backgroundBitmap = getCircleBitmap(drawableToBitmap(clockBackground));
        calculatePointerPath();
        setMeasuredDimension(finalSize, finalSize);
    }

    private void calculatePointerPath() {
        trianglePath.reset();
        //从秒针针尖的1/40开始
        trianglePath.moveTo(halfSize, halfSize - secondHandSize + secondHandSize / 40);
        //达到秒针针尖的7/40开始  横向秒针宽度的3倍
        trianglePath.lineTo(halfSize + 3 * outlineWidth / 2, halfSize - secondHandSize + secondHandSize * 7 / 40);
        //勾回到秒针针尖的5/40
        trianglePath.lineTo(halfSize, halfSize - secondHandSize + secondHandSize * 5 / 40);
        //同上
        trianglePath.lineTo(halfSize - 3 * outlineWidth / 2, halfSize - secondHandSize + secondHandSize * 7 / 40);
        trianglePath.lineTo(halfSize, halfSize - secondHandSize + secondHandSize / 40);
        //最后形成一个箭头↑
        trianglePath.close();
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStrokeWidth(outlineWidth);
        if (srcBgRect == null) {
            srcBgRect = new Rect(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
            dstBgRectF = new RectF(0, 0, finalSize, finalSize);
        }
        canvas.drawBitmap(backgroundBitmap, srcBgRect, dstBgRectF, paint);

        canvas.save();
        for (int i = 0; i < 60; i++) {
            if ((i * 6) % 90 == 0) {
                paint.setStrokeWidth(tickWidth);
                paint.setColor(mainTickColor);
                canvas.drawLine(halfSize, outlineWidth, halfSize, mainTickLength, paint);
            } else if ((i * 6) % 30 == 0) {
                paint.setStrokeWidth(tickWidth / 2);
                paint.setColor(secondTickColor);
                canvas.drawLine(halfSize, outlineWidth , halfSize, secondTickLength, paint);
            } else {
                paint.setColor(thirdTickColor);
                paint.setStrokeWidth(tickWidth / 2);
                canvas.drawLine(halfSize, outlineWidth, halfSize, thirdTickLength, paint);
            }
            canvas.rotate(6f, halfSize, halfSize);
        }
        canvas.restore();

        paint.setColor(outlineColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(halfSize, halfSize, halfSize - outlineWidth / 2, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(timeTextColor);
        float textSize = getWidth() / 10f;
        paint.setTextSize(textSize);

        paint.getTextBounds(zero, 0, zero.length(), zeroRect);
        paint.getTextBounds(three, 0, three.length(), threeRect);
        paint.getTextBounds(six, 0, six.length(), sixRect);
        paint.getTextBounds(nine, 0, nine.length(), nineRect);
        canvas.drawText(zero, halfSize - paint.measureText(zero) / 2, mainTickLength + zeroRect.height(), paint);
        canvas.drawText(three, finalSize - outlineWidth / 2 - mainTickLength - threeRect.width(), halfSize + threeRect.height() / 2f, paint);
        canvas.drawText(six, halfSize - sixRect.width() / 2f, finalSize - mainTickLength - outlineWidth / 2, paint);
        canvas.drawText(nine, mainTickLength, halfSize + nineRect.height() / 2f, paint);

        Date date = new Date(System.currentTimeMillis());
        String[] times = sdf.format(date).split(":");
        int hour = Integer.parseInt(times[0]) % 12;
        int minute = Integer.parseInt(times[1]) % 60;
        int second = Integer.parseInt(times[2]);
        float hourDegrees = 30f * minute / 60;
        float minuteDegrees = 360f * minute / 60;
        float secondDegrees = 360f * second / 60;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(hourHandColor);
        //时针为轮廓线的2背
        paint.setStrokeWidth(outlineWidth * 2);
        canvas.save();
        canvas.rotate(hourDegrees + hour * 30, halfSize, halfSize);
        canvas.drawLine(halfSize, halfSize - hourHandSize, halfSize, halfSize + hourHandSize / 3, paint);
        canvas.restore();
        paint.setStrokeWidth(outlineWidth);
        paint.setColor(minuteHandColor);
        canvas.save();
        canvas.rotate(minuteDegrees + secondDegrees / 60, halfSize, halfSize);
        canvas.drawLine(halfSize, halfSize - minuteHandSize, halfSize, halfSize + minuteHandSize / 3, paint);
        canvas.restore();
        paint.setStrokeWidth(outlineWidth / 2);
        paint.setColor(secondHandColor);
        canvas.save();
        canvas.rotate(secondDegrees, halfSize, halfSize);
        canvas.drawLine(halfSize, halfSize - secondHandSize, halfSize, halfSize + secondHandSize / 3, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(trianglePath, paint);
        canvas.drawCircle(halfSize, halfSize, halfSize * 0.06f, paint);
        paint.setColor(centerColor);
        canvas.drawCircle(halfSize, halfSize, halfSize * 0.04f, paint);
        canvas.restore();
        postInvalidate();
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(finalSize, finalSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, finalSize, finalSize);
            drawable.draw(canvas);
        }
        return bitmap;
    }

    public Bitmap getCircleBitmap(Bitmap bmp) {
        //获取bmp的宽高 小的一个做为圆的直径r
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int r = Math.min(w, h);
        //创建一个paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //新创建一个Bitmap对象newBitmap 宽高都是r
        Bitmap newBitmap = Bitmap.createBitmap(r, r, Bitmap.Config.ARGB_8888);
        //创建一个使用newBitmap的Canvas对象
        Canvas canvas = new Canvas(newBitmap);
        //创建一个BitmapShader对象 使用传递过来的原Bitmap对象bmp
        BitmapShader bitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //paint设置shader
        paint.setShader(bitmapShader);
        //canvas画一个圆 使用设置了shader的paint
        canvas.drawCircle(r / 2f, r / 2f, r / 2f, paint);
        return newBitmap;
    }
}
