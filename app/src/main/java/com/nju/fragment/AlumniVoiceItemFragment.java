package com.nju.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nju.activity.R;
import com.nju.adatper.AlumniVoiceItemAdapter;
import com.nju.model.AlumniVoice;
import com.nju.model.AuthorInfo;
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
        testData();
        initListView(view);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void testData(){
        voices = new ArrayList<>();
        AlumniVoice voice = new AlumniVoice();
        voice.setCommentCount(1);voice.setPraiseCount(1);
        voice.setTitle("大四学生就业建议");
        voice.setContent("1.树立自己就业的信心。\n" +
                "大学生数目的增多，只能说明自己所处的平台更高、更宽，应该对自己就业有信心，\n" +
                "坚信自己一定能找到适合自己的企业和岗位。 　\n" +
                "2.树立找到适合自己职业发展机会和平台的理念。适合的是最好的。 　\n" +
                "3．应该树立良好的就业观念，品德素质要足够重视，不应该受冷落，品行兼修，并且都优秀。 　　\n" +
                "4.多参与用人单位就业讲座或者其它就业讲座建立双方更流畅、有效的沟通交流渠道。大学生对用人单位不了解，\n" +
                "用人单位的人才要求不能畅通、有效的让大学生了解，这其中缺乏有效沟通的平台。 　\n" +
                "5.学校邀请就业比较成功的大学生定期回校进行就业交流沟通.多让部分有社会经验和工作经验的人士回校讲课,演讲,或者成为授课教师。(特别是营销类,管理类)。 " +
                "\n6.学校尽量建立企业的HR、管理层来校..." );
        voice.setDate("2015-03-20");
        voice.setSimpleDesc("1.树立自己就业的信心。大学生数目的增多，只能说明自己...");
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorName("张秦");authorInfo.setLabel("南京大学 软件学院");
        voice.setAuthorInfo(authorInfo);
        voices.add(voice);
    }
    private void initListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.alumni_voice_item_list);
        AlumniVoiceItemAdapter adapter = new AlumniVoiceItemAdapter(getContext(),voices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(AlumniVoiceItemDetail.newInstance(voices.get(position),mType));
            }
        });
    }


}
