package com.letv.receiver.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.letv.receiver.filter.bean.BroadcastReceiverInfo;
import com.letv.receiver.filter.bean.PersistentAppInfo;
import com.letv.receiver.filter.utils.GetAppInfoUtils;

import java.util.ArrayList;

public class ResultActivity extends Activity {
    private static final String TAG = "FILTER RESULT";

    private TextView title;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        title = (TextView) findViewById(R.id.title);
        result = (TextView) findViewById(R.id.result);

        processIntent();

    }

    private void processIntent() {
        Intent intent = getIntent();
        String action = intent.getStringExtra("action");

        if(null == action || action.isEmpty()) {
            Log.d(TAG, "the action is Empty");
            title.setText("the action is Empty");
        }
        else if ("persistent".equals(action)) {
            title.setText("apps with persistent");
            onPersistentClick();
        } else {
            title.setText("receivers of "+ action);
            onBroadcastActionClick(action);
        }
    }

    private void onPersistentClick() {
        ArrayList<PersistentAppInfo> persistentArray = GetAppInfoUtils.getAllPersistentApp(this);
        Log.d(TAG, "print apps with flag Application.FLAG_PERSISTENT");
        printArray(persistentArray);
    }

    private void onBroadcastActionClick(String broadcastAction) {
        ArrayList<BroadcastReceiverInfo> broadCastReceiver = GetAppInfoUtils.getReceiversByName(this, broadcastAction);
        Log.d(TAG, "print " + broadcastAction + " Receivers :" );
        printArray(broadCastReceiver);
    }




    private <T> void printArray(ArrayList<T> array) {
        if (null == array || array.size() == 0) {
            Log.d(TAG, "nothing");
            result.setText("nothing");
        } else {
            StringBuffer sb = new StringBuffer(100);
            int count = array.size();
            sb.append("total count = " + count + "\n");
            for (int i = 0; i < count; i++) {
                sb.append(array.get(i).toString());
                sb.append("\n");
                if(i + 1 != count) {
                    sb.append("\n");
                }
                Log.d(TAG, array.get(i).toString());
                Log.d(TAG, "\n-----------------------\n");
            }
            result.setText(sb.toString());
        }
    }



}
