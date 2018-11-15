package com.dirscanner.service;

import java.util.TimerTask;

/**
 * Created by Belyaev Alexei (lebllex) on 15.11.18.
 */
public class TerminalNotBoring extends TimerTask {
    private int iSelfTick;

    public TerminalNotBoring(){
        iSelfTick = 0;
    }

    public String getWait5Secs(){
        return ".";
    }
    public String getWaitMinute(){
        return "|";
    }

    @Override
    public void run() {
        iSelfTick++;
        if(iSelfTick == 12){
            iSelfTick = 0;
            System.out.print("|");
        }else{
            System.out.print(".");
        }
    }
}
