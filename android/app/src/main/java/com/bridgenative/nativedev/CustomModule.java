package com.bridgenative.nativedev;

import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CustomModule extends ReactContextBaseJavaModule {

    CustomModule(ReactApplicationContext context){
        super(context);
    }

    @Override
    public String getName() {
        return "CustomModule";
    }

    @ReactMethod
    public void goToNativeActivity() {
        ReactApplicationContext reactContext = this.getReactApplicationContext();
        Intent customIntent = new Intent(reactContext,NativeCameraActivity.class);
        customIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        reactContext.startActivity(customIntent);

    }

}
