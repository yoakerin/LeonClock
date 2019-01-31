##   将其添加到根build.gradle中
    allprojects { 
         repositories {
         ... 
         maven { url 'https://jitpack.io' } 
         }
     }

##     添加依赖项
    dependencies{
        implementation'com.github.yoakerin:LeonClock:v1.0.1'
    }

## 使用
```
<com.yoake.clock.LeonClockView
        android:layout_width="200dp"
        android:layout_height="200dp"/>
```

## 支持的属性
<!--时针占表盘半径的百分比-->
        app:hour_hand_percent
        <!--分针占表盘半径的百分比-->
        app:minute_hand_percent
        <!--秒针占表盘半径的百分比-->
        app:second_hand_percent
        <!--表盘背景 BitmapDrawable 或者 ColorDrawable-->
        app:clock_background
        <!--表盘轮廓线颜色 默认是没有的-->
        app:clock_outline_color
        <!--时刻线颜色 每15秒-->
        app:main_tick_color
        <!--时刻线颜色 每5秒-->
        app:second_tick_color
        <!--时刻线颜色 每1秒-->
        app:third_tick_color
        <!--时针颜色-->
        app:hour_hand_color
        <!--分针颜色-->
        app:minute_hand_color
        <!--秒针颜色-->
        app:second_hand_color
        <!--0时刻字符-->
        app:time_zero_text
        <!--3时刻字符-->
        app:time_three_text
        <!--6时刻字符-->
        app:time_six_text
        <!--9时刻字符-->
        app:time_nine_text
        <!--0369字符颜色-->
        app:time_text_color

![](https://github.com/yoakerin/LeonClock/blob/master/0.gif)

