package com.nju.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.nju.activity.R;
import com.nju.adatper.UserLocationAdapter;
import com.nju.model.LocationInfo;
import com.nju.util.Divice;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserLocationFragment extends BaseFragment {

    public static final String TAG = UserLocationFragment.class.getSimpleName();
    private static final int OK = 0 ;
    private TencentLocationManager mLocationManager;
    private TencentLocationListener listener;
    private List<LocationInfo> mLoncationInfolist ;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private UserLocationAdapter mLocationAdapter;
    private int choosedPosition = -1;
    private Handler mHandler = new MyHandler(this);
    public static UserLocationFragment newInstance() {
        return new UserLocationFragment();
    }

    public UserLocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_location, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()),view.getPaddingRight(),view.getPaddingBottom());
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.user_loaction));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        activity.findViewById(R.id.main_viewpager_menu_bn).setVisibility(View.GONE);
        activity.findViewById(R.id.main_viewpager_menu_delete_img).setVisibility(View.GONE);
        activity.findViewById(R.id.main_viewpager_camera_imageView).setVisibility(View.GONE);
        mProgressBar = (ProgressBar)view.findViewById(R.id.user_location_progressBar);
        listener = new MyLocationListener();
        initListContent(view);
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(5000);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        request.setAllowCache(false);
        mLocationManager = TencentLocationManager.getInstance(getActivity());
        int code = mLocationManager.requestLocationUpdates(request,listener);
        Log.e(TAG, code + "==");
        return view;
    }


    private void initListContent(View view) {
        mListView = (ListView)view.findViewById(R.id.user_location_listView);
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.user_location_header, mListView, false);
        final ImageView headImageView = (ImageView) headerView.findViewById(R.id.user_location_header_checkBox);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.btn_check_buttonless_on));
                if(choosedPosition != -1) {
                    mLoncationInfolist.get(choosedPosition).setSelectBg(ContextCompat.getDrawable(getActivity(), R.drawable.publish_content_edittext_bg));
                    choosedPosition = -1;
                    mLocationAdapter.notifyDataSetChanged();
                }
            }
        });

        mListView.addHeaderView(headerView);
        mLoncationInfolist = new ArrayList<>();
        LocationInfo info = new LocationInfo();
        mLoncationInfolist.add(info);
        mLocationAdapter = new UserLocationAdapter(mLoncationInfolist,getActivity());
        mListView.setAdapter(mLocationAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLoncationInfolist.get(position - 1).setSelectBg(ContextCompat.getDrawable(getActivity(), R.drawable.btn_check_buttonless_on));
                if (choosedPosition == -1){
                    headImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.publish_content_edittext_bg));
                } else{
                    mLoncationInfolist.get(choosedPosition).setSelectBg(ContextCompat.getDrawable(getActivity(), R.drawable.publish_content_edittext_bg));
                }
                choosedPosition = position-1;
                mLocationAdapter.notifyDataSetChanged();
            }
        });
    }

    private  class MyLocationListener implements TencentLocationListener{

        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            LocationInfo info;
            info = new LocationInfo();
            info.setLocatName(tencentLocation.getProvince());
            info.setAddress("");
            info.setDistance(1.0);
            List<LocationInfo> locationLists = new ArrayList<>(40);
            locationLists.add(info);
            for(TencentPoi poi:tencentLocation.getPoiList()){
                info = new LocationInfo();
                info.setLocatName(poi.getName());
                info.setAddress(poi.getAddress());
                info.setDistance(poi.getDistance());
                info.setSelectBg(ContextCompat.getDrawable(getActivity(), R.drawable.publish_content_edittext_bg));
                locationLists.add(info);
            }
            Message message = new Message();
            message.what = OK;
            message.obj = locationLists;
            mHandler.sendMessage(message);

        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    }

    private static class LocatCompator implements Comparator<LocationInfo> {
        @Override
        public int compare(LocationInfo lhs, LocationInfo rhs) {
            if(lhs.getDistance()<rhs.getDistance()) {
                return -1;
            }
            else{
                return 1;
            }
        }
    }

    private static class MyHandler extends  Handler {

        private final WeakReference<UserLocationFragment> mUserLocationFragment;

        private MyHandler(UserLocationFragment userLocationFragment) {
            this.mUserLocationFragment = new WeakReference<>(userLocationFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            UserLocationFragment fragment = mUserLocationFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if(msg.what == OK) {
                    fragment.mLocationManager.removeUpdates(fragment.listener);
                    List<LocationInfo> temlists = (List<LocationInfo>) msg.obj;
                    if(temlists!=null && temlists.size()>0) {
                        fragment.mLoncationInfolist.remove(0);
                        fragment.mLoncationInfolist.addAll(temlists);
                        Collections.sort(fragment.mLoncationInfolist, new LocatCompator());
                        fragment.mLocationAdapter.notifyDataSetChanged();
                    }
                    fragment.mProgressBar.setVisibility(View.GONE);
                    fragment.mListView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}
