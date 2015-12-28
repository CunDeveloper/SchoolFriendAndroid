package com.nju.model;

import android.graphics.Bitmap;

/**
 * Created by xiaojuzhang on 2015/12/18.
 */
public class BitmapWrapper {
    private String path;
    private String fileName;
    private String fileType;
    private Bitmap bitmap;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
