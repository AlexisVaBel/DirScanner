package com.dirscanner.service;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class ResultFactory {
    public static IResultService getResultService(String svcname,String[] params){
        // если появятся какие-либо реализации добавим их
        try {
            if(params.length < 2)
                return new ResultToFileService("","UTF-8");
            else
                return new ResultToFileService(params[0],params[1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        };
        return null;
    }
}
