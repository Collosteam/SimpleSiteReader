package com.collosteam.simplesitereader.app.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.collosteam.simplesitereader.app.activity.WebViewActivity;
import com.collosteam.simplesitereader.app.adapter.MyCursorAdapter;
import com.collosteam.simplesitereader.app.db.LessonsColumns;
import com.collosteam.simplesitereader.app.provider.MyContentProvider;
import com.collosteam.simplesitereader.app.service.MyIntentService;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ItemFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> ,
        LessonsColumns{


    private static final String TAG ="{ItemFragment}" ;
    CursorAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        getActivity().startService(new Intent(getActivity(), MyIntentService.class));


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getLoaderManager().initLoader(100001, null, this);
        //Uri.withAppendedPath()
        Cursor cursor = getActivity().getContentResolver().query(MyContentProvider.CONTENT_URI, null, null, null, null);
        mAdapter = new MyCursorAdapter(getActivity(), cursor,true);
        setListAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static String EXTRAS_LESSON_ID = "e.less.id";

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        Cursor cursor = ((Cursor)mAdapter.getItem(position));
        int lessonID = cursor.getInt(cursor.getColumnIndex(_ID));
        intent.putExtra(EXTRAS_LESSON_ID, lessonID);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG , "onCreateLoader id = "+id);
        return new CursorLoader(getActivity(), MyContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG , "onLoadFinished");
        if (mAdapter != null)
            mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG , "onLoaderReset");
        if (mAdapter != null)
            mAdapter.swapCursor(null);
    }
}
