package com.dirscanner.service;

import java.io.IOException;

/**
 * Created by Belyaev Alexei (lebllex) on 14.11.18.
 */

// интерфейс командного сервиса, есть список команд и список ограничений
// может быть команды и не надо вызывать (в зависимости от реализации, может результат уже есть)
public interface ICmdService<T, E >{
    public void setCmdList(T cmds);
    public void setFilterList(T cmds);

    public E    getResults();
}
