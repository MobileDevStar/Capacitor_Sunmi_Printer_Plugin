package com.rebelity.plugins.sunmiprinter;

public class ReceiptBluetoothPrinter {
    private String      name;
    private String      address;

    public ReceiptBluetoothPrinter(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
