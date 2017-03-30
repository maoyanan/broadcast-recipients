package com.letv.receiver.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private EditText input;
    private String[] broadcasts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        broadcasts = getResources().getStringArray(R.array.broadcasts);

        listView = (ListView)findViewById(R.id.list);
        input = (EditText) findViewById(R.id.input);
        listView.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice, broadcasts));
        listView.setOnItemClickListener(this);
    }

    void onGetResultClick (View view) {
        String inputText = input.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "the input is Empty",Toast.LENGTH_SHORT).show();
        } else {
            startResultActivity(inputText);
        }
    }

    void onPersistentClick (View view) {
        startResultActivity("persistent");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startResultActivity(broadcasts[position]);
    }

    void startResultActivity(String action) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("action",action);
        startActivity(intent);
    }
}
