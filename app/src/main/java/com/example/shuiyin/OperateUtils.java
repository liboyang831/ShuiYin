package com.example.shuiyin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

/**
 *  添加文字图片工具类
 */

public class OperateUtils {
    private Activity activity;
    private int screenWidth;// 手机屏幕的宽（像素）
    private int screenHeight;// 手机屏幕的高（像素）

    public static final int LEFTTOP = 1;
    public static final int RIGHTTOP = 2;
    public static final int LEFTBOTTOM = 3;
    public static final int RIGHTBOTTOM = 4;
    public static final int CENTER = 5;

    public OperateUtils(Activity activity)
    {
        this.activity = activity;
        if (screenWidth == 0)
        {
            DisplayMetrics metric = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
            screenWidth = metric.widthPixels; // 屏幕宽度（像素）
            screenHeight = metric.widthPixels; // 屏幕宽度（像素）
        }
    }

    /**
     * 添加图片方法
     *
     * @param srcBmp
     *            被操作的图片
     * @param operateView
     *            容器View对象
     * @param quadrant
     *            需要图片显示的区域 （1、左上方，2、右上方，3、左下方，4、右下方，5、中心）
     * @param x
     *            离边界x坐标
     * @param y
     *            离边界y坐标
     * @return
     */

    public ImageObject getImageObject(Bitmap srcBmp, OperateView operateView,
                                      int quadrant, int x, int y)
    {
        Bitmap rotateBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.rotate);
        Bitmap deleteBm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.delete);
        int width = operateView.getWidth();
        int height = operateView.getHeight();
        switch (quadrant)
        {
            case LEFTTOP :
                break;
            case RIGHTTOP :
                x = width - x;
                break;
            case LEFTBOTTOM :
                y = height - y;
                break;
            case RIGHTBOTTOM :
                x = width - x;
                y = height - y;
                break;
            case CENTER :
                x = width / 2;
                y = height / 2;
                break;
            default :
                break;
        }
        ImageObject imgObject = new ImageObject(srcBmp, x, y, rotateBm,
                deleteBm);
        Point point = new Point(20, 20);
        imgObject.setPoint(point);
        return imgObject;
    }
}
