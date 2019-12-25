package com.ming.base.util;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * bitmap工具类
 */
public class BitmapUtil {

    /**
     * Get bitmap from specified image path
     *
     * @param imgPath
     * @return
     */
    public static Bitmap getBitmap(String imgPath, BitmapFactory.Options options) {
        if (TextUtils.isEmpty(imgPath)) {
            return null;
        }
        return BitmapFactory.decodeFile(imgPath, options);
    }

    public static Bitmap getBitmap(String imgPath) {
        if (TextUtils.isEmpty(imgPath)) {
            return null;
        }
        return getBitmap(imgPath, null);
    }

    /**
     * Store bitmap into specified image path
     *
     * @param bitmap
     * @param outPath
     * @throws FileNotFoundException
     */
    public static void storeImage(Bitmap bitmap, String outPath, int quality) throws FileNotFoundException {
        if (TextUtils.isEmpty(outPath)) {
            return;
        }
        FileUtil.makeFolders(outPath);
        FileOutputStream os = new FileOutputStream(outPath);
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, os);
    }

    public static void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {
        storeImage(bitmap, outPath, 100);
    }

    /**
     * Compress image by pixel, this will modify image width/height.
     * Used to get thumbnail
     *
     * @param imgPath image path
     * @param pixelW  target pixel of width
     * @param pixelH  target pixel of height
     * @return
     */
    public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Config.ARGB_8888;
        BitmapFactory.decodeFile(imgPath, newOpts);
        // Get bitmap info, but notice that bitmap is null now
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        newOpts.inJustDecodeBounds = false;
        // 想要缩放的目标尺寸
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

        return bitmap;
    }

    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param image
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(b)
     * @throws IOException
     */
    public static void compressAndGenImage(Bitmap image, String outPath, long maxSize) throws IOException {

        byte[] bytes = compress(image, maxSize);
        // Generate compressed image file
        if (bytes != null) {
            FileUtil.makeFolders(outPath);
            FileOutputStream fos = new FileOutputStream(outPath);
            fos.write(bytes);
            fos.flush();
            fos.close();
        }
    }

    public static byte[] compress(Bitmap image, long maxSize) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        long length = os.toByteArray().length;
        while (length > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
            length = os.toByteArray().length;
        }
        return os.toByteArray();
    }


    /**
     * Compress by quality,  and generate image to the path specified
     *
     * @param imgPath
     * @param outPath
     * @param maxSize target will be compressed to be smaller than this size.(b)
     * @throws IOException
     */
    public static void compressAndGenImage(String imgPath, String outPath, long maxSize) throws IOException {
        compressAndGenImage(getBitmap(imgPath), outPath, maxSize);
    }

    /**
     * Ratio and generate thumb to the path specified
     *
     * @param imgPath
     * @param outPath
     * @param pixelW  target pixel of width
     * @param pixelH  target pixel of height
     * @throws FileNotFoundException
     */
    public static void ratioAndGenThumb(String imgPath, String outPath, float pixelW, float pixelH) throws FileNotFoundException {
        Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
        storeImage(bitmap, outPath);
    }

    /**
     * 生成缩略图
     *
     * @param imgPath
     * @param outPath
     * @param pixelW
     * @param pixelH
     * @param maxSize
     * @throws IOException
     */
    public static void generateThumb(String imgPath, String outPath, float pixelW, float pixelH, long maxSize) throws IOException {
        Bitmap bitmap = ratio(imgPath, pixelW, pixelH);
        compressAndGenImage(bitmap, outPath, maxSize);
    }

}