package com.nju.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.nju.activity.R;
import com.nju.adatper.UserLocationAdapter;
import com.nju.model.LocationInfo;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserLocationFragment extends Fragment {

    private static final String TAG = UserLocationFragment.class.getSimpleName();
    private static final int OK = 0 ;
    private TencentLocationManager mLocationManager;
    private TencentLocationListener listener;
    private List<LocationInfo> lists ;
    private ProgressBar mProgressBar;
    private ListView mListView;
    private UserLocationAdapter mLocationAdapter;
    private int choosedPosition = -1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == OK) {
                mLocationManager.removeUpdates(listener);
                List<LocationInfo> temlists = (List<LocationInfo>) msg.obj;
                if(temlists!=null && temlists.size()>0) {
                    lists.remove(0);
                    lists.addAll(temlists);
                    Collections.sort(lists, new LocatCompator());
                    mLocationAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
            }
        }
    };

    public static UserLocationFragment newInstance() {
        UserLocationFragment fragment = new UserLocationFragment();
        return fragment;
    }

    public UserLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_location, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(getString(R.string.title_activity_user_loaction));
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.publish_text_with_pics_toolbar);
        Button button = (Button) toolbar.findViewById(R.id.publish_text_with_pics_toolbar_finish_button);
        button.setVisibility(View.GONE);
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
                headImageView.setImageDrawable(getResources().getDrawable(R.drawable.btn_check_buttonless_on));
                if(choosedPosition != -1) {
                    lists.get(choosedPosition).setSelectBg(getResources().getDrawable(R.drawable.publish_content_edittext_bg));
                    choosedPosition = -1;
                    mLocationAdapter.notifyDataSetChanged();
                }
            }
        });

        mListView.addHeaderView(headerView);
        lists = new ArrayList<>();
        LocationInfo info = new LocationInfo();
        lists.add(info);
        mLocationAdapter = new UserLocationAdapter(lists,getActivity());
        mListView.setAdapter(mLocationAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lists.get(position - 1).setSelectBg(getResources().getDrawable(R.drawable.btn_check_buttonless_on));
                if (choosedPosition == -1){
                    headImageView.setImageDrawable(getResources().getDrawable(R.drawable.publish_content_edittext_bg));
                } else{
                    lists.get(choosedPosition).setSelectBg(getResources().getDrawable(R.drawable.publish_content_edittext_bg));
                }
                choosedPosition = position-1;
                mLocationAdapter.notifyDataSetChanged();
            }
        });
    }

    private  class MyLocationListener implements TencentLocationListener{

        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            LocationInfo info = null;
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
                info.setSelectBg(getResources().getDrawable(R.drawable.publish_content_edittext_bg));
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

    private class LocatCompator implements Comparator<LocationInfo> {
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

}
