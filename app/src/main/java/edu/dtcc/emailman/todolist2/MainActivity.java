package edu.dtcc.emailman.todolist2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected DBHelper mDBHelper;
    private List<ToDo_Item> list;
    private MyAdapter adapt;
    private EditText myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Build a reference to the edit text field
        myTask = (EditText) findViewById(R.id.editText1);

        //  Setup the database
        mDBHelper = new DBHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = mDBHelper.getAllTasks();
        adapt = new MyAdapter(this, R.layout.todo_item, list);
        ListView listTask = (ListView) findViewById(R.id.listView1);
        if (listTask != null) {
            listTask.setAdapter(adapt);
        }
    }

    // Handle an event from button to add a new task
    public void addTaskNow (View view) {
        String s = myTask.getText().toString();

        if (s.isEmpty())
            Toast.makeText(getApplicationContext(), "Please enter a task", Toast.LENGTH_SHORT)
                    .show();
        else {
            // Add the new task item to the list
            ToDo_Item task = new ToDo_Item(s, 0);
            mDBHelper.addToDoItem(task);

            // Clear the text edit box
            myTask.setText("");

            // Add the task and notify
            adapt.add(task);
            adapt.notifyDataSetChanged();
        }
    }

    // Handle an event from button to delete all tasks
    public void clearTasks(View view) {
        mDBHelper.clearAll(list);
        adapt.notifyDataSetChanged();
    }

    // Adapter
    private class MyAdapter extends ArrayAdapter<ToDo_Item> {
        Context context;
        List<ToDo_Item> taskList = new ArrayList<>();

        public MyAdapter(Context c, int rID, List<ToDo_Item> objects) {
            super(c, rID, objects);
            taskList = objects;
            context = c;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox isDoneChBx;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.todo_item, parent, false);
                isDoneChBx = (CheckBox)
                        convertView.findViewById(R.id.chkStatus);
                convertView.setTag(isDoneChBx);

                isDoneChBx.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CheckBox cb = (CheckBox) view;
                                ToDo_Item changeTask = (ToDo_Item) cb.getTag();
                                changeTask.setIs_done(cb.isChecked() ? 1 : 0);
                                mDBHelper.update_Task(changeTask);
                            }
                        }
                );
            }
            else
                isDoneChBx = (CheckBox) convertView.getTag();

            ToDo_Item current = taskList.get(position);
            isDoneChBx.setText(current.getDescription());
            isDoneChBx.setChecked(current.getIs_done() == 1);
            isDoneChBx.setTag(current);
            return convertView;
        }
    }
}
