package com.wu.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

public class SharedPreferencesHelper {
    public static SharedPreferencesHelper instance;
    protected Context mContext;
    protected SharedPreferences sharedPreferences;

    private SharedPreferencesHelper(Context var1) {
        this.mContext = var1;
        this.sharedPreferences = this.mContext.getSharedPreferences(var1.getPackageName(), 0);
    }

    public static synchronized SharedPreferencesHelper getInstance(Context var0) {
        if(null == var0) {
            return null;
        } else {
            if(null == instance) {
                instance = new SharedPreferencesHelper(var0.getApplicationContext());
            }

            return instance;
        }
    }

    public String getValue(String var1) {
        return this.sharedPreferences.getString(var1, "");
    }

    public Boolean getBoolValue(String var1) {
        return Boolean.valueOf(this.sharedPreferences.getBoolean(var1, false));
    }

    public Boolean getBoolean(String var1, boolean var2) {
        return Boolean.valueOf(this.sharedPreferences.getBoolean(var1, var2));
    }

    public Long getLong(String var1) {
        return Long.valueOf(this.sharedPreferences.getLong(var1, 0L));
    }
    public Float getFloat(String var1) {
        return Float.valueOf(this.sharedPreferences.getFloat(var1, 0L));
    }


    public int getIntValue(String var1) {
        return this.sharedPreferences.getInt(var1, 0);
    }

    public int getIntValue(String var1, int var2) {
        return this.sharedPreferences.getInt(var1, var2);
    }

    public void setValue(String var1, Boolean var2) {
        Editor var3 = this.sharedPreferences.edit();
        var3.putBoolean(var1, var2.booleanValue());
        var3.apply();
    }

    public void setValue(String var1, String var2) {
        Editor var3 = this.sharedPreferences.edit();
        var3.putString(var1, var2);
        var3.apply();
    }

    public void setValue(String var1, int var2) {
        Editor var3 = this.sharedPreferences.edit();
        var3.putInt(var1, var2);
        var3.apply();
    }

    public void setValue(String var1, long var2) {
        Editor var4 = this.sharedPreferences.edit();
        var4.putLong(var1, var2);
        var4.apply();
    }

    public void setValue(String var1, float var2) {
        Editor var4 = this.sharedPreferences.edit();
        var4.putFloat(var1, var2);
        var4.apply();
    }

    public void remove(String var1) {
        Editor var2 = this.sharedPreferences.edit();
        var2.remove(var1);
        var2.apply();
    }

    public void clear() {
        Editor var1 = this.sharedPreferences.edit();
        var1.clear();
        var1.apply();
    }

    public Set<String> getSaveKeys() {
        return this.sharedPreferences.getAll().keySet();
    }

    public boolean contains(String var1) {
        return this.sharedPreferences.contains(var1);
    }
}
