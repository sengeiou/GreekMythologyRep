package com.module.tools;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by KingZ on 2016/1/5.
 * Discription: 屏幕相关的辅助类
 */
@Deprecated
public class ScreenTools {

    public static final int DESIGN_WIDTH = 1280;
	public static final int DESIGN_HEIGHT = 720;

    public static int SCREEN_WIDTH = 1280;
	public static int SCREEN_HEIGHT = 720;

	private ScreenTools() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

	/*************** 720P/1080P 屏幕适配尺寸转换 ***************/
    public static int OperationHeight(int Original) {
		return Operation(Original);
	}

    public static int OperationHeight(float Original) {
		return Operation((int) Original);
		// return (int) (Original * mainScale + 0.5f);
	}

    public static int OperationWidth(int Original) {
		return Operation(Original);
		// return (int) (SCREEN_WIDTH * (Original * 1.0f / DESIGN_WIDTH) +
		// 0.5f);
	}
    public static int Operation(int Original) {
		return (int) (SCREEN_HEIGHT * (Original * 1.0f / DESIGN_HEIGHT) + 0.5f);
	}
	/*************** 720P/1080P 屏幕适配尺寸转换 ***************/

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if(wm == null){ return 0; }
        DisplayMetrics outMetrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        display.getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if(wm == null){ return 0; }
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    /**
     * 获取系统所能识别出的被认为是滑动的最小像素距离
     * 其他数据：
     *
     * {@link ViewConfiguration#getDoubleTapTimeout()}
     * {@link ViewConfiguration#getLongPressTimeout()}
     * {@link ViewConfiguration#getKeyRepeatTimeout()}
     */
    public static int getTouchSlop(ViewConfiguration vc){
        return vc.getScaledTouchSlop();
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 设置全屏,隐藏状态栏和ActionBar
     */
    public static void setFullScreen(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * 设置状态栏透明
     */
    public static void setStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
//            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = activity.getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * 获取当前窗口的旋转角度
     *
     * @param activity
     * @return
     */
    public static int getDisplayRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * 当前是否是横屏
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLandscape(View view) {
        int orientation = view.getResources().getConfiguration().orientation;
        return  orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 当前是否是竖屏
     * @param context
     */
    public static boolean isPortrait(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isPortrait(View view) {
        int orientation = view.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获得当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static int getScreenBrightnessMode(Context context) {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception ignored) {}
        return screenMode;
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    public static int getScreenBrightnessValue(Context context) {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception ignored) {}
        return screenBrightness;
    }

    /**
     * 设置屏幕亮度值 0--255
     * 退出app也能保持该亮度值
     */
    public static void saveScreenBrightness(Context context,int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static void setScreenBrightnessMode(Context context,int paramInt) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 保存当前窗口的屏幕亮度值，并使之生效
     * @param brightness:  屏幕亮度值 0--255
     */
    public static void setScreenBrightness(Activity activity,int brightness) {
        float ratio = brightness / 255.0f;
        setScreenBrightness(activity,ratio);
    }

    /**
     * 保存当前窗口的屏幕亮度值，并使之生效
     * @param  ratio：0-1 之间，1代表最亮，0代表最暗
     */
    public static void setScreenBrightness(Activity activity,float ratio) {
        if(activity == null){
            return;
        }
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = ratio;
        localWindow.setAttributes(localLayoutParams);
    }

}
