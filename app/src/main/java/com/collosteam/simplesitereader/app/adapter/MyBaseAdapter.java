package com.collosteam.simplesitereader.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.api.data.Person;

import java.util.List;

/**
 * Created by Collos on 7/14/2014.
 */
public class MyBaseAdapter extends BaseAdapter {

    Context mContext;
    List<Person> persons;

    public MyBaseAdapter(Context mContext, List<Person> persons) {
        this.mContext = mContext;
        this.persons = persons;
    }


    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Person getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position+666;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        ViewHolder holder = new ViewHolder();

        if(view==null){
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view =  layoutInflater.inflate(R.layout.item_list_person,parent,false);
            holder.textView = (TextView) view.findViewById(R.id.text);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        holder.textView.setText(persons.get(position).getLogin());

        return view;
    }

   static class ViewHolder{
       TextView textView;
   }

}
