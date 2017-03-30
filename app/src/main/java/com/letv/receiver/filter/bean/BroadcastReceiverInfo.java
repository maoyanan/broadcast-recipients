package com.letv.receiver.filter.bean;

import java.util.List;

/**
 * @author maoyanan1@le.com (yanan) on 17-3-30.
 */
public class BroadcastReceiverInfo {
    public String packageName;
    public String receiverName;
    public boolean isSystemApp;
    public List<String> actions;

    @Override
    public String toString() {
        return "packageName: "+ packageName + "\n" +
                "receiverName: " + receiverName + "\n" +
                "isSystemApp: " + isSystemApp + "\n" +
                "actions: " + actions.toString();
    }
}
