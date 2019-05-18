package com.example.catchtheball;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LastResults extends Activity {

    int id, score, time, orange, pinks, touches;
    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_results);  //Указываем созданную нами ранее разметку
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_SESSIONS, null, null,
                null, null, null, null);

        long numRows = DatabaseUtils.queryNumEntries(database, "Sessions");
        int rows = (int) numRows;


        int[][] array = new int[rows][6];

        if (cursor.moveToFirst()) {
            do {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < 6; j++) {
                    if (j == 0) {
                        int number = cursor.getColumnIndex(DBHelper.KEY_ID);
                        array[i][j] = cursor.getInt(number);
                    } else if (j == 1) {
                        int number = cursor.getColumnIndex(DBHelper.KEY_SCORE);
                        array[i][j] = cursor.getInt(number);
                    } else if (j == 2) {
                        int number = cursor.getColumnIndex(DBHelper.KEY_TIME);
                        array[i][j] = cursor.getInt(number);
                    } else if (j == 3) {
                        int number = cursor.getColumnIndex(DBHelper.KEY_ORANGE);
                        array[i][j] = cursor.getInt(number);
                    } else if (j == 4) {
                        int number = cursor.getColumnIndex(DBHelper.KEY_PINK);
                        array[i][j] = cursor.getInt(number);
                    } else if (j == 5) {
                        int number = cursor.getColumnIndex(DBHelper.KEY_TOUCHES);
                        array[i][j] = cursor.getInt(number);
                    }
                }
            }
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        for (int i = 0; i < rows; i++)
            addRow(array[i][0], array[i][1], array[i][2], array[i][3], array[i][4], array[i][5]);
    }

    public void addRow(int c0, int c1, int c2, int c3, int c4, int c5) {
        //Сначала найдем в разметке активити саму таблицу по идентификатору
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);

        //Создаём экземпляр инфлейтера, который понадобится для создания строки таблицы из шаблона. В качестве контекста у нас используется сама активити
        LayoutInflater inflater = LayoutInflater.from(this);
        //Создаем строку таблицы
        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);

        //Находим ячейку для номера дня по идентификатору
        TextView tv = (TextView) tr.findViewById(R.id.col1);
        //Обязательно приводим число к строке, иначе оно будет воспринято как идентификатор ресурса
        tv.setText(Integer.toString(c0));
        //Ищем следующую ячейку и устанавливаем её значение
        tv = (TextView) tr.findViewById(R.id.col2);
        tv.setText(Integer.toString(c1));
        tv = (TextView) tr.findViewById(R.id.col3);
        tv.setText(Integer.toString(c2));
        tv = (TextView) tr.findViewById(R.id.col4);
        tv.setText(Integer.toString(c3));
        tv = (TextView) tr.findViewById(R.id.col5);
        tv.setText(Integer.toString(c4));
        tv = (TextView) tr.findViewById(R.id.col6);
        tv.setText(Integer.toString(c5));
        tableLayout.addView(tr); //добавляем созданную строку в таблицу
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Result.class);
        startActivity(intent);
    }
}