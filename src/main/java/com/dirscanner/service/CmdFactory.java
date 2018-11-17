package com.dirscanner.service;

public class CmdFactory {
    public static ICmdService getCmdService(String name){
        // если появятся какие-либо реализации добавим их
        return new AllFilesExploreService();
    }
}
