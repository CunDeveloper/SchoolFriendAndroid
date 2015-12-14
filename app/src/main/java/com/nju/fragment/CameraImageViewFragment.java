package com.nju.fragment;

        import android.os.Bundle;
        import android.support.v4.view.ViewPager;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.RelativeLayout;
        import android.widget.Toast;

        import com.nju.View.CustomImageVIew;
        import com.nju.activity.R;
        import com.nju.adatper.OriginPicViewPagerAdapter;
        import com.nju.model.Image;
        import com.nju.util.Divice;

        import java.util.ArrayList;


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
        initViewPageTouchListener();
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
    }

    private void dependLabelConditionInit() {
        if (mLabel.equals(getResources().getString(R.string.choosedReview))) {
            mFinishButton.setText(getResources().getString(R.string.finish));
            mCheckBox.setChecked(false);
            mFinishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {

        }
    }

    private void initViewPagerSlideListener (){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getHostActivity().getToolBar().setTitle((position+1)+""+"/"+mImgs.size());
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

    private void initViewPageTouchListener() {
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"hello",Toast.LENGTH_LONG).show();
            }
        });
    }

}
