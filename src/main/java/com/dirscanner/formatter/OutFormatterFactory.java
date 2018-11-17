package com.dirscanner.formatter;

public class OutFormatterFactory {
    public static IOutFormatter getFormatter(String name){
        return new DirScannerFormatter();
    }
}
