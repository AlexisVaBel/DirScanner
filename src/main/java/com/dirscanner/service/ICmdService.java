package com.dirscanner.service;

import java.io.IOException;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */
public interface ICmdService<T, E >{
    public void setCmdList(T cmds);
    public void setFilterList(T cmds);

    public E    getResults();
}
