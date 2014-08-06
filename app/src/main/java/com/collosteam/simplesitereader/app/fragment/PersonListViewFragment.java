package com.collosteam.simplesitereader.app.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.collosteam.simplesitereader.api.Utils;
import com.collosteam.simplesitereader.api.data.Person;
import com.collosteam.simplesitereader.api.data.User;
import com.collosteam.simplesitereader.app.adapter.MyBaseAdapter;
import com.collosteam.simplesitereader.app.fragment.dummy.DummyContent;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the Callbacks
 * interface.
 */
public class PersonListViewFragment extends ListFragment {

    private static final String TAG = "{PersonListViewFragment}" ;

    public interface OnPersonItemClickListener {
        public void onPersonClick(ListView listView, int position, Person person);
    }

    public void setOnPersonItemClickListener(OnPersonItemClickListener onPersonItemClickListener) {
        this.onPersonItemClickListener = onPersonItemClickListener;
    }

    private OnPersonItemClickListener onPersonItemClickListener;

    public static PersonListViewFragment newInstance() {
        PersonListViewFragment fragment = new PersonListViewFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonListViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListAdapter adapter = new MyBaseAdapter(getActivity(), Utils.getPersonsList());

        setListAdapter(adapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.


      Toast.makeText(getActivity(), (Utils.getPersonsList().get(position)).toString(), Toast.LENGTH_SHORT).show();

        Log.d(TAG, "onPersonItemClickListener = "+onPersonItemClickListener);

        if (onPersonItemClickListener != null) {
            Person person = (Person) getListView().getItemAtPosition(position);
            onPersonItemClickListener.onPersonClick(l, position, person);
        }
    }



}
