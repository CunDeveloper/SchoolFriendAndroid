package com.nju.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nju.activity.BaseActivity;
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
    private static final int OK = 0;
    private TencentLocationManager mLocationManager;
    private TencentLocationListener listener;
    private List<LocationInfo> mLocationInfoList;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private UserLocationAdapter mLocationAdapter;
    private int choosePosition = -1;
    private Handler mHandler = new MyHandler(this);

    public UserLocationFragment() {
    }

    public static UserLocationFragment newInstance() {
        return new UserLocationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_location, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        mProgressBar = (ProgressBar) view.findViewById(R.id.user_location_progressBar);
        listener = new MyLocationListener();
        initListContent(view);
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(5000);
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        request.setAllowCache(false);
        mLocationManager = TencentLocationManager.getInstance(getActivity());
        int code = mLocationManager.requestLocationUpdates(request, listener);
        Log.e(TAG, code + "==");
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle(getString(R.string.user_loaction));
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        getHostActivity().getMenuBn().setVisibility(View.GONE);

    }

    private void initListContent(View view) {
        mListView = (ListView) view.findViewById(R.id.user_location_listView);
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.user_location_header, mListView, false);
        final TextView headImageView = (TextView) headerView.findViewById(R.id.user_location_header_checkBox);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headImageView.setText(getString(R.string.fa_check_icon));
                if (choosePosition != -1) {
                    mLocationInfoList.get(choosePosition).setIconName("");
                    choosePosition = -1;
                    mLocationAdapter.notifyDataSetChanged();
                }
            }
        });

        mListView.addHeaderView(headerView);
        mLocationInfoList = new ArrayList<>();
        LocationInfo info = new LocationInfo();
        mLocationInfoList.add(info);
        mLocationAdapter = new UserLocationAdapter(mLocationInfoList, getActivity());
        mListView.setAdapter(mLocationAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLocationInfoList.get(position - 1).setIconName(getString(R.string.fa_check_icon));
                if (choosePosition == -1) {
                    headImageView.setText("");
                } else {
                    mLocationInfoList.get(choosePosition).setIconName("");
                }
                choosePosition = position - 1;
                mLocationAdapter.notifyDataSetChanged();
                TextView locationText = (TextView) view.findViewById(R.id.user_location_item_name);
                BaseActivity.LocalStack stack = getHostActivity().getBackStack();
                stack.pop();
                final BaseFragment fragment = (BaseFragment) stack.peek();
                if (fragment instanceof PublishDynamicFragment) {
                    PublishDynamicFragment dynamiCfragment = (PublishDynamicFragment) fragment;
                    dynamiCfragment.setLocation(locationText.getText().toString());
                    getHostActivity().open(fragment);
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(listener);
        }
    }

    private static class LocatCompator implements Comparator<LocationInfo> {
        @Override
        public int compare(LocationInfo lhs, LocationInfo rhs) {
            if (lhs.getDistance() < rhs.getDistance()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    private static class MyHandler extends Handler {

        private final WeakReference<UserLocationFragment> mUserLocationFragment;

        private MyHandler(UserLocationFragment userLocationFragment) {
            this.mUserLocationFragment = new WeakReference<>(userLocationFragment);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            UserLocationFragment fragment = mUserLocationFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if (msg.what == OK) {
                    fragment.mLocationManager.removeUpdates(fragment.listener);
                    List<LocationInfo> tempLists = (List<LocationInfo>) msg.obj;
                    if (tempLists != null && tempLists.size() > 0) {
                        fragment.mLocationInfoList.remove(0);
                        fragment.mLocationInfoList.addAll(tempLists);
                        Collections.sort(fragment.mLocationInfoList, new LocatCompator());
                        fragment.mLocationAdapter.notifyDataSetChanged();
                    }
                    fragment.mProgressBar.setVisibility(View.GONE);
                    fragment.mListView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class MyLocationListener implements TencentLocationListener {

        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            LocationInfo info;
            info = new LocationInfo();
            info.setLocationName(tencentLocation.getProvince());
            info.setAddress("");
            info.setDistance(1.0);
            List<LocationInfo> locationLists = new ArrayList<>(40);
            locationLists.add(info);
            for (TencentPoi poi : tencentLocation.getPoiList()) {
                info = new LocationInfo();
                info.setLocationName(poi.getName());
                info.setAddress(poi.getAddress());
                info.setDistance(poi.getDistance());
                info.setIconName("");
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

}
