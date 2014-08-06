package com.collosteam.simplesitereader.app.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.app.db.LessonsColumns;
import com.collosteam.simplesitereader.app.provider.MyContentProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment implements LessonsColumns {

    private static String EXTRA_ID = "e.id";
    private int lesson_id;

    public WebViewFragment() {
    }

    public static WebViewFragment newInstance(int id) {
        WebViewFragment fragment = new WebViewFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, id);

        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getArguments() != null) {
            lesson_id = getArguments().getInt(EXTRA_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toast.makeText(getActivity(), "Current id - " + lesson_id, Toast.LENGTH_SHORT).show();

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }
        });


        Uri uri = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, String.valueOf(lesson_id));
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);


        if (cursor.moveToFirst()) {

            String urlString = cursor.getString(cursor.getColumnIndex(URL_STR));
            String title = cursor.getString(cursor.getColumnIndex(TITLE));
            getActivity().setTitle(title);
            webView.loadUrl(urlString);
        } else {
            Toast.makeText(getActivity(), "Not data!", Toast.LENGTH_SHORT).show();
        }

    }
}
