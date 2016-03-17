package com.nju.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.AlumniVoiceItemAdapter;
import com.nju.model.AlumniVoice;
import com.nju.model.AuthorInfo;
import com.nju.test.TestData;
import com.nju.util.ToastUtil;

import java.util.ArrayList;


public class AlumniVoiceItemFragment extends BaseFragment {

    private static final String PARAM_TYPE = "param";
    private static String mType;
    private ArrayList<AlumniVoice> voices;
    public static AlumniVoiceItemFragment newInstance(String type) {
        AlumniVoiceItemFragment fragment = new AlumniVoiceItemFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TYPE,type);
        fragment.setArguments(args);
        return fragment;
    }

    public AlumniVoiceItemFragment() {
        // Required empty public constructor
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
        View view= inflater.inflate(R.layout.fragment_alumni_voice_item, container, false);
        initListView(view);
        setUpOnRefreshListener(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }


    private void initListView(View view) {
        voices = TestData.getVoicesData();
        ListView listView = (ListView) view.findViewById(R.id.alumni_voice_item_list);
        AlumniVoiceItemAdapter adapter = new AlumniVoiceItemAdapter(getContext(),voices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(AlumniVoiceItemDetail.newInstance(voices.get(position), mType));
            }
        });
    }

    private void setUpOnRefreshListener(View view){
        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateVoices();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateVoices()  {
        new Thread(){
            @Override
            public void run(){
                try {
                    Thread.sleep(12000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
