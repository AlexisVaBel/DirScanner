package com.dirscanner.service;

import java.util.TimerTask;

/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
// просто выводит точку или пайп в свое время
public class TermNotBoringService extends TimerTask {
    private int iSelfTick;

    public TermNotBoringService(){
        iSelfTick = 0;
    }

    public String getWait6Secs(){
        return ".";
    }
    public String getWaitMinute(){
        return "|";
    }

    @Override
    public void run() {
        if(iSelfTick == 10){
            iSelfTick = 0;
            System.out.print("|");
        }else{
            System.out.print(".");
        }
        iSelfTick++;
    }
}
