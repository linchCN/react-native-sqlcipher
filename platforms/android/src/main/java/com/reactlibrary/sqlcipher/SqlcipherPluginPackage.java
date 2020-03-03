/**
 * Written by Andrzej Porebski Nov 14/2015
 *
 * Copyright (c) 2015, Andrzej Porebski
 */
package com.reactlibrary.sqlcipher;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public class SqlcipherPluginPackage implements ReactPackage {

    /**
     * @deprecated, use method without activity
     * activity parameter is ignored
     */
    public SqlcipherPluginPackage(Activity activity){
        this();
    }

    public SqlcipherPluginPackage() {
    }

    @Override
    public List<NativeModule> createNativeModules(
                                ReactApplicationContext reactContext) {
        SQLiteDatabase.loadLibs(reactContext);
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new SqlcipherPlugin(reactContext));

      return modules;
    }

    // Deprecated RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
      return Collections.emptyList();
    }
}
