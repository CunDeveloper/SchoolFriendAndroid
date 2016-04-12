package com.nju.fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.View.SchoolFriendDialog;
import com.nju.activity.NetworkInfoEvent;
import com.nju.activity.R;
import com.nju.adatper.AlumniVoiceItemAdapter;
import com.nju.adatper.CollageAdapter;
import com.nju.db.db.service.AlumniVoiceDbService;
import com.nju.http.ResponseCallback;
import com.nju.http.request.PostRequestJson;
import com.nju.http.response.ParseResponse;
import com.nju.model.AlumniVoice;
import com.nju.service.AlumniVoiceService;
import com.nju.service.CacheIntentService;
import com.nju.util.BottomToolBar;
import com.nju.util.CloseRequestUtil;
import com.nju.util.Constant;
import com.nju.util.DateUtil;
import com.nju.util.Divice;
import com.nju.util.FragmentUtil;
import com.nju.util.ListViewHead;
import com.nju.util.SchoolFriendGson;
import com.nju.util.SearchViewUtil;
import com.nju.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class AlumniVoiceFragment extends BaseFragment {

    private static final String TAG = AlumniVoiceFragment.class.getSimpleName();
    private PostRequestJson mRequestJson;
    private SwipeRefreshLayout mRefreshLayout;
    private ArrayList<AlumniVoice> mVoices = new ArrayList<>();
    private RelativeLayout mFootView;
    private AlumniVoiceItemAdapter mAlumniVoiceItemAdapter;
    private ResponseCallback callback = new ResponseCallback() {
        @Override
        public void onFail(Exception error) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceFragment.this)){
                ToastUtil.ShowText(getContext(), getString(R.string.fail_info_tip));
                mRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.e(TAG, error.getMessage());
                mFootView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onSuccess(String responseBody) {
            if (FragmentUtil.isAttachedToActivity(AlumniVoiceFragment.this)){
                Log.i(TAG, responseBody);
                ParseResponse parseResponse = new ParseResponse();
                try {
                    Object object = parseResponse.getInfo(responseBody,AlumniVoice.class);
                    if (object != null){
                        ArrayList majorAsks = (ArrayList) object;
                        if (majorAsks.size()>0){
                            for (Object obj :majorAsks){
                                 AlumniVoice   alumniVoice = (AlumniVoice) obj;
                                Log.i(TAG, SchoolFriendGson.newInstance().toJson(alumniVoice));
                                mVoices.add(alumniVoice);
                            }
                            Collections.sort(mVoices,new AlumniVoiceSort());
                            int length = mVoices.size();
                            if (length>10){
                                for (int i = length-1;i>10;i--){
                                    mVoices.remove(mVoices.get(i));
                                }
                            }
                            mAlumniVoiceItemAdapter.notifyDataSetChanged();
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.VOICE_PRE_ID,mVoices.get(0).getId()).apply();
                            getHostActivity().getSharedPreferences().edit()
                                    .putInt(Constant.VOICE_NEXT_ID,mVoices.get(mVoices.size()-1).getId()).apply();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    mRefreshLayout.setRefreshing(false);
                    mFootView.setVisibility(View.GONE);
                }
            }
        }
    };

    public static AlumniVoiceFragment newInstance() {
        Log.i(TAG,"new Fragment");
        return  new AlumniVoiceFragment();
    }

    public AlumniVoiceFragment() {
        new ExeCacheTask(this).execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"on create exe");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tu_cao, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        setListView(view);
        BottomToolBar.showVoiceTool(this, view);
        setUpOnRefreshListener(view);
        SearchViewUtil.setUp(this,view);
        return view;
    }

    private void  setListView(View view){

        ListView listView = (ListView) view.findViewById(R.id.listView);
        ListViewHead.setUp(this, view, listView);
        mFootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.list_footer, listView, false);
        mFootView.setVisibility(View.GONE);
        listView.addFooterView(mFootView);
        mAlumniVoiceItemAdapter = new AlumniVoiceItemAdapter(this, mVoices);
        listView.setAdapter(mAlumniVoiceItemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(AlumniVoiceItemDetail.newInstance(mVoices.get(position)));
            }
        });
        mFootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShortText(getContext(),"hhh");
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == (mAlumniVoiceItemAdapter.getCount())) {
                    mFootView.setVisibility(View.VISIBLE);
                    mRequestJson = AlumniVoiceService.queryVoices(AlumniVoiceFragment.this, callback, Constant.ALL,Constant.NEXT);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        final int[] position = {0};
        TextView textView = (TextView) getActivity().findViewById(R.id.main_viewpager_menu_more);
        MaterialDialog.ListCallbackSingleChoice listCallback= new MaterialDialog.ListCallbackSingleChoice(){

            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                position[0] = i;
                mRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(true);
                        mRequestJson = AlumniVoiceService.queryVoices(AlumniVoiceFragment.this,callback,Constant.ALL,Constant.PRE);
                    }
                });
                setTitle(charSequence.toString());
                return true;
            }
        };
        final SchoolFriendDialog dialog  = SchoolFriendDialog.singleChoiceListDialog(getContext(), getString(R.string.chooseType), getResources().getStringArray(R.array.voiceType), listCallback);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setSelectedIndex(position[0]);
                dialog.show();
            }
        });
    }



    private void setUpOnRefreshListener(View view){
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mRequestJson = AlumniVoiceService.queryVoices(AlumniVoiceFragment.this, callback, Constant.ALL,Constant.PRE);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestJson = AlumniVoiceService.queryVoices(AlumniVoiceFragment.this, callback, Constant.ALL,Constant.PRE);
            }
        });
    }

    @Subscribe
    public void onNetStateMessageState(NetworkInfoEvent event){
        if (event.isConnected()){
            mRequestJson = AlumniVoiceService.queryVoices(AlumniVoiceFragment.this, callback, Constant.ALL,Constant.PRE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
        CloseRequestUtil.close(mRequestJson);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle(getString(R.string.alumni_heart));
        getHostActivity().display(5);
    }

    private void setTitle(String title){
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mVoices != null && mVoices.size()>0){
            Intent intent = new Intent(getContext(),CacheIntentService.class);
            intent.putExtra(Constant.LABEL,Constant.ALUMNI_VOICE);
            intent.putExtra(Constant.ALUMNI_VOICE,mVoices);
            getContext().startService(intent);
        }
    }

    private static class AlumniVoiceSort implements Comparator<AlumniVoice>{
        @Override
        public int compare(AlumniVoice lhs, AlumniVoice rhs) {
            final int lhsId = lhs.getId();
            final int rhsId = rhs.getId();
            if (lhsId > rhsId) {
                return -1;
            } else if (lhsId < rhsId) {
                return 1;
            }
            return 0;
        }
    }

    private static class ExeCacheTask extends AsyncTask<Void,Void,ArrayList<AlumniVoice>>
    {
        private final WeakReference<AlumniVoiceFragment> mAlumniVoiceWeakRef;
        public ExeCacheTask(AlumniVoiceFragment  alumniVoiceFragment){
            this.mAlumniVoiceWeakRef = new WeakReference<>(alumniVoiceFragment);
        }
        @Override
        protected ArrayList<AlumniVoice> doInBackground(Void... params) {
            AlumniVoiceFragment alumniVoiceFragment = mAlumniVoiceWeakRef.get();
            if (alumniVoiceFragment!=null){
                return new AlumniVoiceDbService(alumniVoiceFragment.getContext()).getAlumniVoice();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AlumniVoice> alumniVoices) {
            super.onPostExecute(alumniVoices);
            AlumniVoiceFragment alumniVoiceFragment = mAlumniVoiceWeakRef.get();
            if (alumniVoiceFragment!=null){
                if (alumniVoices != null && alumniVoices.size()>0){
                    Log.i(TAG,SchoolFriendGson.newInstance().toJson(alumniVoices));
                    Collections.sort(alumniVoices, new AlumniVoiceSort());
                    alumniVoiceFragment.mVoices.addAll(alumniVoices);
                    alumniVoiceFragment.mAlumniVoiceItemAdapter.notifyDataSetChanged();
                }else {
                    alumniVoiceFragment.getHostActivity().getSharedPreferences().edit()
                            .putInt(Constant.VOICE_PRE_ID,0).apply();
                    alumniVoiceFragment.getHostActivity().getSharedPreferences().edit()
                            .putInt(Constant.VOICE_NEXT_ID,0).apply();
                }
            }
        }
    }

}
