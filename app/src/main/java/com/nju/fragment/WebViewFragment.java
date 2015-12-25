package com.nju.fragment;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nju.activity.R;
import com.nju.animation.ProgressBarAnimation;
import com.nju.util.Divice;


public class WebViewFragment extends BaseFragment {

    private  WebView mWebView;
    private AppCompatActivity mActivity;

    public static WebViewFragment newInstance() {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity = (AppCompatActivity) getActivity();

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.fragment_web_view_progressBar);
        progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        mWebView = (WebView) view.findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //progressBar.setProgress(newProgress);
                //progressBar.invalidate();
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getContext(), "Oh no!" + description, Toast.LENGTH_LONG).show();
            }
        });
        mWebView.loadUrl("http://developer.android.com/");
        ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,0,100);
        animation.setDuration(122222);
        progressBar.startAnimation(animation);
        return view;
    }

    private static class MwebViewClient extends WebViewClient{

    }

}
