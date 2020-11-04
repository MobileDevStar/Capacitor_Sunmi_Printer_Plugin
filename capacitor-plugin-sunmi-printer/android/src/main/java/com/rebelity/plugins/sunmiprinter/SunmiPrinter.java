package com.rebelity.plugins.sunmiprinter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@NativePlugin(
        requestCodes={SunmiPrinter.REQUEST_BLUETOOTH}
)
public class SunmiPrinter extends Plugin {
    protected static final int REQUEST_BLUETOOTH = 199002; // Unique request code

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod()
    public void discoverPrinters(PluginCall call) {
        String value = call.getString("filter");
        // Filter based on the value if want

        saveCall(call);
        pluginRequestPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH);
    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);


        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            Log.d("Test", "No stored plugin call for permissions request result");
            return;
        }

        for(int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                Log.d("Test", "User denied permission");
                return;
            }
        }

        if (requestCode == REQUEST_BLUETOOTH) {
            // We got the permission!
            loadDevices(savedCall);
        }
    }

    void loadDevices(PluginCall call) {
        ArrayList<Map> deviceList = new ArrayList<>();

        Map<String,String> map =  new HashMap<String, String>();
        map.put("firstName", "aaa first name");
        map.put("lastName", "bbb last name");
        map.put("telephone", "12345667");

        deviceList.add(map);

        JSONArray jsonArray = new JSONArray(deviceList);
        JSObject ret = new JSObject();
        ret.put("results", jsonArray);
        call.success(ret);
    }
}
