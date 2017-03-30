package com.letv.receiver.filter.bean;

/**
 * @author maoyanan1@le.com (yanan) on 17-3-30.
 */
public class PersistentAppInfo {
    public String packageName;
    public boolean isSystemApp;

    @Override
    public String toString() {
        return "packageName: " + packageName + "\n" + "isSystemApp: " + isSystemApp;
    }
}


