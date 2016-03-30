package com.nju.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.View.SchoolFriendDialog;
import com.nju.activity.R;
import com.nju.adatper.DynamicCollectAdapter;
import com.nju.adatper.VoiceCollectAdapter;
import com.nju.db.db.service.AlumniVoiceCollectDbService;
import com.nju.model.AlumniVoice;
import com.nju.model.DynamicCollect;
import com.nju.test.TestData;
import com.nju.util.Divice;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class VoiceCollectFragment extends BaseFragment {
    private static final String PARAM_TITLE = "paramTitle";
    private static String mTitle;
    private VoiceCollectAdapter mVoiceCollectAdapter;
    private ArrayList<AlumniVoice> mCollectVoices;
    public static VoiceCollectFragment newInstance(String title) {
        VoiceCollectFragment fragment = new VoiceCollectFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }

    public VoiceCollectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(PARAM_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.refresh_listview, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initListView(view);
        new ExeCollectTask(this).execute();
        return view;
    }

    private void initListView(View view){
        mCollectVoices = TestData.getVoicesData();
        ListView listView = (ListView) view.findViewById(R.id.listView);
        mVoiceCollectAdapter = new VoiceCollectAdapter(getContext(),mCollectVoices);
        listView.setAdapter(mVoiceCollectAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(AlumniVoiceItemDetail.newInstance(mCollectVoices.get(position)));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SchoolFriendDialog.listDialog(getContext(), getResources().getStringArray(R.array.collectItem), null).show();
                return true;
            }
        });

}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mTitle);
        }
        getHostActivity().display(6);
    }

    private static class ExeCollectTask extends AsyncTask<Void,Void,ArrayList<AlumniVoice>>
    {
        private final WeakReference<VoiceCollectFragment> mVoiceCollectFragment;
        public ExeCollectTask(VoiceCollectFragment voiceCollectFragment){
            this.mVoiceCollectFragment = new WeakReference<>(voiceCollectFragment);
        }
        @Override
        protected ArrayList<AlumniVoice> doInBackground(Void... params) {
            VoiceCollectFragment collectFragment = mVoiceCollectFragment.get();
            if (collectFragment!=null){
                return new AlumniVoiceCollectDbService(collectFragment.getContext()).getCollects();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AlumniVoice> alumniVoices) {
            super.onPostExecute(alumniVoices);
            VoiceCollectFragment collectFragment = mVoiceCollectFragment.get();
            if (collectFragment!=null){
                if (alumniVoices != null){
                    collectFragment.mCollectVoices.addAll(alumniVoices);
                    collectFragment.mVoiceCollectAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
