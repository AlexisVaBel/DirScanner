package com.dirscanner.service;

/**
 * Created by Belyaev Alexei (lebllex) on 16.11.18.
 */
public interface IResultService {
    public void setResultProducer(ICmdService cmdService);
    public void processResult();
    public void addByResult(String str);
    public void closeResult();
}
