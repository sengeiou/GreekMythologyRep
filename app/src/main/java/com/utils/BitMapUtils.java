package com.utils;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import com.kingz.customdemo.R;

import java.io.ByteArrayOutputStream;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 18:36
 * description: BitMap工具类
 * <p/>
 * 知识点：
 * http://mp.weixin.qq.com/s?__biz=MzI3MDE0NzYwNA==&mid=2651433713&idx=1&sn=d152b053221c4c0bf1baa684b2a51e9c&scene=23&srcid=0805emSJb7dH8hdYfIcQiChP#rd
 * 1、decodeResource()和decodeFile()
 * decodeFile()用于读取SD卡上的图，得到的是图片的原始尺寸
 * decodeResource()用于读取Res、Raw等资源，得到的是图片的原始尺寸 * 缩放系数，
 * 缩放系数依赖于屏幕密度，参数可以通过BitMapFactory.Option的几个参数调整。
 * public boolean inScaled  //默认True
 * public int inDensity;       //无dip的文件夹下默认160
 * public int inTargetDensity; //取决具体屏幕
 * <p/>
 * ※ inScaled属性
 * 如果inScaled设置为false，则不进行缩放，解码后图片大小为720×720;
 * 如果inScaled设置为true或者不设置，则根据inDensity和inTargetDensity计算缩放系数。
 */
public class BitMapUtils {

    private BitMapUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /************************************ 图形变换  Start*****************************************/
    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 目标图片
     * @return Bitmap    旋转后的图片
     */
    public static Bitmap rotateImage(int angle, Bitmap bitmap) {
        // 图片旋转矩阵
        Matrix matrix = new Matrix(); // 每一种变化都包括set，pre，post三种，分别为设置、矩阵先乘、矩阵后乘。
        matrix.postRotate(angle);
        // 得到旋转后的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 按比例缩放/裁剪图片
     * 通过矩阵方式
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 设置图片倾斜
     *
     * @param bm    源BitMap
     * @param skewX x轴倾斜度
     * @param skewY y轴倾斜度
     * @return
     */
    public static Bitmap setSkew(Bitmap bm, float skewX, float skewY) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 取得想要倾斜的matrix参数
        Matrix matrix = new Matrix();
        matrix.setSkew(skewX, skewY);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;

    }

    /**
     * 控制Matrix以px和py为轴心进行倾斜。例如，创建一个Matrix对象，并将其以(100,100)为轴心在X轴和Y轴上均倾斜0.1
     * Matrix m=new Matrix();
     * m.setSkew(0.1f,0.1f,100,100);
     *
     * @param bm
     * @param kx
     * @param ky
     * @param px
     * @param py
     * @return
     */
    public static Bitmap setSkew(Bitmap bm, float kx, float ky, float px, float py) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 取得想要倾斜的matrix参数
        Matrix matrix = new Matrix();
        matrix.setSkew(kx, ky, px, py);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    /**
     * 设置圆角
     *
     * @param bm     源BitMap
     * @param radius 圆角弧度
     * @return
     */
    public static Bitmap setRoundCorner(Bitmap bm, int radius) {
        //初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //准备裁剪的矩阵
        Rect rect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
        RectF rectF = new RectF(0, 0, bm.getWidth(), bm.getHeight());

        // 建立对应 bitmap
        Bitmap roundBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundBitmap);
        //圆角矩形，radius为圆角大小
        canvas.drawRoundRect(rectF, radius, radius, paint);
        //设置PorterDuffXfermode，把圆角矩阵套在原Bitmap上取交集得到圆角Bitmap。
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, rect, rect, paint);
        return roundBitmap;
    }

    /**
     * 绘制圆形BitMap ---- 原理同绘制圆角矩形
     * 从圆角、圆形的处理上能看的出来绘制任意多边形都是可以的
     *
     * @param bm 源BitMap
     * @return
     */
    public static Bitmap setCircle(Bitmap bm) {
        int min = bm.getWidth() < bm.getHeight() ? bm.getWidth() : bm.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap circleBitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);
        //绘制圆形
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //居中显示
        int left = -(bm.getWidth() - min) / 2;
        int top = -(bm.getHeight() - min) / 2;

        canvas.drawBitmap(bm, left, top, paint);
        return circleBitmap;
    }

    /**
     * drawable转换为Bitmap
     *
     * @param drawable 目标Drawable
     * @param width    要求宽度
     * @param height   要求的高度
     * @return 转换后的BitMap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable, int width, int height) {
        if (drawable == null) {
            return null;
        }
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap 转 Drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmap2Drawable(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        BitmapDrawable bd = new BitmapDrawable(bm);
        bd.setTargetDensity(bm.getDensity());
        return new BitmapDrawable(bm);
    }

    /**
     * 生成水印图片
     *
     * @param photo     原图片
     * @param watermark 水印图片
     * @param mark_x    水印X坐标
     * @param mark_y    水印Y坐标
     * @return
     */
    public static Bitmap createWaterMarkBitmap(Bitmap photo, Bitmap watermark, int mark_x, int mark_y) {
        //左上角 mark_x = 0；mark_y=0;
        //右上角 mark_x = photo.getWidth() - watermark.getWidth()；mark_y=0;
        //左下角 mark_x = 0；mark_y=photo.getHeight() - watermark.getHeight();
        /*左上角 mark_x = photo.getWidth() - watermark.getWidth()；
        /mark_y = photo.getHeight() - watermark.getHeight();*/
        if (photo == null) {
            return null;
        }
        int photoWidth = photo.getWidth();
        int photoHeight = photo.getHeight();
        int markWidth = watermark.getWidth();
        int markHeight = watermark.getHeight();

        // create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(photoWidth, photoHeight, Bitmap.Config.ARGB_8888);
        // 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);

        // draw src into
        // 在 0，0坐标开始画入src
        cv.drawBitmap(photo, 0, 0, null);
        // draw watermark into
        // 在src的右下角画入水印
        cv.drawBitmap(watermark, mark_x, mark_y, null);
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        return newb;
    }

    /**
     * 生成水印文字
     *
     * @param photo     原图片
     * @param str       水印文字
     * @param mark_x    水印X坐标
     * @param mark_y    水印Y坐标
     * @return
     */
    public static Bitmap createWaterMarkText(Bitmap photo, String str, int mark_x, int mark_y) {
        int width = photo.getWidth();
        int hight = photo.getHeight();
        //建立一个空的BItMap
        Bitmap icon = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
        //初始化画布绘制的图像到icon上
        Canvas canvas = new Canvas(icon);

        Paint photoPaint = new Paint();     //建立画笔
        photoPaint.setDither(true);         //获取跟清晰的图像采样
        photoPaint.setFilterBitmap(true);   //过滤一些

        //创建一个指定的新矩形的坐标
        Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());
        //创建一个指定的新矩形的坐标
        Rect dst = new Rect(0, 0, width, hight);
        //将photo 缩放或则扩大到 dst使用的填充区photoPaint
        canvas.drawBitmap(photo, src, dst, photoPaint);

        //设置画笔
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(20.0f);//字体大小
        //采用默认的宽度
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        //采用的颜色
        textPaint.setColor(Color.parseColor("#5FCDDA"));
        //影音的设置
        //textPaint.setShadowLayer(3f, 1, 1,this.getResources().getColor(android.R.color.background_dark));
        //绘制上去字，开始未知x,y采用那只笔绘制
        canvas.drawText(str, mark_x, mark_y, textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return icon;
    }

    /************************************ 图形变换 End************************************/


    /************************************ BitMap转变 ************************************/

    /**
     * 把bitmap转换成base64
     *
     * @param bitmap        目标BitMap
     * @param bitmapQuality
     * @return
     */
    public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 把base64转换成bitmap
     */
    public static Bitmap getBitmapFromBase64(String string) {
        byte[] bitmapArray = null;
        try {
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * Bitmap 转 byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte[] 转 Bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }


}
