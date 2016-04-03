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

    public static void sync(BaseFragment fragment,View view){
        Set<String> levels = fragment.getHostActivity().getSharedPreferences().
                getStringSet(fragment.getString(R.string.level), new HashSet<String>());
        LinearLayout syncLayout = (LinearLayout) view.findViewById(R.id.sync_choose_layout);
        CheckBox graduateCB = (CheckBox) view.findViewById(R.id.graduate_cb);
        CheckBox masterCB = (CheckBox) view.findViewById(R.id.master_cb);
        CheckBox doctorCB = (CheckBox) view.findViewById(R.id.doctor_cb);
        int size = levels.size();
        if (size == 3){
            syncLayout.setVisibility(View.VISIBLE);
            graduateCB.setVisibility(View.VISIBLE);
            masterCB.setVisibility(View.VISIBLE);
        } else if (size == 4){
            syncLayout.setVisibility(View.VISIBLE);
            graduateCB.setVisibility(View.VISIBLE);
            masterCB.setVisibility(View.VISIBLE);
            doctorCB.setVisibility(View.VISIBLE);
        }
    }
}
