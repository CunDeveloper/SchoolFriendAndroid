package com.nju.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojuzhang on 2016/3/16.
 */
public class ShareUtil {

    public static void share(Context context){
        File imagePath = new File(context.getFilesDir(), "images");
        File newFile = new File(imagePath, "test.jpg");
        Uri contentUri = FileProvider.getUriForFile(context, "com.example.xiaojuzhang.test1.fileprovider", newFile);
        ToastUtil.ShowText(context,contentUri.toString());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        shareIntent.setData(contentUri);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/jpg");

        context.startActivity(Intent.createChooser(shareIntent, "share to other app"));
    }
}
