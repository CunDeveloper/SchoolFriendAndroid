package util.dialog;

import android.app.Activity;
import android.content.Context;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.activity.R;

/**
 * Created by xiaojuzhang on 2015/11/26.
 */
public class SchoolFriendDialog extends MaterialDialog {
    private SchoolFriendDialog(Builder builder) {
        super(builder);
    }

    public static SchoolFriendDialog showProgressDialog(Context context,String title,String content) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context).title(title).content(content).progress(true,0);
        SchoolFriendDialog testDialog = new SchoolFriendDialog(builder);
        return testDialog;
    }

    public static SchoolFriendDialog exitReminderDialog(final Activity context,String title) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context).title(title);
        builder.positiveText(context.getString(R.string.cancle))
        .negativeText(context.getString(R.string.exit));
        builder.onNegative(new MaterialDialog.SingleButtonCallback() {

            @Override
            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                context.finish();
            }
        });
        SchoolFriendDialog testDialog = new SchoolFriendDialog(builder);
        return testDialog;
    }


}
