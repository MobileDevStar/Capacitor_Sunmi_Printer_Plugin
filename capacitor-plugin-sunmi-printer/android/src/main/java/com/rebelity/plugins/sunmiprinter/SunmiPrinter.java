package com.rebelity.plugins.sunmiprinter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.rebelity.plugins.sunmiprinter.utils.BluetoothUtil;
import com.rebelity.plugins.sunmiprinter.utils.ESCUtil;
import com.rebelity.plugins.sunmiprinter.utils.SettingUtil;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static com.rebelity.plugins.sunmiprinter.Define.*;

@NativePlugin(
        requestCodes={SunmiPrinter.REQUEST_BLUETOOTH}
)
public class SunmiPrinter extends Plugin {
    protected static final int REQUEST_BLUETOOTH = 1990; // Unique request code

    /**
     * Called when the plugin has been connected to the bridge
     * and is ready to start initializing.
     */
    @Override
    public void load() {
        new SettingUtil();
    }

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod()
    public void discoverPrinters(PluginCall call) {
        saveCall(call);
        pluginRequestPermission(Manifest.permission.BLUETOOTH, REQUEST_BLUETOOTH);
    }

    @PluginMethod()
    public void connectPrinter(PluginCall call) {
        String address = call.getString("address");
        Log.d("address", address);

        BluetoothUtil.disconnectBlueTooth(getContext());

        Boolean isConnected = false;
        if(!BluetoothUtil.connectBlueToothByAddress(getContext(), address)){
            BluetoothUtil.isBlueToothPrinter = false;
            isConnected = false;
        }else{
            BluetoothUtil.isBlueToothPrinter = true;
            isConnected = true;
        }

        JSObject ret = new JSObject();
        ret.put("results", isConnected);
        call.success(ret);
    }

    @PluginMethod()
    public void disconnectPrinter(PluginCall call) {
        BluetoothUtil.disconnectBlueTooth(getContext());

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
    }

    @PluginMethod()
    public void printString(PluginCall call) {
        String contents = call.getString("contents");
        Boolean isBold = call.getBoolean("is_bold");
        Boolean isUnderLine = call.getBoolean("is_underline");

        Log.d("Contents", contents);
        Log.d("isBold", isBold.toString());

        printStringByBluetooth(contents, isBold, isUnderLine);

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
    }

    @PluginMethod()
    public void printBarcode(PluginCall call) {
        String barcode = call.getString("barcode");
        Log.d("Barcode", barcode);

        printBarcodeByBluetooth(barcode);

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
    }

    @PluginMethod()
    public void printCommand(PluginCall call) {
        String command = call.getString("command");
        Log.d("Command", command);

        switch (command) {
            case PRINT_ALIGN_LEFT:
                BluetoothUtil.sendData(ESCUtil.alignLeft());
                break;
            case PRINT_ALIGN_RIGHT:
                BluetoothUtil.sendData(ESCUtil.alignRight());
                break;
            case PRINT_ALIGN_CENTER:
                BluetoothUtil.sendData(ESCUtil.alignCenter());
                break;
            case PRINT_NEXT_LINE:
                BluetoothUtil.sendData(ESCUtil.nextLine(1));
                break;
        }

        JSObject ret = new JSObject();
        ret.put("results", true);
        call.success(ret);
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

    @Override
    protected void handleOnDestroy() {
        BluetoothUtil.disconnectBlueTooth(getContext());
    }

    void loadDevices(PluginCall call) {
        ArrayList<Map> deviceList = BluetoothUtil.loadBluetoothDevices();

        JSONArray jsonArray = new JSONArray(deviceList);
        JSObject ret = new JSObject();
        ret.put("results", jsonArray);
        call.success(ret);
    }

    private void printStringByBluetooth(String content, boolean isBold, boolean isUnderLine) {
        try {
            if (isBold) {
                BluetoothUtil.sendData(ESCUtil.boldOn());
            } else {
                BluetoothUtil.sendData(ESCUtil.boldOff());
            }

            if (isUnderLine) {
                BluetoothUtil.sendData(ESCUtil.underlineWithOneDotWidthOn());
            } else {
                BluetoothUtil.sendData(ESCUtil.underlineOff());
            }

            int recode = SettingUtil.getInstance().getDefaultStringEncodingIndex();

            if (recode < 17) {
                BluetoothUtil.sendData(ESCUtil.singleByte());
                BluetoothUtil.sendData(ESCUtil.setCodeSystemSingle(codeParse(recode)));
            } else {
                BluetoothUtil.sendData(ESCUtil.singleByteOff());
                BluetoothUtil.sendData(ESCUtil.setCodeSystem(codeParse(recode)));
            }

            String stringEncoding = SettingUtil.getInstance().getStringEncoding(recode);

            BluetoothUtil.sendData(content.getBytes(stringEncoding));
            //BluetoothUtil.sendData(ESCUtil.nextLine(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte codeParse(int value) {
        byte res = 0x00;
        switch (value) {
            case 0:
                res = 0x00;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                res = (byte) (value + 1);
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                res = (byte) (value + 8);
                break;
            case 12:
                res = 21;
                break;
            case 13:
                res = 33;
                break;
            case 14:
                res = 34;
                break;
            case 15:
                res = 36;
                break;
            case 16:
                res = 37;
                break;
            case 17:
            case 18:
            case 19:
                res = (byte) (value - 17);
                break;
            case 20:
                res = (byte) 0xff;
                break;
            default:
                break;
        }
        return (byte) res;
    }

    private void printBarcodeByBluetooth(String barcode) {
        int encode = SettingUtil.getInstance().getDefaultBarcodeEncoding();
        int position = SettingUtil.getInstance().getDefaultHriPosition();

        int width = 2;
        int height = 162;

        BluetoothUtil.sendData(ESCUtil.getPrintBarCode(barcode, encode, height, width, position));
    }
}
