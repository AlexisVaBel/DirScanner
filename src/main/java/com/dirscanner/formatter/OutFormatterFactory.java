package com.dirscanner.formatter;

// фабрика в любом случае не вернет null
public class OutFormatterFactory {
    public static IOutFormatter getFormatter(String name){
        return new DirScannerFormatter();
    }
}
