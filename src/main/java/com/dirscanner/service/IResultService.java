package com.dirscanner.service;

/**
 * Created by Belyaev Alexei (lebllex) on 16.11.18.
 */

// сервис вывода, может и не откывался никогда, нам не важно, но освободить ресурс обязательно(спорный момент, конечно)

public interface IResultService {
    public void appendToResult(String str);
    public void closeResult();
}
