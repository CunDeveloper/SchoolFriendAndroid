package com.nju.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.adatper.WhoScanListAdapter;
import com.nju.model.Entry;
import com.nju.model.WhoScan;
import com.nju.util.Divice;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WhoScanFragment extends BaseFragment {

    public static final String TAG = WhoScanFragment.class.getSimpleName();
    private int mChoosePosition;

    public WhoScanFragment() {
    }

    public static WhoScanFragment newInstance() {
        return new WhoScanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_who_scan, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        ExpandableListView mExpandableListView = (ExpandableListView) view.findViewById(R.id.fragment_who_scan_expandablelistView);
        mExpandableListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        final ArrayList<Entry> labelInfo = loadLabel();
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                labelInfo.get(mChoosePosition).setDrawable("");
                mChoosePosition = groupPosition;
                labelInfo.get(mChoosePosition).setDrawable(getString(R.string.fa_check_icon));
                if (groupPosition < 2) {
                    BaseActivity.LocalStack stack = getHostActivity().getBackStack();
                    stack.pop();
                    BaseFragment fragment = (BaseFragment) stack.peek();
                    if (fragment.getClass().getSimpleName().equals(PublishDynamicFragment.TAG)) {
                        PublishDynamicFragment dynamicFragment = (PublishDynamicFragment) fragment;
                        TextView bigText = (TextView) v.findViewById(R.id.who_scan_group_item_small_label);
                        dynamicFragment.setWhoScan(bigText.getText().toString());
                        getHostActivity().open(dynamicFragment);
                    } else if (fragment instanceof PublishVoiceFragment) {
                        PublishVoiceFragment voiceFragment = (PublishVoiceFragment) fragment;
                        TextView bigText = (TextView) v.findViewById(R.id.who_scan_group_item_small_label);
                        voiceFragment.setWhoScan(bigText.getText().toString());
                        getHostActivity().open(voiceFragment);
                    }
                }
                return false;
            }
        });
        WhoScanListAdapter mAdapter = new WhoScanListAdapter(this, labelInfo);
        mExpandableListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle(getString(R.string.who_scan));
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        Button mFinishBn = getHostActivity().getMenuBn();
        mFinishBn.setText(getString(R.string.finish));
        mFinishBn.setEnabled(true);
        mFinishBn.setAlpha(1);
        mFinishBn.setVisibility(View.VISIBLE);
    }

    private ArrayList<Entry> loadLabel() {
        ArrayList<Entry> groupList = new ArrayList<>();
        String[] bigLabel = getResources().getStringArray(R.array.who_scan_group);
        String[] smallLabel = getResources().getStringArray(R.array.who_san_small_group);
        Entry entry;
        for (int i = 0; i < bigLabel.length; i++) {
            entry = new Entry();
            entry.setBigLabel(bigLabel[i]);
            entry.setSmallLabel(smallLabel[i]);
            if (i == 0) {
                mChoosePosition = 0;
                entry.setDrawable(getString(R.string.fa_check_icon));
            } else {
                entry.setDrawable("");
            }
            if (i == 2) {
                ArrayList<Entry> childList = new ArrayList<>();
                Entry childEntry;
                String[] onlySee = getResources().getStringArray(R.array.only_people_see);
                for (int j = 0; j < onlySee.length; j++) {
                    childEntry = new Entry();
                    childEntry.setBigLabel(onlySee[j]);
                    childList.add(childEntry);
                }
                entry.setChildItems(childList);
            } else {
                entry.setChildItems(new ArrayList<Entry>());
            }
            groupList.add(entry);
        }
        return groupList;
    }

    private class LoadData extends AsyncTask<String, Void, ArrayList<WhoScan>> {

        @Override
        protected void onPostExecute(ArrayList<WhoScan> whoScans) {
            super.onPostExecute(whoScans);
            for (WhoScan whoScan : whoScans) {
                Log.e(TAG, whoScan.getBigLabel() + "==" + whoScan.getChooseLabel() + "==" + whoScan.getSmallLabel());
                for (WhoScan.ChildItem childItem : whoScan.getChildItems()) {
                    Log.e(TAG, childItem.getChildBigLabel() + "==" + childItem.getChildSmallLabel());
                }
            }
        }

        @Override
        protected ArrayList<WhoScan> doInBackground(String... params) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                InputStream inputStream = getActivity().getAssets().open(params[0]);
                DocumentBuilder db = factory.newDocumentBuilder();
                InputSource inputSource = new InputSource(inputStream);
                return praiseDom(db.parse(inputSource));
            } catch (ParserConfigurationException e) {
                Log.e(TAG, e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e(TAG, e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }

        private ArrayList<WhoScan> praiseDom(Document document) {
            NodeList groupList = document.getElementsByTagName("group");
            Log.e(TAG, groupList.getLength() + "===");
            int length = groupList.getLength();
            ArrayList<WhoScan> whoScans = new ArrayList<>();
            WhoScan whoScan;
            WhoScan.ChildItem childItem;
            ArrayList<WhoScan.ChildItem> childItems;
            for (int i = 0; i < length; i++) {
                whoScan = new WhoScan();
                Node node = groupList.item(i);
                NodeList nodeList = node.getChildNodes();
                childItems = new ArrayList<>();
                for (int j = 0; j < nodeList.getLength(); j++) {
                    Node node1 = nodeList.item(j);
                    String name = node1.getNodeName();
                    Log.e(TAG, name);
                    switch (name) {
                        case "bigLabel": {
                            whoScan.setBigLabel(node1.getTextContent());
                            break;
                        }
                        case "smallLabel": {
                            whoScan.setSmallLabel(node1.getTextContent());
                            break;
                        }
                        case "drwable": {
                            whoScan.setChooseLabel(node1.getTextContent());
                            break;
                        }
                        case "child": {
                            NodeList nodeList1 = node1.getChildNodes();
                            childItem = new WhoScan.ChildItem();
                            for (int k = 0; k < nodeList1.getLength(); k++) {
                                Node node2 = nodeList1.item(k);
                                String name1 = node2.getNodeName();
                                if (name1.equals("childBigLabel")) {
                                    childItem.setChildBigLabel(node2.getTextContent());
                                } else if (name1.equals("childSmallLabel")) {
                                    childItem.setChildSmallLabel(node2.getTextContent());
                                }
                            }
                            childItems.add(childItem);
                            break;
                        }
                    }
                    whoScan.setChildItems(childItems);

                }
                whoScans.add(whoScan);
            }
            return whoScans;
        }
    }
}


