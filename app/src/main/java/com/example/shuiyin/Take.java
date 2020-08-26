package com.example.shuiyin;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Take extends Activity {
        public static final int CHOOSE_PHOTO = 2;
        private static ImageView picture;

        public static void openAlbum(MainActivity mActivity, ImageView pic) {
            picture = pic;
            if (ContextCompat.checkSelfPermission(mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.
                    PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                mActivity.startActivityForResult(intent, CHOOSE_PHOTO);//打开相册
            }
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
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
                Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
                picture.setImageBitmap(bitmap);//将图片放置在控件上
            }else {
                Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
            }
        }


    }
