package vite.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by trs on 17-9-11.
 */

public final class BitmapUtil {

    public static byte[] toBytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                if (byteArrayOutputStream.toByteArray() == null) {
                    LogUtil.e("BitmapUtil - toBytes", "byteArrayOutputStream.toByteArray null");
                }
                return byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
            return null;
        } else {
            LogUtil.e("BitmapUtil - toBytes", "bitmap null or bitmap isRecycled");
            return null;
        }
    }

    /**
     * 压缩图片到指定字节
     *
     * @param data
     * @param limitCount 指定字节数
     * @return
     */
    public static byte[] compressBitmap(byte[] data, int limitCount) {
        boolean isFinish = false;
        if (data != null && data.length > limitCount) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Bitmap tmpBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            int times = 1;
            double percentage = 1.0D;

            while (!isFinish && times <= 10) {
                percentage = Math.pow(0.8D, (double) times);
                int compress_datas = (int) (100.0D * percentage);
                tmpBitmap.compress(Bitmap.CompressFormat.JPEG, compress_datas, outputStream);
                if (outputStream != null && outputStream.size() < limitCount) {
                    isFinish = true;
                } else {
                    outputStream.reset();
                    ++times;
                }
            }

            if (outputStream != null) {
                byte[] outputStreamByte = outputStream.toByteArray();
                if (!tmpBitmap.isRecycled()) {
                    tmpBitmap.recycle();
                }

                if (outputStreamByte.length > limitCount) {
                    LogUtil.e("BitmapUtil - compressBitmap", "compressBitmap cannot compress to " + limitCount + ", after compress size=" +
                            outputStreamByte.length);
                }

                return outputStreamByte;
            }
        }

        return data;
    }
}
