package com.nju.View;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.activity.R;

/**
 * Created by xiaojuzhang on 2015/11/26.
 */
public class SchoolFriendDialog extends MaterialDialog {
    private SchoolFriendDialog(Builder builder) {
        super(builder);
    }

    public static SchoolFriendDialog showProgressDialog(final Context context, final String title, final String content) {
        Builder builder = new Builder(context).title(title).content(content).progress(true, 0);
        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog showProgressDialogNoTitle(final Context context, final String content) {
        Builder builder = new Builder(context).content(content).contentColor(ContextCompat.getColor(context, android.R.color.black)).progress(true, 0);
        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog exitReminderDialog(final Activity context, String title) {
        Builder builder = new Builder(context).title(title);
        builder.positiveText(context.getString(R.string.cancle))
                .negativeText(context.getString(R.string.exit));

        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog remindDialog(final Context context, final String title, final String content) {
        Builder builder = new Builder(context).title(title).content(content);
        builder.positiveText(context.getString(R.string.sure));
        builder.negativeText(context.getString(R.string.cancle));
        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog listDialog(final Context context, final String[] itmes, final ListItemCallback listCallback) {
        Builder builder = new Builder(context)
                .items(itmes).contentColor(ContextCompat.getColor(context, android.R.color.black))
                .itemsCallback(listCallback);
        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog singleChoiceListDialog(final Context context, final String title, final String[] itmes, final ListCallbackSingleChoice listCallback) {
        final Builder builder = new Builder(context)
                .title(title)
                .items(itmes)
                .itemsColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
                .itemsCallbackSingleChoice(0, listCallback);
        return new SchoolFriendDialog((builder));
    }

    public static abstract class ListItemCallback implements ListCallback {

    }
}
