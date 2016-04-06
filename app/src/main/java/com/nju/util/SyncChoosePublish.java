package com.nju.util;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.nju.activity.R;
import com.nju.fragment.BaseFragment;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by cun on 2016/4/3.
 */
public class SyncChoosePublish {
    private String defaultLevel;
    private CheckBox graduateCB,masterCB,doctorCB;
    private  LinearLayout syncLayout;
    private static SyncChoosePublish choosePublish = null;
    public static SyncChoosePublish newInstance(View view){
        if (choosePublish == null){
            choosePublish = new SyncChoosePublish(view);
        }
        return choosePublish;
    }


    private SyncChoosePublish(View view){
        syncLayout = (LinearLayout) view.findViewById(R.id.sync_choose_layout);
        graduateCB = (CheckBox) view.findViewById(R.id.graduate_cb);
        masterCB = (CheckBox) view.findViewById(R.id.master_cb);
        doctorCB = (CheckBox) view.findViewById(R.id.doctor_cb);
    }

    public  SyncChoosePublish sync(BaseFragment fragment){
        Set<String> levels = fragment.getHostActivity().getSharedPreferences().
                getStringSet(fragment.getString(R.string.level), new HashSet<String>());
        int size = levels.size();
        if (size == 3){
            syncLayout.setVisibility(View.VISIBLE);
            graduateCB.setVisibility(View.VISIBLE);
            masterCB.setVisibility(View.VISIBLE);
            defaultLevel = Constant.MASTER;
        } else if (size == 4){
            syncLayout.setVisibility(View.VISIBLE);
            graduateCB.setVisibility(View.VISIBLE);
            masterCB.setVisibility(View.VISIBLE);
            doctorCB.setVisibility(View.VISIBLE);
            defaultLevel = Constant.DOCTOR;
        }
        return this;
    }

    public String level(){
        String result = "";
        if (graduateCB.isChecked()){
            result = Constant.UNDERGRADUATE;
        }else if (masterCB.isChecked()){
            result = Constant.MASTER;
        }else if (doctorCB.isChecked()){
            result = Constant.DOCTOR;
        } else if (graduateCB.isChecked() && masterCB.isChecked()){
            result = Constant.ALL;
        }else {
            result = defaultLevel;
        }
        return result;
    }
}
