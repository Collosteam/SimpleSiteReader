package com.collosteam.simplesitereader.app.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.api.data.Person;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class UserContentFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "{UserContentFragmentAndrew}" ;


    private int mSelectedID;
    private Person mPerson;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selectedID Parameter 1.
     * @param person Parameter 2.
     * @return A new instance of fragment UserContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserContentFragment newInstance(int selectedID, Person person) {
        UserContentFragment fragment = new UserContentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, selectedID);
        args.putSerializable(ARG_PARAM2, person);
        fragment.setArguments(args);

        Log.d(TAG, "newInstance = " + fragment);

        return fragment;
    }
    public UserContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectedID = getArguments().getInt(ARG_PARAM1);
            mPerson = (Person) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_content, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvMail = (TextView) view.findViewById(R.id.tvMail);

        tvName.setText(String.format("Name: %1$s and selected id - %2$d",mPerson.getLogin(),mSelectedID));
        tvMail.setText(String.format("Email: %1$s",mPerson.getEmail()));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
