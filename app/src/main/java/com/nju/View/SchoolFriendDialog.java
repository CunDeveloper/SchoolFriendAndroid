package com.nju.View;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.activity.MessageDeleteEvent;
import com.nju.activity.MessageEvent;
import com.nju.activity.R;
import com.nju.event.MessageComplainEvent;
import com.nju.util.Constant;

import org.greenrobot.eventbus.EventBus;

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
        builder.positiveText(context.getString(R.string.exit))
                .negativeText(context.getString(R.string.cancle));

        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog remindDialog(final Context context, final String title, final String content) {
        Builder builder = new Builder(context).title(title).content(content);
        builder.positiveText(context.getString(R.string.sure));
        builder.negativeText(context.getString(R.string.cancle));
        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog listDialog(final Context context, final String[] itmes) {
        Builder builder = new Builder(context)
                .items(itmes).contentColor(ContextCompat.getColor(context, android.R.color.black))
                .itemsCallback(new ListItemCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        EventBus.getDefault().post(new MessageEvent(charSequence.toString()));
                    }
                });
        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog listItemDialog(final Context context, final String[] itmes ) {
        Builder builder = new Builder(context)
                .items(itmes).contentColor(ContextCompat.getColor(context, android.R.color.black))
                .itemsCallback(new ListItemCallback(){
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        EventBus.getDefault().post(new MessageDeleteEvent(charSequence.toString()));
                    }
                });
        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog listItemDialog(final Context context, final String[] itmes,ListItemCallback callback) {
        Builder builder = new Builder(context)
                .items(itmes).contentColor(ContextCompat.getColor(context, android.R.color.black))
                .itemsCallback(callback);
        return new SchoolFriendDialog(builder);
    }

    public static SchoolFriendDialog listItemDialog(final Context context) {
        String[] items = {
                "收藏",
                "投诉"
        };
        Builder builder = new Builder(context)
                .items(items).contentColor(ContextCompat.getColor(context, android.R.color.black))
                .itemsCallback(new ListItemCallback(){
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        EventBus.getDefault().post(new MessageComplainEvent(charSequence.toString()));
                    }
                });
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

    public static SchoolFriendDialog inputDialog(final Context context, final String title, final String content, final ListCallbackSingleChoice listCallback) {
        final Builder builder = new Builder(context)
                .title(title)
                .content(content)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                        EventBus.getDefault().post(new MessageEvent(charSequence.toString()));
                    }
                });
        return new SchoolFriendDialog((builder));
    }

    public static SchoolFriendDialog changeBgDialog(final Context context, final String content) {
        final Builder builder = new Builder(context)
                .content(content);
        return new SchoolFriendDialog((builder));
    }

    public static abstract class ListItemCallback implements ListCallback {

    }
}
