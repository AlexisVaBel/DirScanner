package com.dirscanner.controller;

import com.dirscanner.service.ICmdService;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public class TermInfoController extends  Thread{
    private static ICmdService cmdService;
    private volatile boolean bWork;

    public TermInfoController(ICmdService cmdservice){
        this.cmdService = cmdservice;
    }

    @Override
    public void run() {
        while(bWork){
            cmdService.getResults();
        };
    }
}
