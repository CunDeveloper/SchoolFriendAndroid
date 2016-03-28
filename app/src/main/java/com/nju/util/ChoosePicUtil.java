package com.nju.util;

import com.nju.activity.BaseActivity;
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
            BaseActivity.LocalStack<BaseFragment> fragments = fragment.getHostActivity().getBackStack();
            while (fragments.size() > 0) {
                BaseFragment baseFragment = fragments.pop();
                if (baseFragment instanceof PublishDynamicFragment) {
                    PublishDynamicFragment dynamicFragment = (PublishDynamicFragment) baseFragment;
                    dynamicFragment.setImages(imageWrappers);
                    fragment.getHostActivity().open(dynamicFragment);
                    break;
                } else if (baseFragment instanceof RecommendPublishFragment) {
                    RecommendPublishFragment recommendPublishFragment = (RecommendPublishFragment) baseFragment;
                    recommendPublishFragment.setImages(imageWrappers);
                    fragment.getHostActivity().open(recommendPublishFragment);
                    break;
                } else if (baseFragment instanceof AskPublishFragment) {
                    AskPublishFragment askPublishFragment = (AskPublishFragment) baseFragment;
                    askPublishFragment.setImages(imageWrappers);
                    fragment.getHostActivity().open(askPublishFragment);
                    break;
                } else if (baseFragment instanceof PublishVoiceFragment) {
                    PublishVoiceFragment publishVoiceFragment = (PublishVoiceFragment) baseFragment;
                    publishVoiceFragment.setImages(imageWrappers);
                    fragment.getHostActivity().open(publishVoiceFragment);
                    break;
                }
            }
        }
//            if (label.equals(PublishTextWithPicsFragment.TAG)){
//                fragment.getHostActivity().open(PublishTextWithPicsFragment.newInstance(imageWrappers));
//            }else if (label.equals(RecommendPublishFragment.TAG)){
//                fragment.getHostActivity().open(RecommendPublishFragment.newInstance(fragment.getString(R.string.publish_recommend),imageWrappers));
//            }
//            else if (label.equals(PublishVoiceFragment.TAG)){
//                fragment.getHostActivity().open(PublishVoiceFragment.newInstance(fragment.getString(R.string.publish_voice),imageWrappers));
//            }
//            else if (label.equals(AskPublishFragment.TAG)){
//                fragment.getHostActivity().open(AskPublishFragment.newInstance(fragment.getString(R.string.publsh_ask),imageWrappers));
//            }
//            else if (label.equals(PublishDynamicFragment.TAG)){
//                fragment.getHostActivity().open(PublishDynamicFragment.newInstance(fragment.getString(R.string.publish_dynamic),imageWrappers));
//            }
    }

    private static void peekFragment(BaseFragment fragment, ArrayList<ImageWrapper> images) {
        BaseActivity.LocalStack<BaseFragment> fragments = fragment.getHostActivity().getBackStack();
        while (fragments.size() > 0) {
            BaseFragment baseFragment = fragments.pop();
            if (baseFragment instanceof PublishDynamicFragment) {
                fragment.getHostActivity().open(baseFragment);
                break;
            } else if (baseFragment instanceof RecommendPublishFragment) {
                fragment.getHostActivity().open(baseFragment);
                break;
            } else if (baseFragment instanceof AskPublishFragment) {
                fragment.getHostActivity().open(baseFragment);
                break;
            } else if (baseFragment instanceof PublishVoiceFragment) {
                fragment.getHostActivity().open(baseFragment);
                break;
            }
        }
    }

}
