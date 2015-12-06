package util.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.nju.activity.R;


/**
 * Created by xiaojuzhang on 2015/11/20.
 */
public class CommentDialog extends DialogFragment {

    public CommentDialog(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_comment,container);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.TOP| Gravity.LEFT);
        params.x = 150;
        params.y = 150;
        window.setAttributes(params);
        return view;
    }

}
