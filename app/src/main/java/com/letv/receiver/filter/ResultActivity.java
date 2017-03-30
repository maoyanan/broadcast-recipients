package com.letv.receiver.filter;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.letv.receiver.filter.bean.BroadcastReceiverInfo;
import com.letv.receiver.filter.bean.PersistentAppInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        ArrayList<PersistentAppInfo> persistentArray = getAllPersistentApp();
        Log.d(TAG, "print apps with flag Application.FLAG_PERSISTENT");
        printArray(persistentArray);
    }

    private void onBroadcastActionClick(String broadcastAction) {
        ArrayList<BroadcastReceiverInfo> broadCastReceiver = getReceiversByName(broadcastAction);
        Log.d(TAG, "print " + broadcastAction + " Receivers :" );
        printArray(broadCastReceiver);
    }

    private ArrayList<PersistentAppInfo> getAllPersistentApp() {
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ApplicationInfo> ail =  pm.getInstalledApplications(0);
        ArrayList<PersistentAppInfo> persistentArray = new ArrayList<PersistentAppInfo>(10);
        if (null != ail) {
            int count = ail.size();
            ApplicationInfo ai;
            for (int i = 0; i < count; i++) {
                ai = ail.get(i);
                if (isPersistentApp(ai)) {
                    PersistentAppInfo pai = new PersistentAppInfo();
                    pai.packageName = ai.packageName;
                    pai.isSystemApp = isSystemApp(ai);
                    persistentArray.add(pai);
                }
            }
        }
        return persistentArray;
    }

    private ArrayList<BroadcastReceiverInfo> getReceiversByName (String broadcastAction) {
        ArrayList<BroadcastReceiverInfo> receivers = new ArrayList<BroadcastReceiverInfo>(10);
        Intent intent = new Intent();
        intent.setAction(broadcastAction);

        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryBroadcastReceivers(intent,PackageManager.GET_RESOLVED_FILTER);

        int count = resolveInfo.size();
        Log.d(TAG, "count : " + count);
        ResolveInfo info;
        for (int i=0; i < count; i++) {
            info = resolveInfo.get(i);
            if (null != info && null != info.activityInfo ) {
                BroadcastReceiverInfo appInfo = new BroadcastReceiverInfo();
                appInfo.packageName = info.activityInfo.packageName;
                appInfo.receiverName = info.activityInfo.name;
                appInfo.isSystemApp = isSystemApp(GetApplicationInfo(pm,appInfo.packageName));
                appInfo.actions = getActions(info.filter);
                receivers.add(appInfo);
            }
        }

        return receivers;
    }

    private ArrayList<String> getActions(IntentFilter filter) {
        ArrayList<String> actions = new ArrayList<String>(10);
        if (null != filter) {
            Iterator<String> iterator = filter.actionsIterator();
            while (iterator.hasNext()) {
                actions.add(iterator.next());
            }
        }
        return actions;
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

    private ApplicationInfo GetApplicationInfo(PackageManager pm, String packageName) {
        ApplicationInfo ai ;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            ai = null;
        }
        return ai;
    }


    private boolean isSystemApp (ApplicationInfo ai) {
        boolean isSysApp = false;
        if (null != ai) {
            isSysApp = (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        }
        return isSysApp;
    }

    private boolean isPersistentApp(ApplicationInfo ai) {
        boolean isPersistentApp = false;
        if(null != ai) {
            isPersistentApp = (ai.flags & ApplicationInfo.FLAG_PERSISTENT) != 0;
        }
        return isPersistentApp;
    }
}
