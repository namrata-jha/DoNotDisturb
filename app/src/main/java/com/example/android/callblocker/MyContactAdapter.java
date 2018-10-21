package com.example.android.callblocker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyContactAdapter extends CursorAdapter {

    MyDatabaseHelper databaseHelper;
    SQLiteDatabase myDatabase;


    public MyContactAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.contact_row, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView name = view.findViewById(R.id.name);
        TextView number = view.findViewById(R.id.number);
        ImageView delete = view.findViewById(R.id.delete);



        final int position = cursor.getPosition();
        Log.i("PositionMessage", String.valueOf(position));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseHelper = new MyDatabaseHelper(context);
                myDatabase = databaseHelper.getWritableDatabase();

                cursor.moveToPosition(position);
                int idDelete = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("contact_name"));
                Toast.makeText(context, name+ " removed.", Toast.LENGTH_SHORT).show();

                myDatabase.delete(MyDatabaseHelper.DB_TABLE, "_id=?", new String[]{String.valueOf(idDelete)});

                cursor.requery();
                notifyDataSetChanged();
            }
        });

        String contactName = cursor.getString(cursor.getColumnIndexOrThrow("contact_name"));
        String contactNumber = cursor.getString(cursor.getColumnIndexOrThrow("contact_no"));

        name.setText(contactName);
        number.setText(contactNumber);

    }
}
