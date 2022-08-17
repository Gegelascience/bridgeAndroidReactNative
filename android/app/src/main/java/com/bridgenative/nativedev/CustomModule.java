package com.bridgenative.nativedev;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CustomModule extends ReactContextBaseJavaModule implements LifecycleEventListener {


    String photoB64 = "";

    CustomModule(ReactApplicationContext context){
        super(context);
        context.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "CustomModule";
    }

    @ReactMethod
    public void goToNativeActivity() {
        photoB64 = "";
        Activity reactActivity = getCurrentActivity();
        Intent customIntent = new Intent(reactActivity,NativeCameraActivity.class);
        customIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reactActivity.startActivity(customIntent);

    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public String getPhotoBase64(){
        return photoB64;
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public void clearPhotoBase64() {
        photoB64 = "";
    }

    @Override
    public void onHostResume() {
        Activity reactActivity = getCurrentActivity();
        SharedPreferences sh = reactActivity.getSharedPreferences("photoStorage", Context.MODE_PRIVATE);
        photoB64 = sh.getString("photoBase64", "");
    }

    @Override
    public void onHostPause(){

    }

    @Override
    public void onHostDestroy(){

    }



}
