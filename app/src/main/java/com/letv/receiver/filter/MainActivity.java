package com.letv.receiver.filter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.letv.receiver.filter.bean.BroadcastReceiverBean;
import com.letv.receiver.filter.bean.BroadcastReceiverInfo;
import com.letv.receiver.filter.bean.PersistentAppInfo;
import com.letv.receiver.filter.bean.SaveInfo;
import com.letv.receiver.filter.utils.GetAppInfoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private static final String FILE_NAME = "/sdcard/persistentAndReceiver.json";

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

        makeJson();
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

    void makeJson() {
        SaveInfo saveInfo = new SaveInfo();

        // persistent
        List<PersistentAppInfo> persistentAppList = GetAppInfoUtils.getAllPersistentApp(this);
        if (null != persistentAppList) {
            int persistentAppCount = persistentAppList.size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < persistentAppCount; i++) {
                sb.append(persistentAppList.get(i).packageName);
                if ( i != persistentAppCount - 1 ) {
                    sb.append(",");
                }
            }
            saveInfo.persistentList = sb.toString();
        }

        // receivers

        Map<String,List<BroadcastReceiverInfo>> broadcastReceiverList = new HashMap<String,List<BroadcastReceiverInfo>>(10);
        saveInfo.receiverList = new HashMap<String, String>(10);
        List<BroadcastReceiverInfo> receiverInfos;
        if(null != broadcasts) {
            int count = broadcasts.length;
            for (int i = 0; i < count; i++) {
                receiverInfos = GetAppInfoUtils.getReceiversByName(this,broadcasts[i]);
                if (null != receiverInfos) {
                    int receiverCount = receiverInfos.size();

                    String receiverListStr;
                    String packageName;

                    for (int j = 0; j < receiverCount; j++ ){
                        packageName = receiverInfos.get(j).packageName;
                        if (saveInfo.receiverList.containsKey(receiverInfos.get(j).packageName)) {
                            receiverListStr = saveInfo.receiverList.get(packageName);
                            receiverListStr += "," + broadcasts[i];

                        } else {
                          receiverListStr = broadcasts[i];
                        }
                        saveInfo.receiverList.put(packageName,receiverListStr);
                    }
                }
            }
        }




        Gson gson = new Gson();
        String str = gson.toJson(saveInfo,SaveInfo.class);
        Log.d("+++++++++++++",str);
        saveFile(str);
    }

    void saveFile(String gsonStr) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(file.exists()) {
            try {
                FileOutputStream fis = new FileOutputStream(file) ;
                byte [] bytes = gsonStr.getBytes();
                fis.write(bytes);
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
