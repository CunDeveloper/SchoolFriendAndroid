package com.nju.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

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
    private ExpandableListView mExpandableListView;
    private WhoScanListAdapter mAdapter;
    private int choosedPosition;
    private Button mFinishBn;
    public static WhoScanFragment newInstance( ) {
        WhoScanFragment fragment = new WhoScanFragment();
        return fragment;
    }

    public WhoScanFragment() {
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
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.fragment_who_scan_expandablelistView);
        mExpandableListView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
        final ArrayList<Entry> labelInfo = loadLabel();
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                labelInfo.get(choosedPosition).setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.publish_content_edittext_bg));
                choosedPosition = groupPosition;
                labelInfo.get(choosedPosition).setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.btn_check_buttonless_on));
                return false;
            }
        });
        mAdapter = new WhoScanListAdapter(getContext(),labelInfo);
        mExpandableListView.setAdapter(mAdapter);
        new LoadData().execute("who_scan.xml");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle(getString(R.string.who_scan));
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        mFinishBn = getHostActivity().getMenuBn();
        mFinishBn.setText(getString(R.string.finish));
        mFinishBn.setVisibility(View.VISIBLE);
    }

    private ArrayList<Entry> loadLabel(){
        ArrayList<Entry> groupList = new ArrayList<>();
        String[] bigLabel = getResources().getStringArray(R.array.who_scan_group);
        String[] smallLabel = getResources().getStringArray(R.array.who_san_small_group);
        Entry entry;
        for (int i =0;i<bigLabel.length;i++) {
            entry = new Entry();
            entry.setBigLabel(bigLabel[i]);
            entry.setSamllLabel(smallLabel[i]);
            if(i == 0){
                choosedPosition = 0;
                entry.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.btn_check_buttonless_on));
            }
            else{
                entry.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.publish_content_edittext_bg));
            }
            if (i == 2){
                String[] bigChildLabel = getResources().getStringArray(R.array.who_scan_partily_big_scan);
                String[] smallChildLabel = getResources().getStringArray(R.array.who_scan_partily_small_scan);
                ArrayList<Entry> childList = new ArrayList<>();
                Entry childEntry;
                SharedPreferences sharedPreferences = getHostActivity().getSharedPreferences();
                int size = sharedPreferences.getInt(getString(R.string.size),0);
                for (int j =1;j<=size;j++){
                    childEntry = new Entry();
                    childEntry.setBigLabel(sharedPreferences.getString(getString(R.string.school)+j,""));
                    childEntry.setSamllLabel(sharedPreferences.getString(getString(R.string.xueyuan)+j,""));
                    childList.add(childEntry);
                }
                childEntry = new Entry();
                childEntry.setBigLabel(getString(R.string.editor_label));
                childList.add(childEntry);
                entry.setChildItems(childList);
            }
            else {
                entry.setChildItems(new ArrayList<Entry>());
            }
            groupList.add(entry);
        }
        return groupList;
    }

    private class LoadData extends AsyncTask<String,Void,ArrayList<WhoScan>> {

        @Override
        protected void onPostExecute(ArrayList<WhoScan> whoScans) {
            super.onPostExecute(whoScans);
            for (WhoScan whoScan:whoScans) {
                Log.e(TAG,whoScan.getBigLabel()+"=="+whoScan.getChoosedLabel()+"=="+whoScan.getSmallLabel());
                for (WhoScan.ChildItem childItem:whoScan.getChildItems()) {
                    Log.e(TAG,childItem.getChildBigLabel()+"=="+childItem.getChildSmallLabel());
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
            Log.e(TAG,groupList.getLength()+"===");
            int length = groupList.getLength();
            ArrayList<WhoScan> whoScans = new ArrayList<>();
            WhoScan whoScan ;
            WhoScan.ChildItem childItem;
            ArrayList<WhoScan.ChildItem> childItems;
            for (int i =0;i<length;i++) {
                whoScan = new WhoScan();
                Node node = groupList.item(i);
                NodeList nodeList = node.getChildNodes();
                childItems = new ArrayList<>();
                for (int j=0;j<nodeList.getLength();j++) {
                    Node node1 = nodeList.item(j);
                    String name = node1.getNodeName();
                    Log.e(TAG, name);
                    switch (name){
                        case "bigLabel":{
                            whoScan.setBigLabel(node1.getTextContent());
                            break;
                        }
                        case "smallLabel":{
                            whoScan.setSmallLabel(node1.getTextContent());
                            break;
                        }
                        case "drwable":{
                            whoScan.setChoosedLabel(node1.getTextContent());
                            break;
                        }
                        case "child":{
                            NodeList nodeList1 = node1.getChildNodes();
                            childItem = new WhoScan.ChildItem();
                            for (int k=0;k<nodeList1.getLength();k++) {
                                Node node2 = nodeList1.item(k);
                                String name1 =node2.getNodeName();
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


