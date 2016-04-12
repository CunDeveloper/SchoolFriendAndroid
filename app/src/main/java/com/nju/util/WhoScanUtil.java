package com.nju.util;

import android.view.View;

/**
 * Created by cun on 2016/4/12.
 */
public class WhoScanUtil {

    public static String access(String chooseText){
        switch (chooseText){
            case Constant.ONLY_ME_SCAN:
                return 6+"";
            case Constant.SAME_YEAR_NOT_COLLEGE:
                return 5+"";
            case Constant.SAME_NOT_YEAR_NOT_COLLEGE:
                return 4+"";
            case Constant.SAME_COLLEGE_NOT_YEAR:
                return 3+"";
            case Constant.SAME_YEAR_COLLEAGE:
                return 2+"";
            case Constant.ALL_SACN:
                return 1+"";
        }
        return 1+"";
    }

}
