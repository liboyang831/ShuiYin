package com.example.shuiyin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 添加水印
 */

public class MainActivity extends Activity implements View.OnClickListener {

    private LinearLayout content_layout;
    private OperateView operateView;
    OperateUtils operateUtils;
    private Context context;
    private ImageButton btn_ok, btn_cancel;

    private TextView chunvzuo, shenhuifu, qiugouda, guaishushu, haoxingzuo,
            wanhuaile, xiangsi, xingzuokong, xinnian, zaoan, zuile, jiuyaozuo,zui;

    private ImageView imageShow;
    private String iPath;
    private MainActivity mActivity=this;
    public static final int CHOOSE_PHOTO = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = this;
//        openAlbum();

        operateUtils = new OperateUtils(this);
        initView();
        // 延迟每次延迟10 毫秒 隔1秒执行一次
        timer.schedule(task, 10, 1000);
    }

    @SuppressLint("HandlerLeak")
    final Handler myHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 1)
            {
                if (content_layout.getWidth() != 0)
                {
                    Log.i("LinearLayoutW", content_layout.getWidth() + "");
                    Log.i("LinearLayoutH", content_layout.getHeight() + "");
                    // 取消定时器
                    timer.cancel();
//                    openAlbum();
                    fillContent();
                }
            }
        }
    };

    Timer timer = new Timer();
    TimerTask task = new TimerTask()
    {
        public void run()
        {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    private void initView(){
        content_layout = (LinearLayout) findViewById(R.id.mainLayout);

        btn_ok = (ImageButton) findViewById(R.id.btn_ok);
        btn_cancel = (ImageButton) findViewById(R.id.btn_cancel);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        chunvzuo = (TextView) findViewById(R.id.chunvzuo);
        shenhuifu = (TextView) findViewById(R.id.shenhuifu);
        qiugouda = (TextView) findViewById(R.id.qiugouda);
        guaishushu = (TextView) findViewById(R.id.guaishushu);
        haoxingzuo = (TextView) findViewById(R.id.haoxingzuo);
        wanhuaile = (TextView) findViewById(R.id.wanhuaile);
        xiangsi = (TextView) findViewById(R.id.xiangsi);
        xingzuokong = (TextView) findViewById(R.id.xingzuokong);
        xinnian = (TextView) findViewById(R.id.xinnian);
        zaoan = (TextView) findViewById(R.id.zaoan);
        zuile = (TextView) findViewById(R.id.zuile);
        jiuyaozuo = (TextView) findViewById(R.id.jiuyaozuo);
        zui = (TextView) findViewById(R.id.zui);
//        mSourImage = (ImageView) findViewById(R.id.pictureShow);
//        Bitmap sourBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marilyn);
//        mSourImage.setImageBitmap(sourBitmap);

//        imageShow = (ImageView) findViewById(R.id.pictureShow);

        chunvzuo.setOnClickListener(this);
        shenhuifu.setOnClickListener(this);
        qiugouda.setOnClickListener(this);
        guaishushu.setOnClickListener(this);
        haoxingzuo.setOnClickListener(this);
        wanhuaile.setOnClickListener(this);
        xiangsi.setOnClickListener(this);
        xingzuokong.setOnClickListener(this);
        xinnian.setOnClickListener(this);
        zaoan.setOnClickListener(this);
        zuile.setOnClickListener(this);
        jiuyaozuo.setOnClickListener(this);
        zui.setOnClickListener(this);
    }

    private void fillContent()
    {
//        openAlbum();
//        Bitmap resizeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.marilyn);
//        imageShow.setImageBitmap(resizeBmp);
//        imagePath = Take.getImagePath();
//        Bitmap resizeBmp = BitmapFactory.decodeFile("image/storage/emulated/0/Pictures/1597757759614.jpg");
////        System.out.print("11111111111111111111111111111111111111111111111111");
        Log.i("path***************:",  iPath+' ');
////        System.out.print(imagePath);
////        System.out.print("222222222222222222222222222222222222222222222222");
////        Bitmap resizeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.marilyn);
//        operateView = new OperateView(MainActivity.this, resizeBmp);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                resizeBmp.getWidth(), resizeBmp.getHeight());
//        operateView.setLayoutParams(layoutParams);
//        content_layout.addView(operateView);
//        operateView.setMultiAdd(true); // 设置此参数，可以添加多个图片
    }



    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
    }

    // 使用startActivityForResult()方法开启Intent的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null){
            return;
        }
        else {
            switch (requestCode) {
                case CHOOSE_PHOTO:
                    //相册照片
                    if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && null != data) {
                        if (Build.VERSION.SDK_INT >= 19) {
                            handleImageOnKitkat(data);
                        } else {
                            handleImageBeforeKitKat(data);
                        }
                    }
                default:
                    break;
            }
        }
    }
    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(((Cursor) cursor).moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Log.i("path11111:",  imagePath+' ');

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap Bmp= BitmapFactory.decodeFile(imagePath,options);
            if (options.outHeight>4000)
                options.inSampleSize = 4;/*图片长宽方向缩小倍数*/
            else if(options.outHeight>2000)
                options.inSampleSize = 3;/*图片长宽方向缩小倍数*/
            else
                options.inSampleSize = 1;/*图片长宽方向缩小倍数*/

            options.inJustDecodeBounds = false;

//            options.inSampleSize = 2;/*图片长宽方向缩小倍数*/

            Bitmap resizeBmp= BitmapFactory.decodeFile(imagePath,options);
            iPath = imagePath;
            Log.i("path:",  iPath+' ');
//            imageShow.setImageBitmap(resizeBmp);//将图片放置在控件上


            operateView = new OperateView(MainActivity.this, resizeBmp);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(//LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                resizeBmp.getWidth(), resizeBmp.getHeight());
                //LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        LinearLayout.LayoutParams.WRAP_CONTENT
        operateView.setLayoutParams(layoutParams);
        content_layout.addView(operateView);

        operateView.setMultiAdd(true); // 设置此参数，可以添加多个图片
        }else {
            Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
        }
    }

        private void btnSave()
    {
        operateView.save();
        Bitmap bmp = getBitmapByView(operateView);
        if (bmp != null)
        {
            Save.saveToSystemGallery(MainActivity.this,bmp);
//            this.finish();
        }

//        Take.openAlbum(MainActivity.this,operateView);
    }

    private void btnTake()
    {
//        operateView.save();
//        Bitmap bmp = getBitmapByView(operateView);
//        if (bmp != null)
//        {
//            Save.saveToSystemGallery(MainActivity.this,bmp);
////            this.finish();
//        }

        openAlbum();
//        Bitmap resizeBmp;
//        resizeBmp = BitmapFactory.decodeFile(iPath);
//        System.out.print("111111111111111111111111111111111111111111111111");
        Log.i("path***************:",  iPath+' ');
//        System.out.print(imagePath);
//        System.out.print("222222222222222222222222222222222222222222222222");
//        Bitmap resizeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.marilyn);
//        operateView = new OperateView(MainActivity.this, resizeBmp);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                resizeBmp.getWidth(), resizeBmp.getHeight());
//        operateView.setLayoutParams(layoutParams);
//        content_layout.addView(operateView);
//        operateView.setMultiAdd(true); // 设置此参数，可以添加多个图片

    }

    // 将模板View的图片转化为Bitmap
    public Bitmap getBitmapByView(View v)
    {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void addpic(int position)
    {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), position);
        // ImageObject imgObject = operateUtils.getImageObject(bmp);
        ImageObject imgObject = operateUtils.getImageObject(bmp, operateView,
                5, 150, 100);
        operateView.addItem(imgObject);
    }

    int watermark[] = {R.drawable.watermark_chunvzuo, R.drawable.comment,
            R.drawable.gouda, R.drawable.guaishushu, R.drawable.haoxingzuop,
            R.drawable.wanhuaile, R.drawable.xiangsi, R.drawable.xingzuokong,
            R.drawable.xinnian, R.drawable.zaoan, R.drawable.zuile,
            R.drawable.zuo,R.drawable.zui};
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chunvzuo:
                addpic(watermark[0]);
                break;
            case R.id.shenhuifu:
                addpic(watermark[1]);
                break;
            case R.id.qiugouda:
                addpic(watermark[2]);
                break;
            case R.id.guaishushu:
                addpic(watermark[3]);
                break;
            case R.id.haoxingzuo:
                addpic(watermark[4]);
                break;
            case R.id.wanhuaile:
                addpic(watermark[5]);
                break;
            case R.id.xiangsi:
                addpic(watermark[6]);
                break;
            case R.id.xingzuokong:
                addpic(watermark[7]);
                break;
            case R.id.xinnian:
                addpic(watermark[8]);
                break;
            case R.id.zaoan:
                addpic(watermark[9]);
                break;
            case R.id.zuile:
                addpic(watermark[10]);
                break;
            case R.id.jiuyaozuo:
                addpic(watermark[11]);
                break;
            case R.id.zui:
                addpic(watermark[12]);
                break;
            case R.id.btn_ok :
                btnSave();
                break;
            case R.id.btn_cancel :
                btnTake();
//                finish();
                break;
            default:
                break;
        }
    }
}