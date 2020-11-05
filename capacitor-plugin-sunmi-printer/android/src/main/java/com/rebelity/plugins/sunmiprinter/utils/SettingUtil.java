package com.rebelity.plugins.sunmiprinter.utils;

public class SettingUtil {
    private static com.rebelity.plugins.sunmiprinter.utils.SettingUtil instance;

    private String[]        stringEncodings = new String[]{"CP437", "CP850", "CP860", "CP863", "CP865", "CP857", "CP737", "CP928", "Windows-1252", "CP866", "CP852", "CP858", "CP874", "Windows-775", "CP855", "CP862", "CP864", "GB18030", "BIG5", "KSC5601", "utf-8"};
    private String[]        barcodeEncodings = new String[]{"UPC-A", "UPC-E", "EAN13", "EAN8", "CODE39", "ITF", "CODABAR", "CODE93", "CODE128A", "CODE128B", "CODE128C"};

    private final int         HRI_POSITION_NO_PRINT = 0;
    private final int         HRI_POSITION_BARCODE_UP = 1;
    private final int         HRI_POSITION_BARCODE_DOWN = 2;
    private final int         HRI_POSITION_BARCODE_UPDOWN = 3;

    private final int           DEFAULT_STRING_ENCODING_INDEX = 20;
    private final int           DEFAULT_HRI_POSITION = HRI_POSITION_BARCODE_DOWN;

    private final int           DEFAULT_BARCODE_ENCODING_INDEX = 7;

    public SettingUtil() {
        this.instance = this;
    }

    public static com.rebelity.plugins.sunmiprinter.utils.SettingUtil getInstance() {
        return instance;
    }

    public String getStringEncoding(int index) {
        return stringEncodings[index];
    }

    public int getDefaultStringEncodingIndex() {
        return DEFAULT_STRING_ENCODING_INDEX;
    }

    public int getDefaultBarcodeEncoding() {
        return DEFAULT_BARCODE_ENCODING_INDEX;
    }

    public int getDefaultHriPosition() {
        return DEFAULT_HRI_POSITION;
    }
}
