package com.dirscanner.service;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public interface ICmdService<T, E > extends Runnable{
    public void setCmdList(T cmds);
    public E    getResults();
    public boolean isFinished();

    @Override
    void run();
}
