package com.nju.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nju.activity.R;
import com.nju.animation.ProgressBarAnimation;
import com.nju.util.Divice;


public class WebViewFragment extends BaseFragment {

    private static final String PARAM_URL = "url";
    private static final String PARAM_TITLE = "title";
    private String mUrl;
    private String mTitle;
    public static WebViewFragment newInstance(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_URL,url);
        fragment.setArguments(args);
        return fragment;
    }

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(PARAM_URL);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        view.setPadding(view.getPaddingLeft(), Divice.getStatusBarHeight(getContext()), view.getPaddingRight(), view.getPaddingBottom());
        initWebView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.delete);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");

        }
        getHostActivity().display(5);
    }

    private void initWebView(View view){
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.fragment_web_view_progressBar);
        progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        WebView mWebView = (WebView) view.findViewById(R.id.webview);
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
        //"file:///android_asset/html/test.html"
        mWebView.loadUrl(mUrl);
        ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,0,100);
        animation.setDuration(122);
        progressBar.startAnimation(animation);
    }

    private static class MwebViewClient extends WebViewClient{

    }

}
