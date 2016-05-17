package com.nju.util;

/**
 * Created by cun on 2016/4/12.
 */
public class WhoScanUtil {

    public static String access(String chooseText) {
        switch (chooseText) {
            case Constant.ONLY_ME_SCAN:
                return 6 + "";
            case Constant.SAME_YEAR_NOT_COLLEGE:
                return 5 + "";
            case Constant.SAME_NOT_YEAR_NOT_COLLEGE:
                return 4 + "";
            case Constant.SAME_COLLEGE_NOT_YEAR:
                return 3 + "";
            case Constant.SAME_YEAR_COLLEAGE:
                return 2 + "";
            case Constant.ALL_SACN:
                return 1 + "";
        }
        return 1 + "";
    }

    public static String accessStr(int value) {
        value = value +1;
        switch (value) {
            case 6:
                return Constant.ONLY_ME_SCAN;
            case 5:
                return Constant.SAME_YEAR_NOT_COLLEGE;
            case 4:
                return Constant.SAME_NOT_YEAR_NOT_COLLEGE;
            case 3:
                return Constant.SAME_COLLEGE_NOT_YEAR;
            case 2:
                return Constant.SAME_YEAR_COLLEAGE;
            case 1:
                return Constant.ALL_SACN;
        }
        return 1 + "";
    }

}
