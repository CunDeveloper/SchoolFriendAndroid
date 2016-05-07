package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.AlumniVoiceItemAdapter;
import com.nju.http.HttpManager;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.model.AlumniVoice;
import com.nju.test.TestData;
import com.nju.util.CloseRequestUtil;
import com.nju.util.FragmentUtil;
import com.nju.util.ToastUtil;

import java.util.ArrayList;


public class AlumniVoiceItemFragment extends BaseFragment {

    private static final String TAG = AlumniVoiceItemFragment.class.getSimpleName();
    private static final String PARAM_TYPE = "param";
    private static String mType;
    private ArrayList<AlumniVoice> voices;
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemFragment.this)) {
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceItemFragment.this)) {
                Log.i(TAG, responseBody);
                mRefreshLayout.setRefreshing(false);
            }
        }
    };

    public AlumniVoiceItemFragment() {
        // Required empty public constructor
    }

    public static AlumniVoiceItemFragment newInstance(String type) {
        AlumniVoiceItemFragment fragment = new AlumniVoiceItemFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(PARAM_TYPE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alumni_voice_item, container, false);
        initListView(view);
        setUpOnRefreshListener(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void initListView(View view) {
        voices = TestData.getVoicesData();
        ListView listView = (ListView) view.findViewById(R.id.alumni_voice_item_list);
        AlumniVoiceItemAdapter adapter = new AlumniVoiceItemAdapter(this, voices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(AlumniVoiceItemDetailFragment.newInstance(voices.get(position)));
            }
        });
    }

    private void setUpOnRefreshListener(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                updateVoices();
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateVoices();
            }
        });
    }

    private void updateVoices() {
        mRequestJson = new PostRequestJson("https://api.myjson.com/bins/3ucpf", "", callback);
        HttpManager.getInstance().exeRequest(mRequestJson);
    }

    @Override
    public void onStop() {
        super.onStop();
        CloseRequestUtil.close(mRequestJson);
    }

}
