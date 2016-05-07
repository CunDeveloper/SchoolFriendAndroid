package com.nju.util;

import com.nju.model.ContentComment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by cun on 2016/3/29.
 */
public class SortUtil {

    public static ArrayList<ContentComment> softByDate(ArrayList<ContentComment> arrayList) {
        Collections.sort(arrayList, new Comparator<ContentComment>() {
            @Override
            public int compare(ContentComment lhs, ContentComment rhs) {
                final long lhsTime = DateUtil.getTime(lhs.getDate());
                final long rhsTime = DateUtil.getTime(rhs.getDate());
                if (lhsTime > rhsTime) {
                    return -1;
                } else if (lhsTime < rhsTime) {
                    return 1;
                }
                return 0;
            }
        });
        return arrayList;
    }

}
