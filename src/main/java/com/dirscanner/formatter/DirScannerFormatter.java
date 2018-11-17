package com.dirscanner.formatter;

public class DirScannerFormatter implements IOutFormatter{
    @Override
    public String getFormatter() {
        return "[\nfile = %s\ndate = %s\nsize = %s]";
    }
}
