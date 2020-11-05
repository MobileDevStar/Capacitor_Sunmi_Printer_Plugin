package com.rebelity.plugins.sunmiprinter.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import com.rebelity.plugins.sunmiprinter.ReceiptBluetoothPrinter;
import com.rebelity.plugins.sunmiprinter.capacitorpluginsunmiprinter.R;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *  Simple package for connecting a sunmi printer via Bluetooth
 */
public class BluetoothUtil {

    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //private static final UUID PRINTER_UUID = UUID.fromString("00001112-0000-1000-8000-00805f9b34fb");

    //private static final String Innerprinter_Address = "00:11:22:33:44:55";
    private static final String Innerprinter_Address = "A0:82:1F:64:93:49";

    public static boolean isBlueToothPrinter = false;

    private static BluetoothSocket bluetoothSocket;

    private static BluetoothAdapter getBTAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public static ArrayList<ReceiptBluetoothPrinter> getBluetoothDevices() {
        BluetoothAdapter bluetoothAdapter = getBTAdapter();
        ArrayList<ReceiptBluetoothPrinter> printers = new ArrayList<ReceiptBluetoothPrinter>();

        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();
            ReceiptBluetoothPrinter printer = new ReceiptBluetoothPrinter(deviceName, deviceAddress);
            printers.add(printer);
        }
        return printers;
    }

    public static ArrayList<Map> loadBluetoothDevices() {
        BluetoothAdapter bluetoothAdapter = getBTAdapter();
        ArrayList<Map> deviceList = new ArrayList<>();

        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();

            Map<String,String> map =  new HashMap<String, String>();
            map.put("name", deviceName);
            map.put("address", deviceAddress);

            deviceList.add(map);
        }
        return deviceList;
    }

    private static BluetoothDevice getDevice(BluetoothAdapter bluetoothAdapter) {
        BluetoothDevice innerprinter_device = null;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals(Innerprinter_Address)) {
                innerprinter_device = device;
                break;
            }
        }
        return innerprinter_device;
    }

    private static BluetoothDevice getDeviceByAddress(BluetoothAdapter bluetoothAdapter, String address) {
        BluetoothDevice printer_device = null;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals(address)) {
                printer_device = device;
                break;
            }
        }
        return printer_device;
    }

    private static BluetoothSocket getSocket(BluetoothDevice device) throws IOException {
        BluetoothSocket socket;
        socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID);
        socket.connect();
        return  socket;
    }

    /**
     * connect bluetooth
     */
    public static boolean connectBlueTooth(Context context) {
        if (bluetoothSocket == null) {
            if (getBTAdapter() == null) {
                Toast.makeText(context,  R.string.toast_3, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!getBTAdapter().isEnabled()) {
                Toast.makeText(context, R.string.toast_4, Toast.LENGTH_SHORT).show();
                return false;
            }
            BluetoothDevice device;
            if ((device = getDevice(getBTAdapter())) == null) {
                Toast.makeText(context, R.string.toast_5, Toast.LENGTH_SHORT).show();
                return false;
            }

            try {
                bluetoothSocket = getSocket(device);
            } catch (IOException e) {
                Toast.makeText(context, R.string.toast_6, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public static boolean connectBlueToothByAddress(Context context, String address) {
        if (bluetoothSocket == null) {
            if (getBTAdapter() == null) {
                Toast.makeText(context,  R.string.toast_3, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!getBTAdapter().isEnabled()) {
                Toast.makeText(context, R.string.toast_4, Toast.LENGTH_SHORT).show();
                return false;
            }
            BluetoothDevice device;
            if ((device = getDeviceByAddress(getBTAdapter(), address)) == null) {
                Toast.makeText(context, R.string.toast_5, Toast.LENGTH_SHORT).show();
                return false;
            }

            try {
                bluetoothSocket = getSocket(device);
            } catch (IOException e) {
                Toast.makeText(context, R.string.toast_6, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /**
     * disconnect bluethooth
     */
    public static void disconnectBlueTooth(Context context) {
        if (bluetoothSocket != null) {
            try {
                OutputStream out = bluetoothSocket.getOutputStream();
                out.close();
                bluetoothSocket.close();
                bluetoothSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  send esc cmd
     */
    public static void sendData(byte[] bytes) {
        if (bluetoothSocket != null) {
            OutputStream out = null;
            try {
                out = bluetoothSocket.getOutputStream();
                out.write(bytes, 0, bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //TODO handle disconnect event
        }
    }
}
