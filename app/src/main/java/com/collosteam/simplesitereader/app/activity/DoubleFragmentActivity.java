package com.collosteam.simplesitereader.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.api.data.Person;
import com.collosteam.simplesitereader.api.data.User;
import com.collosteam.simplesitereader.app.fragment.FragmentFactoryToConteiner;
import com.collosteam.simplesitereader.app.fragment.FragmentFactoryToConteiner2;
import com.collosteam.simplesitereader.app.fragment.PersonListViewFragment;
import com.collosteam.simplesitereader.app.fragment.UserContentFragment;

public class DoubleFragmentActivity extends Activity implements PersonListViewFragment.OnPersonItemClickListener {

    private static final String TAG = "{DoubleFragmentActivity}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_fragment);
        //Если это первый запуск
        if (savedInstanceState == null) {

            PersonListViewFragment listViewFragment = PersonListViewFragment.newInstance();
            listViewFragment.setOnPersonItemClickListener(this);

            //Добавляем новый фрагметн со списком
                 new FragmentFactoryToConteiner().setFragmentToConteiner(this, listViewFragment);

             //Добавляем новый фрагметн отображающий содержимое
                 new FragmentFactoryToConteiner2().setFragmentToConteiner(this,UserContentFragment.newInstance(0, new User("Hello", "12345", "h@i.ua")));
        }
       }

    @Override
    public void onPersonClick(ListView listView, int position, Person person) {


        Log.d(TAG, "onPersonClick = "+person);

        getFragmentManager().beginTransaction().replace(R.id.conteiner2, UserContentFragment.newInstance(position, person)).commit();

    }
}
