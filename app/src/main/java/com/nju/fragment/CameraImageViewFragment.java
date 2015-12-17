package com.nju.fragment;

        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.ListView;
        import android.widget.RelativeLayout;
        import android.widget.Toast;

        import com.nju.View.CustomImageVIew;
        import com.nju.activity.R;
        import com.nju.adatper.OriginPicViewPagerAdapter;
        import com.nju.model.Image;
        import com.nju.util.Constant;
        import com.nju.util.Divice;
        import com.nju.util.SoftInput;

        import java.lang.ref.WeakReference;
        import java.util.ArrayList;
        import java.util.HashSet;

        import static com.nju.adatper.UserCommentItemListAdapter.COMMENT_OK;
        import static com.nju.adatper.UserCommentItemListAdapter.PRAISE_OK;


public class CameraImageViewFragment extends BaseFragment {

    private static final String TAG = CameraImageViewFragment.class.getSimpleName();
    public static final String POSITION = "position";
    private static final String LABEL ="label";
    private ViewPager mViewPager;
    private ArrayList<Image> mImgs;
    private int mPosition;
    private ActionBar mActionBar;
    private Button mFinishButton;
    private CheckBox mCheckBox;
    private String mLabel;//
    private RelativeLayout mBottomLayout;
    private static final int ADD_PIC = 203;
    private static final int SUB_PIC = 204;
    private final ArrayList<Image> mChoosedPic = new ArrayList<>();
    private final HashSet<Integer> mChoosedIndex = new HashSet<>();
    private final Handler mHandler = new MyHandler(this);
    public static CameraImageViewFragment newInstance(ArrayList<Image> imgPaths,int position,String label) {
        CameraImageViewFragment fragment = new CameraImageViewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(TAG,imgPaths);
        args.putInt(POSITION,position);
        args.putString(LABEL,label);
        fragment.setArguments(args);
        return fragment;
    }

    public CameraImageViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgs = getArguments().getParcelableArrayList(TAG);
            mPosition = getArguments().getInt(POSITION);
            mLabel = getArguments().getString(LABEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_camera_image_view, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getActivity()), view.getPaddingRight(), view.getPaddingBottom());
        mCheckBox = (CheckBox) view.findViewById(R.id.fragment_camera_image_view_checkBox);
        mViewPager = (ViewPager) view.findViewById(R.id.fragment_camera_imgage_view_pager);
        mBottomLayout = (RelativeLayout) view.findViewById(R.id.fragment_camera_image_view_bottom_layout);
        mViewPager.setAdapter(new OriginPicViewPagerAdapter(getFragmentManager(), mImgs));
        mViewPager.setCurrentItem(mPosition);
        initViewPagerSlideListener();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHostActivity().getToolBar().setTitle((mPosition + 1) + "" + "/" + mImgs.size());
        getHostActivity().getMenuCameraView().setVisibility(View.GONE);
        getHostActivity().getMenuDeleteView().setVisibility(View.GONE);
        mFinishButton = getHostActivity().getMenuBn();
        mFinishButton.setVisibility(View.VISIBLE);
        dependLabelConditionInit();
        initFinishEvent();
        initCheckBoxEvent();
    }

    private void dependLabelConditionInit() {
        if (mLabel.equals(getString(R.string.choosedReview))) {
            mFinishButton.setText(getResources().getString(R.string.finish)+"("+mImgs.size()+"/9"+")");
            mCheckBox.setChecked(true);
            mChoosedPic.addAll(mImgs);
            for (int i=0;i<mImgs.size();i++){
                mChoosedIndex.add(i);
            }
        } else if (mLabel.equals(getString(R.string.capture_image))){
            mFinishButton.setText(getString(R.string.finish));
            mFinishButton.setEnabled(true);
            mFinishButton.setAlpha(1);

            hideBottomLayout();
        }
    }

    private void initFinishEvent() {
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getHostActivity().open(PublishTextWithPicsFragment.newInstance(mImgs));
                Toast.makeText(getContext(), "HELLO", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initCheckBoxEvent() {
        mCheckBox.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Message message = new Message();
                if (!mCheckBox.isChecked()) {
                    message.what = ADD_PIC;
                    message.obj = mViewPager.getCurrentItem();
                    mChoosedIndex.add(mViewPager.getCurrentItem());
                    mHandler.sendMessage(message);
                } else {
                    message.what = SUB_PIC;
                    message.obj = mViewPager.getCurrentItem();
                    mChoosedIndex.remove(mViewPager.getCurrentItem());
                    mHandler.sendMessage(message);
                }
            }

        });
    }

    private void initViewPagerSlideListener (){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getHostActivity().getToolBar().setTitle((position + 1) + "" + "/" + mImgs.size());
                if (mChoosedIndex.contains(position)) {
                    mCheckBox.setChecked(true);
                } else {
                    mCheckBox.setChecked(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void hideBottomLayout() {
        mBottomLayout.setVisibility(View.GONE);
    }

    public void showBottomLayout() {
        mBottomLayout.setVisibility(View.VISIBLE);
    }

    private static class MyHandler extends Handler {

        private final WeakReference<CameraImageViewFragment> mCameraImageViewFragment;

        private MyHandler(CameraImageViewFragment cameraImageViewFragment) {
            this.mCameraImageViewFragment = new WeakReference<>(cameraImageViewFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            CameraImageViewFragment fragment = mCameraImageViewFragment.get();
            if (fragment != null) {
                super.handleMessage(msg);
                if (fragment.mLabel.equals(fragment.getString(R.string.choosedReview))) {
                    reviewImage(fragment,msg);
                }

            }
        }

        private void reviewImage(CameraImageViewFragment fragment,Message msg) {
            if (msg.what == ADD_PIC) {
                fragment.mChoosedPic.add(fragment.mImgs.get((Integer) msg.obj));
                fragment.mFinishButton.setText(fragment.getString(R.string.finish) + "(" + (fragment.mChoosedPic.size()+1)+"/9)");
            } else if (msg.what == SUB_PIC){
                fragment.mChoosedPic.remove(fragment.mImgs.get((Integer) msg.obj));
                fragment.mFinishButton.setText(fragment.getString(R.string.finish) + "(" + (fragment.mChoosedPic.size() + 1) + "/9)");
            }
        }
    }

}
