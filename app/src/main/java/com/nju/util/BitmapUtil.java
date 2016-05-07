package com.nju.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.nju.fragment.BaseFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.BitmapFactory.decodeStream;
import static com.nju.util.Constant.ALUMNI_PUBLIC_PIC_ROOT;

/**
 * Created by cun on 2016/5/4.
 */
public class BitmapUtil {
    private static final String TAG = BitmapUtil.class.getSimpleName();

    public static void saveToFile(final BaseFragment context, final Bitmap bitmap, String fileName) {
        String filePath = alumniRootPic();
        Log.i(TAG, filePath);
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File bitmapFile = new File(dir, fileName);
        Log.i(TAG, bitmapFile.getName());
        if (!bitmapFile.exists()) {
            try {
                final boolean result = bitmapFile.createNewFile();
                FileOutputStream outputStream;
                Log.i(TAG, "EXE SAVE");
                if (result) {
                    outputStream = new FileOutputStream(bitmapFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i(TAG, "SAVE OK");

                    scanPhoto(context, bitmapFile.toString());
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
        }


    }

    private static void scanPhoto(BaseFragment fragment, String fileName) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(fileName);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        //this.cordova.getContext().sendBroadcast(mediaScanIntent); //this is deprecated
        fragment.getActivity().sendBroadcast(mediaScanIntent);
    }

    public static String alumniRootPic() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + ALUMNI_PUBLIC_PIC_ROOT;
    }

    public static Bitmap get(String fileName) {
        String path = alumniRootPic();
        File dir = new File(path);
        boolean isOk = true;
        if (!dir.exists()) {
            isOk = dir.mkdir();
        }
        if (isOk) {
            File bitmapFile = new File(dir, fileName);
            if (bitmapFile.exists()) {
                try {
                    return decodeStream(new FileInputStream(bitmapFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public static boolean deleteFile(String fileName) {
        String path = alumniRootPic();
        File dir = new File(path);
        boolean isOk = true;
        if (!dir.exists()) {
            isOk = dir.mkdir();
        }
        if (isOk) {
            File bitmapFile = new File(dir, fileName);
            if (bitmapFile.exists()) {
                return bitmapFile.delete();
            }
        }
        return false;
    }

    public static File file(String fileName) {
        String path = alumniRootPic();
        File dir = new File(path);
        boolean isOk = true;
        if (!dir.exists()) {
            isOk = dir.mkdir();
        }
        if (isOk) {
            File bitmapFile = new File(dir, fileName);
            if (bitmapFile.exists()) {
                return bitmapFile;
            }
        }
        return null;
    }
}
