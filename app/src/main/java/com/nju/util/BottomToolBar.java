package com.nju.util;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nju.activity.R;
import com.nju.adatper.CollageAdapter;
import com.nju.adatper.LabelAdapter;
import com.nju.event.MessageEvent;
import com.nju.event.MessageLabelEvent;
import com.nju.event.RecommendWorkTypeEvent;
import com.nju.fragment.BaseFragment;
import com.nju.model.DegreeInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by cun on 2016/4/5.
 */
public class BottomToolBar {
    private static final String TAG = BottomToolBar.class.getSimpleName();
    private static SchoolFriendGson gson = SchoolFriendGson.newInstance();
    private static Map<String, ArrayList<String>> mCollegeMap;
    private static ArrayList<String> mVoiceLabels;
    private static ArrayList<String> mAskLabels;
    private static ArrayList<DegreeInfo> mDegreeInfos;

    private static void initMap(BaseFragment fragment) {
        if (mCollegeMap == null || mCollegeMap.size() == 0) {
            String collegeJson = fragment.getHostActivity().getSharedPreferences()
                    .getString(Constant.COLLEGES, "");
            if (!collegeJson.equals("")) {
                Log.i(TAG,collegeJson);
                mCollegeMap = gson.fromJsonToMap(collegeJson);
            } else {
                mCollegeMap = new HashMap<>();
            }

        }
        if (mDegreeInfos == null || mDegreeInfos.size() == 0) {
            String degreeStr = fragment.getHostActivity().getSharedPreferences().getString(fragment.getString(R.string.person_info),"");
            if (!degreeStr .equals(""))
            {
                mDegreeInfos = gson.fromJsonToList(degreeStr,DegreeInfo.class);
            }else {
                mDegreeInfos = new ArrayList<>();
            }
        }
        if (mVoiceLabels == null) {
            Set<String> voiceLabelSet = fragment.getHostActivity().getSharedPreferences()
                    .getStringSet(Constant.VOICE_LABEL, new HashSet<String>());
            mVoiceLabels = new ArrayList<>(voiceLabelSet);
        }

        if (mAskLabels == null) {
            Set<String> askLabelSet = fragment.getHostActivity().getSharedPreferences()
                    .getStringSet(Constant.ASK_LABEL, new HashSet<String>());
            mAskLabels = new ArrayList<>(askLabelSet);
        }
    }

    public static void show(final BaseFragment fragment, View view) {
        initMap(fragment);
        final ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        final ListView listView = (ListView) view.findViewById(R.id.college_choose_dialog_listview);
        final RelativeLayout mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        openChooseDialog(mCollegeMainLayout, listView, view);
        hideChooseDialog(mCollegeMainLayout, listView, view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCollegeMainLayout.setVisibility(View.GONE);
            }
        });
        Set<String> levels = fragment.getHostActivity().getSharedPreferences().getStringSet(fragment.getString(R.string.level), new HashSet<String>());

        for (String level : levels) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(fragment.getContext()).inflate(R.layout.bottom_choose_textview, null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(fragment.getString(R.string.undergraduate))) {
                textView.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.primayDark));
            }
            layout.addView(textView);
            mChooseLevelViews.add(textView);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView tv = (TextView) v;
                    String level = tv.getText().toString();
                    if (!level.equals(Constant.ALL)) {
                        if (mCollegeMap != null) {
                            ArrayList<String> colleges = mCollegeMap.get(level);
                            for (DegreeInfo dInfo : mDegreeInfos) {
                                if (dInfo.getLevel().equals(level)) {
                                    if (colleges != null && !colleges.contains(dInfo.getUniversityName())){
                                        colleges.add(0, dInfo.getUniversityName());
                                    }
                                    break;
                                }
                            }
                            if (colleges != null) {
                                listView.setAdapter(new CollageAdapter(fragment.getContext(), colleges));
                                listView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), tv);
                    return true;
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    mCollegeMainLayout.setVisibility(View.GONE);
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), mTV);
                    EventBus.getDefault().post(new MessageEvent(mTV.getText().toString()));
                }
            });
        }
    }

    public static void showVoiceTool1(final BaseFragment fragment, View view) {
        initMap(fragment);
        final ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        final ListView listView = (ListView) view.findViewById(R.id.mListview);
        final Spinner spinner = (Spinner) view.findViewById(R.id.mSpinner);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(fragment.getContext(),
                android.R.layout.simple_dropdown_item_1line, mVoiceLabels.toArray(new String[mVoiceLabels.size()]));

        final RelativeLayout mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        openChooseDialog(mCollegeMainLayout, listView, view);
        hideChooseDialog(mCollegeMainLayout, listView, view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCollegeMainLayout.setVisibility(View.GONE);
            }
        });
        Set<String> levels = fragment.getHostActivity().getSharedPreferences().getStringSet(fragment.getString(R.string.level), new HashSet<String>());

        for (String level : levels) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(fragment.getContext()).inflate(R.layout.bottom_choose_textview, null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(fragment.getString(R.string.undergraduate))) {
                textView.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.primayDark));
            }
            layout.addView(textView);
            mChooseLevelViews.add(textView);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView tv = (TextView) v;
                    String level = tv.getText().toString();
                    if (!level.equals(Constant.ALL)) {
                        ArrayList<String> colleges = mCollegeMap.get(level);
                        for (DegreeInfo dInfo : mDegreeInfos) {
                            if (dInfo.getLevel().equals(level)) {
                                colleges.add(0, dInfo.getUniversityName());
                                break;
                            }
                        }
                        if (colleges != null) {
                            listView.setAdapter(new CollageAdapter(fragment.getContext(), colleges));
                            listView.setVisibility(View.VISIBLE);
                            spinner.setAdapter(adapter);
                            spinner.setVisibility(View.VISIBLE);
                        }

                    }
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), tv);
                    return true;
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    mCollegeMainLayout.setVisibility(View.GONE);
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), mTV);
                    EventBus.getDefault().post(new MessageEvent(mTV.getText().toString()));
                }
            });
        }
    }


    public static void showRecommendTool(final BaseFragment fragment, View view) {
        initMap(fragment);
        final ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        final Spinner spinner = (Spinner) view.findViewById(R.id.mSpinner);
        final RelativeLayout mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        final LinearLayout typeLinearLayout = (LinearLayout) view.findViewById(R.id.recommendType);
        typeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Set<String> levels = fragment.getHostActivity().getSharedPreferences().getStringSet(fragment.getString(R.string.level), new HashSet<String>());
        initDialogRecommend(mCollegeMainLayout, typeLinearLayout, view);
        for (String level : levels) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(fragment.getContext()).inflate(R.layout.bottom_choose_textview, null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(fragment.getString(R.string.undergraduate))) {
                textView.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.primayDark));
            }
            layout.addView(textView);
            mChooseLevelViews.add(textView);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView tv = (TextView) v;
                    String level = tv.getText().toString();
                    if (!level.equals(Constant.ALL)) {
                        ArrayList<String> colleges = mCollegeMap.get(level);
                        for (DegreeInfo dInfo : mDegreeInfos) {
                            if (dInfo.getLevel().equals(level)) {
                                if (!colleges.contains(dInfo.getUniversityName()))
                                    colleges.add(0, dInfo.getUniversityName());
                                break;
                            }
                        }
                        if (colleges != null) {
                            typeLinearLayout.setVisibility(View.VISIBLE);
                            final ArrayAdapter<String> adapter = new ArrayAdapter<>(fragment.getContext(),
                                    android.R.layout.simple_dropdown_item_1line, colleges.toArray(new String[colleges.size()]));
                            spinner.setAdapter(adapter);
                            spinner.setVisibility(View.VISIBLE);
                        }

                    }
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), tv);
                    return true;
                }
            });
            final RadioButton rBn1 = (RadioButton) view.findViewById(R.id.rBn1);
            rBn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        EventBus.getDefault().post(new RecommendWorkTypeEvent(rBn1.getText().toString()));
                    }
                    mCollegeMainLayout.setVisibility(View.GONE);
                }
            });

            final RadioButton rBn2 = (RadioButton) view.findViewById(R.id.rBn2);
            rBn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        EventBus.getDefault().post(new RecommendWorkTypeEvent(rBn2.getText().toString()));
                    }
                    mCollegeMainLayout.setVisibility(View.GONE);
                }
            });

            final RadioButton rBn3 = (RadioButton) view.findViewById(R.id.rBn3);
            rBn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        EventBus.getDefault().post(new RecommendWorkTypeEvent(rBn3.getText().toString()));
                    }
                    mCollegeMainLayout.setVisibility(View.GONE);
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    mCollegeMainLayout.setVisibility(View.GONE);
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), mTV);
                    EventBus.getDefault().post(new MessageEvent(mTV.getText().toString()));
                }
            });
        }
    }

    public static String getRecommendType(View view) {
        RadioButton rBn1 = (RadioButton) view.findViewById(R.id.rBn1);
        RadioButton rBn2 = (RadioButton) view.findViewById(R.id.rBn2);
        RadioButton rBn3 = (RadioButton) view.findViewById(R.id.rBn3);
        if (rBn1.isChecked()) {
            return rBn1.getText().toString();
        }
        if (rBn2.isChecked()) {
            return rBn2.getText().toString();
        }
        if (rBn3.isChecked()) {
            return rBn3.getText().toString();
        }
        return rBn1.getText().toString();
    }


    public static void showMajorTool(final BaseFragment fragment, View view) {
        initMap(fragment);
        final ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        final GridView gridView = (GridView) view.findViewById(R.id.mGridView);
        final RelativeLayout mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        final Spinner spinner = (Spinner) view.findViewById(R.id.mSpinner);
        openChooseDialogHasGrid(mCollegeMainLayout, gridView, view);
        hideChooseDialogHasGrid(mCollegeMainLayout, gridView, view);
        Set<String> levels = fragment.getHostActivity().getSharedPreferences().getStringSet(fragment.getString(R.string.level), new HashSet<String>());
        for (String level : levels) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(fragment.getContext()).inflate(R.layout.bottom_choose_textview, null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(fragment.getString(R.string.undergraduate))) {
                textView.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.primayDark));
            }
            layout.addView(textView);
            mChooseLevelViews.add(textView);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView tv = (TextView) v;
                    if (!tv.getText().toString().equals(Constant.ALL)) {
                        gridView.setAdapter(new LabelAdapter(fragment.getContext(), mAskLabels));
                        gridView.setVisibility(View.VISIBLE);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView tv = (TextView) view;
                                EventBus.getDefault().post(new MessageLabelEvent(tv.getText().toString()));
                                mCollegeMainLayout.setVisibility(View.GONE);
                                spinner.setVisibility(View.GONE);
                            }
                        });
                    }
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), tv);
                    return true;
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    mCollegeMainLayout.setVisibility(View.GONE);
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), mTV);
                    EventBus.getDefault().post(new MessageEvent(mTV.getText().toString()));
                }
            });
        }
    }


    public static void showVoiceTool(final BaseFragment fragment, View view) {
        initMap(fragment);
        final ArrayList<TextView> mChooseLevelViews = new ArrayList<>();
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.college_choose_dialog_choose_layout);
        final GridView gridView = (GridView) view.findViewById(R.id.mGridView);
        final RelativeLayout mCollegeMainLayout = (RelativeLayout) view.findViewById(R.id.college_choose_dialog_relayout);
        final Spinner spinner = (Spinner) view.findViewById(R.id.mSchoolSpinner);
        initVoiceDialog(mCollegeMainLayout, gridView, spinner, view);
        Set<String> levels = fragment.getHostActivity().getSharedPreferences().getStringSet(fragment.getString(R.string.level), new HashSet<String>());
        for (String level : levels) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            TextView textView = (TextView) LayoutInflater.from(fragment.getContext()).inflate(R.layout.bottom_choose_textview, null);
            textView.setLayoutParams(param);
            textView.setText(level);
            if (textView.getText().toString().equals(fragment.getString(R.string.undergraduate))) {
                textView.setTextColor(ContextCompat.getColor(fragment.getContext(), R.color.primayDark));
            }
            layout.addView(textView);
            mChooseLevelViews.add(textView);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView tv = (TextView) v;
                    String level = tv.getText().toString();
                    if (!level.equals(Constant.ALL)) {
                        ArrayList<String> colleges = mCollegeMap.get(level);
                        for (DegreeInfo dInfo : mDegreeInfos) {
                            if (dInfo.getLevel().equals(level)) {
                                if (!colleges.contains(dInfo.getUniversityName()))
                                    colleges.add(0, dInfo.getUniversityName());
                                break;
                            }
                        }
                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(fragment.getContext(),
                                android.R.layout.simple_dropdown_item_1line, colleges.toArray(new String[colleges.size()]));
                        spinner.setVisibility(View.VISIBLE);
                        spinner.setAdapter(adapter);
                        gridView.setAdapter(new LabelAdapter(fragment.getContext(), mVoiceLabels));
                        gridView.setVisibility(View.VISIBLE);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView tv = (TextView) view;
                                EventBus.getDefault().post(new MessageLabelEvent(tv.getText().toString()));
                                mCollegeMainLayout.setVisibility(View.GONE);
                                spinner.setVisibility(View.GONE);
                            }
                        });
                    }
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), tv);
                    return true;
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView mTV = (TextView) v;
                    mCollegeMainLayout.setVisibility(View.GONE);
                    changeLevelTVColor(mChooseLevelViews, fragment.getContext(), mTV);
                    EventBus.getDefault().post(new MessageEvent(mTV.getText().toString()));
                }
            });
        }
    }


    private static void changeLevelTVColor(ArrayList<TextView> mChooseLevelViews, Context context, TextView view) {
        for (TextView textView : mChooseLevelViews) {
            if (view.getText().toString().equals(textView.getText().toString())) {
                view.setTextColor(ContextCompat.getColor(context, R.color.primayDark));
            } else {
                textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }
        }
    }

    private static void openChooseDialog(final RelativeLayout mCollegeMainLayout, final ListView listView, final View view) {
        FloatingActionButton floatBn = (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        final Spinner spinner = (Spinner) view.findViewById(R.id.mSpinner);
        floatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        });
    }

    private static void initDialogRecommend(final RelativeLayout mCollegeMainLayout, final LinearLayout typeLinearLayout, final View view) {
        FloatingActionButton floatBn = (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        final Spinner spinner = (Spinner) view.findViewById(R.id.mSpinner);
        floatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
                typeLinearLayout.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        });
        View mView = view.findViewById(R.id.college_choose_dialog_view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
                typeLinearLayout.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        });
    }

    private static void openChooseDialogHasGrid(final RelativeLayout mCollegeMainLayout, final GridView gridView, final View view) {
        FloatingActionButton floatBn = (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        floatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            }
        });
    }

    private static void initVoiceDialog(final RelativeLayout mCollegeMainLayout, final GridView gridView, final Spinner spinner, final View view) {
        FloatingActionButton floatBn = (FloatingActionButton) view.findViewById(R.id.college_choose_dialog_actionBn);
        floatBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        });

        View mView = view.findViewById(R.id.college_choose_dialog_view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        });
    }


    private static void hideChooseDialogHasGrid(final RelativeLayout mCollegeMainLayout, final GridView gridView, View view) {
        View mView = view.findViewById(R.id.college_choose_dialog_view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
            }
        });
    }


    private static void hideChooseDialog(final RelativeLayout mCollegeMainLayout, final ListView listView, View view) {
        View mView = view.findViewById(R.id.college_choose_dialog_view);
        final Spinner spinner = (Spinner) view.findViewById(R.id.mSpinner);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCollegeMainLayout.setVisibility(View.GONE);
                listView.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        });
    }
}
