package com.itcast.whw.tool;

import android.content.Context;

/**
 * 单位转换工具
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从dip的单位转换为px(像素)
     */
    public static int dipToPx(Context context,float dpValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px(像素)的单位转换为dip
     */
    public static int pxToDip(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }
}
