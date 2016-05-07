package com.nju.util;

import com.nju.activity.BaseActivity;
import com.nju.activity.R;
import com.nju.fragment.AskPublishFragment;
import com.nju.fragment.BaseFragment;
import com.nju.fragment.PublishDynamicFragment;
import com.nju.fragment.PublishVoiceFragment;
import com.nju.fragment.RecommendPublishFragment;
import com.nju.model.ImageWrapper;

import java.util.ArrayList;

/**
 * Created by xiaojuzhang on 2016/3/23.
 */
public class ChoosePicUtil {

    public static void openFragment(BaseFragment fragment, String label, ArrayList<ImageWrapper> imageWrappers) {
        if (FragmentUtil.isAttachedToActivity(fragment)) {
            BaseActivity.LocalStack<BaseFragment> backStack = fragment.getHostActivity().getBackStack();
            while (backStack.size() > 1) {
                backStack.pop();
                BaseFragment baseFragment = backStack.peek();
                if (label.equals(PublishDynamicFragment.TAG)) {
                    if (baseFragment instanceof PublishDynamicFragment) {
                        //baseFragment = backStack.pop();
                        PublishDynamicFragment dynamicFragment = (PublishDynamicFragment) baseFragment;
                        dynamicFragment.setImages(imageWrappers);
                        fragment.getHostActivity().open(dynamicFragment);
                        break;
                    } else {
                        fragment.getHostActivity().open(PublishDynamicFragment.newInstance(fragment.getString(R.string.publish_dynamic), imageWrappers));
                        break;
                    }
                } else if (label.equals(RecommendPublishFragment.TAG)) {
                    if (baseFragment instanceof RecommendPublishFragment) {
                        //baseFragment = backStack.pop();
                        RecommendPublishFragment recommendPublishFragment = (RecommendPublishFragment) baseFragment;
                        recommendPublishFragment.setImages(imageWrappers);
                        fragment.getHostActivity().open(recommendPublishFragment);
                        break;
                    } else {
                        fragment.getHostActivity().open(AskPublishFragment.newInstance(fragment.getString(R.string.publish_recommend), imageWrappers));
                        break;
                    }
                } else if (label.equals(AskPublishFragment.TAG)) {
                    if (baseFragment instanceof AskPublishFragment) {
                        //baseFragment = backStack.pop();
                        AskPublishFragment askPublishFragment = (AskPublishFragment) baseFragment;
                        askPublishFragment.setImages(imageWrappers);
                        fragment.getHostActivity().open(askPublishFragment);
                        break;
                    } else {
                        fragment.getHostActivity().open(AskPublishFragment.newInstance(fragment.getString(R.string.major_ask), imageWrappers));
                        break;
                    }
                } else if (label.equals(PublishVoiceFragment.TAG)) {
                    if (baseFragment instanceof PublishVoiceFragment) {
                        //baseFragment = backStack.pop();
                        PublishVoiceFragment publishVoiceFragment = (PublishVoiceFragment) baseFragment;
                        publishVoiceFragment.setImages(imageWrappers);
                        fragment.getHostActivity().open(publishVoiceFragment);
                        break;
                    } else {
                        fragment.getHostActivity().open(PublishVoiceFragment.newInstance(fragment.getString(R.string.publish_voice), imageWrappers));
                        break;
                    }
                }
            }
        }
    }
}
