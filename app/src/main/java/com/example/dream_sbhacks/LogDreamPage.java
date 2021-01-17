package com.example.dream_sbhacks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class LogDreamPage extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_dream_page);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        // if click "explore dreams", goes to swiping
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogDreamPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
        //adding new dreams
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(v);
            }
        });
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        setUpListViewListener();

    }

    private void setUpListViewListener() {
        //using long click, can delete entries
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Dream has been released to the stars (deleted)", Toast.LENGTH_LONG).show();

                items.remove(position);
                //refreshes and shows item has been removed
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void addItem(View v) {
        EditText input = findViewById(R.id.editTextTextPersonName7);
        //extract text
        String itemText = input.getText().toString();
        //check user has typed something, not empty string
        if(!(itemText.equals(""))) {
            itemsAdapter.add(itemText);
            input.setText(""); //set back to empty
        } else{
            Toast.makeText(getApplicationContext(), "No dreams, head empty.", Toast.LENGTH_LONG).show();
        }
    }
}