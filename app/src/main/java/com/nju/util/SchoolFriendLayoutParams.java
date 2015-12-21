package com.nju.util;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nju.activity.R;


/**
 * Created by xiaojuzhang on 2015/11/23.
 */
public class SchoolFriendLayoutParams {

    private static final String TAG = SchoolFriendLayoutParams.class.getSimpleName();
    private Context mContext;
    public SchoolFriendLayoutParams(Context context) {
        mContext = context;
    }
    public LinearLayout.LayoutParams imgParams(){
        int screenWidth = Divice.getDisplayWidth(mContext);
        int pxPadding = pxPadding();
        int headWidth = (int) Divice.convertDpToPixel(80,mContext);
        int width = (screenWidth-6*pxPadding-headWidth)/3;
        int height = width;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(0,pxPadding/2,pxPadding/2,0);
        return params;
    }

    public LinearLayout.LayoutParams imgPhoneParams(float headIconDP, float paddingDp){
        int screenWidth = Divice.getDisplayWidth(mContext);
        int pxPadding = (int) Divice.convertDpToPixel(headIconDP+paddingDp,mContext);
        int imgMargin = (int) Divice.convertDpToPixel(20,mContext);
        int width = (screenWidth-pxPadding)/3+imgMargin/2;
        int height = width;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(0,imgMargin/2,imgMargin/2,0);
        return params;
    }

    public LinearLayout.LayoutParams locationParams(){
        LinearLayout.LayoutParams locationParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        locationParams.topMargin = pxPhonePadding()/2;
        return locationParams;
    }

    public LinearLayout.LayoutParams noSoftInputParams(int subHeight){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, subHeight);
        return params;
    }

    public RelativeLayout.LayoutParams noSoftInputRelayoutParams(int subHeight){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, subHeight);
        return params;
    }

    public FrameLayout.LayoutParams noSoftInputParamsFrame(int subHeight){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, subHeight);
        return params;
    }

    public LinearLayout.LayoutParams noSoftInputParams(int subHeight,View view){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, subHeight-view.getHeight());
        return params;
    }

    public LinearLayout.LayoutParams noSoftInputParams(int subHeight,int paddingHeight){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, subHeight+paddingHeight+100);
        return params;
    }

    public LinearLayout.LayoutParams softInputParams(int subHeight,int bottomViewHeight,View toolBar){
        int scrollWidth = subHeight - (int) Divice.convertDpToPixel(bottomViewHeight,mContext)
                - toolBar.getHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollWidth);
        return params;
    }

    public LinearLayout.LayoutParams softInputParams(int subHeight,int bottomViewHeight,int statasBarHeight){
        int scrollWidth = subHeight - (int) Divice.convertDpToPixel(bottomViewHeight,mContext)
                -statasBarHeight;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollWidth);
        return params;
    }

    public RelativeLayout.LayoutParams softInputRelLayoutParams(int subHeight,int bottomViewHeight,int statasBarHeight){
        int scrollWidth = subHeight - (int) Divice.convertDpToPixel(bottomViewHeight,mContext)
                -statasBarHeight;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollWidth);
        return params;
    }

    public LinearLayout.LayoutParams softInputParams(int subHeight,int bottomViewHeight){
        int scrollWidth = subHeight - (int) Divice.convertDpToPixel(bottomViewHeight,mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollWidth);
        return params;
    }

    public FrameLayout.LayoutParams softInputParamsFrame(int subHeight,int bottomViewHeight){
        int scrollWidth = subHeight - (int) Divice.convertDpToPixel(bottomViewHeight,mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollWidth);
        return params;
    }

    public LinearLayout.LayoutParams softInputParams1(int subHeight,int bottomViewHeight){
        int scrollWidth = subHeight - bottomViewHeight;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, scrollWidth);
        return params;
    }

    public AbsListView.LayoutParams  chooseImgParams(){
        int screenWidth = Divice.getDisplayWidth(mContext);
        int width = screenWidth/3;
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(width,width);
        return params;
    }

    public LinearLayout.LayoutParams  uploadImgParams(){
        int screenWidth = Divice.getDisplayWidth(mContext);
        float paddingLeft = mContext.getResources().getDimension(R.dimen.padding_right);
        int paddingWidth = (int) Divice.convertDpToPixel(2*paddingLeft,mContext);
        int width = (screenWidth-paddingWidth)/4;
        int height = width;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        return params;
    }

    public LinearLayout.LayoutParams  uploadImgLeftParams(){
        float paddingLeft = mContext.getResources().getDimension(R.dimen.padding_right);
        int paddingWidth = (int) Divice.convertDpToPixel(paddingLeft,mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,paddingWidth,paddingWidth);
        return params;
    }
    public LinearLayout.LayoutParams  uploadImgNoLeftParams(){
        float paddingLeft = mContext.getResources().getDimension(R.dimen.padding_right);
        int paddingWidth = (int) Divice.convertDpToPixel(paddingLeft,mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,paddingWidth);
        return params;
    }
    private int pxPadding(){
        int padding = (int) mContext.getResources().getDimension(R.dimen.padding_right);
        int pxPadding = (int) Divice.convertDpToPixel(padding, mContext);
        return pxPadding;
    }

    private int pxPhonePadding(){
        int padding = (int) mContext.getResources().getDimension(R.dimen.phone_apadding);
        int pxPadding = (int) Divice.convertDpToPixel(padding, mContext);
        return pxPadding;
    }

    public ViewGroup.LayoutParams noSoftInputParams(int subHeight, View view , int statusBarHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, subHeight-view.getHeight()-statusBarHeight);
        return params;
    }

    public ViewGroup.LayoutParams noSoftInputParamsFragment(int subHeight, View view , int statusBarHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, subHeight+statusBarHeight+80);
        return params;
    }

    public static LinearLayout.LayoutParams phoneImageMarginBottom(Context context){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        float marginDp = context.getResources().getDimension(R.dimen.phone_apadding);
        int bottom = (int) Divice.convertDpToPixel(marginDp,context);
        params.setMargins(0,0,0,bottom/2);
        return params;
    }
}
