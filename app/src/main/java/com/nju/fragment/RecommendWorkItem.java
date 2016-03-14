package com.nju.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.RecommendWorkItemAdapter;
import com.nju.model.AuthorInfo;
import com.nju.model.RecommendWork;

import java.util.ArrayList;


public class RecommendWorkItem extends BaseFragment {

    private static final String PARAM_TYPE = "type";
    private static String mType ;
    private static ArrayList<RecommendWork> tests = new ArrayList<>();
    static {
        RecommendWork recommendWork = new RecommendWork();
        recommendWork.setTitle("百度质量部-百度云测试开发工程师");
        recommendWork.setContent("（Android/iOS、server方向）。参与百度云服务器、客户端的质量保证以及质量工具、平台开发 \n" +
                "要求： \n" +
                "（1）计算机相关专业本科以上、实习时间长者优先（至少6个月） \n" +
                "（2）熟悉基本数据结构和算法设计，精通C/C++、Java、PHP, shell中至少一门编程语言 \n" +
                "（3） 熟悉Android/IOS自动化测试工具的使用和编写，如junit、instrumentation、robotium、monkey、monkeyrunner等 、熟悉java、object-C相关经验者优先 \n" +
                "\n" +
                "请将简历投递至: ");
        recommendWork.setEmail("wen_twinkle@126.com");
        recommendWork.setCommentCount(2);
        recommendWork.setType(0);
        recommendWork.setDate("2016-03-10");
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setAuthorId(1);authorInfo.setAuthorName("张小军");
        authorInfo.setLabel("南京大学软件学院2014");
        recommendWork.setAuthor(authorInfo);
        tests.add(recommendWork);
    }

    public static RecommendWorkItem newInstance(String type) {
        RecommendWorkItem fragment = new RecommendWorkItem();
        Bundle args = new Bundle();
        args.putString(PARAM_TYPE, type);

        fragment.setArguments(args);
        return fragment;
    }

    public RecommendWorkItem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType= getArguments().getString(PARAM_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_recommend_work_item, container, false);
        ListView listView = (ListView) view.findViewById(R.id.fragment_recommend_work_item_listview);
        listView.setAdapter(new RecommendWorkItemAdapter(getContext(), tests));
        addListViewClickEvent(listView);
        return view;
    }

    private void addListViewClickEvent(ListView listView){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getHostActivity().open(RecommendWorkItemDetailFragment.newInstance(tests.get(0)));
            }
        });
    }

}
