package com.collosteam.simplesitereader.app.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.collosteam.simplesitereader.R;
import com.collosteam.simplesitereader.app.db.LessonsColumns;
import com.collosteam.simplesitereader.app.provider.MyContentProvider;

/**
 * Created by Collos on 7/21/2014.
 */
public class MyCursorAdapter extends CursorAdapter implements LessonsColumns {

    private LayoutInflater inflater;

    public MyCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        initInflater(context);
    }

    /**
     * */
    private void initInflater(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        initInflater(context);
    }


    private ViewHolder holder;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        holder = new ViewHolder();

        View rootView = inflater.inflate(R.layout.item_site_with_check, parent, false);

        holder.tvLessonNumber = (TextView) rootView.findViewById(R.id.tvLessonNumber);
        holder.tvTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        holder.ivReadStatus = (ImageView) rootView.findViewById(R.id.ivReadStatus);
        holder.cbFavorite = (CheckBox) rootView.findViewById(R.id.cbFavorite);

        rootView.setTag(holder);

        return rootView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        holder = (ViewHolder) view.getTag();

        int lesson_id = cursor.getInt(cursor.getColumnIndex(_ID));

        String lessonNumber = String.format("%d) ", lesson_id);

        holder.tvLessonNumber.setText(lessonNumber);

        String title = cursor.getString(cursor.getColumnIndex(TITLE));

        holder.tvTitle.setText(title);

        boolean checked = cursor.getInt(cursor.getColumnIndex(FAVORITE)) != 0;

        holder.cbFavorite.setOnCheckedChangeListener(null); // Для нормальной работы необходимо было добавить эту строку она удаляет слушатель для данного CheckBox
         // Все изз за того что следующая строчка вызывала onCheckedChanged()
        holder.cbFavorite.setChecked(checked);

        //Добавляем тег
        holder.cbFavorite.setTag(lesson_id);

        holder.cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

              // вызываем тег
                int id = (Integer) buttonView.getTag();

                Log.d("OnCheckedChangeListener"," id - " + id );

                Uri lesson_uri = Uri.withAppendedPath(MyContentProvider.CONTENT_URI, "" + id);


                ContentValues contentValues = new ContentValues();
                contentValues.put(FAVORITE, isChecked ? 1 : 0);
                buttonView.getContext().getContentResolver().update(lesson_uri, contentValues, null, null);
                MyCursorAdapter.this.notifyDataSetChanged();

            }
        });


    }

    static class ViewHolder {
        TextView tvLessonNumber;
        TextView tvTitle;
        ImageView ivReadStatus;
        CheckBox cbFavorite;
    }

}
