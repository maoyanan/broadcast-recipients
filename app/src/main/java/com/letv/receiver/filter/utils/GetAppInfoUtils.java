package com.letv.receiver.filter.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.letv.receiver.filter.bean.BroadcastReceiverInfo;
import com.letv.receiver.filter.bean.PersistentAppInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author maoyanan1@le.com (yanan) on 4/7/17.
 */
public class GetAppInfoUtils {

    public static ArrayList<PersistentAppInfo> getAllPersistentApp(Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
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

    public static ArrayList<BroadcastReceiverInfo> getReceiversByName (Context context, String broadcastAction) {
        ArrayList<BroadcastReceiverInfo> receivers = new ArrayList<BroadcastReceiverInfo>(10);
        Intent intent = new Intent();
        intent.setAction(broadcastAction);

        PackageManager pm = context.getApplicationContext().getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryBroadcastReceivers(intent,PackageManager.GET_RESOLVED_FILTER);

        int count = resolveInfo.size();
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

    public static ArrayList<String> getActions(IntentFilter filter) {
        ArrayList<String> actions = new ArrayList<String>(10);
        if (null != filter) {
            Iterator<String> iterator = filter.actionsIterator();
            while (iterator.hasNext()) {
                actions.add(iterator.next());
            }
        }
        return actions;
    }

    public static boolean isSystemApp (ApplicationInfo ai) {
        boolean isSysApp = false;
        if (null != ai) {
            isSysApp = (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        }
        return isSysApp;
    }

    public static boolean isPersistentApp(ApplicationInfo ai) {
        boolean isPersistentApp = false;
        if(null != ai) {
            isPersistentApp = (ai.flags & ApplicationInfo.FLAG_PERSISTENT) != 0;
        }
        return isPersistentApp;
    }

    public static ApplicationInfo GetApplicationInfo(PackageManager pm, String packageName) {
        ApplicationInfo ai ;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            ai = null;
        }
        return ai;
    }
}
