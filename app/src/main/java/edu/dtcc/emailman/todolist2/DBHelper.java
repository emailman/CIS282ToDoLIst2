package edu.dtcc.emailman.todolist2;

/*
 * Created by emailman on 3/13/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper{

    //Define the database and tables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "toDo_DB";
    private static final String DATABASE_TABLE = "toDo_Items";

    // Define the column names for the table
    private static final String TASK_ID = "_id";
    private static final String DESCRIPTION = "description";
    private static final String IS_DONE = "is_done";

    // private int taskCount;

    // Constructor
    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + DATABASE_TABLE + "(" +
                TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DESCRIPTION + " TEXT, " +
                IS_DONE + " INTEGER" +")";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop old table, if it exists
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

        // Create table again
        onCreate(db);
    }

    // Add a new task
    public void addToDoItem(ToDo_Item task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the new values
        values.put(DESCRIPTION, task.getDescription());
        values.put(IS_DONE, task.getIs_done());

        // Insert the row in the table
        db.insert(DATABASE_TABLE, null, values);
        // taskCount++;

        // Close the connection
        db.close();
    }

    public List<ToDo_Item> getAllTasks() {
        List<ToDo_Item> todoList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ToDo_Item task = new ToDo_Item();
                task.setDescription(cursor.getString(1));
                task.setIs_done(cursor.getInt(2));
                todoList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();

    return todoList;
    }

    // On click handler to clear the list and the table
    public void clearAll (List<ToDo_Item> list) {
        list.clear();

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, null, new String[] {});
        db.close();
    }

    // On click handler to update
    public void update_Task(ToDo_Item task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DESCRIPTION, task.getDescription());
        values.put(IS_DONE, task.getIs_done());

        db.update(DATABASE_TABLE, values, TASK_ID + " = ?",
                new String[]{String.valueOf(task.get_id())});

        db.close();
    }
}
